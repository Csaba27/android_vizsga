package com.example.trackerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private final ArrayList<Expense> expenses;

    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Expense> expenses) {
        super(context, resource, expenses);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Expense expense = expenses.get(position);
        TextView tvDescription = convertView.findViewById(R.id.tvDescr);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        tvDescription.setText(expense.getDescription());
        tvCategory.setText(String.format("Category: %s", expense.getCategory()));
        tvAmount.setText(String.format("Amount: $%.2f", expense.getAmount()));

        return convertView;
    }
}
