package com.cendra.prayogo.pklmobile2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cendra.prayogo.pklmobile2.db.ProductDbHelper;
import com.cendra.prayogo.pklmobile2.db.TransactionDbHelper;
import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

import java.util.List;

public class RecapActivity extends AppCompatActivity {
    private ContentValues loggedInPkl;
    private TransactionDbHelper transactionDbHelper;
    private ProductDbHelper productDbHelper;

    private ListView listView;
    private TextView totalPrice;

    private List<ContentValues> transactionlist;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        this.loggedInPkl = PklAccountManager.getLoggedIn(RecapActivity.this);
        this.transactionDbHelper = new TransactionDbHelper(RecapActivity.this);
        this.productDbHelper = new ProductDbHelper(RecapActivity.this);

        this.listView = (ListView) findViewById(R.id.activity_recap_list);
        this.totalPrice = (TextView) findViewById(R.id.activity_recap_total_price);

        this.transactionlist = this.transactionDbHelper.getAllTransaction(this.loggedInPkl.getAsString(PklAccountManager.LOGGED_IN_EMAIL));

        int transactionCount = transactionlist.size();
        String[] dates = new String[transactionCount];
        String[] names = new String[transactionCount];
        int[] quantities = new int[transactionCount];
        int[] soldPrices = new int[transactionCount];
        int[] totalPrices = new int[transactionCount];
        int totalPrice = 0;

        for (int i = 0; i < transactionCount; ++i) {
            ContentValues transaction = transactionlist.get(i);
            ContentValues product = this.productDbHelper.getProduct(transaction.getAsInteger(TransactionDbHelper.COLUMN_NAME_PRODUCT_ID));
            dates[i] = transaction.getAsString(TransactionDbHelper.COLUMN_NAME_DATE);
            names[i] = product.getAsString(ProductDbHelper.COLUMN_NAME_NAME);
            quantities[i] = transaction.getAsInteger(TransactionDbHelper.COLUMN_NAME_QUANTITY);
            soldPrices[i] = transaction.getAsInteger(TransactionDbHelper.COLUMN_NAME_SOLD_PRICE);
            totalPrices[i] = quantities[i] * soldPrices[i];
            totalPrice += totalPrices[i];
        }

        this.adapter = new RecapAdapter(dates, names, quantities, soldPrices, totalPrices);
        this.listView.setAdapter(this.adapter);

        this.totalPrice.setText(totalPrice + "");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RecapActivity.this, SplashOutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void catalog(View view) {
        Intent intent = new Intent(RecapActivity.this, CatalogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void transaction(View view) {
        Intent intent = new Intent(RecapActivity.this, TransactionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        onBackPressed();
    }

    private class RecapAdapter extends ArrayAdapter {

        private String[] dates;
        private String[] names;
        private int[] quantities;
        private int[] soldPrices;
        private int[] totalPrices;
        private LayoutInflater inflater;

        RecapAdapter(String[] dates, String[] names, int[] quantities, int[] soldPrices, int[] totalPrices) {
            super(RecapActivity.this, R.layout.transaction_recap, dates);
            this.dates = dates;
            this.names = names;
            this.quantities = quantities;
            this.soldPrices = soldPrices;
            this.totalPrices = totalPrices;
            this.inflater = getLayoutInflater();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = this.inflater.inflate(R.layout.transaction_recap, null, true);

            TextView left = (TextView) v.findViewById(R.id.transaction_recap_left);
            TextView right = (TextView) v.findViewById(R.id.transaction_recap_right);

            left.setText((i + 1) + ". " + dates[i] + " " + names[i] + " " + quantities[i] + "x" + soldPrices[i]);
            right.setText(totalPrices[i] + "");
            return v;
        }
    }
}

