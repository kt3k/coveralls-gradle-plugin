package org.kt3k.bankaccount;

public class TransferContext {

    private BankAccountSender sender;
    private BankAccountReceiver receiver;

    public TransferContext(BankAccount sender, BankAccount receiver) {
        this.sender = new BankAccountSender(sender);
        this.receiver = new BankAccountReceiver(receiver);
    }

    static private class BankAccountSender {

        private BankAccount actor;

        public BankAccountSender(BankAccount actor) {
            this.actor = actor;
        }

        public void send(Integer money, BankAccountReceiver receiver) {
            this.actor.decrease(money);

            receiver.onReceive(money);
        }
    }

    static private class BankAccountReceiver {

        private BankAccount actor;

        public BankAccountReceiver(BankAccount actor) {
            this.actor = actor;
        }

        public void onReceive(Integer money) {
            this.actor.increase(money);
        }
    }

    public void transfer(Integer money) {
        this.sender.send(money, this.receiver);
    }
    
}
