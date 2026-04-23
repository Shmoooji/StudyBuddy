package com.example.ginansya.ui.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.ginansya.R;
import com.example.ginansya.domain.CompoundInterestCalculator;
import com.example.ginansya.util.CurrencyUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class CalculatorFragment extends Fragment {

    private EditText inputPrincipal;
    private EditText inputMonthly;
    private EditText inputRate;
    private EditText inputYears;

    private TextView futureValue;
    private TextView contributedView;
    private TextView interestView;

    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.calc_scroll),
                (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), bars.top,
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        inputPrincipal = view.findViewById(R.id.input_principal);
        inputMonthly = view.findViewById(R.id.input_monthly);
        inputRate = view.findViewById(R.id.input_rate);
        inputYears = view.findViewById(R.id.input_years);
        futureValue = view.findViewById(R.id.calc_future_value);
        contributedView = view.findViewById(R.id.calc_contributed);
        interestView = view.findViewById(R.id.calc_interest);
        chart = view.findViewById(R.id.calc_chart);

        configureChart();

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { recalc(); }
        };
        inputPrincipal.addTextChangedListener(watcher);
        inputMonthly.addTextChangedListener(watcher);
        inputRate.addTextChangedListener(watcher);
        inputYears.addTextChangedListener(watcher);

        recalc();
    }

    private void recalc() {
        double principal = readDouble(inputPrincipal, 0);
        double monthly = readDouble(inputMonthly, 0);
        double rate = readDouble(inputRate, 0);
        int years = (int) readDouble(inputYears, 0);

        CompoundInterestCalculator.Result r =
                CompoundInterestCalculator.project(principal, monthly, rate, years);

        futureValue.setText(CurrencyUtils.format(r.futureValue));
        contributedView.setText(CurrencyUtils.format(r.totalContributed));
        interestView.setText(CurrencyUtils.format(Math.max(0, r.interestEarned)));

        updateChart(principal, monthly, years, r.monthlyBalances);
    }

    private void updateChart(double principal, double monthly,
                             int years, List<Double> balances) {
        List<Entry> totalEntries = new ArrayList<>();
        List<Entry> contribEntries = new ArrayList<>();

        int points = balances.size();
        int step = Math.max(1, points / 60);

        for (int i = 0; i < points; i += step) {
            totalEntries.add(new Entry(i, balances.get(i).floatValue()));
            float contrib = (float) (principal + monthly * i);
            contribEntries.add(new Entry(i, contrib));
        }
        if (totalEntries.isEmpty()
                || totalEntries.get(totalEntries.size() - 1).getX() < points - 1) {
            int last = points - 1;
            totalEntries.add(new Entry(last, balances.get(last).floatValue()));
            contribEntries.add(new Entry(last, (float) (principal + monthly * last)));
        }

        LineDataSet totalSet = new LineDataSet(totalEntries, "Total");
        totalSet.setColor(ContextCompat.getColor(requireContext(), R.color.chart_actual));
        totalSet.setLineWidth(2.6f);
        totalSet.setDrawCircles(false);
        totalSet.setDrawValues(false);
        totalSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        totalSet.setCubicIntensity(0.15f);
        totalSet.setDrawFilled(true);
        totalSet.setFillDrawable(ContextCompat.getDrawable(
                requireContext(), R.drawable.bg_chart_fill));
        totalSet.setHighlightEnabled(true);
        totalSet.setHighLightColor(ContextCompat.getColor(requireContext(),
                R.color.chart_actual));

        LineDataSet contribSet = new LineDataSet(contribEntries, "Contributions");
        contribSet.setColor(ContextCompat.getColor(requireContext(),
                R.color.light_on_primary_container));
        contribSet.setLineWidth(1.6f);
        contribSet.setDrawCircles(false);
        contribSet.setDrawValues(false);
        contribSet.enableDashedLine(8f, 6f, 0f);
        contribSet.setHighlightEnabled(false);

        List<ILineDataSet> sets = new ArrayList<>();
        sets.add(contribSet);
        sets.add(totalSet);
        chart.setData(new LineData(sets));

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int months = Math.round(value);
                if (months == 0) return "Now";
                int y = months / 12;
                return y + "y";
            }
        });

        chart.invalidate();
    }

    private void configureChart() {
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
        x.setLabelCount(5, false);

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
    }

    private double readDouble(EditText et, double fallback) {
        String s = et.getText() == null ? "" : et.getText().toString().trim();
        if (s.isEmpty()) return fallback;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
