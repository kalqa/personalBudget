package com.example.mybudgetv2.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirstFunctionCalculator {

    public String amountOfCreditPV;
    public String bankTax;
    public String howManyMonths;

    public FirstFunctionCalculator() {

    }

    public FirstFunctionCalculator(String amountOfCreditPV, String bankTax, String howManyMonths) {
        this.amountOfCreditPV = amountOfCreditPV;
        this.bankTax = bankTax;
        this.howManyMonths = howManyMonths;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depositAmount", amountOfCreditPV);
        result.put("interestRate", bankTax);
        result.put("howManyMonths", howManyMonths);

        return result;
    }

    //    @Bindable
    public String getAmountOfCreditPV() {
        return amountOfCreditPV;
    }

    //    @Bindable
    public void setAmountOfCreditPV(String amountOfCreditPV) {
        this.amountOfCreditPV = amountOfCreditPV;
//        notifyPropertyChanged(BR.depositAmount);
    }

    //    @Bindable
    public String getBankTax() {
        return bankTax;
    }

    //    @Bindable
    public void setBankTax(String bankTax) {
        this.bankTax = bankTax;
//        notifyPropertyChanged(BR.interestRate);
    }

    //    @Bindable
    public String getHowManyMonths() {
        return howManyMonths;
    }

    //    @Bindable
    public void setHowManyMonths(String howManyMonths) {
        this.howManyMonths = howManyMonths;
//        notifyPropertyChanged(BR.howManyMonths);
    }
}