; various examples for testing, taken from the SMT-LIB standardisation document and our paper draft
(define-contract BankAccount.init
  ((this (out BankAccount)))
  ((true (= (balance this) 0))))

(define-contract BankAccount.balance
  ((this (in BankAccount))
   (result (out Int)))
  ((true (= result (balance this)))))

(define-contract BankAccount.deposit
  ((this (inout BankAccount))
   (amount (in Int)))
  ((true (= (balance this) (+ (old (balance this)) amount)))))

(define-contract BankAccount.withdraw
  ((this (inout BankAccount))
   (amount (in Int)))
  (((<= amount (balance this))
  (= (balance this) (- (old (balance this)) amount)))))

; an enumeration datatype
(declare-datatypes
  ((Color 0))
  (((red) (green) (blue))))
; testers : (_ is red) , ( _ is green )
; integer lists with "empty " and " insert " constructors
(declare-datatypes
  ((IntList 0))
  (((empty) (insert (head Int) (tail IntList)))))
; testers : ( _ is empty ) , ( _ is insert )
; parametric lists with " nil " and " cons " constructors
( declare-datatypes ( ( List 1) ) (
( par ( T ) ( ( nil ) ( cons ( car T ) ( cdr ( List T )) )))))
; option datatype
( declare-datatypes ( ( Option 1) ) (
( par ( X ) ( ( none ) ( some ( val X )) ))))
; parametric pairs
( declare-datatypes ( ( Pair 2) ) (
( par ( X Y ) ( ( pair ( first X ) ( second Y )) ))))
; two mutually recursive datatypes
(declare-datatypes
  ((Tree 1) (TreeList 1))
  (
; Tree
   (par (X) ((node (value X) (children (TreeList X)))))
; TreeList
   (par (Y) ((empty)
             (insert (head (Tree Y)) (tail (TreeList Y)))))))

(declare-abstractions
  ((Tree 1) (TreeList 1))
  (
; Tree
   (par (X) ((node (value X) (children (TreeList X)))))
; TreeList
   (par (Y) ((empty)
             (insert (head (Tree Y)) (tail (TreeList Y)))))))

; either the grammar is wrong or this example
;(declare-abstractions
;  ((Cache 0))
;  (((Cache
;     ((entries (Map Key Entry))
;      (uniques (Set Int)))
;  )))
;)

; corrected version?
(declare-abstractions
  ((Cache 0))
  (((Cache
     ((entries (Map Key Entry))
      (uniques (Set Int)))
  )))
)

; integer lists with "empty " and " insert " constructors
(declare-datatypes
  ((IntList 0))
  (((empty) (insert (head Int) (tail IntList)))))
; testers : ( _ is empty ) , ( _ is insert )
; parametric lists with " nil " and " cons " constructors
(declare-datatypes
  ((List 1))
  ((par (T) ((nil)
             (cons (car T) (cdr (List T))))))
)


(declare-abstractions
  ((IntList 0))
  (((empty) (insert (head Int) (tail IntList))))
)

(declare-abstractions
  ((BankAccount 0))
  (((BankAccount (balance Int)))))

;(declare-abstraction
;    BankAccount
;    ((BankAccount (balance Int))))

