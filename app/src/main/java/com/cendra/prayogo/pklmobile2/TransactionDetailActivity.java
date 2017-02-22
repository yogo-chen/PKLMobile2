package com.cendra.prayogo.pklmobile2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cendra.prayogo.pklmobile2.db.ProductDbHelper;
import com.cendra.prayogo.pklmobile2.db.TransactionDbHelper;
import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class TransactionDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ContentValues loggedInPkl;
    private ProductDbHelper productDbHelper;
    private TransactionDbHelper transactionDbHelper;

    private TextView name;
    private TextView sellPriceText;
    private EditText quantity;

    private int productId;
    private int sellPrice;
    private String emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        this.loggedInPkl = PklAccountManager.getLoggedIn(TransactionDetailActivity.this);
        this.productDbHelper = new ProductDbHelper(TransactionDetailActivity.this);
        this.transactionDbHelper = new TransactionDbHelper(TransactionDetailActivity.this);

        this.name = (TextView) findViewById(R.id.activity_transaction_detail_name);
        this.sellPriceText = (TextView) findViewById(R.id.activity_transaction_detail_sell_price);
        this.quantity = (EditText) findViewById(R.id.activity_transaction_detail_quantity);

        this.productId = getIntent().getIntExtra(PRODUCT_ID, -1);
        this.emailText = this.loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL);

        ContentValues product = this.productDbHelper.getProduct(this.productId);
        this.name.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_NAME));
        this.sellPrice = product.getAsInteger(ProductDbHelper.COLUMN_NAME_SELL_PRICE);
        this.sellPriceText.setText(this.sellPrice + "");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionDetailActivity.this, TransactionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getQuantityField() {
        return this.quantity.getText().toString().trim();
    }

    public void cancel(View view) {
        onBackPressed();
    }

    public void process(View view) {
        int quantity = 0;
        try {
            quantity = Integer.parseInt(getQuantityField());
        } catch (NumberFormatException e) {
        }
        if (quantity == 0) {
            makeText(TransactionDetailActivity.this, "Mohon isi kolom kuantitas", Toast.LENGTH_LONG).show();
        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            String date = df.format(c.getTime());
            this.transactionDbHelper.insertTransaction(this.productId, this.sellPrice, quantity, date, this.emailText);
            makeText(TransactionDetailActivity.this, "Transaksi produk " + name.getText().toString() + " sebanyak " + quantity + " berhasil dilakukan", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}
