package main.model;

public class Player {

    private String name;
    private int balance;
    private int lossCount;

    public Player(String name, int lossCount, int balance) {
        this.name = name;
        this.balance = balance;
        this.lossCount = lossCount;
    }

    public int getLosCount() {
        return lossCount;
    }

    public void addLost() {
        lossCount++;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
