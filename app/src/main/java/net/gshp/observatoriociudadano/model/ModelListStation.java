package net.gshp.observatoriociudadano.model;

import net.gshp.observatoriociudadano.adapter.RVListStation;
import net.gshp.observatoriociudadano.dao.DaoPdv;
import net.gshp.observatoriociudadano.dao.DaoReport;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;

import java.util.List;

/**
 * Created by gnu on 15/02/18.
 */

public class ModelListStation {

    private List<DtoPdvPdv> lstPdv;
    private RVListStation adapter;

    public ModelListStation() {

    }

    public RVListStation getAdapter(OnItemClickListenerRV onItemClickListenerRV){
        lstPdv=new DaoPdv().select();
        return adapter=new RVListStation(lstPdv,onItemClickListenerRV);
    }

    public boolean isReportIncomplete(){
        return new DaoReport().isReportIncomplete();
    }
}
