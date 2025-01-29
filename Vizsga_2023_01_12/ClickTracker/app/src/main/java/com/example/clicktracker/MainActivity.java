package com.example.clicktracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private Button btnNum1;
    private EditText edtNum1;
    private EditText edtNum2;
    private ActivityResultLauncher<Intent> launcher;

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

        Button btnSecondActivity = findViewById(R.id.btnSecondActivity);
        Button btnThirdActivity = findViewById(R.id.btnThirdActivity);
        btnNum1 = findViewById(R.id.btnNum1);
        edtNum1 = findViewById(R.id.edtNum1);
        edtNum2 = findViewById(R.id.edtNum2);

        pref = getSharedPreferences("count", MODE_PRIVATE);

        if (pref.contains("num1") && pref.contains("num2")) {
            String num1 = pref.getString("num1", null);
            String num2 = pref.getString("num2", null);

            edtNum1.setText(num1);
            edtNum2.setText(num2);
            Toast.makeText(this, "Visszaállítva", Toast.LENGTH_SHORT).show();
        }

        btnNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(edtNum1.getText().toString());
                num++;
                edtNum1.setText(String.valueOf(num));
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    Intent data = o.getData();
                    if (data != null) {
                        String result = data.getStringExtra("result");
                        if (result != null) {
                            if (result.equals("ok")) {
                                String num = edtNum1.getText().toString();
                                Toast.makeText(MainActivity.this, String.format("Kattintasok száma: %s", num), Toast.LENGTH_SHORT).show();
                            } else if (result.equals("cancel")) {
                                Toast.makeText(MainActivity.this, "Cancel gombra kattintottál", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        btnSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = edtNum1.getText().toString();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("count", num);
                launcher.launch(intent);
            }
        });

        btnThirdActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    public void increaseNum(View view) {
        int num = Integer.parseInt(edtNum2.getText().toString());
        num++;
        edtNum2.setText(String.valueOf(num));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            SharedPreferences.Editor edt = pref.edit();
            String num1 = edtNum1.getText().toString();
            String num2 = edtNum2.getText().toString();
            edt.putString("num1", num1);
            edt.putString("num2", num2);
            edt.commit();

            Toast.makeText(this, "Mentve", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.reset) {
            SharedPreferences.Editor edt = pref.edit();
            edt.remove("num1");
            edt.remove("num2");
            edt.commit();

            edtNum1.setText("0");
            edtNum2.setText("0");
            Toast.makeText(this, "Nullázva", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}