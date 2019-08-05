package com.example.mybudgetv2.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Category {

    public String categoryName;

    public Category() {

    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryName", categoryName);

        return result;
    }
}