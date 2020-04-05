package com.database.studentlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        dm = new DisplayMetrics();
        SplashScreen.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                            Intent login = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(login);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                } catch (InterruptedException ex) {

                }
            }
        }.start();
    }
}

