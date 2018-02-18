package net.gshp.observatoriociudadano.model;

import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoReportCensus;
import net.gshp.observatoriociudadano.dao.DaoSepomex;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoSepomex;
import net.gshp.observatoriociudadano.listener.OnFinishLocation;
import net.panamiur.geolocation.Geolocation;
import net.panamiur.geolocation.interfaces.OnApiGeolocation;

import java.util.List;

/**
 * Created by leo on 16/02/18.
 */

public class ModelCensus implements OnApiGeolocation {

    private DaoSepomex daoSepomex;
    private DaoReportCensus daoReportCensus;
    private List<String> lstDtoSepomex;
    private OnFinishLocation onFinishLocation;
    private Geolocation geolocation;
    private Context context;
    private float bestAccuracy = 0;

    public ModelCensus(Context context, OnFinishLocation onFinishLocation) {
        daoSepomex = new DaoSepomex();
        daoReportCensus = new DaoReportCensus();
        this.context = context;
        this.onFinishLocation = onFinishLocation;
    }

    public ModelCensus() {
        daoSepomex = new DaoSepomex();
        daoReportCensus = new DaoReportCensus();
    }

    public void onStart() {
        geolocation = new Geolocation(ModelCensus.class);
        geolocation.setOnApiGeolocationListener(this)
                .setContext(context)
                .setTimeUpdateLocation(context.getResources().getInteger(R.integer.geolocation_time_update));
        geolocation.stopGeo();
        geolocation.startGeo();

    }

    @Override
    public void onApiGeolocationChange(Location location) {
        if (bestAccuracy == 0 || bestAccuracy > location.getAccuracy()) {
            bestAccuracy = location.getAccuracy();
            onFinishLocation.onFinishLocation(location);
        }
    }

    public void onStopGeo() {
        geolocation.stopGeo();
    }

    public void saveCensus(DtoReportCensus dtoReportCensus) {

        daoReportCensus.insert(dtoReportCensus);
    }


}
