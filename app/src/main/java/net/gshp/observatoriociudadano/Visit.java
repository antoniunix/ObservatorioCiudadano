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

import net.gshp.observatoriociudadano.adapter.RVVisit;
import net.gshp.observatoriociudadano.dialog.DialogDeleteVisit;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportVisit;
import net.gshp.observatoriociudadano.listener.OnFinishSendReports;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelVisit;
import net.gshp.observatoriociudadano.util.BottomNavigationViewHelper;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.Config;

public class Visit extends AppCompatActivity implements OnItemClickListenerRV,
        BottomNavigationView.OnNavigationItemSelectedListener, OnFinishSendReports {

    private TextView txtTBDate, txtTBTitle;
    private BottomNavigationView bottomNavigationView;
    private LinearLayoutManager lmy;
    private RecyclerView rcvVisit;
    private ModelVisit model;
    private RVVisit adapter;


    private void init() {
        txtTBDate = findViewById(R.id.txtTBDate);
        txtTBTitle = findViewById(R.id.txtTBTitle);
        rcvVisit = findViewById(R.id.rcvVisit);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        lmy = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvVisit.setLayoutManager(lmy);
        model = new ModelVisit();
        adapter = model.getAdapter(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_visit);
        new ModelInfoPerson(this).loadImage(this).loadInfo();
        ChangeFontStyle.changeFont(txtTBDate, txtTBTitle);
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
        txtTBTitle.setText(R.string.label_visit);
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
                startActivity(new Intent(this, ListStation.class));
                finish();
                break;
            case R.id.action_visit:
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemClickListener(View v, int position) {
        switch (v.getId()) {
            case R.id.rltMain:
                DtoBundle dtoBundle = new DtoBundle();
                dtoBundle.setIdReportLocal(model.getItem(position).getId());
                dtoBundle.setIdPdv(model.getItem(position).getIdPdv());
                dtoBundle.setIdTypeMenuReport(model.getItem(position).getTypePoll());
                startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                finish();
                break;
            case R.id.imgTrash:
                DtoReportVisit dtoReportVisit = new DtoReportVisit();
                dtoReportVisit.setId(model.getItem(position).getId());
                dtoReportVisit.setHash(model.getItem(position).getHash());
                dtoReportVisit.setDateCheckIn(model.getItem(position).getDateCheckIn());
                dtoReportVisit.setDateCheckOut(model.getItem(position).getDateCheckOut());
                dtoReportVisit.setName(model.getItem(position).getName());
                DialogDeleteVisit dialogDeleteVisit = new DialogDeleteVisit();
                dialogDeleteVisit.setDtoReportVisit(dtoReportVisit);
                dialogDeleteVisit.show(getSupportFragmentManager(), "dialogDelete");
                break;
        }
    }

    @Override
    public void onFinishSendReports() {
        rcvVisit.setAdapter(model.getAdapter(this));
    }
}
