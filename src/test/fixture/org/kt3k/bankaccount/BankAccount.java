package org.kt3k.bankaccount;

public class BankAccount {

    private String id;
    private Integer balance;

    public BankAccount(String id, Integer balance) {
        this.id = id;
        this.balance = balance;
    }


    public void increase(Integer money) {
        this.balance += money;
    }

    public void decrease(Integer money) {
        this.balance -= money;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public String getId() {
        return this.id;
    }

}
