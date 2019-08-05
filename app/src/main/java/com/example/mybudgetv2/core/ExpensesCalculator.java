package com.example.mybudgetv2.core;

import android.graphics.Color;
import android.os.Build.VERSION_CODES;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.mybudgetv2.models.Expense;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.mybudgetv2.fragments.ExpensesSummaryFragment.chart;

public class ExpensesCalculator {

    private static final String JANUARY = "January";
    private static final String JUNE = "June";
    private static final String JULY = "July";
    private static final String FEBRUARY = "February";
    private static final String MARCH = "March";
    private static final String APRIL = "April";
    private static final String MAY = "May";
    private static final String AUGUST = "August";
    private static final String SEPTEMBER = "September";
    private static final String NOVEMBER = "November";
    private static final String OCTOBER = "October";
    private static final String DECEMBER = "December";

    private static final Map<String, String> monthsNumbers = new HashMap<String, String>() {{
        put(JANUARY, "01");
        put(FEBRUARY, "02");
        put(MARCH, "03");
        put(APRIL, "04");
        put(MAY, "05");
        put(JUNE, "06");
        put(JULY, "07");
        put(AUGUST, "08");
        put(SEPTEMBER, "09");
        put(NOVEMBER, "10");
        put(OCTOBER, "11");
        put(DECEMBER, "12");
    }};

    public static String getMonthNumber(String monthName) {
        return monthsNumbers.get(monthName);
    }

    public static String getCurrentMonthNumber() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM");
        return mdformat.format(calendar.getTime());
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        return mdformat.format(calendar.getTime());
    }

    public void calculateExpensesForMonth(DatabaseReference databaseReference, String monthSelected) {
        String monthNumber = monthsNumbers.get(monthSelected);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<Expense> monthlyExpensesForCategory = new HashSet<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);

                    if (expense == null) {
                        return;
                    }

                    String[] split = expense.date.split("-");
                    String month = split[1];

                    if (month.equals(monthNumber)) {
                        monthlyExpensesForCategory.add(expense);
                    }
                }

                List<PieEntry> entries = new ArrayList<>();

                monthlyExpensesForCategory
                        .forEach(expense -> entries.add(new PieEntry(Float.valueOf(expense.expense.toString()), expense.categoryName)));

                PieDataSet pieDataSet = new PieDataSet(entries, "");
                pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                pieDataSet.setValueLineColor(Color.BLACK);
                PieData pieData = new PieData(pieDataSet);
                Description description = new Description();
                description.setText("");

                chart.setData(pieData);
                chart.setDescription(description);
                chart.setEntryLabelColor(Color.BLACK);
                chart.invalidate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}