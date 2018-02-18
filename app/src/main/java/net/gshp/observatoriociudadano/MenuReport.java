package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import net.gshp.apiencuesta.Encuesta;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.geolocation.ServiceCheck;
import net.gshp.observatoriociudadano.faceDetection.PhotoWizardActivity;
import net.gshp.observatoriociudadano.model.ModelAHBottomNavigationMenuReport;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelMenuReport;
import net.gshp.observatoriociudadano.util.Config;

public class MenuReport extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener {

    private ModelMenuReport modelMenuReport;
    private ModelAHBottomNavigationMenuReport modelAHBottomNavigationMenuReport;
    private DtoBundle dtoBundle;

    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        new ModelInfoPerson(this).loadImage(this);
        modelMenuReport = new ModelMenuReport(dtoBundle);
        new ModelInfoPerson(this).loadImage(this).loadInfo();
        modelAHBottomNavigationMenuReport = new ModelAHBottomNavigationMenuReport(this, modelMenuReport, this, dtoBundle);
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
        if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollSupervisor)) {
            modelMenuReport.createNewReportSupervisor(this);
        }
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollSupervisor)) {
            switch (position) {
                case 0://poll supervisor
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)));
                    break;
                case 1:// census
                    startActivity(new Intent(this,Census.class).putExtra(getString(R.string.app_bundle_name),dtoBundle));
                    break;
                case 2://Photos
                    Intent intent = new Intent(this, PhotoWizardActivity.class);
                    intent.putExtra(getString(R.string.user_roll), getResources().getInteger(R.integer.rollSupervisor));
                    startActivity(intent);
                    break;
                case 3://check out
                    startService(new Intent(ContextApp.context, ServiceCheck.class).
                            putExtra(getString(R.string.app_bundle_name), dtoBundle).
                            putExtra("typeCheck", getResources().getInteger(R.integer.type_check_out)));
                    finish();
                    break;
            }

        } else if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollRepresentanteCasilla)) {
            switch (position) {
                case 0:// poll station
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)));
                    break;
                case 1:// poll representative
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollCasilla)));
                    break;
                case 2://census
                    startActivity(new Intent(this,Census.class).putExtra(getString(R.string.app_bundle_name),dtoBundle));
                    break;
                case 3://photo
                    Intent intent = new Intent(this, PhotoWizardActivity.class);
                    intent.putExtra(getString(R.string.user_roll), getResources().getInteger(R.integer.rollRepresentanteCasilla));
                    startActivity(intent);
                    break;
                case 4://CHECKOUT
                    startService(new Intent(ContextApp.context, ServiceCheck.class).
                            putExtra(getString(R.string.app_bundle_name), dtoBundle).
                            putExtra("typeCheck", getResources().getInteger(R.integer.type_check_out)));
                    finish();
                    break;
            }
        }

        return true;
    }
}
