package net.gshp.observatoriociudadano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoPolitic;
import net.gshp.observatoriociudadano.dialog.DialogChangePassword;
import net.gshp.observatoriociudadano.dialog.DialogPrivacyPolitics;
import net.gshp.observatoriociudadano.dialog.DialogUpdateApp;
import net.gshp.observatoriociudadano.dto.DtoPolitic;
import net.gshp.observatoriociudadano.faceDetection.FaceDetectionActivity;
import net.gshp.observatoriociudadano.listener.OnProgressSync;
import net.gshp.observatoriociudadano.model.ModelSincronizar;

import org.apache.http.HttpStatus;

import java.io.File;

public class Login extends AppCompatActivity implements View.OnClickListener, OnProgressSync {

    private Button btn_sync, btn_sync_agree, btn_sync_cancel, btn_next;
    private ProgressBar id_progressbar;
    private SharedPreferences prefs;
    private EditText edt_user_name, edt_pass;
    private RelativeLayout rlt_authentication, rlyt_delete_data, rlyt_sync;
    private TextView txtPorcent;
    private ModelSincronizar model;
    private int statusSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
    }

    private void init() {

        File f = new File(getString(R.string.app_path_photo));
        if (!f.exists()) {
            if (!f.mkdirs()) {
            }
        }
        DtoPolitic dtoPolitic = new DaoPolitic().Select();
        String version = dtoPolitic.getVersion() == null || dtoPolitic.getValue().isEmpty() ? "TERMS_1.0" : dtoPolitic.getVersion();
        prefs = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        if (!prefs.getString(getResources().getString(R.string.app_share_preference_privacy_politic), "")
                .equals(version)) {
            DialogPrivacyPolitics dialogPrivacyPolitics = new DialogPrivacyPolitics();
            dialogPrivacyPolitics.setCancelable(false);
            dialogPrivacyPolitics.show(getSupportFragmentManager(), "DialogPolitic");
        }

        if ((System.currentTimeMillis() - prefs.getLong(getResources().getString(R.string.app_share_preference_time_synch), 0L))
                > getResources().getInteger(R.integer.time_synch)) {
            id_progressbar = findViewById(R.id.id_progressbar);
            btn_sync = findViewById(R.id.btn_sync);
            btn_sync_agree = findViewById(R.id.btn_sync_agree);
            btn_sync_cancel = findViewById(R.id.btn_sync_cancel);
            btn_next = findViewById(R.id.btn_next);
            edt_user_name = findViewById(R.id.edt_user_name);
            edt_pass = findViewById(R.id.edt_pass);
            rlt_authentication = findViewById(R.id.rlt_authentication);
            rlyt_delete_data = findViewById(R.id.rlyt_delete_data);
            rlyt_sync = findViewById(R.id.rlyt_sync);
            txtPorcent = findViewById(R.id.txtPorcent);
            btn_sync.setOnClickListener(this);
            btn_sync_agree.setOnClickListener(this);
            btn_sync_cancel.setOnClickListener(this);
            btn_next.setOnClickListener(this);
            edt_user_name.setText(prefs.getString(getString(R.string.app_share_preference_user_account), ""));
            edt_pass.setText(prefs.getString(getString(R.string.app_share_preference_user_pass), ""));
        } else {
            if (prefs.getLong(getResources().getString(R.string.app_share_preference_time_synch), 0L) > 0) {
                Intent intent = new Intent(this, FaceDetectionActivity.class);
                intent.putExtra("userName",prefs.getString(getString(R.string.app_share_preference_user_account),""));
                startActivity(intent);
            } else {
                startActivity(new Intent(this, Home.class));
            }
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sync:
                if (edt_user_name.getText().toString().isEmpty()) {
                    edt_user_name.setError(getString(R.string.error_empty_user_name));
                } else if (edt_pass.getText().toString().isEmpty()) {
                    edt_pass.setError(getString(R.string.error_empty_password));
                } else if (prefs.getString(getString(R.string.app_share_preference_user_account), null) != null &&
                        !prefs.getString(getString(R.string.app_share_preference_user_account), "").equals(edt_user_name.getText().toString().trim())) {
                    rlt_authentication.setVisibility(View.GONE);
                    rlyt_delete_data.setVisibility(View.VISIBLE);
                    rlyt_sync.setVisibility(View.GONE);
                } else {
                    prefs.edit().putString(getString(R.string.app_share_preference_user_account), edt_user_name.getText().toString().trim()).apply();
                    prefs.edit().putString(getString(R.string.app_share_preference_user_pass), edt_pass.getText().toString().trim()).apply();
                    rlt_authentication.setVisibility(View.GONE);
                    rlyt_delete_data.setVisibility(View.GONE);
                    rlyt_sync.setVisibility(View.VISIBLE);
                    model = new ModelSincronizar(this, this);
                    model.setAuthentication();
                    txtPorcent.setText("% 0");
                }
                break;
            case R.id.btn_sync_agree:
                rlt_authentication.setVisibility(View.GONE);
                rlyt_delete_data.setVisibility(View.GONE);
                rlyt_sync.setVisibility(View.VISIBLE);
                prefs.edit().putString(getString(R.string.app_share_preference_user_account), edt_user_name.getText().toString().trim()).apply();
                prefs.edit().putString(getString(R.string.app_share_preference_user_pass), edt_pass.getText().toString().trim()).apply();
                deleteDatabase(getResources().getString(R.string.app_db_name));
                startActivity(new Intent(this, Splash.class));
                finish();
                break;
            case R.id.btn_sync_cancel:
                rlt_authentication.setVisibility(View.VISIBLE);
                rlyt_delete_data.setVisibility(View.GONE);
                rlyt_sync.setVisibility(View.GONE);
                break;
            case R.id.btn_next:
                switch (statusSync) {
                    case HttpStatus.SC_OK:
                        Log.w("Login","time "+prefs.getLong(getResources().getString(R.string.app_share_preference_time_synch), 0L));
                        if ((System.currentTimeMillis() - prefs.getLong(getResources().getString(R.string.app_share_preference_time_synch), 0L))
                                > 60000) {
                            Intent intent = new Intent(this, FaceDetectionActivity.class);
                            intent.putExtra("userName",prefs.getString(getString(R.string.app_share_preference_user_account),""));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(this, Home.class));
                        }
                        finish();
                        break;
                    case HttpStatus.SC_UNAUTHORIZED:
                        btn_next.setVisibility(View.VISIBLE);
                        rlt_authentication.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                        rlyt_delete_data.setVisibility(View.GONE);
                        rlyt_sync.setVisibility(View.GONE);
//                        DialogAccount dialogAccount = new DialogAccount();
//                        dialogAccount.show(getSupportFragmentManager(), "dialog");
                        break;
                    case HttpStatus.SC_PAYMENT_REQUIRED:

                        DialogChangePassword dialogChangePassword = new DialogChangePassword();
                        dialogChangePassword.show(getSupportFragmentManager(), "dialogChange");
                        rlt_authentication.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                        rlyt_delete_data.setVisibility(View.GONE);
                        rlyt_sync.setVisibility(View.GONE);
                        break;

                    default:
                        btn_next.setVisibility(View.GONE);
                        rlt_authentication.setVisibility(View.VISIBLE);
                        rlyt_delete_data.setVisibility(View.GONE);
                        rlyt_sync.setVisibility(View.GONE);
                        break;
                }
                break;
        }

    }


    @Override
    public void onProgresSync(int porcentOfProgress, int httpstatus, String service) {
        id_progressbar.setProgress(porcentOfProgress);
        txtPorcent.setText("%" + porcentOfProgress);
    }

    @Override
    public void onNewVersionExist(boolean isExist) {
        if (isExist) {
            DialogUpdateApp dialogUpdateApp = new DialogUpdateApp();
            dialogUpdateApp.setCancelable(false);
            dialogUpdateApp.show(getSupportFragmentManager(), "Dialog Update App");

        }
    }

    @Override
    public void onFinishSync(int httpstatus, String response, Object obj) {
        statusSync = httpstatus;
        btn_next.setVisibility(View.VISIBLE);
        switch (httpstatus) {
            case HttpStatus.SC_OK:
//                prefs.edit().putLong(getString(R.string.app_share_time_last_sync), System.currentTimeMillis()).apply();
//                if (!prefs.getBoolean(getString(R.string.terms_and_conditions_sharepreference_is_accepted), false)) {
//                    startActivityForResult(new Intent(this, TermsAndConditions.class), 1);
//                }
                prefs.edit().putLong(getResources().getString(R.string.app_share_preference_time_synch), System.currentTimeMillis())
                        .commit();
                break;
            case HttpStatus.SC_CONFLICT:
                txtPorcent.setText(getString(R.string.network_sc_conflict));
                break;
            case HttpStatus.SC_UNAUTHORIZED:
                txtPorcent.setText(getString(R.string.network_sc_unauthorized));
                break;
            case HttpStatus.SC_NO_CONTENT:
                txtPorcent.setText(getString(R.string.network_no_content));
                break;
            case HttpStatus.SC_FORBIDDEN:
                txtPorcent.setText(getString(R.string.network_forbidden));
                break;
            case HttpStatus.SC_METHOD_FAILURE:
                prefs.edit().putInt(getString(R.string.app_share_preference_count_unauthorized), 1).commit();
                txtPorcent.setText(obj.toString());
                break;
            case HttpStatus.SC_PAYMENT_REQUIRED:
                txtPorcent.setText(getString(R.string.network_sc_precondition_failed));
                break;
            case HttpStatus.SC_BAD_GATEWAY:
                txtPorcent.setText(getString(R.string.network_error));
                break;
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                txtPorcent.setText(R.string.network_server_disable);
                break;

        }
    }

}
