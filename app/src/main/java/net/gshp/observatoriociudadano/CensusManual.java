package net.gshp.observatoriociudadano;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import net.gshp.observatoriociudadano.dto.DtoBundle;

/**
 * Created by leo on 17/02/18.
 */

public class CensusManual extends AppCompatActivity {

    private DtoBundle dtoBundle;
    private Spinner spinnerSuburb;
    private EditText edtStreet, edtStreetLeft, edtStreetRight, edtNumberOut, edtNumberin;
    private AutoCompleteTextView edtcp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.census_manual);
        init();
    }

    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
    }
}
