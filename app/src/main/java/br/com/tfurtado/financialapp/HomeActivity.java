package br.com.tfurtado.financialapp;

import static java.lang.Float.parseFloat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private Date investedTime = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listenToCalendarView();
        listenToCalculateOnClick();
    }

    private void listenToCalculateOnClick() {
        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(view -> {
            EditText investedMoney = findViewById(R.id.invested_money);
            TextView error = findViewById(R.id.error);
            if (investedMoney.getText().toString().isEmpty()) {
                investedMoney.setError("Valor para investir é obrigatório");
                error.setText("Valor para investir é obrigatório");
                return;
            }
            Intent intent = new Intent(this, CalculateActivity.class);
            intent.putExtra("investedMoney", parseFloat(investedMoney.getText().toString()));
            intent.putExtra("investedTime", investedTime);
            startActivity(intent);
        });
    }

    private void listenToCalendarView() {
        CalendarView calendarView = findViewById(R.id.invested_time);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            investedTime = new Date(year, month, dayOfMonth);
        });
    }
}