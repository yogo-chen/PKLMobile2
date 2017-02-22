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

public class CatalogActivity extends AppCompatActivity {
    private ContentValues loggedInPkl;
    private ProductDbHelper productDbHelper;

    private TextView title;
    private ListView listView;

    private List<ContentValues> productList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        this.loggedInPkl = PklAccountManager.getLoggedIn(CatalogActivity.this);

        this.title = (TextView) findViewById(R.id.activity_catalog_title);
        this.title.setText("KATALOG PRODUK PKL " + loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL));

        this.listView = (ListView) findViewById(R.id.activity_catalog_list);
        this.productDbHelper = new ProductDbHelper(CatalogActivity.this);
        this.productList = this.productDbHelper.getAllProduct(this.loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL));

        String[] products = new String[this.productList.size()];
        for (int i = 0, len = products.length; i < len; ++i) {
            products[i] = this.productList.get(i).getAsString(ProductDbHelper.COLUMN_NAME_NAME);
        }

        this.adapter = new ArrayAdapter<>(this, R.layout.catalog_product, products);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int productId = productList.get(i).getAsInteger(ProductDbHelper.COLUMN_NAME_ID);
                Intent intent = new Intent(CatalogActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.PRODUCT_ID, productId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CatalogActivity.this, SplashOutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        onBackPressed();
    }

    public void transaction(View view) {
        Intent intent = new Intent(CatalogActivity.this, TransactionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void add(View view) {
        Intent intent = new Intent(CatalogActivity.this, ProductDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
