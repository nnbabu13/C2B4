package com.koiy.playercompanion;

public class DiceOutCome {
    public DiceOutCome() {
    }

    public DiceOutCome(int total, boolean isTriple) {
        this.total = total;
        this.isTriple = isTriple;
    }


    private int total;
    private  boolean isTriple;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isTriple() {
        return isTriple;
    }

    public void setTriple(boolean triple) {
        isTriple = triple;
    }
}
