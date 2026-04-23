package com.example.ginansya.ui.plans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginansya.R;
import com.example.ginansya.data.FinanceStore;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

public class PlansFragment extends Fragment {

    private PlansAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View header = view.findViewById(R.id.plans_header);
        ViewCompat.setOnApplyWindowInsetsListener(header, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(),
                    bars.top + getResources().getDimensionPixelSize(R.dimen.spacing_md),
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        FinanceStore store = FinanceStore.getInstance();

        RecyclerView list = view.findViewById(R.id.plans_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlansAdapter();
        list.setAdapter(adapter);
        adapter.submit(store.getAllPlans());

        ChipGroup filters = view.findViewById(R.id.plans_filters);
        filters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chip_all) {
                adapter.submit(store.getAllPlans());
            } else if (id == R.id.chip_active) {
                adapter.submit(store.getActivePlans());
            } else if (id == R.id.chip_completed) {
                adapter.submit(store.getCompletedPlans());
            }
        });

        view.findViewById(R.id.new_plan_fab).setOnClickListener(v ->
                Snackbar.make(view, "Create-plan flow coming soon", Snackbar.LENGTH_SHORT).show());
    }
}
