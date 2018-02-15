package net.gshp.observatoriociudadano;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.model.ModelAHBottomNavigationMenuReport;
import net.gshp.observatoriociudadano.model.ModelMenuReport;
import net.gshp.observatoriociudadano.util.Config;

public class MenuReport extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener {

    private TextView txtTBDate;
    private ModelMenuReport modelMenuReport;
    private ModelAHBottomNavigationMenuReport modelAHBottomNavigationMenuReport;
    private DtoBundle dtoBundle;

    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));

        txtTBDate = findViewById(R.id.txtTBDate);

        modelMenuReport = new ModelMenuReport(dtoBundle);
        modelAHBottomNavigationMenuReport = new ModelAHBottomNavigationMenuReport(this, modelMenuReport, this);
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
        txtTBDate.setText(Config.formatDate());
        if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollSupervisor)) {
            modelMenuReport.createNewReportSupervisor(this);
        }
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        Log.e("position botton","botton "+position);



        return true;
    }
}