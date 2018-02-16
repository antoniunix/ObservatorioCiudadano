package net.gshp.observatoriociudadano;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import net.gshp.observatoriociudadano.listener.OnFinishThread;
import net.gshp.observatoriociudadano.model.ModelSplash;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity implements OnFinishThread{

    private Timer timer;
    private Context context;
    private WeakReference<Splash> weakReference;
    private ModelSplash modelSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        init();

    }

    private void init() {
        modelSplash = new ModelSplash(this);
        timer = new Timer();
        weakReference = new WeakReference<Splash>(this);
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
                        startActivity(new Intent(Splash.this, Login.class));
                        finish();
                    }
                }
            };
            timer.schedule(timerTask,1500);
        }else {
            Snackbar.make(findViewById(R.id.activity_splash),R.string.Splash_configuration_init, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onFinishThread() {
        startActivity(new Intent(Splash.this, Login.class));
        finish();
    }
}
