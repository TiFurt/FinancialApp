package br.com.tfurtado.financialapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CalculateActivity extends AppCompatActivity {
    private double cdi = 10.4;
    private double cdiPercentage = 100;
    private int workingDays = 252;
    private double realCdi = (cdi * cdiPercentage) / 100;
    private double IR = 22.5;

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

        long investedTime = getIntent().getLongExtra("investedTime", new Date().getTime());
        double investedMoney = getIntent().getFloatExtra("investedMoney", 0);

        final Duration investedDuration = calculateCalendarDays(investedTime);
        final double calculateGrossPercentageProfit = calculatePercentageProfit(investedDuration.toDays());
        final double calculateGrossProfit = calculateGrossProfit(calculateGrossPercentageProfit, investedMoney);
        final double calculateIR = calculateIR(calculateGrossProfit);

        setStartAppliedValue(investedMoney);
        setCalendarDays(investedDuration);
        setGrossValues(investedMoney, calculateGrossProfit);
        setIR(calculateIR);
        setLiquidValue(investedMoney, calculateGrossProfit, calculateIR);
        setMonthProfit();
        setCdiPercentage();
        setYearlyIncome();
        setPeriodIncome(calculateGrossPercentageProfit);
        setRedemptionDate(investedTime);

        listenToSimulateAgainOnClick();
    }

    private Duration calculateCalendarDays(long investedTime) {
        final Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        resetTimeToZero(currentDate);
        final Timestamp newDate = new Timestamp(investedTime);
        resetTimeToZero(newDate);
        Duration diff = Duration.between(currentDate.toInstant(), newDate.toInstant());

        return diff;
    }

    private double calculatePercentageProfit(long investedDuration) {
        return Math.pow(1 + (realCdi / 100), (double) investedDuration / workingDays) - 1;
    }

    private double calculateGrossProfit(double calculateGrossPercentageProfit, double investedMoney) {
        return (double) (investedMoney * calculateGrossPercentageProfit);
    }

    private double calculateIR(double calculateGrossProfit) {
        return calculateGrossProfit * (IR / 100);
    }

    private void setStartAppliedValue(double investedMoney) {
        TextView startAppliedValue = findViewById(R.id.start_applied_value);
        startAppliedValue.setText("R$ " + investedMoney);
    }

    private void setCalendarDays(Duration investedDuration) {
        TextView calendarDays = findViewById(R.id.calendar_days);
        calendarDays.setText(investedDuration.toDays() + " dias");
    }

    private void setGrossValues(double investedMoney, double calculateGrossProfit) {
        TextView grossProfitTitle = findViewById(R.id.gross_value_label);
        double grossValue = investedMoney + calculateGrossProfit;

        grossProfitTitle.setText("R$ " + String.format("%.2f", grossValue));
        TextView grossProfit = findViewById(R.id.gross_value_invested);
        grossProfit.setText("R$ " + String.format("%.2f", grossValue));
        TextView incomeValueLabel = findViewById(R.id.income_value);
        incomeValueLabel.setText("R$ " + String.format("%.2f", calculateGrossProfit));
        TextView totalYieldLabel = findViewById(R.id.total_yield_label);
        totalYieldLabel.setText("R$ " + String.format("%.2f", calculateGrossProfit));
    }

    private void setIR(double calculateIR) {
        TextView irValue = findViewById(R.id.tax);
        irValue.setText("R$ " + String.format("%.2f", calculateIR));
    }

    private void setLiquidValue(double investedMoney, double calculateGrossProfit, double calculateIR) {
        TextView liquidValue = findViewById(R.id.liquid_value);
        double liquid = investedMoney + calculateGrossProfit - calculateIR;
        liquidValue.setText("R$ " + String.format("%.2f", liquid));
    }

    private void setMonthProfit() {
        TextView monthProfit = findViewById(R.id.monthly_income);
        double monthProfitValue = calculatePercentageProfit(30);
        monthProfit.setText(String.format("%.2f", monthProfitValue * 100) + "%");
    }

    private void setCdiPercentage() {
        TextView percentageIncome = findViewById(R.id.percentage_income);
        percentageIncome.setText(String.format("%.2f", cdiPercentage) + "%");
    }

    private void setYearlyIncome() {
        TextView yearlyIncome = findViewById(R.id.yearly_income);
        yearlyIncome.setText(String.format("%.2f", realCdi) + "%");
    }

    private void setPeriodIncome(double calculateGrossPercentageProfit) {
        TextView periodIncome = findViewById(R.id.period_income);
        periodIncome.setText(String.format("%.2f", calculateGrossPercentageProfit * 100) + "%");
    }

    private void setRedemptionDate(long investedTime) {
        TextView redemptionDate = findViewById(R.id.redemption_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        redemptionDate.setText(new Timestamp(investedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter));
    }

    private void resetTimeToZero(Timestamp date) {
        date.setNanos(0);
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
    }

    private void listenToSimulateAgainOnClick() {
        findViewById(R.id.simulate_again_button).setOnClickListener(view -> {
            finish();
        });
    }
}