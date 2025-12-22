open! Core
open! Hardcaml
open! Signal

module Byte_with_valid = With_valid.Vector (struct
    let width = 8
  end)

module Make (Config : sig
    val baud_rate : int
    val clock_freq_hz : int
    val tx_fifo_depth : int
    val rx_fifo_depth : int
    val xoff_threshold : int
  end) =
  struct
    open Config

    let cycles_per_baud : int = clock_freq_hz / baud_rate
    let cycles_per_half_baud : int = cycles_per_baud / 2

    module State = struct
      type t =
        | Idle
        | Start
        | Data0 
        | Data1 
        | Data2 
        | Data3 
        | Data4 
        | Data5 
        | Data6 
        | Data7 
        | Stop
      [@@deriving sexp_of, compare ~localize, enumerate, variants]
    end

    module I = struct
      type 'a t =
        { clock    : 'a
        ; clear    : 'a
        ; uart_rx  : 'a
        ; rx_ready : 'a
        ; tx_data  : 'a Byte_with_valid.t
        }
      [@@deriving hardcaml]
    end

    module O = struct
      type 'a t =
        { uart_tx     : 'a
        ; tx_ready    : 'a
        ; rx_data     : 'a Byte_with_valid.t
        ; rx_overflow : 'a
        }
      [@@deriving hardcaml]
    end

    let create scope ({ clock; clear; uart_rx; rx_ready; tx_data } : _ I.t) : _ O.t =
      let open Signal in

      (* RX *)
      let spec = Reg_spec.create ~clock ~clear () in
      let rx_sm = Always.State_machine.create (module State) spec in
      let rx_cnt = Always.Variable.reg ~enable:vdd spec ~width:(num_bits_to_represent(cycles_per_half_baud - 1)) in
      let rx_byte = Always.Variable.reg ~enable:vdd spec ~width:8 in
      let rx_fifo_wr = Always.Variable.reg ~enable:vdd spec ~width:1 in
      let rx_half_baud = Always.Variable.reg ~enable:vdd spec ~width:1 in
      let rx_half_baud_d = reg spec ~enable:vdd rx_half_baud.value in
      let rx_tick = (~:rx_half_baud_d) &: rx_half_baud.value in (* half_baud rising edge *)
      Always.(compile [
        rx_fifo_wr <-- gnd;

        if_ (rx_cnt.value >=:. cycles_per_half_baud) [
          rx_half_baud <-- ~:(rx_half_baud.value);
          rx_cnt <--. 0;
        ] [
          rx_cnt <-- rx_cnt.value +:. 1;
        ];

        rx_sm.switch [
          Idle,  [
            rx_cnt <--. 0;
            rx_half_baud <-- gnd;
            when_ ~:(uart_rx) [ rx_sm.set_next Start; ]
          ];
          Start, [ when_ rx_tick [rx_sm.set_next Data0] ];
          Data0, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data1] ];
          Data1, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data2] ];
          Data2, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data3] ];
          Data3, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data4] ];
          Data4, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data5] ];
          Data5, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data6] ];
          Data6, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Data7] ];
          Data7, [ when_ rx_tick [rx_byte <-- uart_rx @: msbs rx_byte.value; rx_sm.set_next Stop; rx_fifo_wr <-- vdd; ] ];
          Stop,  [ when_ rx_tick [rx_sm.set_next Idle] ];
        ]
      ]);
      let%tydi { q = rx_fifo_out; full = rx_fifo_full; empty = rx_fifo_empty; nearly_full = xoff; _ } =
        Fifo.create
          ~showahead:true
          ~scope:(Scope.sub_scope scope "rx_fifo")
          ~capacity:rx_fifo_depth
          ~overflow_check:true
          ~underflow_check:true
          ~nearly_full:xoff_threshold
          ~clock
          ~clear
          ~wr:rx_fifo_wr.value
          ~d:rx_byte.value
          ~rd:rx_ready
          ()
      in
      let rx_overflow = reg_fb spec ~width:1 ~f:(fun x -> x |: (rx_fifo_full &: rx_fifo_wr.value)) in

      (* XOFF/XON *)
      let spec = Reg_spec.create ~clock ~clear () in
      let tx_ready = Always.Variable.wire ~default:gnd () in
      let xoff_set = Always.Variable.reg ~enable:Signal.vdd spec ~width:1 in
      let xoff_xon_send = Always.Variable.reg ~enable:Signal.vdd spec ~width:1 in
      let xoff_xon_data = mux2 xoff_set.value (of_int_trunc ~width:8 0x13) (of_int_trunc ~width:8 0x11) in
      Always.(compile [
        when_ tx_ready.value [
          xoff_xon_send <-- gnd; (* xoff/xon message sent *)
        ];

        when_ (xoff_set.value <>: xoff) [
            xoff_xon_send <-- vdd;
            xoff_set <-- xoff;
        ]
      ]);

      (* TX *)
      let%tydi { q = tx_fifo_out; full = tx_fifo_full; empty = tx_fifo_empty; _ } =
        Fifo.create
          ~showahead:true
          ~scope:(Scope.sub_scope scope "tx_fifo")
          ~capacity:tx_fifo_depth
          ~overflow_check:true
          ~underflow_check:true
          ~clock
          ~clear
          ~wr:tx_data.valid
          ~d:tx_data.value
          ~rd:(tx_ready.value &: ~:(xoff_xon_send.value))
          ()
      in
      let spec = Reg_spec.create ~clock ~clear () in
      let tx_data = mux2 xoff_xon_send.value xoff_xon_data tx_fifo_out in
      let tx_data_valid = mux2 xoff_xon_send.value vdd ~:(tx_fifo_empty) in
      let tx_sm = Always.State_machine.create (module State) spec in
      let tx_cnt = Always.Variable.reg ~enable:Signal.vdd spec ~width:(num_bits_to_represent(cycles_per_baud - 1)) in
      let tx_byte = Always.Variable.reg ~enable:Signal.vdd spec ~width:8 in
      let tx_tick = Always.Variable.reg ~enable:Signal.vdd spec ~width:1 in
      let uart_tx = Always.Variable.reg ~enable:Signal.vdd spec ~width:1 in
      Always.(compile [
        if_ (tx_cnt.value >=:. cycles_per_baud - 1) [
          tx_cnt <--. 0;
          tx_tick <-- vdd;
        ] [
          tx_cnt <-- tx_cnt.value +:. 1;
          tx_tick <-- gnd;
        ];

        when_ tx_tick.value [
          tx_byte <-- vdd @: msbs tx_byte.value; 
          uart_tx <-- lsb tx_byte.value;
        ];

        tx_sm.switch [
          Idle,  [
            tx_cnt <--. 0;
            tx_ready <-- vdd;
            tx_byte <-- tx_data;
            uart_tx <-- vdd;
            when_ tx_data_valid [
              tx_sm.set_next Start;
            ]
          ];
          Start, [
            if_ tx_tick.value
              [ tx_sm.set_next Data0 ]
              [ uart_tx <-- gnd; ] (* start bit *)
          ];
          Data0, [ when_ tx_tick.value [tx_sm.set_next Data1] ];
          Data1, [ when_ tx_tick.value [tx_sm.set_next Data2] ];
          Data2, [ when_ tx_tick.value [tx_sm.set_next Data3] ];
          Data3, [ when_ tx_tick.value [tx_sm.set_next Data4] ];
          Data4, [ when_ tx_tick.value [tx_sm.set_next Data5] ];
          Data5, [ when_ tx_tick.value [tx_sm.set_next Data6] ];
          Data6, [ when_ tx_tick.value [tx_sm.set_next Data7] ];
          Data7, [ when_ tx_tick.value [tx_sm.set_next Stop] ];
          Stop,  [ when_ tx_tick.value [tx_sm.set_next Idle] ];
        ]
      ]);

      { rx_data = { value=rx_fifo_out; valid=(~:rx_fifo_empty) }; tx_ready=(~:tx_fifo_full); uart_tx=uart_tx.value; rx_overflow=rx_overflow };
    ;;
  end
