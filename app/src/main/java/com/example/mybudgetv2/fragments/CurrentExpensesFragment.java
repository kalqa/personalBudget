package com.example.mybudgetv2.fragments;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.core.ExpensesCalculator;
import com.example.mybudgetv2.database.DataBaseService;
import com.example.mybudgetv2.models.Category;
import com.example.mybudgetv2.models.Expense;
import com.example.mybudgetv2.viewholder.CurrentExpensesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentExpensesFragment extends Fragment {

    public static final String EXPENSE = "expense";
    public static final String EXPENSES = "expenses";
    public static final String CATEGORIES = "categories";
    public static final String WRONG_TYPE = "Wrong type!";
    public static final String UNEXPECTED_ERROR = "Unexpected error!";
    public static final String SUCCESS = "Success!";
    public static final String DATA_ALREADY_EXISTS = "Data already exists!";
    public static final String EMPTY_CATEGORY_NAME = "Empty category name...";

    private RecyclerView mRecycler;
    private View rootView;
    private TextView currentDateTextView;
    private EditText addNewCategoryEditText;
    private Button addNewCategoryButton;

    private FirebaseRecyclerAdapter<Expense, CurrentExpensesViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mDatabase;
    private String currentDate;
    private String currentMonth;
    private long maxExpenseId;
    private long maxCategoryId;

    public CurrentExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_current_expenses, container, false);

        mDatabase = DataBaseService.instanteDatabase();

        mRecycler = rootView.findViewById(R.id.dailyExpensesList);
        mRecycler.setHasFixedSize(true);

        currentDateTextView = rootView.findViewById(R.id.currentDateTextView);
        addNewCategoryButton = rootView.findViewById(R.id.addNewCategoryButton);
        addNewCategoryEditText = rootView.findViewById(R.id.addNewCategoryEditText);

        addMaxExpenseIdListener();
        addMaxCategoryIdListener();

        currentDate = ExpensesCalculator.getCurrentDate();
        currentMonth = ExpensesCalculator.getCurrentMonthNumber();
        currentDateTextView.setText(currentDate);

        addNewCategoryButton.setOnClickListener(view -> {
            String newCategoryName = addNewCategoryEditText.getText().toString();

            if ("".equals(newCategoryName)) {
                Toast.makeText(getContext(), EMPTY_CATEGORY_NAME, Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase.child(CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    boolean isPresentInDataBase = false;
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);

                        if (category == null || category.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        Category newCategory = new Category(newCategoryName);
                        mDatabase.child(CATEGORIES).child(String.valueOf(maxCategoryId + 1)).setValue(newCategory)
                                .addOnCompleteListener(task -> Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabase.child(EXPENSES).addListenerForSingleValueEvent(new ValueEventListener() {
                boolean isPresentInDataBase = false;
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Expense expense = dataSnapshot.getValue(Expense.class);

                        if (expense == null || expense.categoryName.equals(newCategoryName)) {
                            isPresentInDataBase = true;
                            Toast.makeText(getContext(), DATA_ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!isPresentInDataBase) {
                        Expense newExpense = new Expense(newCategoryName, 0.0, currentDate, currentMonth);
                        mDatabase.child(EXPENSES).child(String.valueOf(maxExpenseId + 1)).setValue(newExpense)
                                .addOnCompleteListener(task -> Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(task -> Toast.makeText(getContext(), UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query expensesQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Expense>()
                .setQuery(expensesQuery, Expense.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Expense, CurrentExpensesViewHolder>(options) {

            @Override
            public CurrentExpensesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new CurrentExpensesViewHolder(inflater.inflate(R.layout.item_category, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final CurrentExpensesViewHolder viewHolder, int position,
                                            final Expense model) {
                final DatabaseReference expenseRef = getRef(position);
                viewHolder.addExpensesToCategoryButton.setOnClickListener(v -> {
                    String categoryValue = viewHolder.addExpensesToCategoryEditText.getText().toString().trim();
                    try {
                        Integer integer = Integer.valueOf(categoryValue);
                        expenseRef.child(EXPENSE).setValue(model.expense + integer.longValue());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), WRONG_TYPE, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
                viewHolder.bindToPost(model);
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    private Query getQuery(DatabaseReference mDatabase) {
        return mDatabase.child(EXPENSES).orderByChild("date").equalTo(currentDate);
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

    private void addMaxExpenseIdListener() {
        mDatabase.child(EXPENSES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxExpenseId = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}