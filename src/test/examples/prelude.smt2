; This file documents the required types and function symbols for Contract-LIB.
; It is very much inspired by what CVC5 offers.

(declare-sort Set 1)

(declare-fun set.empty
  (par (A) () (Set A)))

(declare-fun set.card
  (par (A) ((Set A)) Int))

(declare-fun set.member
  (par (A) (A (Set A)) Bool))

(declare-fun set.subset
  (par (A) ((Set A) (Set A)) Bool))

(declare-fun set.singleton
  (par (A) (A) (Set A)))

(declare-fun set.union
  (par (A) ((Set A) (Set A)) (Set A)))

(declare-fun set.inter
  (par (A) ((Set A) (Set A)) (Set A)))

(declare-fun set.minus
  (par (A) ((Set A) (Set A)) (Set A)))