package com.example.mybudgetv2.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class IrregularExpense {

    public String categoryName;
    public Double expense;

    public IrregularExpense() {
    }

    public IrregularExpense(String categoryName, Double expense) {
        this.categoryName = categoryName;
        this.expense = expense;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryName", categoryName);
        result.put("expense", expense);
        return result;
    }
}