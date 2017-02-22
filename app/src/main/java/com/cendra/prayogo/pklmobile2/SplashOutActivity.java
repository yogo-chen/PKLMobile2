package com.cendra.prayogo.pklmobile2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cendra.prayogo.pklmobile2.service.PklAccountManager;

public class SplashOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_out);

        PklAccountManager.logout(SplashOutActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
    }
}
