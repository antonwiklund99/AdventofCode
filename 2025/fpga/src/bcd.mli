open! Core
open! Hardcaml

module Bcd_num : sig
  type 'a t = {
    value   : 'a;
    ndigits : 'a;
    valid   : 'a;
  } [@@deriving sexp_of, hardcaml]
end

val bin_2_bcd 
  :  clock:Signal.t
  -> clear:Signal.t
  -> ?ready:Signal.t
  -> Signal.t With_valid.t
  -> Signal.t Bcd_num.t

val bcd_2_bin 
  :  clock:Signal.t
  -> clear:Signal.t
  -> valid:Signal.t
  -> ?ready:Signal.t
  -> Signal.t list
  -> Signal.t With_valid.t

val char_2_digit : Signal.t -> Signal.t
