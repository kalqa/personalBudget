package com.example.mybudgetv2.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mybudgetv2.BR;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Earning extends BaseObservable {

    public String earning;
    public String date;
    public String month;

    public Earning() {

    }

    public Earning(String earning, String date, String month) {
        this.earning = earning;
        this.date = date;
        this.month = month;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("earning", earning);
        result.put("date", date);
        result.put("month", month);
        return result;
    }

    @Bindable
    public String getEarning() {
        return earning;
    }

    @Bindable
    public void setEarning(String earning) {
        this.earning = earning;
        notifyPropertyChanged(BR.earning);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}