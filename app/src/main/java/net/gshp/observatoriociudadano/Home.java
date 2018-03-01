package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.gshp.observatoriociudadano.dialog.DialogAccount;
import net.gshp.observatoriociudadano.dialog.DialogMessageGeneric;
import net.gshp.observatoriociudadano.dialog.DialogSync;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.model.ModelHome;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.Config;
import net.gshp.observatoriociudadano.util.SharePreferenceCustom;

public class Home extends AppCompatActivity implements View.OnClickListener {

    public static int stateApp = 0;//1 resume,pause.  2 destroy

    private ImageButton btnTBSettings, btnTBHelp, btnTBSync, btnTBAccount;
    private LinearLayout lnyStartRegister, lnyExit;
    private Button startRegister,exit;

    private ModelHome model;

    private void init() {
        btnTBHelp = findViewById(R.id.btnTBHelp);
        btnTBSync = findViewById(R.id.btnTBSync);
        btnTBAccount = findViewById(R.id.btnTBAccount);
        lnyStartRegister = findViewById(R.id.lnyStartRegister);
        lnyExit = findViewById(R.id.lnyExit);
        startRegister = findViewById(R.id.startRegister);
        exit = findViewById(R.id.exit);
        ChangeFontStyle.changeFont(startRegister, exit, findViewById(R.id.txtLabelInit));

        if (!SharePreferenceCustom.contains(R.string.app_share_preference_name, R.string.app_share_preference_user_account)) {
            SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_user_account, R.string.user);
            SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_user_pass, R.string.pass);

        }

        new ModelInfoPerson(this).loadImage(this).loadInfo("INICIO");
        btnTBHelp.setOnClickListener(this);
        btnTBSync.setOnClickListener(this);
        btnTBAccount.setOnClickListener(this);
        lnyStartRegister.setOnClickListener(this);
        lnyExit.setOnClickListener(this);
        startRegister.setOnClickListener(this);
        exit.setOnClickListener(this);
        model = new ModelHome();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateApp = 1;
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stateApp = 1;

        if ((System.currentTimeMillis() - Long.valueOf(SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_time_synch, "0")))
                > getResources().getInteger(R.integer.time_synch)) {
            if (SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_user_account, null) != null) {
                DialogSync diFragmentSync = new DialogSync();
                diFragmentSync.setCancelable(false);
                diFragmentSync.show(getSupportFragmentManager(), "DialogFragmentSync");
            } else {
                new DialogAccount().show(getSupportFragmentManager(), "Fragment_dialog_account");
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        stateApp = 1;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (checkBasic()) {
            stateApp = 1;
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == btnTBAccount.getId()) {
            new DialogAccount().show(getSupportFragmentManager(), "Fragment_dialog_account");

        } else if (v.getId() == btnTBSync.getId()) {
            if (SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_user_account, null) != null) {
                DialogSync diFragmentSync = new DialogSync();
                diFragmentSync.setCancelable(false);
                diFragmentSync.show(getSupportFragmentManager(), "DialogFragmentSync");
            } else {
                new DialogAccount().show(getSupportFragmentManager(), "Fragment_dialog_account");
            }

        } else if (v.getId() == btnTBHelp.getId()) {

        } else if (v.getId() == lnyStartRegister.getId()|| v.getId()==startRegister.getId()) {
            DtoBundle dtoBundle = new DtoBundle();
            int statusReportSupervisor;
            DialogMessageGeneric dialog = new DialogMessageGeneric();
            statusReportSupervisor = model.isRolledSupervisorDone();

            if (statusReportSupervisor == getResources().getInteger(R.integer.statusModuleReportDone)) {
                dialog.setData("REGISTRO COMPLETADO", "Ya te has registrado anteriormente", 0).
                        setShowButton(false, true);
                dialog.show(getSupportFragmentManager(), "");

            } else if (statusReportSupervisor == getResources().getInteger(R.integer.statusModuleReportWithOut)) {
                dialog.setData("SIN INFORMACIÓN", "No cuenta con la información necesaria, reportelo con su supervisor", 0).
                        setShowButton(false, true);
                dialog.show(getSupportFragmentManager(), "");

            } else if (statusReportSupervisor == getResources().getInteger(R.integer.statusModuleReportIncomplete)) {
                dtoBundle.setIdReportLocal(model.getIdReportIncompleteSupervisor());
                dtoBundle.setIdTypeMenuReport(getResources().getInteger(R.integer.idPollSupervisor));
                startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
            } else {
                dtoBundle.setIdTypeMenuReport(getResources().getInteger(R.integer.idPollSupervisor));
                startActivity(new Intent(this, MenuReport.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
            }

        } else if (v.getId() == lnyExit.getId()|| v.getId()==exit.getId()) {
            finish();
        }

    }

    private boolean checkBasic() {
        boolean flag = false;
        if (!Config.isDateAutomatic()) {
            Toast.makeText(getApplicationContext(),
                    "Debe poner la hora en autamatico", Toast.LENGTH_LONG)
                    .show();
            startActivityForResult(new Intent(
                    android.provider.Settings.ACTION_DATE_SETTINGS), -1);
        } else if (!Config.isGPSenabled()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Debe encender la localización, activar opción \"GPS\" para continuar",
                    Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(
                    android.provider.Settings.ACTION_SETTINGS), -1);
        } else if (!Config.isDateAutomatic1()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Debe Activar Zona Horaria Automatica",
                    Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(
                            android.provider.Settings.ACTION_DATE_SETTINGS),
                    -1);
        } else if (Config.isMockLocation()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Desactivar opción \"Coordenadas falsas\" para continuar",
                    Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(
                            android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                    -1);
        } else {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stateApp = 2;
    }
}
