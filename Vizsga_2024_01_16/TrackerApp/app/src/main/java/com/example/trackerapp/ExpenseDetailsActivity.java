package com.example.trackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExpenseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtAmount = findViewById(R.id.txtAmount);

        txtTitle.setTextColor(getResources().getColor(R.color.purple_500));
        txtCategory.setTextColor(getResources().getColor(R.color.black));
        txtAmount.setTextColor(getResources().getColor(R.color.pink));

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        if (data != null) {
            if (data.containsKey("expense")) {
                Expense expense = (Expense) data.get("expense");
                txtTitle.setText(String.format("Title: %s", expense.getDescription()));
                txtCategory.setText(String.format("Category: %s", expense.getCategory()));
                txtAmount.setText(String.format("Amount: $%.2f", expense.getAmount()));
            }
        }
    }
}