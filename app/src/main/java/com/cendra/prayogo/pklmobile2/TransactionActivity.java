package com.cendra.prayogo.pklmobile2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cendra.prayogo.pklmobile2.db.ProductDbHelper;
import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private ContentValues loggedInPkl;
    private ProductDbHelper productDbHelper;

    private TextView title;
    private ListView listView;

    private List<ContentValues> productList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        this.loggedInPkl = PklAccountManager.getLoggedIn(TransactionActivity.this);

        this.title = (TextView) findViewById(R.id.activity_transaction_title);
        this.title.setText("TRANSAKSI PENJUALAN PKL " + loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL));

        this.listView = (ListView) findViewById(R.id.activity_transaction_list);
        this.productDbHelper = new ProductDbHelper(TransactionActivity.this);
        this.productList = this.productDbHelper.getAllProduct(this.loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL));

        String[] products = new String[this.productList.size()];
        for (int i = 0, len = products.length; i < len; ++i) {
            products[i] = this.productList.get(i).getAsString(ProductDbHelper.COLUMN_NAME_NAME);
        }

        this.adapter = new ArrayAdapter<>(this, R.layout.transaction_product, products);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int productId = productList.get(i).getAsInteger(ProductDbHelper.COLUMN_NAME_ID);
                Intent intent = new Intent(TransactionActivity.this, TransactionDetailActivity.class);
                intent.putExtra(TransactionDetailActivity.PRODUCT_ID, productId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionActivity.this, SplashOutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        onBackPressed();
    }

    public void recap(View view) {
        Intent intent = new Intent(TransactionActivity.this, RecapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void catalog(View view) {
        Intent intent = new Intent(TransactionActivity.this, CatalogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
