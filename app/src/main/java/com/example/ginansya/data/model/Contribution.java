package com.example.ginansya.data.model;

public class Contribution {
    public final int monthOffset;
    public final double amount;
    public final String note;

    public Contribution(int monthOffset, double amount, String note) {
        this.monthOffset = monthOffset;
        this.amount = amount;
        this.note = note;
    }
}
