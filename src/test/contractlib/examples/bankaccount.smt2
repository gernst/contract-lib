(declare-abstraction
  ((BankAccount 0))
  (((BankAccount (balance Int)))))

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