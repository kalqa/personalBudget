package com.example.mybudgetv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.core.ExpensesCalculator;
import com.example.mybudgetv2.database.DataBaseService;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DatabaseReference;

import static com.example.mybudgetv2.fragments.CurrentExpensesFragment.EXPENSES;

public class ExpensesSummaryFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemSelectedListener {

    public static PieChart chart;
    ExpensesCalculator expensesCalculator;
    private DatabaseReference mDatabase;
    private View rootView;
    private Spinner monthPickingSpinner;
    private String monthSelected;

    public ExpensesSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_expenses_summary, container, false);
        mDatabase = DataBaseService.instanteDatabase();
        chart = rootView.findViewById(R.id.chart);

        monthPickingSpinner = rootView.findViewById(R.id.monthPickingSpinner);
        expensesCalculator = new ExpensesCalculator();

        setupSpinner();

        return rootView;
    }

    private void setupSpinner() {
        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.months, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            monthPickingSpinner.setAdapter(adapter);
            monthPickingSpinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        monthSelected = parent.getItemAtPosition(position).toString();
        DatabaseReference databaseReference = mDatabase.child(EXPENSES);
        expensesCalculator.calculateExpensesForMonth(databaseReference, monthSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
