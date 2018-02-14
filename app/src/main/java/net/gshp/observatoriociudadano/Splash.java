package net.gshp.observatoriociudadano;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {


    private final static Long TIME_OF_SPLASH = 3000L;
    private Timer timer;
    private Context context;
    private WeakReference<Splash> weakReference;

    public void init() {
        context = this;
        timer = new Timer();
        weakReference = new WeakReference<>(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        init();

    }

    @Override
    protected void onResume() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Splash activity = weakReference.get();
                if (activity != null && !activity.isFinishing()) {
                    startActivity(new Intent(context, Login.class));
                    finish();
                }
            }
        };
        timer.schedule(timerTask, TIME_OF_SPLASH);
        super.onResume();
    }

}
