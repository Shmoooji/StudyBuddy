package com.example.ginansya.ui.insights;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.ginansya.R;
import com.example.ginansya.data.FinanceStore;
import com.example.ginansya.data.model.Plan;
import com.example.ginansya.util.CurrencyUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class InsightsFragment extends Fragment {

    private static final String[] MONTHS = {
            "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec", "Jan", "Feb", "Mar", "Apr"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.insights_scroll),
                (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), bars.top,
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        configureBarChart(view);
        configurePieChart(view);
    }

    private void configureBarChart(View root) {
        BarChart chart = root.findViewById(R.id.bar_chart);
        TextView total = root.findViewById(R.id.bar_total);

        int[] series = FinanceStore.getInstance().monthlyContributionTotals();
        int grand = 0;
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < series.length; i++) {
            entries.add(new BarEntry(i, series[i]));
            grand += series[i];
        }
        total.setText(CurrencyUtils.format(grand) + " total");

        BarDataSet set = new BarDataSet(entries, "Contributions");
        set.setColor(ContextCompat.getColor(requireContext(), R.color.brand_teal));
        set.setDrawValues(false);
        set.setHighLightColor(ContextCompat.getColor(requireContext(),
                R.color.brand_teal_dark));

        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        chart.setData(data);
        chart.setFitBars(true);

        int onSurface = ContextCompat.getColor(requireContext(), R.color.light_on_surface);
        int grid = ContextCompat.getColor(requireContext(), R.color.light_outline_variant);

        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setExtraBottomOffset(8f);

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setTextColor(onSurface);
        x.setGranularity(1f);
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int i = Math.max(0, Math.min(MONTHS.length - 1, Math.round(value)));
                return MONTHS[i];
            }
        });

        YAxis left = chart.getAxisLeft();
        left.setDrawAxisLine(false);
        left.setGridColor(grid);
        left.setTextColor(onSurface);
        left.setGridLineWidth(0.6f);
        left.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return CurrencyUtils.formatCompact(value);
            }
        });
        chart.getAxisRight().setEnabled(false);

        chart.animateY(800);
        chart.invalidate();
    }

    private void configurePieChart(View root) {
        PieChart chart = root.findViewById(R.id.pie_chart);
        LinearLayout legend = root.findViewById(R.id.pie_legend);

        int[] palette = {
                ContextCompat.getColor(requireContext(), R.color.brand_teal),
                ContextCompat.getColor(requireContext(), R.color.brand_teal_light),
                ContextCompat.getColor(requireContext(), R.color.brand_amber),
                ContextCompat.getColor(requireContext(), R.color.brand_amber_light)
        };

        List<Plan> plans = FinanceStore.getInstance().getActivePlans();
        List<PieEntry> entries = new ArrayList<>();
        for (Plan p : plans) {
            entries.add(new PieEntry((float) p.currentBalance(), p.name));
        }

        PieDataSet set = new PieDataSet(entries, "");
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < plans.size(); i++) colors.add(palette[i % palette.length]);
        set.setColors(colors);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(11f);
        set.setSliceSpace(2f);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Math.round(value * 100f) / 100f > 0
                        ? String.format("%.0f%%", percent(value, plans)) : "";
            }
        });

        PieData data = new PieData(set);
        chart.setData(data);

        chart.setUsePercentValues(false);
        chart.setDrawEntryLabels(false);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setHoleRadius(62f);
        chart.setTransparentCircleAlpha(0);
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setCenterTextSize(16f);
        chart.setCenterTextColor(ContextCompat.getColor(requireContext(),
                R.color.light_on_surface));
        double total = 0;
        for (Plan p : plans) total += p.currentBalance();
        chart.setCenterText(CurrencyUtils.formatCompact(total));
        chart.setExtraOffsets(0f, 0f, 0f, 0f);
        chart.animateY(900);
        chart.invalidate();

        legend.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int i = 0; i < plans.size(); i++) {
            View row = inflater.inflate(R.layout.item_legend, legend, false);
            View dot = row.findViewById(R.id.legend_dot);
            TextView label = row.findViewById(R.id.legend_label);
            TextView amount = row.findViewById(R.id.legend_amount);
            dot.getBackground().mutate().setTint(palette[i % palette.length]);
            label.setText(plans.get(i).name);
            amount.setText(CurrencyUtils.formatCompact(plans.get(i).currentBalance()));
            legend.addView(row);
        }
    }

    private static float percent(float value, List<Plan> plans) {
        double total = 0;
        for (Plan p : plans) total += p.currentBalance();
        if (total <= 0) return 0;
        return (float) (value / total * 100.0);
    }
}
