package com.example.mybudgetv2.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mybudgetv2.fragments.FirstFunctionCalculatorFragment;
import com.example.mybudgetv2.fragments.SecondFunctionCalculatorFragment;

public class CalculatorCollectionPagerAdapter extends FragmentPagerAdapter {

    public CalculatorCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstFunctionCalculatorFragment();
            case 1:
                return new SecondFunctionCalculatorFragment();
            default:
                return null; // Problem occurs at this condition!
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Loan";
            case 1:
                return "Deposit";
            default:
                return null; // Problem occurs at this condition!
        }
    }
}