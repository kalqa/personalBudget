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
import com.example.mybudgetv2.models.IrregularExpense;
import com.example.mybudgetv2.viewholder.IrregularExpensesViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.mybudgetv2.fragments.CurrentExpensesFragment.EXPENSE;
import static com.example.mybudgetv2.fragments.CurrentExpensesFragment.WRONG_TYPE;

public class IrregularExepensesPlansAdapter extends RecyclerView.Adapter<IrregularExpensesViewHolder> {

    private static final String TAG = "IrregularExepensesPlans";

    private Context context;
    private List<IrregularExpense> irregularExpenses;
    private DatabaseReference mDatabaseReference;
    private TextView moneyRemainingValueTextView;
    private List<String> irregularExpensesIds = new ArrayList<>();
//    private ChildEventListener mChildEventListener;

    public IrregularExepensesPlansAdapter(Context context, List<IrregularExpense> irregularExpenses, DatabaseReference mDatabaseReference, TextView moneyRemainingValueTextView) {
        this.context = context;
        this.irregularExpenses = irregularExpenses;
        this.mDatabaseReference = mDatabaseReference;
        this.moneyRemainingValueTextView = moneyRemainingValueTextView;

        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new irregularExpense has been added, add it to the displayed list
                IrregularExpense irregularExpense = dataSnapshot.getValue(IrregularExpense.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                irregularExpensesIds.add(dataSnapshot.getKey());
                irregularExpenses.add(irregularExpense);
                String moneyRemainingValue = moneyRemainingValueTextView.getText().toString();
                if (irregularExpense!= null && !moneyRemainingValue.isEmpty()) {
                    Double as = Double.parseDouble(moneyRemainingValue);
                    as -= irregularExpense.expense;
                    String text = as.toString();
                    moneyRemainingValueTextView.setText(text);
                }
                notifyItemInserted(irregularExpenses.size() - 1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                IrregularExpense newIrregularExpense = dataSnapshot.getValue(IrregularExpense.class);
                String irregularExpenseKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int irregularExpenseIndex = irregularExpensesIds.indexOf(irregularExpenseKey);
                if (irregularExpenseIndex > -1) {
                    // Replace with the new data

                    String moneyRemainingValue = moneyRemainingValueTextView.getText().toString();
                    if (newIrregularExpense!= null && !moneyRemainingValue.isEmpty()) {
                        Double as = Double.parseDouble(moneyRemainingValue);
                        as -= Math.abs(newIrregularExpense.expense - irregularExpenses.get(irregularExpenseIndex).expense);
                        String text = as.toString();
                        moneyRemainingValueTextView.setText(text);
                    }

                    irregularExpenses.set(irregularExpenseIndex, newIrregularExpense);
                    // Update the RecyclerView
                    notifyItemChanged(irregularExpenseIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + irregularExpenseKey);
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

        mDatabaseReference.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
//        mChildEventListener = childEventListener;
    }

    @NonNull
    @Override
    public IrregularExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new IrregularExpensesViewHolder(inflater.inflate(R.layout.item_irregular_expense_plan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IrregularExpensesViewHolder viewHolder, int position) {
        viewHolder.bindToPost(irregularExpenses.get(position));

        String id = irregularExpensesIds.get(position);
        IrregularExpense oldIrregularExpense = irregularExpenses.get(position);

//        final DatabaseReference expenseRef =  mDatabase.child(IRREGULAR_EXPENSES);
        viewHolder.addIrregularExpenseToPlanButton.setOnClickListener(v -> {
            String expenseValue = viewHolder.addIrregularExpenseToPlanEditText.getText().toString().trim();
            try {
                Integer integer = Integer.valueOf(expenseValue);
                mDatabaseReference.child(id).child(EXPENSE).setValue(oldIrregularExpense.expense + integer.longValue())
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
        return irregularExpenses.size();
    }
}