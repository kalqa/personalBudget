package com.example.mybudgetv2.fragments;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.databinding.FragmentSecondFunctionFinancialCalculatorBinding;
import com.example.mybudgetv2.viewmodel.SecondFunctionCalculatorViewModel;

public class SecondFunctionCalculatorFragment extends Fragment {

    SecondFunctionCalculatorViewModel secondFunctionCalculatorViewModel;

    @TargetApi(VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second_function_financial_calculator, container, false);
        FragmentSecondFunctionFinancialCalculatorBinding binding = FragmentSecondFunctionFinancialCalculatorBinding.bind(rootView);
        secondFunctionCalculatorViewModel = new SecondFunctionCalculatorViewModel();
        binding.setSecondFunctionCalculatorViewModel(secondFunctionCalculatorViewModel);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}