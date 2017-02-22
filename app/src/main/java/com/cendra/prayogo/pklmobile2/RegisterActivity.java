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

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText birthday;
    private EditText product;

    private Calendar birthdayCalendar;
    private DatePickerDialog birthdayDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.email = (EditText) findViewById(R.id.activity_register_email);
        this.name = (EditText) findViewById(R.id.activity_register_name);
        this.address = (EditText) findViewById(R.id.activity_register_address);
        this.phone = (EditText) findViewById(R.id.activity_register_phone);
        this.birthday = (EditText) findViewById(R.id.activity_register_birthday);
        this.product = (EditText) findViewById(R.id.activity_register_product);

        this.birthdayCalendar = Calendar.getInstance();
        this.birthdayDatePickerDialog = new DatePickerDialog(
                RegisterActivity.this,
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
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getEmailField() {
        return this.email.getText().toString().trim().toLowerCase();
    }

    private String getNameField() {
        return this.name.getText().toString().trim();
    }

    private String getAddressField() {
        return this.address.getText().toString().trim();
    }

    private String getPhoneField() {
        return this.phone.getText().toString().trim();
    }

    private String getBirthdayField() {
        return this.birthday.getText().toString().trim();
    }

    private String getProductField() {
        return this.product.getText().toString().trim();
    }

    public void save(View view) {
        String emailText = getEmailField();
        String nameText = getNameField();
        String addressText = getAddressField();
        String phoneText = getPhoneField();
        String birthdayText = getBirthdayField();
        String productText = getProductField();

        if (emailText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom alamat email", Toast.LENGTH_LONG).show();
        } else if (nameText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom nama lengkap", Toast.LENGTH_LONG).show();
        } else if (addressText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom alamat lapak", Toast.LENGTH_LONG).show();
        } else if (phoneText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom nomor hp", Toast.LENGTH_LONG).show();
        } else if (birthdayText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom tanggal lahir", Toast.LENGTH_LONG).show();
        } else if (productText.equals("")) {
            makeText(RegisterActivity.this, "Mohon isi kolom produk unggul", Toast.LENGTH_LONG).show();
        } else {
            boolean canRegister = PklAccountManager.register(RegisterActivity.this, emailText, nameText, addressText, phoneText, birthdayText);
            if (!canRegister) {
                makeText(RegisterActivity.this, "Registrasi gagal, alamat email " + emailText + " telah digunakan!", Toast.LENGTH_LONG).show();
            } else {
                makeText(RegisterActivity.this, "Selamat anda telah terdaftar, silakan login untuk menggunakan aplikasi ini! Saat login, gunakan alamat email yang anda telah daftarkan sebagai " + emailText + "!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
