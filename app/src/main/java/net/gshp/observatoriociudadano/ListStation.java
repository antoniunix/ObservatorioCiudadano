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
import android.widget.Toast;

import net.gshp.observatoriociudadano.adapter.RVListStation;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelListStation;
import net.gshp.observatoriociudadano.util.BottomNavigationViewHelper;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.Config;

public class ListStation extends AppCompatActivity implements OnItemClickListenerRV {

    private RecyclerView rcvVisit;
    private LinearLayoutManager lmy;
    private ModelListStation model;
    private RVListStation adapter;

    private void init() {
        rcvVisit = findViewById(R.id.rcvVisit);
        model = new ModelListStation();
        lmy = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        new ModelInfoPerson(this).loadImage(this).loadInfo("CASILLAS");
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
        rcvVisit.setLayoutManager(lmy);
        rcvVisit.setAdapter(adapter);
    }


    @Override
    public void onItemClickListener(View v, int position) {
        if(model.isReportIncomplete()){
            Toast.makeText(this, "Tiene un reporte incompleto", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, Visit.class));
        }else {
            DtoPdvPdv dtoPdvPdv = adapter.getItem(position);
            DtoBundle dtoBundle = new DtoBundle();
            dtoBundle.setIdTypeMenuReport(getResources().getInteger(R.integer.idPollRepresentanteCasilla)).
                    setIdPdv(dtoPdvPdv.getId());
            startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
            finish();
        }

    }
}
