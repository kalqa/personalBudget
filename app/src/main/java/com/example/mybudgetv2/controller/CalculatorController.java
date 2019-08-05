package com.example.mybudgetv2.controller;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build.VERSION_CODES;

import androidx.annotation.RequiresApi;

import com.example.mybudgetv2.viewmodel.CalculatorFunctions;
import com.example.mybudgetv2.viewmodel.FirstFunctionCalculatorViewModel;
import com.example.mybudgetv2.viewmodel.SecondFunctionCalculatorViewModel;

import java.util.Map;

import static com.example.mybudgetv2.viewmodel.FirstFunctionCalculatorViewModel.AMOUNT_OF_CREDIT;
import static com.example.mybudgetv2.viewmodel.FirstFunctionCalculatorViewModel.BANK_TAX;
import static com.example.mybudgetv2.viewmodel.FirstFunctionCalculatorViewModel.HOW_MANY_MONTHS;
import static com.example.mybudgetv2.viewmodel.SecondFunctionCalculatorViewModel.CAPITALIZATION_PERIOD;
import static com.example.mybudgetv2.viewmodel.SecondFunctionCalculatorViewModel.DEPOSIT_AMOUNT;
import static com.example.mybudgetv2.viewmodel.SecondFunctionCalculatorViewModel.INTEREST_RATE;

public class CalculatorController {

    private static final Double BANK_LOAN_TAX = 2.00;
    private static final Double WIBOR_3M = 1.67;

    @RequiresApi(api = VERSION_CODES.N)
    public static void calculateLoan(FirstFunctionCalculatorViewModel firstFunctionCalculatorViewModel) {
        Map<String, Double> calculatorValues = collectAllCalculatorValues(firstFunctionCalculatorViewModel);

        Double bankTax = calculatorValues.get(BANK_TAX);
        Double amountOfCredit = calculatorValues.get(AMOUNT_OF_CREDIT);
        Double howManyMonths = calculatorValues.get(HOW_MANY_MONTHS);

        if (bankTax == null || amountOfCredit == null || howManyMonths == null) {
            return;
        }

        Double loanToValue = amountOfCredit * 0.80;
        Double afterBanksTax = loanToValue * (1.0 + bankTax / 100.0);
        Double withoutWibor = afterBanksTax / howManyMonths;
        Double loanValue = withoutWibor + (withoutWibor * (1.0 + ((BANK_LOAN_TAX + WIBOR_3M) / 100.0)));

        NumberFormat formatter = new DecimalFormat("#0.00");
        firstFunctionCalculatorViewModel.setLoanValue(formatter.format(loanValue));
    }

    @RequiresApi(api = VERSION_CODES.N)
    public static void calculateDepositValueAfter(SecondFunctionCalculatorViewModel secondFunctionCalculatorViewModel) {
        Map<String, Double> calculatorValues = collectAllCalculatorValues(secondFunctionCalculatorViewModel);

        Double depositAmount = calculatorValues.get(DEPOSIT_AMOUNT);
        Double capitalizationPeriod = calculatorValues.get(CAPITALIZATION_PERIOD);
        Double interestRate = calculatorValues.get(INTEREST_RATE);
        Double howManyMonths = calculatorValues.get(HOW_MANY_MONTHS);
        Double result = depositAmount;

        if (howManyMonths != null && capitalizationPeriod != null && interestRate != null && result != null) {
            for (int i = 1; i <= howManyMonths.intValue(); i++) {
                for (int j = 1; j <= capitalizationPeriod.intValue(); j++) {
                    result = result * (1.0 + (interestRate / 100));
                }
            }
            NumberFormat formatter = new DecimalFormat("#0.00");
            secondFunctionCalculatorViewModel.setDepositValueAfter(formatter.format(result));
        }
    }

    private static Map<String, Double> collectAllCalculatorValues(CalculatorFunctions calculatorFunctions) {
        return calculatorFunctions.getAllFunctionValues();
    }
}