package net.gshp.observatoriociudadano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.gshp.observatoriociudadano.util.Config;

public class Visit extends AppCompatActivity {

    private TextView txtTBDate;

    private void init(){
        txtTBDate=findViewById(R.id.txtTBDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtTBDate.setText(Config.formatDate());
    }
}
