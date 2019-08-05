package com.example.mybudgetv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.adapters.CalculatorCollectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FinancialCalculatorFragment extends androidx.fragment.app.Fragment {

    CalculatorCollectionPagerAdapter calculatorCollectionPagerAdapter;
    ViewPager viewPager;

    public FinancialCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_financial_calculator, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        calculatorCollectionPagerAdapter = new CalculatorCollectionPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(calculatorCollectionPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
