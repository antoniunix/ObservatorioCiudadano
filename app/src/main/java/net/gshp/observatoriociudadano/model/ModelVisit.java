package net.gshp.observatoriociudadano.model;

import net.gshp.observatoriociudadano.adapter.RVVisit;
import net.gshp.observatoriociudadano.dao.DaoReport;
import net.gshp.observatoriociudadano.dto.DtoReportVisit;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;

import java.util.List;

/**
 * Created by gnu on 16/02/18.
 */

public class ModelVisit {

    private RVVisit adapter;
    private List<DtoReportVisit> lstVisit;

    public ModelVisit() {

    }

    public RVVisit getAdapter(OnItemClickListenerRV onItemClickListenerRV) {
        lstVisit = new DaoReport().SelectReportVisit();
        return adapter = new RVVisit(lstVisit, onItemClickListenerRV);
    }
}
