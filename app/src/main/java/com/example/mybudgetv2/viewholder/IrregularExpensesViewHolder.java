package com.example.mybudgetv2.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.models.IrregularExpense;

public class IrregularExpensesViewHolder extends RecyclerView.ViewHolder {

    public Button addIrregularExpenseToPlanButton;
    public EditText addIrregularExpenseToPlanEditText;

    private TextView irregularExpenseCategoryNameTextView;
    private TextView irregularExpenseValueTextView;

    public IrregularExpensesViewHolder(View itemView) {
        super(itemView);

        irregularExpenseCategoryNameTextView = itemView.findViewById(R.id.irregularExpenseCategoryNameTextView);
        irregularExpenseValueTextView = itemView.findViewById(R.id.irregularExpenseValueTextView);
        addIrregularExpenseToPlanButton = itemView.findViewById(R.id.addIrregularExpenseToPlanButton);
        addIrregularExpenseToPlanEditText = itemView.findViewById(R.id.addIrregularExpenseToPlanEditText);
    }

    public void bindToPost(IrregularExpense irregularExpense) {
        String text = irregularExpense.expense.toString();
        irregularExpenseValueTextView.setText(text);
        irregularExpenseCategoryNameTextView.setText(irregularExpense.categoryName);
    }
}