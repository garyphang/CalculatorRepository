package com.example.phang.gary.myapplication;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sp = getSharedPreferences("MyData", MODE_PRIVATE);

        String mr = sp.getString("MR", null);
        String tr = sp.getString("TR", null);
        String ti = sp.getString("TI", null);
        String mi = sp.getString("MI", null);

        etLoanAmount = findViewById(R.id.loan_amount);
        etDownPayment = findViewById(R.id.down_payment);
        etTerm = findViewById(R.id.term);
        etAnnualInterestRate = findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment = findViewById(R.id.monthly_repayment);
        tvTotalRepayment = findViewById(R.id.total_repayment);
        tvTotalInterest = findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = findViewById(R.id.average_monthly_interest);

        tvMonthlyPayment.setText(mr==null? getString(R.string.default_result):mr);
        tvTotalRepayment.setText(tr==null? getString(R.string.default_result):tr);
        tvTotalInterest.setText(ti==null? getString(R.string.default_result):ti);
        tvAverageMonthlyInterest.setText(mi==null? getString(R.string.default_result):mi);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_calculate:
                Log.d("Check", "Calculate!");
                calculate();
                break;
            case R.id.button_reset:
                Log.d("Check", "Reset!");
                reset();
                break;
        }
    }

    private void calculate() {
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        double loanAmount = Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term) * 12);
        if (noOfMonth > 0) {
            double monthlyRepayment = loanAmount * (interest + (interest / (java.lang.Math.pow((1 + interest), noOfMonth) - 1)));
            double totalRepayment = monthlyRepayment * noOfMonth;
            double totalInterest = totalRepayment - loanAmount;
            double monthlyInterest = totalInterest / noOfMonth;

            tvMonthlyPayment.setText(String.valueOf(monthlyRepayment));
            tvTotalRepayment.setText(String.valueOf(totalRepayment));
            tvTotalInterest.setText(String.valueOf(totalInterest));
            tvAverageMonthlyInterest.setText(String.valueOf(monthlyInterest));

            SharedPreferences.Editor editor = sp.edit();

            editor.putString("MR",String.valueOf(monthlyRepayment));
            editor.putString("TR",String.valueOf(totalRepayment));
            editor.putString("TI",String.valueOf(totalInterest));
            editor.putString("MI",String.valueOf(monthlyInterest));

            editor.commit();
        }
    }

    private void reset () {
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }
}
