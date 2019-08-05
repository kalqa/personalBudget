package com.example.mybudgetv2.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class SecondFunctionCalculator {

    public String depositAmount;
    public String interestRate;
    public String howManyMonths;
    public String capitalizationPeriod;

    public SecondFunctionCalculator() {

    }

    public SecondFunctionCalculator(String depositAmount, String interestRate, String howManyMonths, String capitalizationPeriod) {
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.howManyMonths = howManyMonths;
        this.capitalizationPeriod = capitalizationPeriod;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depositAmount", depositAmount);
        result.put("interestRate", interestRate);
        result.put("howManyMonths", howManyMonths);
        result.put("capitalizationPeriod", capitalizationPeriod);

        return result;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getHowManyMonths() {
        return howManyMonths;
    }

    public void setHowManyMonths(String howManyMonths) {
        this.howManyMonths = howManyMonths;
    }

    public String getCapitalizationPeriod() {
        return capitalizationPeriod;
    }

    public void setCapitalizationPeriod(String capitalizationPeriod) {
        this.capitalizationPeriod = capitalizationPeriod;
    }
}