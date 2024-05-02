(declare-sort Elem 0)

(define-sort Stream (A)
  (Array Int A))

(declare-fun choose (Int Int) Int)

(assert
  (forall ((i Int) (j Int))
    (or (= i (choose i j))
        (= j (choose i j)))))

(define-fun inc ((i Int)) Int
  (+ i 1))
