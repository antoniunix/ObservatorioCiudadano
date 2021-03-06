package net.gshp.observatoriociudadano;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import net.gshp.observatoriociudadano.dialog.DialogDeleteCensus;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.model.ModelCensus;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

/**
 * Created by leo on 17/02/18.
 */

public class CensusManual extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private ModelCensus modelCensus;
    private DtoBundle dtoBundle;
    private Spinner spinnerSuburb;
    private EditText edtStreet, edtNumberOut, edtNumberin;
    private AutoCompleteTextView edtcp;
    private DtoReportCensus dtoReportCensus;
    private Button btn_save;
    private LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.census_manual);
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        latLng = (LatLng) getIntent().getExtras().get(getString(R.string.latlon));
        dtoReportCensus = new DtoReportCensus();
        modelCensus = new ModelCensus(dtoBundle);
        if (latLng != null) {
            dtoReportCensus.setLat(latLng.latitude);
            dtoReportCensus.setLon(latLng.longitude);
        }
        edtcp = findViewById(R.id.edt_cp);
        edtNumberin = findViewById(R.id.edt_numberin);
        edtNumberOut = findViewById(R.id.edt_numberout);
        edtStreet = findViewById(R.id.edt_street);
        spinnerSuburb = findViewById(R.id.spn_suburb);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        edtcp.addTextChangedListener(this);
        edtcp.setAdapter(modelCensus.getAdapterCp());
        edtcp.setThreshold(1);
        btn_save.setOnClickListener(this);
        new ModelInfoPerson(this).loadImage(this);
        new ModelInfoPerson(this).loadImage(this).loadInfo("REGISTRA TU DOMICILIO");
        ChangeFontStyle.changeFont(edtcp, edtNumberin, edtNumberOut, edtStreet
                , edtStreet, spinnerSuburb, btn_save);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!edtcp.getText().toString().equals("") && edtcp.getText().length() > 0) {
            spinnerSuburb.setAdapter(modelCensus.getAdapterSuburb(edtcp.getText().toString()));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void save() {
        if (edtStreet.getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.rlt_census_manual), R.string.empty_street, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (edtNumberOut.getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.rlt_census_manual), R.string.empty_numberout, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            if (spinnerSuburb.getAdapter().getCount() != 0) {
                dtoReportCensus.setSuburb(modelCensus.getItemSuburb(spinnerSuburb.getSelectedItemPosition()).getSuburb());
                dtoReportCensus.setState(modelCensus.getItemSuburb(spinnerSuburb.getSelectedItemPosition()).getState());
                dtoReportCensus.setTown(modelCensus.getItemSuburb(spinnerSuburb.getSelectedItemPosition()).getTown());
            }

            dtoReportCensus.setCp(edtcp.getText().toString());
            dtoReportCensus.setAddress(edtStreet.getText().toString());
            dtoReportCensus.setExternalNumber(edtNumberOut.getText().toString());
            dtoReportCensus.setInternalNumber(edtNumberin.getText().toString());
            dtoReportCensus.setProvider(getString(R.string.providerManual));
            modelCensus.saveCensus(dtoReportCensus);
            Toast.makeText(this, "Se guardo ", Toast.LENGTH_SHORT).show();
            finish();

        }

    }

    @Override
    public void onClick(View view) {
        save();

    }
}
