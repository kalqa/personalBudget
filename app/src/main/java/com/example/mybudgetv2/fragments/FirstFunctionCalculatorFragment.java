package com.example.mybudgetv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.databinding.FragmentFirstFunctionFinancialCalculatorBinding;
import com.example.mybudgetv2.viewmodel.FirstFunctionCalculatorViewModel;

public class FirstFunctionCalculatorFragment extends Fragment {

    FirstFunctionCalculatorViewModel firstFunctionCalculatorViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_function_financial_calculator, container, false);
        FragmentFirstFunctionFinancialCalculatorBinding binding = FragmentFirstFunctionFinancialCalculatorBinding.bind(rootView);
        firstFunctionCalculatorViewModel = new FirstFunctionCalculatorViewModel();
        binding.setFirstFunctionCalculatorViewModel(firstFunctionCalculatorViewModel);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}