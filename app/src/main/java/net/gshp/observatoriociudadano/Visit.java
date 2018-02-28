package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class Visit extends AppCompatActivity implements OnItemClickListenerRV, OnFinishSendReports {

    private LinearLayoutManager lmy;
    private RecyclerView rcvVisit;
    private ModelVisit model;
    private RVVisit adapter;


    private void init() {
        rcvVisit = findViewById(R.id.rcvVisit);
        lmy = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvVisit.setLayoutManager(lmy);
        model = new ModelVisit();
        adapter = model.getAdapter(this);
        new ModelInfoPerson(this).loadImage(this).loadInfo("CONTROL DE REGISTROS");
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
        rcvVisit.setAdapter(adapter);
    }


    @Override
    public void onItemClickListener(View v, int position) {
        switch (v.getId()) {
            case R.id.rltMain:
                if (model.getItem(position).getDateCheckOut() == 0) {
                    DtoBundle dtoBundle = new DtoBundle();
                    dtoBundle.setIdReportLocal(model.getItem(position).getId());
                    dtoBundle.setIdPdv(model.getItem(position).getIdPdv());
                    dtoBundle.setIdTypeMenuReport(model.getItem(position).getTypePoll());
                    startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.lyt_visit), getString(R.string.report_complete), Snackbar.LENGTH_LONG).show();
                }

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
