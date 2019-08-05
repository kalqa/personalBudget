package com.example.mybudgetv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.adapters.BudgetHintsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BudgetHintsFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View rootView;

    public BudgetHintsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_budget_hints, container, false);

        recyclerView = rootView.findViewById(R.id.budgetHintsList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BudgetHintsAdapter(fillWithBudgetHints());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private List<String> fillWithBudgetHints() {

        List<String> hints = new ArrayList<>();

        hints.add("By definition, the budget plan covers only one month. In essence, we take care of it to prepare for long-term spending.");
        hints.add("Several budget categories are sufficient to create a budget.");
        hints.add("Partner support is important - it helps to maintain regularity.");
        hints.add("Better inexact budget than none.");
        hints.add("Training makes perfect - do not be put off by failures, not always 100% of the budget works.");
        hints.add("For irregular expenditures, one must write the total annual costs of a given category.");

        return hints;
    }
}
