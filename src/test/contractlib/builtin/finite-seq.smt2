(declare-sort Seq 1)

(declare-fun seq.empty
  (par (A) () (Seq A)))

(declare-fun seq.len
  (par (A) ((Seq A)) Int))

(declare-fun seq.contains
  (par (A) ((Seq A) (Seq A)) Bool))

(declare-fun seq.unit
  (par (A) (A) (Seq A)))

(declare-fun seq.++
  (par (A) ((Seq A) (Seq A)) (Seq A)))

(declare-fun seq.nth
  (par (A) ((Seq A) Int) A))

(declare-fun seq.at
  (par (A) ((Seq A) Int) (Seq A)))

(declare-fun seq.update
  (par (A) ((Seq A) Int (Seq A)) (Seq A)))

(declare-fun seq.extract
  (par (A) ((Seq A) Int Int) (Seq A)))