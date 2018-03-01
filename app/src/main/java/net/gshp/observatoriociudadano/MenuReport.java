package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.gshp.apiencuesta.Encuesta;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dialog.DialogMessageGeneric;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.faceDetection.PhotoWizardActivity;
import net.gshp.observatoriociudadano.faceDetection.PhotosActivity;
import net.gshp.observatoriociudadano.geolocation.ServiceCheck;
import net.gshp.observatoriociudadano.listener.OnDissmisDialogListener;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelMenuReport;

public class MenuReport extends AppCompatActivity implements View.OnClickListener,
        OnDissmisDialogListener {

    private ModelMenuReport modelMenuReport;
    private DtoBundle dtoBundle;
    private LinearLayout lytStep1, lytStep2, lytStep3, lytStep4;
    private RelativeLayout lytStep5;

    private boolean flagTest = true;


    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        lytStep1 = findViewById(R.id.lytStep1);
        lytStep2 = findViewById(R.id.lytStep2);
        lytStep3 = findViewById(R.id.lytStep3);
        lytStep4 = findViewById(R.id.lytStep4);
        lytStep5 = findViewById(R.id.lytStep5);
        modelMenuReport = new ModelMenuReport(dtoBundle);
        if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollSupervisor)) {
            new ModelInfoPerson(this).loadImage(this).loadInfo("REGISTRO");
        } else if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollRepresentanteCasilla)) {
            new ModelInfoPerson(this).loadImage(this).loadInfo("REPRESENTANTE");
        }
        lytStep1.setOnClickListener(this);
        lytStep2.setOnClickListener(this);
        lytStep3.setOnClickListener(this);
        lytStep4.setOnClickListener(this);
        lytStep5.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_report);
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dtoBundle.getIdReportLocal() == 0) {
            modelMenuReport.createNewReport(this);
        }
        int statusReportSupervisor, statusReportCensusSupervisor, statusReportDevice;
        statusReportSupervisor = modelMenuReport.isReportPollSup();
        statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();
        statusReportDevice = modelMenuReport.isReportPollDevice();
        if (statusReportSupervisor == getResources().getInteger(R.integer.statusModuleReportDone)) {
            //boton registro
            ((ImageView) findViewById(R.id.imgStep1)).setImageResource(R.drawable.reg2);
        }
        if (statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportDone)) {
            // boton censo
            ((ImageView) findViewById(R.id.imgStep2)).setImageResource(R.drawable.dom2);
        }

        if (statusReportDevice == getResources().getInteger(R.integer.statusModuleReportDone)) {
            //boton registro
            ((ImageView) findViewById(R.id.imgStep3)).setImageResource(R.drawable.tel2);
        }

        if (modelMenuReport.isReportPhotoComplete(dtoBundle.getIdReportLocal())) {
            //boton fotos
            ((ImageView) findViewById(R.id.imgStep4)).setImageResource(R.drawable.foto2);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onDissmisDialogListener(int status, int request, Object object) {
        finish();
    }

    @Override
    public void onClick(View v) {
        DialogMessageGeneric dialog = new DialogMessageGeneric();
        int statusReportSupervisor, statusReportCensusSupervisor, statusReportDevice;
        switch (v.getId()) {
            case R.id.lytStep1: //poll personal
                startActivity(new Intent(this, Encuesta.class)
                        .putExtra("numeroEncuesta", 0)
                        .putExtra("idReporte", dtoBundle.getIdReportLocal())
                        .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)));
                break;
            case R.id.lytStep2:// poll direction
                statusReportSupervisor = modelMenuReport.isReportPollSup();

                if (flagTest && statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 1", 0).
                            setShowButton(false, true).setCancelable(false);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    startActivity(new Intent(this, CensusManual.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                }


                break;
            case R.id.lytStep3://poll job
                statusReportSupervisor = modelMenuReport.isReportPollSup();
                statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();

                if (flagTest && statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 1", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 2", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollDevice)));
                }

                break;
            case R.id.lytStep4://photos
                statusReportSupervisor = modelMenuReport.isReportPollSup();
                statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();
                statusReportDevice = modelMenuReport.isReportPollDevice();

                if (flagTest && statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 1", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 2", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && statusReportDevice != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 3", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    startActivity(new Intent(this, PhotosActivity.class)
                            .putExtra(getString(R.string.is_reco), false).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                }
                break;
            case R.id.lytStep5://check out
                statusReportSupervisor = modelMenuReport.isReportPollSup();
                statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();
                statusReportDevice = modelMenuReport.isReportPollDevice();

                if (flagTest && statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 1", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 2", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && statusReportDevice != getResources().getInteger(R.integer.statusModuleReportDone)) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 3", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else if (flagTest && !modelMenuReport.isReportPhotoComplete(dtoBundle.getIdReportLocal())) {
                    dialog.setData("SE ESTA SALTANDO UN PASO", "Debe completar el paso 4", 0).
                            setShowButton(false, true);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    startService(new Intent(ContextApp.context, ServiceCheck.class).
                            putExtra(getString(R.string.app_bundle_name), dtoBundle).
                            putExtra("typeCheck", getResources().getInteger(R.integer.type_check_out)));
                    String msg = "Error al obtener datos";
                    msg = modelMenuReport.getUserPassword(dtoBundle.getIdReportLocal());
                    dialog.setData("USUARIO Y CONTARSEÑA", msg, 0).
                            setShowButton(false, true).
                            setOnDissmisDialogListener(this);
                    dialog.show(getSupportFragmentManager(), "");
                }


                break;
        }


    }
}