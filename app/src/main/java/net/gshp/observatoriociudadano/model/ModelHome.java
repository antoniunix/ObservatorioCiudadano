package net.gshp.observatoriociudadano.model;

import android.content.Context;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoEAEncuesta;
import net.gshp.observatoriociudadano.dao.DaoEaAnswerPdv;
import net.gshp.observatoriociudadano.dao.DaoReport;

/**
 * Created by gnu on 15/02/18.
 */

public class ModelHome {

    private Context context;

    public ModelHome() {
        context = ContextApp.context;
    }

    public int isRolledSupervisorDone() {

        if (new DaoEaAnswerPdv().isResponsePollSupervisor()) {
            return context.getResources().getInteger(R.integer.statusModuleReportDoneBeforeVisit);
        } else if (!new DaoEAEncuesta().existPoll(context.getResources().getInteger(R.integer.idPollSupervisor))) {
            return context.getResources().getInteger(R.integer.statusModuleReportWithOut);
        } else if (new DaoReport().isCompleteReportSupervisor()) {
            return context.getResources().getInteger(R.integer.statusModuleReportDone);
        }else if (new DaoReport().isIncompleteReportSupervisor()) {
            return context.getResources().getInteger(R.integer.statusModuleReportIncomplete);
        } else {
            return context.getResources().getInteger(R.integer.statusModuleReportNotDone);

        }

    }


}
