package com.example.mybudgetv2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.models.Expense;
import com.example.mybudgetv2.viewholder.RegularExpensesViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.mybudgetv2.fragments.CurrentExpensesFragment.EXPENSE;
import static com.example.mybudgetv2.fragments.CurrentExpensesFragment.WRONG_TYPE;
import static com.example.mybudgetv2.fragments.EarningsAndExpensesPlansFragment.MONTH;

public class RegularExepensesPlansAdapter extends RecyclerView.Adapter<RegularExpensesViewHolder> {

    private static final String TAG = "RegularExepensesPlans";

    private Context context;
    private List<Expense> regularExpenses;
    private String currentMonth;
    private DatabaseReference mDatabaseReference;
    private TextView moneyRemainingValueTextView;
    private List<String> regularExpensesIds = new ArrayList<>();

//    private ChildEventListener mChildEventListener;

    public RegularExepensesPlansAdapter(Context context, List<Expense> regularExpenses, DatabaseReference mDatabaseReference, String currentMonth, TextView moneyRemainingValueTextView) {
        this.context = context;
        this.regularExpenses = regularExpenses;
        this.mDatabaseReference = mDatabaseReference;
        this.currentMonth = currentMonth;
        this.moneyRemainingValueTextView = moneyRemainingValueTextView;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new regularExpense has been added, add it to the displayed list
                Expense regularExpense = dataSnapshot.getValue(Expense.class);

                // [START_EXCLUDE]
                // Update RecyclerView
//                mCommentIds.add(dataSnapshot.getKey());
                regularExpensesIds.add(dataSnapshot.getKey());
                regularExpenses.add(regularExpense);

                String moneyRemainingValue = moneyRemainingValueTextView.getText().toString();
                if (regularExpense != null && !moneyRemainingValue.isEmpty()) {
                    Double as = Double.parseDouble(moneyRemainingValue);
                    as -= regularExpense.expense;
                    String text = as.toString();
                    moneyRemainingValueTextView.setText(text);
                }
                notifyItemInserted(regularExpenses.size() - 1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Expense newRegularExpense = dataSnapshot.getValue(Expense.class);
                String regularExpenseKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int regularExpenseIndex = regularExpensesIds.indexOf(regularExpenseKey);
                if (regularExpenseIndex > -1) {
                    // Replace with the new data
                    String moneyRemainingValue = moneyRemainingValueTextView.getText().toString();
                    if (newRegularExpense != null && !moneyRemainingValue.isEmpty()) {
                        Double as = Double.parseDouble(moneyRemainingValue);
                        as -= Math.abs(newRegularExpense.expense - regularExpenses.get(regularExpenseIndex).expense);
                        String text = as.toString();
                        moneyRemainingValueTextView.setText(text);
                    }

                    regularExpenses.set(regularExpenseIndex, newRegularExpense);

                    // Update the RecyclerView
                    notifyItemChanged(regularExpenseIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + regularExpenseKey);
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

        mDatabaseReference.orderByChild(MONTH).equalTo(currentMonth).addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
//        mChildEventListener = childEventListener;
    }

    @NonNull
    @Override
    public RegularExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new RegularExpensesViewHolder(inflater.inflate(R.layout.item_expense_plan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RegularExpensesViewHolder viewHolder, int position) {
        viewHolder.bindToPost(regularExpenses.get(position));

        String id = regularExpensesIds.get(position);
        Expense oldRegularExpense = regularExpenses.get(position);

//        final DatabaseReference expenseRef =  mDatabase.child(IRREGULAR_EXPENSES);
        viewHolder.addExpensesToPlanButton.setOnClickListener(v -> {
            String expenseValue = viewHolder.addExpensesToPlanEditText.getText().toString().trim();
            try {
                Integer integer = Integer.valueOf(expenseValue);
                mDatabaseReference.child(id).child(EXPENSE).setValue(oldRegularExpense.expense + integer.longValue())
                        .addOnCompleteListener(task -> Toast.makeText(context, "Changed Successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(task -> Toast.makeText(context, "Changing Fail!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Toast.makeText(context, WRONG_TYPE, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return regularExpenses.size();
    }
}