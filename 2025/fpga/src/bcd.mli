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
