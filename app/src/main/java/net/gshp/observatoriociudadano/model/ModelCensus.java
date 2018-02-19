package net.gshp.observatoriociudadano.model;

import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.adapter.AdapterSpinnerCp;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoReportCensus;
import net.gshp.observatoriociudadano.dao.DaoSepomex;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoSepomex;
import net.gshp.observatoriociudadano.listener.OnFinishLocation;
import net.panamiur.geolocation.Geolocation;
import net.panamiur.geolocation.interfaces.OnApiGeolocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/02/18.
 */

public class ModelCensus implements OnApiGeolocation {

    private DaoSepomex daoSepomex;
    private DaoReportCensus daoReportCensus;
    private List<DtoSepomex> lstDtoSepomex;
    private OnFinishLocation onFinishLocation;
    private Geolocation geolocation;
    private Context context;
    private float bestAccuracy = 0;
    private DtoBundle dtoBundle;

    public ModelCensus(Context context, OnFinishLocation onFinishLocation, DtoBundle dtoBundle) {
        daoSepomex = new DaoSepomex();
        daoReportCensus = new DaoReportCensus();
        this.context = context;
        this.onFinishLocation = onFinishLocation;
        this.dtoBundle = dtoBundle;
    }

    public ModelCensus(DtoBundle dtoBundle) {
        daoSepomex = new DaoSepomex();
        daoReportCensus = new DaoReportCensus();
        this.dtoBundle = dtoBundle;
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
        daoReportCensus.deleteByIdReport(dtoBundle.getIdReportLocal());
        daoReportCensus.insert(dtoReportCensus, dtoBundle.getIdReportLocal());
    }


    public ArrayAdapter getAdapterCp() {
        List<String> lst = daoSepomex.SelectCp();
        return new AdapterSpinnerCp(ContextApp.context, R.layout.spinner_simple_list, lst);
    }

    public SpinnerAdapter getAdapterSuburb(String postalCode) {
        lstDtoSepomex = daoSepomex.Select(postalCode);
        List<String> lst = new ArrayList<>(lstDtoSepomex.size());
        for (DtoSepomex dto : lstDtoSepomex) {
            lst.add(dto.getSuburb().trim());
        }
        return new AdapterSpinnerCp(ContextApp.context, R.layout.spinner_simple_list, lst);
    }

    public DtoSepomex getItemSuburb(int position) {
        return lstDtoSepomex.get(position);
    }


    public boolean isCompleteCensus() {
        return daoReportCensus.isCompleteReportSupervisor();
    }

    public String getAddressInfo() {
        return daoReportCensus.getAddress(dtoBundle.getIdReportLocal());

    }


}
