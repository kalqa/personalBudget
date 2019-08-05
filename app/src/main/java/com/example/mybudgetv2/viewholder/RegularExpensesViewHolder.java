package com.example.mybudgetv2.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.models.Expense;

public class RegularExpensesViewHolder extends RecyclerView.ViewHolder {

    public Button addExpensesToPlanButton;
    public EditText addExpensesToPlanEditText;

    private TextView expenseNameTextView;
    private TextView expenseValueTextView;

    public RegularExpensesViewHolder(View itemView) {
        super(itemView);

        expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
        expenseValueTextView = itemView.findViewById(R.id.expenseValueTextView);
        addExpensesToPlanButton = itemView.findViewById(R.id.addExpensesToPlanButton);
        addExpensesToPlanEditText = itemView.findViewById(R.id.addExpensesToPlanEditText);
    }

    public void bindToPost(Expense expense) {
        expenseValueTextView.setText(expense.expense.toString());
        expenseNameTextView.setText(expense.categoryName);
    }
}