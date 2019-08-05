package com.example.mybudgetv2.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Expense {

    public String categoryName;
    public Double expense;
    public String date;
    public String month;

    public Expense() {
    }

    public Expense(String categoryName, Double expense, String date, String month) {
        this.categoryName = categoryName;
        this.expense = expense;
        this.date = date;
        this.month = month;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryName", categoryName);
        result.put("expense", expense);
        result.put("date", date);
        result.put("month", month);
        return result;
    }
}