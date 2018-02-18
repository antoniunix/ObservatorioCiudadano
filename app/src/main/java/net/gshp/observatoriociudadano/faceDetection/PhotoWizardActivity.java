package net.gshp.observatoriociudadano.faceDetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoBundle;

/**
 * Created by alejandro on 16/01/18.
 */

public class PhotoWizardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wizard);
        getSupportActionBar().hide();

        final DtoBundle dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (getIntent().hasExtra(getString(R.string.user_roll)))
                    startActivity(new Intent(PhotoWizardActivity.this, PhotosActivity.class)
                            .putExtra(getString(R.string.user_roll), getIntent().getIntExtra(getString(R.string.user_roll),
                                    getResources().getInteger(R.integer.rollSupervisor)))
                            .putExtra(getString(R.string.is_reco), false).putExtra(getString(R.string.app_bundle_name), dtoBundle));
            }
        });
    }
}
