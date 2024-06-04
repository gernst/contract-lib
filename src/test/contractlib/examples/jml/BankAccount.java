interface BankAccount {
    //@ ghost int balance;

    /*@ normal_behavior
      @  requires true;
      @  ensures this.balance == 0;
      @*/
    BankAccount init();

    /*@ normal_behavior
      @  requires true;
      @  ensures \result == this.balance;
      @*/
    int balance();

    /*@ normal_behavior
      @  requires true;
      @  ensures this.balance == \old(this.balance) + amount;
      @*/
    void deposit(int amount);

    /*@ normal_behavior
      @  requires amount <= this.balance;
      @  ensures this.balance == \old(this.balance) - amount;
      @*/
    void withdraw(int amount);
}