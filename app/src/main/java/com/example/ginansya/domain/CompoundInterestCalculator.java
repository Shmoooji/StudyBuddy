package com.example.ginansya.domain;

import java.util.ArrayList;
import java.util.List;

public final class CompoundInterestCalculator {

    public static class Result {
        public final double futureValue;
        public final double totalContributed;
        public final double interestEarned;
        public final List<Double> monthlyBalances;

        Result(double futureValue, double totalContributed,
               double interestEarned, List<Double> monthlyBalances) {
            this.futureValue = futureValue;
            this.totalContributed = totalContributed;
            this.interestEarned = interestEarned;
            this.monthlyBalances = monthlyBalances;
        }
    }

    private CompoundInterestCalculator() {}

    public static Result project(double principal,
                                 double monthlyContribution,
                                 double annualRatePct,
                                 int years) {
        int months = Math.max(1, years * 12);
        double monthlyRate = annualRatePct / 100.0 / 12.0;
        double balance = principal;

        List<Double> series = new ArrayList<>(months + 1);
        series.add(balance);

        for (int m = 1; m <= months; m++) {
            balance = balance * (1 + monthlyRate) + monthlyContribution;
            series.add(balance);
        }

        double contributed = principal + monthlyContribution * months;
        double interest = balance - contributed;
        return new Result(balance, contributed, interest, series);
    }

    public static List<Double> projectionSeries(double principal,
                                                double monthlyContribution,
                                                double annualRatePct,
                                                int months) {
        double monthlyRate = annualRatePct / 100.0 / 12.0;
        double balance = principal;
        List<Double> series = new ArrayList<>(months + 1);
        series.add(balance);
        for (int m = 1; m <= months; m++) {
            balance = balance * (1 + monthlyRate) + monthlyContribution;
            series.add(balance);
        }
        return series;
    }

    public static List<Double> actualSeries(double principal,
                                            double annualRatePct,
                                            int months,
                                            double[] monthlyActualDeposits) {
        double monthlyRate = annualRatePct / 100.0 / 12.0;
        double balance = principal;
        List<Double> series = new ArrayList<>(months + 1);
        series.add(balance);
        for (int m = 1; m <= months; m++) {
            double deposit = m - 1 < monthlyActualDeposits.length
                    ? monthlyActualDeposits[m - 1] : 0;
            balance = balance * (1 + monthlyRate) + deposit;
            series.add(balance);
        }
        return series;
    }
}
