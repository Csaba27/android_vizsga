package com.example.trackerapp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Expense implements Serializable {
    private String description;
    private float amount;
    private String category;

    public Expense(String description, float amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static List<Expense> populateExpense() {
        return Arrays.asList(
                new Expense("Groceries", 50.0f, "Food"),
                new Expense("Utilities", 100.00f, "Bills"),
                new Expense("Transportation", 30.00f, "Transportation"),
                new Expense("Entertainment", 20.00f, "Entertainment"),
                new Expense("Clothing", 40.00f, "Shopping")
        );
    }
}
