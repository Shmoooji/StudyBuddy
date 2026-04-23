package com.example.ginansya.data.model;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    public final String id;
    public final String name;
    public final int iconRes;
    public final double targetAmount;
    public final double principal;
    public final double monthlyContribution;
    public final double annualRatePct;
    public final int monthsElapsed;
    public final int totalMonths;
    public final PlanStatus status;
    public final List<Contribution> contributions;

    public Plan(String id,
                String name,
                int iconRes,
                double targetAmount,
                double principal,
                double monthlyContribution,
                double annualRatePct,
                int monthsElapsed,
                int totalMonths,
                PlanStatus status,
                List<Contribution> contributions) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
        this.targetAmount = targetAmount;
        this.principal = principal;
        this.monthlyContribution = monthlyContribution;
        this.annualRatePct = annualRatePct;
        this.monthsElapsed = monthsElapsed;
        this.totalMonths = totalMonths;
        this.status = status;
        this.contributions = contributions == null ? new ArrayList<>() : contributions;
    }

    public double currentBalance() {
        double balance = principal;
        for (Contribution c : contributions) {
            balance += c.amount;
        }
        double monthlyRate = annualRatePct / 100.0 / 12.0;
        balance *= Math.pow(1 + monthlyRate, Math.max(1, monthsElapsed));
        return balance;
    }

    public int progressPct() {
        if (targetAmount <= 0) return 0;
        int pct = (int) Math.round(currentBalance() / targetAmount * 100.0);
        return Math.max(0, Math.min(100, pct));
    }

    public double totalContributed() {
        double total = 0;
        for (Contribution c : contributions) total += c.amount;
        return total;
    }
}
