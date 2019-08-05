package com.example.mybudgetv2.fragments;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.adapters.IrregularExepensesPlansAdapter;
import com.example.mybudgetv2.adapters.RegularExepensesPlansAdapter;
import com.example.mybudgetv2.core.ExpensesCalculator;
import com.example.mybudgetv2.databinding.FragmentEarningsAndExpensesPlansBinding;
import com.example.mybudgetv2.models.Category;
import com.example.mybudgetv2.models.Earning;
import com.example.mybudgetv2.models.Expense;
import com.example.mybudgetv2.models.IrregularExpense;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EarningsAndExpensesPlansFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemSelectedListener {

    public static final String EARNING_PLANS = "earningPlans";
    public static final String IRREGULAR_EXPENSES = "irregularExpenses";
    public static final String CATEGORIES = "categories";
    public static final String EARNINGS = "earnings";
    public static final String MONTH = "month";

    public static final int INITIAL_SELECTED_SPINNER_ITEM = 6;
    public static final String DATA_ALREADY_EXISTS_TOAST_MESSAGE = "Data already exists!";
    public static final String EMPTY_CATEGORY_NAME_TOAST_MESSAGE = "Empty category name...";
    public static final String UNEXPECTED_ERROR = "Unexpected error!";
    public static final String SUCCESS = "Success!";

    private View rootView;
    private RecyclerView earningsPlansList;
    private RecyclerView irregularExpensesPlansList;
    private EditText lastMonthEarningsEditText;
    private Spinner monthPickingEarningsAndExpensesSpinner;
    private Button addNewRegularExpenseToPlanButton;
    private Button addNewIrregularExpenseToPlanButton;
    private EditText addNewIrregularExpenseToPlanEditText;
    private EditText addNewRegularExpenseToPlanEditText;
    private TextView moneyRemainingValueTextView;

    private RegularExepensesPlansAdapter regularExpensesPlansAdapter;
    private IrregularExepensesPlansAdapter irregularExpensesPlansAdapter;
    private DatabaseReference mDatabase;
    private String currentMonth;
    private List<Expense> monthlyExpensesPlanForCategory;
    private List<IrregularExpense> irregularExpensesPlanForCategory;
    private long maxCategoryId;
    private Earning bindedEarningToCurrentEarningTextView;
    private ChildEventListener earningsFromEarningPlanChildEventListener;
    private Double beforeEarning = 0.0;

    public EarningsAndExpensesPlansFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_earnings_and_expenses_plans, container, false);

        currentMonth = ExpensesCalculator.getCurrentMonthNumber();

        FragmentEarningsAndExpensesPlansBinding binding = FragmentEarningsAndExpensesPlansBinding.bind(rootView);
        bindedEarningToCurrentEarningTextView = new Earning();
        binding.setEarning(bindedEarningToCurrentEarningTextView);

        monthPickingEarningsAndExpensesSpinner = rootView.findViewById(R.id.monthPickingEarningsAndExpensesSpinner);
        lastMonthEarningsEditText = rootView.findViewById(R.id.lastMonthEarningsEditText);
        addNewRegularExpenseToPlanButton = rootView.findViewById(R.id.addNewRegularExpenseToPlanButton);
        addNewIrregularExpenseToPlanButton = rootView.findViewById(R.id.addNewIrregularExpenseToPlanButton);
        addNewRegularExpenseToPlanEditText = rootView.findViewById(R.id.addNewRegularExpenseToPlanEditText);
        addNewIrregularExpenseToPlanEditText = rootView.findViewById(R.id.addNewIrregularExpenseToPlanEditText);
        moneyRemainingValueTextView = rootView.findViewById(R.id.moneyRemainingValueTextView);

        moneyRemainingValueTextView.setText("0.0");

        earningsPlansList = rootView.findViewById(R.id.regularExpensesPlansList);
        earningsPlansList.setLayoutManager(new LinearLayoutManager(getContext()));
        earningsPlansList.setHasFixedSize(true);

        irregularExpensesPlansList = rootView.findViewById(R.id.irregularExpensesPlansList);
        irregularExpensesPlansList.setLayoutManager(new LinearLayoutManager(getContext()));
        irregularExpensesPlansList.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        makeListenersForChangingCurrentMonthEarnings();
        retrieveEarningFromCurrentMonth(bindedEarningToCurrentEarningTextView);

        addMaxCategoryIdListener();

        setupSpinner();
        setupAddNewRegularExpenseToPlanButtonListener();
        setupAddNewIrregularExpenseToPlanButtonListener();
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (earningsFromEarningPlanChildEventListener != null) {
            FirebaseDatabase.getInstance().getReference(EARNING_PLANS).child(EARNINGS).removeEventListener(earningsFromEarningPlanChildEventListener);
        }

        moneyRemainingValueTextView.setText("0.0");
        currentMonth = ExpensesCalculator.getMonthNumber(parent.getItemAtPosition(position).toString());

        retrieveEarningFromCurrentMonth(bindedEarningToCurrentEarningTextView);
        retrieveRegularExpensesForMonthPlan();
        retrieveIrregularExpensesForMonthPlan();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void makeListenersForChangingCurrentMonthEarnings() {
        lastMonthEarningsEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null && !s.toString().equals("")) {
                    beforeEarning = Double.parseDouble(s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable textAfterChange) {
                mDatabase.child(EARNING_PLANS)
                        .child(EARNINGS)
                        .orderByChild(MONTH)
                        .equalTo(currentMonth)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            String textAfterChangeParsed = textAfterChange.toString();

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Earning earning = ds.getValue(Earning.class);
                                    if (earning != null && !earning.getMonth().equals(currentMonth)) {
                                        return;
                                    }

                                    if (!textAfterChangeParsed.equals("") && earning != null) {
                                        try {
                                            if (Double.parseDouble(textAfterChangeParsed) != Double.parseDouble(earning.earning)) {
                                                Earning newEarning = new Earning(textAfterChangeParsed, earning.date, earning.month);

                                                dataSnapshot
                                                        .child(ds.getKey())
                                                        .getRef()
                                                        .setValue(newEarning);
                                                updateMoneyRemainingTextView(newEarning);
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "Parse error", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private double parseTextToDouble(String s) {
        if (!s.equals("")) {
            return Double.parseDouble(s);
        }

        Toast.makeText(getContext(), "Fill with number!", Toast.LENGTH_SHORT).show();
        return 0.0;
    }

    private void updateMoneyRemainingTextView(Earning newEarning) {
        double newEarningParsed = Double.parseDouble(newEarning.getEarning());
        double howMuchAddOrSubtract = Math.abs(beforeEarning - newEarningParsed);
        double remainingMoneyValue = parseTextToDouble(moneyRemainingValueTextView.getText().toString());

        if (beforeEarning < newEarningParsed) {
            moneyRemainingValueTextView.setText(Double.toString(remainingMoneyValue + howMuchAddOrSubtract));
        } else if (beforeEarning > newEarningParsed) {
            moneyRemainingValueTextView.setText(Double.toString(remainingMoneyValue - howMuchAddOrSubtract));
        }
    }

    private void retrieveEarningFromCurrentMonth(Earning bindedEarningToCurrentEarningTextView) {
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Earning earning = dataSnapshot.getValue(Earning.class);

                if (earning != null && currentMonth.equals(earning.getMonth())) {
                    try {
                        bindedEarningToCurrentEarningTextView.setEarning(earning.earning);
                        refreshMoneyRemainingWhenEarningRetrieved(bindedEarningToCurrentEarningTextView.getEarning());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Earning earning = dataSnapshot.getValue(Earning.class);
                if (earning != null && currentMonth.equals(earning.getMonth())) {
                    bindedEarningToCurrentEarningTextView.setEarning(earning.earning);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        FirebaseDatabase.getInstance()
                .getReference(EARNING_PLANS)
                .child(EARNINGS)
                .addChildEventListener(listener);

        FirebaseDatabase.getInstance()
                .getReference(EARNING_PLANS)
                .child(EARNINGS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        Set<String> months = new HashSet<>();
                        for (DataSnapshot dataSnapshot1 : children) {
                            Earning earning = dataSnapshot1.getValue(Earning.class);
                            months.add(earning.getMonth());
                        }

                        if (!months.contains(currentMonth)) {
                            Earning earning = new Earning("0.0", "01-" + currentMonth + "-2019", currentMonth);
                            FirebaseDatabase.getInstance()
                                    .getReference(EARNING_PLANS)
                                    .child(EARNINGS)
                                    .push()
                                    .setValue(earning);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        earningsFromEarningPlanChildEventListener = listener;
    }

    private void refreshMoneyRemainingWhenEarningRetrieved(String newEarning) {
        String newMoneyRemaining = Double.toString(parseTextToDouble(newEarning));
        moneyRemainingValueTextView.setText(Double.toString(Double.parseDouble(moneyRemainingValueTextView.getText().toString()) + Double.parseDouble(newMoneyRemaining)));
    }

    private void setupAddNewRegularExpenseToPlanButtonListener() {
        addNewRegularExpenseToPlanButton.setOnClickListener(view -> {
            String newCategoryName = addNewRegularExpenseToPlanEditText.getText().toString();

            if ("".equals(newCategoryName)) {
                Toast.makeText(getContext(), EMPTY_CATEGORY_NAME_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase.child(EARNING_PLANS).child(CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    boolean isPresentInDataBase = false;
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Expense expense = dataSnapshot.getValue(Expense.class);

                        if (expense == null || expense.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        Expense expense = new Expense(newCategoryName, 0.0, "01-" + currentMonth + "-2019", currentMonth);
                        mDatabase.child(EARNING_PLANS).child(CATEGORIES).push().setValue(expense)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                                    addNewRegularExpenseToPlanEditText.setText("");
                                })
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabase.child(CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    boolean isPresentInDataBase = false;
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);

                        if (category == null || category.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        Category category = new Category(newCategoryName);
                        mDatabase.child(CATEGORIES).push().setValue(category)
                                .addOnCompleteListener(task -> Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    private void setupAddNewIrregularExpenseToPlanButtonListener() {
        addNewIrregularExpenseToPlanButton.setOnClickListener(view -> {
            String newCategoryName = addNewIrregularExpenseToPlanEditText.getText().toString();

            if ("".equals(newCategoryName)) {
                Toast.makeText(getContext(), EMPTY_CATEGORY_NAME_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase.child(IRREGULAR_EXPENSES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    boolean isPresentInDataBase = false;
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        IrregularExpense irregularExpense = dataSnapshot.getValue(IrregularExpense.class);

                        if (irregularExpense == null || irregularExpense.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        IrregularExpense irregularExpense = new IrregularExpense(newCategoryName, 0.0);
                        mDatabase.child(IRREGULAR_EXPENSES).push().setValue(irregularExpense)
                                .addOnCompleteListener(task -> {
                                    addNewIrregularExpenseToPlanEditText.setText("");
                                    Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());

                        ;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabase.child(CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    boolean isPresentInDataBase = false;
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);

                        if (category == null || category.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        Category category = new Category(newCategoryName);
                        mDatabase.child(CATEGORIES).child(String.valueOf(maxCategoryId + 1)).setValue(category)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    private void setupSpinner() {
        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            monthPickingEarningsAndExpensesSpinner.setAdapter(adapter);
            monthPickingEarningsAndExpensesSpinner.setOnItemSelectedListener(this);
            monthPickingEarningsAndExpensesSpinner.setSelection(INITIAL_SELECTED_SPINNER_ITEM);
        }
    }

    private void retrieveRegularExpensesForMonthPlan() {
        mDatabase.child(EARNING_PLANS).child(CATEGORIES)
                .orderByChild(MONTH)
                .equalTo(currentMonth)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        monthlyExpensesPlanForCategory = new ArrayList<>();
                        regularExpensesPlansAdapter = new RegularExepensesPlansAdapter(
                                getContext(),
                                monthlyExpensesPlanForCategory, mDatabase.child(EARNING_PLANS).child(CATEGORIES),
                                currentMonth,
                                moneyRemainingValueTextView);
                        earningsPlansList.setAdapter(regularExpensesPlansAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void retrieveIrregularExpensesForMonthPlan() {
        mDatabase.child(IRREGULAR_EXPENSES).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                irregularExpensesPlanForCategory = new ArrayList<>();
                irregularExpensesPlansAdapter = new IrregularExepensesPlansAdapter(
                        getContext(),
                        irregularExpensesPlanForCategory,
                        mDatabase.child(IRREGULAR_EXPENSES),
                        moneyRemainingValueTextView);
                irregularExpensesPlansList.setAdapter(irregularExpensesPlansAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMaxCategoryIdListener() {
        mDatabase.child(CATEGORIES).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxCategoryId = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}