package com.cendra.prayogo.pklmobile2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cendra.prayogo.pklmobile2.db.ProductDbHelper;
import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

import static android.widget.Toast.makeText;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ContentValues loggedInPkl;
    private ProductDbHelper productDbHelper;

    private EditText name;
    private EditText basePrice;
    private EditText sellPrice;

    private int productId;
    private boolean isAddItem;
    private String emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        this.loggedInPkl = PklAccountManager.getLoggedIn(ProductDetailActivity.this);
        this.productDbHelper = new ProductDbHelper(ProductDetailActivity.this);

        this.name = (EditText) findViewById(R.id.activity_product_detail_name);
        this.basePrice = (EditText) findViewById(R.id.activity_product_detail_base_price);
        this.sellPrice = (EditText) findViewById(R.id.activity_product_detail_sell_price);

        this.productId = getIntent().getIntExtra(PRODUCT_ID, -1);
        this.isAddItem = this.productId < 0;
        this.emailText = this.loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL);

        if (!this.isAddItem) {
            ContentValues product = this.productDbHelper.getProduct(this.productId);
            this.name.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_NAME));
            this.basePrice.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_BASE_PRICE));
            this.sellPrice.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_SELL_PRICE));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailActivity.this, CatalogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getNameField() {
        return this.name.getText().toString().trim();
    }

    private String getBasePriceField() {
        return this.basePrice.getText().toString().trim();
    }

    private String getSellPriceField() {
        return this.sellPrice.getText().toString().trim();
    }

    public void save(View view) {
        String nameText = getNameField();
        int basePrice = 0;
        try {
            basePrice = Integer.parseInt(getBasePriceField());
        } catch (NumberFormatException e) {
        }
        int sellPrice = 0;
        try {
            sellPrice = Integer.parseInt(getSellPriceField());
        } catch (NumberFormatException e) {
        }
        if (nameText.equals("")) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom nama produk", Toast.LENGTH_LONG).show();
        } else if (basePrice == 0) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom harga pokok", Toast.LENGTH_LONG).show();
        } else if (sellPrice == 0) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom harga jual", Toast.LENGTH_LONG).show();
        } else if (isAddItem) {
            this.productDbHelper.insertProduct(nameText, basePrice, sellPrice, this.emailText);
            makeText(ProductDetailActivity.this, "Produk " + nameText + " berhasil ditambahkan", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            this.productDbHelper.updateProduct(this.productId, nameText, basePrice, sellPrice);
            makeText(ProductDetailActivity.this, "Produk " + nameText + " berhasil di-update", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    public void add(View view) {
        String nameText = getNameField();
        int basePrice = 0;
        try {
            basePrice = Integer.parseInt(getBasePriceField());
        } catch (NumberFormatException e) {
        }
        int sellPrice = 0;
        try {
            sellPrice = Integer.parseInt(getSellPriceField());
        } catch (NumberFormatException e) {
        }
        if (nameText.equals("")) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom nama produk", Toast.LENGTH_LONG).show();
        } else if (basePrice == 0) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom harga pokok", Toast.LENGTH_LONG).show();
        } else if (sellPrice == 0) {
            makeText(ProductDetailActivity.this, "Mohon isi kolom harga jual", Toast.LENGTH_LONG).show();
        } else if (isAddItem) {
            this.productDbHelper.insertProduct(nameText, basePrice, sellPrice, this.emailText);
            makeText(ProductDetailActivity.this, "Produk " + nameText + " berhasil ditambahkan", Toast.LENGTH_LONG).show();
            this.name.setText("");
            this.basePrice.setText("");
            this.sellPrice.setText("");
        } else {
            this.productDbHelper.updateProduct(this.productId, nameText, basePrice, sellPrice);
            makeText(ProductDetailActivity.this, "Produk " + nameText + " berhasil diperbaharui", Toast.LENGTH_LONG).show();
            this.productId = -1;
            this.isAddItem = true;
            this.name.setText("");
            this.basePrice.setText("");
            this.sellPrice.setText("");
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
