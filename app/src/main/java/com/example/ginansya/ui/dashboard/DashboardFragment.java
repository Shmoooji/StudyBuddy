package com.example.ginansya.ui.dashboard;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginansya.R;
import com.example.ginansya.data.FinanceStore;
import com.example.ginansya.data.model.Plan;
import com.example.ginansya.util.CurrencyUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final String[] MONTH_LABELS = {
            "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec", "Jan", "Feb", "Mar", "Apr"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applyTopInset(view.findViewById(R.id.dashboard_scroll));

        FinanceStore store = FinanceStore.getInstance();

        TextView totalSaved = view.findViewById(R.id.total_saved);
        TextView delta = view.findViewById(R.id.this_month_delta);
        totalSaved.setText(CurrencyUtils.format(store.totalSaved()));
        delta.setText(CurrencyUtils.formatSigned(store.gainedThisMonth())
                + " this month");

        RecyclerView carousel = view.findViewById(R.id.plans_carousel);
        carousel.setLayoutManager(new LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false));
        List<Plan> activePlans = store.getActivePlans();
        carousel.setAdapter(new PlanCarouselAdapter(activePlans));

        view.findViewById(R.id.plans_see_all).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.plansFragment));
        view.findViewById(R.id.action_calc).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.calculatorFragment));

        configureChart(view.findViewById(R.id.growth_chart));
    }

    private void applyTopInset(View target) {
        ViewCompat.setOnApplyWindowInsetsListener(target, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), bars.top,
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
    }

    private void configureChart(LineChart chart) {
        int onSurface = ContextCompat.getColor(requireContext(), R.color.light_on_surface);
        int grid = ContextCompat.getColor(requireContext(), R.color.light_outline_variant);
        int actualColor = ContextCompat.getColor(requireContext(), R.color.chart_actual);
        int projColor = ContextCompat.getColor(requireContext(), R.color.chart_projected);

        List<Entry> actual = new ArrayList<>();
        List<Entry> projected = new ArrayList<>();
        double[] actualSeries = {
                11800, 12700, 13650, 14500, 15450, 16350, 17300,
                18150, 19200, 20250, 21300, 22450
        };
        double[] projSeries = {
                11800, 12650, 13500, 14400, 15300, 16200, 17100,
                18000, 18950, 19900, 20850, 21800
        };
        for (int i = 0; i < actualSeries.length; i++) {
            actual.add(new Entry(i, (float) actualSeries[i]));
            projected.add(new Entry(i, (float) projSeries[i]));
        }

        LineDataSet actualSet = new LineDataSet(actual, "Actual");
        actualSet.setColor(actualColor);
        actualSet.setLineWidth(2.4f);
        actualSet.setDrawCircles(false);
        actualSet.setDrawValues(false);
        actualSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        actualSet.setCubicIntensity(0.15f);
        actualSet.setDrawFilled(true);
        actualSet.setFillDrawable(ContextCompat.getDrawable(
                requireContext(), R.drawable.bg_chart_fill));
        actualSet.setHighlightEnabled(false);

        LineDataSet projSet = new LineDataSet(projected, "Projected");
        projSet.setColor(projColor);
        projSet.setLineWidth(1.6f);
        projSet.setDrawCircles(false);
        projSet.setDrawValues(false);
        projSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        projSet.setCubicIntensity(0.15f);
        projSet.enableDashedLine(8f, 6f, 0f);
        projSet.setHighlightEnabled(false);

        List<ILineDataSet> sets = new ArrayList<>();
        sets.add(projSet);
        sets.add(actualSet);
        chart.setData(new LineData(sets));

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
        x.setLabelCount(6, true);
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                int i = Math.max(0, Math.min(MONTH_LABELS.length - 1, Math.round(value)));
                return MONTH_LABELS[i];
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
        chart.animateY(900);
        chart.invalidate();
    }
}
