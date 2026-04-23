package com.example.ginansya.ui.plans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginansya.R;
import com.example.ginansya.data.model.Plan;
import com.example.ginansya.data.model.PlanStatus;
import com.example.ginansya.util.CurrencyUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.VH> {

    private final List<Plan> items = new ArrayList<>();

    public void submit(List<Plan> plans) {
        items.clear();
        items.addAll(plans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Plan p = items.get(position);
        h.icon.setImageResource(p.iconRes);
        h.name.setText(p.name);
        h.target.setText("Target: " + CurrencyUtils.format(p.targetAmount)
                + " · " + p.annualRatePct + "% APR");
        h.balance.setText(CurrencyUtils.format(p.currentBalance()));
        h.ofTarget.setText("of " + CurrencyUtils.format(p.targetAmount));
        int pct = p.progressPct();
        h.pct.setText(pct + "%");
        h.progress.setProgressCompat(pct, true);
        h.time.setText(p.monthsElapsed + " of " + p.totalMonths + " months");
        h.monthly.setText(CurrencyUtils.format(p.monthlyContribution) + " / mo");
        bindStatus(h.status, p.status);
    }

    private void bindStatus(TextView v, PlanStatus s) {
        int bg;
        int color;
        String text;
        switch (s) {
            case AHEAD:
                bg = R.drawable.bg_status_ahead;
                color = R.color.status_ahead;
                text = "Ahead";
                break;
            case BEHIND:
                bg = R.drawable.bg_status_behind;
                color = R.color.status_behind;
                text = "Behind";
                break;
            case COMPLETED:
                bg = R.drawable.bg_status_ahead;
                color = R.color.status_ahead;
                text = "Completed";
                break;
            case ON_TRACK:
            default:
                bg = R.drawable.bg_status_on_track;
                color = R.color.status_on_track;
                text = "On track";
                break;
        }
        v.setBackgroundResource(bg);
        v.setTextColor(ContextCompat.getColor(v.getContext(), color));
        v.setText(text);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView name;
        final TextView target;
        final TextView balance;
        final TextView ofTarget;
        final TextView pct;
        final TextView status;
        final TextView time;
        final TextView monthly;
        final LinearProgressIndicator progress;

        VH(View v) {
            super(v);
            icon = v.findViewById(R.id.plan_icon);
            name = v.findViewById(R.id.plan_name);
            target = v.findViewById(R.id.plan_target);
            balance = v.findViewById(R.id.plan_balance);
            ofTarget = v.findViewById(R.id.plan_of_target);
            pct = v.findViewById(R.id.plan_pct);
            status = v.findViewById(R.id.plan_status);
            time = v.findViewById(R.id.plan_time);
            monthly = v.findViewById(R.id.plan_monthly);
            progress = v.findViewById(R.id.plan_progress);
        }
    }
}
