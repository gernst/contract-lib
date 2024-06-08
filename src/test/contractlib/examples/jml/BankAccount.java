interface BankAccount {
    //@ ghost int balance;

    /*@ normal_behavior
      @  requires true;
      @  ensures (=(balancethis)0);
      @*/
    BankAccount init();

    /*@ normal_behavior
      @  requires true;
      @  ensures (=result(balancethis));
      @*/
    int balance(BankAccount this);

    /*@ normal_behavior
      @  requires true;
      @  ensures (=(balancethis)(+(old(balancethis))amount));
      @*/
    BankAccount deposit(BankAccount this, int amount);

    /*@ normal_behavior
      @  requires (<=amount(balancethis));
      @  ensures (=(balancethis)(-(old(balancethis))amount));
      @*/
    BankAccount withdraw(BankAccount this, int amount);

}
