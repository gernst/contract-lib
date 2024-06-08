(declare-sort Elem 0)

(define-sort Stream (A)
  (Array Int A))

(declare-datatypes
  ((Unit 0))
  (((unit))))

(declare-datatypes
  ((Color 0))
  (((red) (green) (blue))))

(declare-datatypes
  ((List 1))
  ((par (A)
      ((nil)
       (cons (head A)
             (tail (List A)))))))

(declare-fun choose (Int Int) Int)

(assert
  (forall ((i Int) (j Int))
    (or (= i (choose i j))
        (= j (choose i j)))))

(define-fun inc ((i Int)) Int
  (+ i 1))
