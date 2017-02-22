package com.cendra.prayogo.pklmobile2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText birthday;

    private Calendar birthdayCalendar;
    private DatePickerDialog birthdayDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.email = (EditText) findViewById(R.id.activity_login_email);
        this.birthday = (EditText) findViewById(R.id.activity_login_birthday);

        this.birthdayCalendar = Calendar.getInstance();
        this.birthdayDatePickerDialog = new DatePickerDialog(
                LoginActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthdayCalendar.set(Calendar.YEAR, year);
                        birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                        birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
                        birthday.setText(dateFormat.format(birthdayCalendar.getTime()));
                    }
                },
                birthdayCalendar.get(Calendar.YEAR),
                birthdayCalendar.get(Calendar.MONTH),
                birthdayCalendar.get(Calendar.DAY_OF_MONTH)
        );
        this.birthdayDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        this.birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!birthdayDatePickerDialog.isShowing()) {
                    birthdayDatePickerDialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, SplashOutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getEmailField() {
        return this.email.getText().toString().trim().toLowerCase();
    }

    private String getBirthdayField() {
        return this.birthday.getText().toString().trim();
    }

    public void login(View view) {
        String emailText = getEmailField();
        String birthdayText = getBirthdayField();

        if (emailText.equals("")) {
            makeText(LoginActivity.this, "Mohon isi kolom user name", Toast.LENGTH_LONG).show();
        } else if (birthdayText.equals("")) {
            makeText(LoginActivity.this, "Mohon isi kolom password", Toast.LENGTH_LONG).show();
        } else {
            boolean canLogin = PklAccountManager.login(LoginActivity.this, emailText, birthdayText);
            if (!canLogin) {
                makeText(LoginActivity.this, "Mohon maaf user name atau password yang anda masukkan salah, silakan ulangi login, atau Registrasi jika belum terdaftar", Toast.LENGTH_LONG).show();
            } else {
                makeText(LoginActivity.this, "Selamat anda telah berhasil login, selanjutnya silakan mengelola katalog atau bertransaksi pada halaman selanjutnya!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
