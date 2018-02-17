package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.gshp.observatoriociudadano.adapter.RVListStation;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelListStation;
import net.gshp.observatoriociudadano.util.BottomNavigationViewHelper;
import net.gshp.observatoriociudadano.util.Config;

public class ListStation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        OnItemClickListenerRV {

    private TextView txtTBDate, txtTBTitle;
    private RecyclerView rcvVisit;
    private BottomNavigationView bottomNavigationView;
    private LinearLayoutManager lmy;
    private ModelListStation model;
    private RVListStation adapter;

    private void init() {
        txtTBDate = findViewById(R.id.txtTBDate);
        txtTBTitle = findViewById(R.id.txtTBTitle);
        rcvVisit = findViewById(R.id.rcvVisit);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        model = new ModelListStation();
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        lmy = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        new ModelInfoPerson(this).loadImage(this).loadInfo();
        adapter = model.getAdapter(this);

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
        rcvVisit.setLayoutManager(lmy);
        rcvVisit.setAdapter(adapter);
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

    @Override
    public void onItemClickListener(View v, int position) {
        DtoPdvPdv dtoPdvPdv = adapter.getItem(position);
        DtoBundle dtoBundle = new DtoBundle();
        dtoBundle.setIdTypeMenuReport(getResources().getInteger(R.integer.idPollRepresentanteCasilla)).
                setIdPdv(dtoPdvPdv.getId());
        startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
        finish();
    }
}
