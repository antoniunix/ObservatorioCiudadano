package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.util.BottomNavigationViewHelper;
import net.gshp.observatoriociudadano.util.Config;

public class ListStation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView txtTBDate, txtTBTitle;
    private BottomNavigationView bottomNavigationView;

    private void init() {
        txtTBDate = findViewById(R.id.txtTBDate);
        txtTBTitle = findViewById(R.id.txtTBTitle);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_station);
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtTBDate.setText(Config.formatDate());
        txtTBTitle.setText(R.string.label_station);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DtoBundle dtoBundle = new DtoBundle();
        switch (item.getItemId()) {
            case R.id.action_supervisor:
                dtoBundle.setIdTypeMenuReport(getResources().getInteger(R.integer.idPollSupervisor));
                startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                finish();
                break;
            case R.id.action_representative:
                break;
            case R.id.action_visit:
                startActivity(new Intent(this, Visit.class));
                finish();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return true;
    }
}
