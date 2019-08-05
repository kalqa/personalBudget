package com.example.mybudgetv2.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.models.Expense;

public class CurrentExpensesViewHolder extends RecyclerView.ViewHolder {

    public Button addExpensesToCategoryButton;
    public EditText addExpensesToCategoryEditText;

    private TextView categoryExpensesTextView;
    private TextView categoryNameTextView;

    public CurrentExpensesViewHolder(View itemView) {
        super(itemView);

        categoryExpensesTextView = itemView.findViewById(R.id.categoryExpensesTextView);
        categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
        addExpensesToCategoryButton = itemView.findViewById(R.id.addExpensesToCategoryButton);
        addExpensesToCategoryEditText = itemView.findViewById(R.id.addExpensesToCategoryEditText);
    }

    public void bindToPost(Expense expense) {
        categoryExpensesTextView.setText(expense.expense.toString());
        categoryNameTextView.setText(expense.categoryName);
    }
}