package com.example.mybudgetv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgetv2.R;
import com.example.mybudgetv2.adapters.BudgetHintsAdapter.SingleBudgetHintViewHolder;

import java.util.List;

public class BudgetHintsAdapter extends RecyclerView.Adapter<SingleBudgetHintViewHolder> {

    private List<String> hints;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BudgetHintsAdapter(List<String> hints) {
        this.hints = hints;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public SingleBudgetHintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SingleBudgetHintViewHolder(inflater.inflate(R.layout.item_budget_hint, parent, false));


//        // create a new view
//        TextView v = parent.findViewById(R.id.budgetHintTextView);
//
//        SingleBudgetHintViewHolder vh = new SingleBudgetHintViewHolder(v);
//        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SingleBudgetHintViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(hints.get(position));
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return hints.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SingleBudgetHintViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView textView;
        public SingleBudgetHintViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.budgetHintTextView);
        }
    }
}