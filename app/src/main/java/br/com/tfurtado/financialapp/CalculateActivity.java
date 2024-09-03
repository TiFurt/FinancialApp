package br.com.tfurtado.financialapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class CalculateActivity extends AppCompatActivity {
    private double cdi = 11.5;
    private double cdiPercentage = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Date investedTime = (Date) getIntent().getSerializableExtra("investedTime");

        calculateCalendarDays(investedTime);

        listenToSimulateAgainOnClick();
    }

    private void calculateCalendarDays(Date investedTime) {
        Date currentDate = new Date();
        long diff = currentDate.getTime() - investedTime.getTime();

        TextView calendarDays = findViewById(R.id.calendar_days);
        calendarDays.setText(String.valueOf(diff / (1000 * 60 * 60 * 24)));
    }

    private void listenToSimulateAgainOnClick() {
        findViewById(R.id.simulate_again_button).setOnClickListener(view -> {
            finish();
        });
    }
}