package net.gshp.observatoriociudadano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
import net.gshp.observatoriociudadano.listener.OnFinishThread;
import net.gshp.observatoriociudadano.model.ModelSplash;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity implements OnFinishThread {

    private Timer timer;
    private WeakReference<Splash> weakReference;
    private SharedPreferences preferences;
    private ModelSplash modelSplash;

    public void init() {
        modelSplash = new ModelSplash(this);
        timer = new Timer();
        weakReference = new WeakReference<Splash>(this);
        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        init();
        if (preferences.getInt(getString(R.string.DPI), 0) == 0)
            setDpi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!modelSplash.fillSepomex()) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Splash activity = weakReference.get();
                    if (activity != null && !activity.isFinishing()) {
                        startActivity(new Intent(Splash.this, Home.class));
                        finish();
                    }
                }
            };
            timer.schedule(timerTask, 1500);
        } else {
            Snackbar.make(findViewById(R.id.activity_splash), R.string.Splash_configuration_init, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        }
    }

    private void setDpi() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        switch (metrics.densityDpi) {
            case 160:
                //preferences.edit().putInt(DPI, 160).putInt(IMAGE_SIZE, 60).apply();
                preferences.edit().putInt(getString(R.string.DPI), 160).putInt(getString(R.string.IMAGE_SIZE), 120).apply();
                break;
            case 240:
                //preferences.edit().putInt(DPI, 240).putInt(IMAGE_SIZE, 90).apply();
                preferences.edit().putInt(getString(R.string.DPI), 240).putInt(getString(R.string.IMAGE_SIZE), 180).apply();
                break;
            case 320:
                //preferences.edit().putInt(DPI, 320).putInt(IMAGE_SIZE, 120).apply();
                preferences.edit().putInt(getString(R.string.DPI), 320).putInt(getString(R.string.IMAGE_SIZE), 240).apply();
                break;
            case 480:
                //preferences.edit().putInt(DPI, 480).putInt(IMAGE_SIZE, 180).apply();
                preferences.edit().putInt(getString(R.string.DPI), 480).putInt(getString(R.string.IMAGE_SIZE), 360).apply();
                break;
            case 640:
                //preferences.edit().putInt(DPI, 640).putInt(IMAGE_SIZE, 240).apply();
                preferences.edit().putInt(getString(R.string.DPI), 640).putInt(getString(R.string.IMAGE_SIZE), 480).apply();
                break;
        }
    }

    @Override
    public void onFinishThread() {
        startActivity(new Intent(Splash.this, Home.class));
        finish();
    }
}
