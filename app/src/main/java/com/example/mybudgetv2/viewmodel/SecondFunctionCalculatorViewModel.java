package com.example.mybudgetv2.viewmodel;

import android.os.Build.VERSION_CODES;

import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mybudgetv2.BR;
import com.example.mybudgetv2.controller.CalculatorController;
import com.example.mybudgetv2.models.SecondFunctionCalculator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SecondFunctionCalculatorViewModel extends BaseObservable implements CalculatorFunctions {

    public static final String DEPOSIT_AMOUNT = "depositAmount";
    public static final String HOW_MANY_MONTHS = "howManyMonths";
    public static final String CAPITALIZATION_PERIOD = "capitalizationPeriod";
    public static final String INTEREST_RATE = "interestRate";

    private String depositValueAfter = "Fill All Values";
    private SecondFunctionCalculator secondFunctionCalculator = new SecondFunctionCalculator();

    public SecondFunctionCalculatorViewModel() {
    }

    @Bindable
    public String getDepositValueAfter() {
        return this.depositValueAfter;
    }

    @Bindable
    public void setDepositValueAfter(String depositValueAfter) {
        this.depositValueAfter = depositValueAfter;
        notifyPropertyChanged(BR.depositValueAfter);
    }

    @Bindable
    public String getDepositAmount() {
        return secondFunctionCalculator.depositAmount;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setDepositAmount(String depositAmount) {
        if (secondFunctionCalculator.depositAmount != depositAmount) {
            secondFunctionCalculator.depositAmount = depositAmount;

            // React to the change.
            CalculatorController.calculateDepositValueAfter(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.depositAmount);
        }
    }

    @Bindable
    public String getInterestRate() {
        return secondFunctionCalculator.interestRate;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setInterestRate(String interestRate) {
        if (secondFunctionCalculator.interestRate != interestRate) {
            secondFunctionCalculator.interestRate = interestRate;

            // React to the change.
            CalculatorController.calculateDepositValueAfter(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.interestRate);
        }
    }

    @Bindable
    public String getHowManyMonths() {
        return secondFunctionCalculator.howManyMonths;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setHowManyMonths(String howManyMonths) {
        if (secondFunctionCalculator.howManyMonths != howManyMonths) {
            secondFunctionCalculator.howManyMonths = howManyMonths;

            // React to the change.
            CalculatorController.calculateDepositValueAfter(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.howManyMonths);
        }
    }

    @Bindable
    public String getCapitalizationPeriod() {
        return secondFunctionCalculator.capitalizationPeriod;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Bindable
    public void setCapitalizationPeriod(String capitalizationPeriod) {
        if (secondFunctionCalculator.capitalizationPeriod != capitalizationPeriod) {
            secondFunctionCalculator.capitalizationPeriod = capitalizationPeriod;

            // React to the change.
            CalculatorController.calculateDepositValueAfter(this);

            // Notify observers of a new value.
            notifyPropertyChanged(BR.capitalizationPeriod);
        }
    }

    @Override
    public Map<String, Double> getAllFunctionValues() {
        Map<String, Double> calculatorValues = new HashMap<>();

        if (!areAllFieldsFilled()) {
            return Collections.emptyMap();
        }

        try {
            double depositAmount = Double.parseDouble(secondFunctionCalculator.depositAmount);
            double howManyMonths = Double.parseDouble(secondFunctionCalculator.howManyMonths);
            double capitalizationPeriod = Double.parseDouble(secondFunctionCalculator.capitalizationPeriod);
            double interestRate = Double.parseDouble(secondFunctionCalculator.interestRate);
            calculatorValues.put(DEPOSIT_AMOUNT, depositAmount);
            calculatorValues.put(HOW_MANY_MONTHS, howManyMonths);
            calculatorValues.put(CAPITALIZATION_PERIOD, capitalizationPeriod);
            calculatorValues.put(INTEREST_RATE, interestRate);
        } catch (Exception e) {
            setDepositValueAfter("Error while parsing");
        }
        return calculatorValues;
    }

    private boolean areAllFieldsFilled() {
        return secondFunctionCalculator != null &&
                secondFunctionCalculator.howManyMonths != null &&
                secondFunctionCalculator.interestRate != null &&
                secondFunctionCalculator.depositAmount != null &&
                secondFunctionCalculator.capitalizationPeriod != null &&
                !secondFunctionCalculator.getHowManyMonths().equals("") &&
                !secondFunctionCalculator.getInterestRate().equals("") &&
                !secondFunctionCalculator.getDepositAmount().equals("") &&
                !secondFunctionCalculator.getCapitalizationPeriod().equals("");
    }
}