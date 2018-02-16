package net.gshp.observatoriociudadano.faceDetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import net.gshp.observatoriociudadano.R;

/**
 * Created by alejandro on 16/01/18.
 */

public class PhotoWizardActivity extends AppCompatActivity {

    private static final String TAG = "PhotoWizardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wizard);
        getSupportActionBar().hide();

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PhotoWizardActivity.this, PhotosActivity.class));
            }
        });
    }
}
