package com.example.trackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Expense> expenses;
    ExpenseAdapter adapter;
    ListView listView;
    TextView tvTotal;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pref = getPreferences(MODE_PRIVATE);

        tvTotal = findViewById(R.id.tvTotal);
        listView = findViewById(R.id.listView);

        expenses = new ArrayList<>(loadList());

        adapter = new ExpenseAdapter(this, R.layout.list_item, expenses);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Expense expense = expenses.get(i);
                Intent intent = new Intent(MainActivity.this, ExpenseDetailsActivity.class);
                intent.putExtra("expense", expense);
                startActivity(intent);
            }
        });
    }

    public List<Expense> loadList() {
        List<Expense> list = new ArrayList<>();
        float total = 0f;

        if (pref.contains("expenses")) {
            String json = pref.getString("expenses", null);

            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject expenseObj = array.getJSONObject(i);
                    String descr = expenseObj.getString("description");
                    String category = expenseObj.getString("category");
                    float amount = (float) expenseObj.getDouble("amount");
                    list.add(new Expense(descr, amount, category));
                }
            } catch (JSONException e) {
                Toast.makeText(this, "Érvénytelen json", Toast.LENGTH_LONG).show();
            }
        } else {
            list = Expense.populateExpense();
        }

        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getAmount();
        }

        setTotal(total);
        Toast.makeText(this, "Betöltve", Toast.LENGTH_LONG).show();
        return list;
    }

    public void saveList() {
        SharedPreferences.Editor edt = pref.edit();
        float total = 0f;

        if (expenses.isEmpty()) {
            edt.remove("expenses");
        } else {
            JSONArray array = new JSONArray();
            for (int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("description", expense.getDescription());
                    obj.put("category", expense.getCategory());
                    obj.put("amount", expense.getAmount());
                    total += expense.getAmount();
                    array.put(obj);
                } catch (JSONException ignored) {
                }
            }
            edt.putString("expenses", array.toString());
        }
        setTotal(total);
        edt.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            expenses.add(new Expense("Description example", 500.0f, "Category"));
            saveList();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Hozzáadva", Toast.LENGTH_LONG).show();
        } else if (id == R.id.delete) {
            expenses.clear();
            saveList();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Törölve", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTotal(float amount) {
        tvTotal.setText(String.format("Total: $%.2f", amount));
    }

    @Override
    protected void onPause() {
        saveList();
        super.onPause();
    }
}