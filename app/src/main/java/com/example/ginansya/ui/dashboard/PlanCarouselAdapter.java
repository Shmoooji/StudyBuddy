package com.example.ginansya.ui.dashboard;

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

import java.util.List;

public class PlanCarouselAdapter extends RecyclerView.Adapter<PlanCarouselAdapter.VH> {

    private final List<Plan> plans;

    public PlanCarouselAdapter(List<Plan> plans) {
        this.plans = plans;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_carousel, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Plan p = plans.get(position);
        h.icon.setImageResource(p.iconRes);
        h.name.setText(p.name);
        h.amounts.setText(CurrencyUtils.format(p.currentBalance())
                + " of " + CurrencyUtils.format(p.targetAmount));
        int pct = p.progressPct();
        h.progress.setProgressCompat(pct, true);
        h.progressPct.setText(pct + "%");
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
        return plans.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView name;
        final TextView amounts;
        final LinearProgressIndicator progress;
        final TextView progressPct;
        final TextView status;

        VH(View v) {
            super(v);
            icon = v.findViewById(R.id.plan_icon);
            name = v.findViewById(R.id.plan_name);
            amounts = v.findViewById(R.id.plan_amounts);
            progress = v.findViewById(R.id.plan_progress);
            progressPct = v.findViewById(R.id.plan_progress_pct);
            status = v.findViewById(R.id.plan_status);
        }
    }
}
