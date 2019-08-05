package com.example.mybudgetv2.viewmodel;

import android.os.Build.VERSION_CODES;

import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mybudgetv2.BR;
import com.example.mybudgetv2.controller.CalculatorController;
import com.example.mybudgetv2.models.FirstFunctionCalculator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FirstFunctionCalculatorViewModel extends BaseObservable implements CalculatorFunctions {

    public static final String BANK_TAX = "interestRate";
    public static final String AMOUNT_OF_CREDIT = "amountOfCredit";
    public static final String HOW_MANY_MONTHS = "howManyMonths";

    private String loanValue = "Fill All Values";
    private FirstFunctionCalculator firstFunctionCalculator = new FirstFunctionCalculator();

    public FirstFunctionCalculatorViewModel() {
    }

    @Bindable
    public String getLoanValue() {
        return this.loanValue;
    }

    @Bindable
    public void setLoanValue(String loanValue) {
        this.loanValue = loanValue;
        notifyPropertyChanged(BR.loanValue);
    }

    @Bindable
    public String getAmountOfCreditPV() {
        return firstFunctionCalculator.amountOfCreditPV;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setAmountOfCreditPV(String amountOfCreditPV) {
        if (firstFunctionCalculator.amountOfCreditPV != amountOfCreditPV) {
            firstFunctionCalculator.amountOfCreditPV = amountOfCreditPV;

            // React to the change.
            CalculatorController.calculateLoan(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.amountOfCreditPV);
        }
    }

    @Bindable
    public String getBankTax() {
        return firstFunctionCalculator.bankTax;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setBankTax(String bankTax) {
        if (firstFunctionCalculator.bankTax != bankTax) {
            firstFunctionCalculator.bankTax = bankTax;

            // React to the change.
            CalculatorController.calculateLoan(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.bankTax);
        }
    }

    @Bindable
    public String getHowManyMonths() {
        return firstFunctionCalculator.howManyMonths;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setHowManyMonths(String howManyMonths) {
        if (firstFunctionCalculator.howManyMonths != howManyMonths) {
            firstFunctionCalculator.howManyMonths = howManyMonths;

            // React to the change.
            CalculatorController.calculateLoan(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.howManyMonths);
        }
    }

    @Override
    public Map<String, Double> getAllFunctionValues() {
        Map<String, Double> calculatorValues = new HashMap<>();

        if (!areAllFieldsFilled()) {
            return Collections.emptyMap();
        }

        try {
            double amountOfCredit = Double.parseDouble(firstFunctionCalculator.amountOfCreditPV);
            double howManyMonths = Double.parseDouble(firstFunctionCalculator.howManyMonths);
            double bankTax = Double.parseDouble(firstFunctionCalculator.bankTax);
            calculatorValues.put(AMOUNT_OF_CREDIT, amountOfCredit);
            calculatorValues.put(HOW_MANY_MONTHS, howManyMonths);
            calculatorValues.put(BANK_TAX, bankTax);
        } catch (Exception e) {
            setLoanValue("Error while parsing");
        }
        return calculatorValues;
    }

    private boolean areAllFieldsFilled() {
        return firstFunctionCalculator != null &&
                firstFunctionCalculator.howManyMonths != null &&
                firstFunctionCalculator.bankTax != null &&
                firstFunctionCalculator.amountOfCreditPV != null &&
                !firstFunctionCalculator.getHowManyMonths().equals("") &&
                !firstFunctionCalculator.getBankTax().equals("") &&
                !firstFunctionCalculator.getAmountOfCreditPV().equals("");
    }
}