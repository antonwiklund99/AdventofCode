open! Core
open! Hardcaml
open! Signal

module Bcd_num = struct
  type 'a t = {
    value   : 'a;
    ndigits : 'a;
    valid   : 'a;
  } [@@deriving sexp_of, hardcaml]
end

let rec fix_digits n digits =
  match (n, digits) with
  | (0, _) -> digits
  | (_, []) -> []
  | (_, h::t) -> (mux2 (h >=:. 5) (h +:. 3) h)::(fix_digits (n-1) t)
;;

let rec double_dabble_rec value bit bcd =
  if bit >= (width value) then
    bcd 
  else
    let fixed_bcd = concat_lsb (fix_digits (bit / 4 + 1) (split_lsb ~part_width:4 bcd)) in
    let bcd = (lsbs fixed_bcd) @: (msb value) in
    let value = sll ~by:1 value in
    double_dabble_rec value (bit + 1) bcd

let bin_2_bcd ~clock ~clear ?(ready = vdd) (x : _ With_valid.t) =
  let spec = Reg_spec.create ~clock ~clear () in
  let num_digits = 
    Int.of_float (Float.round_up (Float.of_int (width x.value) *. 0.30103))
  in
  let bcd_width = 4 * num_digits in
  let valid_in = ready &: x.valid in
  let bcd = reg spec ~enable:valid_in (double_dabble_rec x.value 0 (zero bcd_width)) in
  let valid = reg spec valid_in in
  let ndigits = (of_int_trunc ~width:(num_bits_to_represent num_digits) num_digits) -: (drop_bottom ~width:2 (leading_zeros bcd)) in (* max_digits - nzeros/4 *)
  { Bcd_num.value=bcd; ndigits; valid; }
;;
