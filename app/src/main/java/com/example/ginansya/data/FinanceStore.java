package com.example.ginansya.data;

import com.example.ginansya.R;
import com.example.ginansya.data.model.Contribution;
import com.example.ginansya.data.model.Plan;
import com.example.ginansya.data.model.PlanStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinanceStore {

    private static FinanceStore instance;

    private final List<Plan> plans = new ArrayList<>();
    private final int[] monthlyTotals = {
            950, 1100, 1250, 1050, 1350, 1400,
            1200, 1500, 1380, 1420, 1550, 1680
    };
    private final double gainedThisMonth = 1240.50;

    private FinanceStore() {
        plans.addAll(buildInitialPlans());
    }

    public static synchronized FinanceStore getInstance() {
        if (instance == null) instance = new FinanceStore();
        return instance;
    }

    public List<Plan> getAllPlans() {
        return new ArrayList<>(plans);
    }

    public List<Plan> getActivePlans() {
        List<Plan> result = new ArrayList<>();
        for (Plan p : plans) {
            if (p.status != PlanStatus.COMPLETED) result.add(p);
        }
        return result;
    }

    public List<Plan> getCompletedPlans() {
        List<Plan> result = new ArrayList<>();
        for (Plan p : plans) {
            if (p.status == PlanStatus.COMPLETED) result.add(p);
        }
        return result;
    }

    public Plan findById(String id) {
        for (Plan p : plans) {
            if (p.id.equals(id)) return p;
        }
        return null;
    }

    public double totalSaved() {
        double total = 0;
        for (Plan p : getActivePlans()) total += p.currentBalance();
        return total;
    }

    public double gainedThisMonth() {
        return gainedThisMonth;
    }

    public int[] monthlyContributionTotals() {
        return monthlyTotals.clone();
    }

    private List<Plan> buildInitialPlans() {
        List<Plan> list = new ArrayList<>();

        list.add(new Plan(
                "emergency",
                "Emergency Fund",
                R.drawable.ic_shield,
                10_000, 2_000, 500, 4.5,
                8, 20,
                PlanStatus.AHEAD,
                emergencyContribs()));

        list.add(new Plan(
                "house",
                "House Deposit",
                R.drawable.ic_house,
                50_000, 10_000, 800, 5.25,
                14, 60,
                PlanStatus.ON_TRACK,
                houseContribs()));

        list.add(new Plan(
                "car",
                "Dream Car",
                R.drawable.ic_car,
                25_000, 3_000, 400, 3.8,
                6, 48,
                PlanStatus.BEHIND,
                carContribs()));

        list.add(new Plan(
                "japan",
                "Japan Trip",
                R.drawable.ic_plane,
                5_000, 1_000, 350, 2.5,
                12, 12,
                PlanStatus.COMPLETED,
                japanContribs()));

        return list;
    }

    private List<Contribution> emergencyContribs() {
        return Arrays.asList(
                new Contribution(1, 500, "Payday deposit"),
                new Contribution(2, 500, "Payday deposit"),
                new Contribution(3, 600, "Bonus + payday"),
                new Contribution(4, 500, "Payday deposit"),
                new Contribution(5, 750, "Tax refund"),
                new Contribution(6, 500, "Payday deposit"),
                new Contribution(7, 500, "Payday deposit"),
                new Contribution(8, 650, "Payday + side gig"));
    }

    private List<Contribution> houseContribs() {
        List<Contribution> c = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            boolean bonus = i % 3 == 0;
            double amount = bonus ? 950 : 800;
            c.add(new Contribution(i, amount, bonus ? "Payday + bonus" : "Payday deposit"));
        }
        return c;
    }

    private List<Contribution> carContribs() {
        return Arrays.asList(
                new Contribution(1, 400, "Payday deposit"),
                new Contribution(2, 400, "Payday deposit"),
                new Contribution(3, 300, "Partial (travel expenses)"),
                new Contribution(4, 200, "Partial (unexpected bill)"),
                new Contribution(5, 350, "Payday deposit"),
                new Contribution(6, 400, "Payday deposit"));
    }

    private List<Contribution> japanContribs() {
        List<Contribution> c = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            c.add(new Contribution(i, 350, "Payday deposit"));
        }
        return c;
    }
}
