package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.gshp.api.utils.Crypto;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoEAEncuesta;
import net.gshp.observatoriociudadano.dao.DaoEaAnswerPdv;
import net.gshp.observatoriociudadano.dao.DaoReport;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReport;
import net.gshp.observatoriociudadano.geolocation.ServiceCheck;
import net.gshp.observatoriociudadano.util.Config;

/**
 * Created by gnu on 15/02/18.
 */

public class ModelMenuReport {

    private DtoBundle dtoBundle;
    private Context context;

    public ModelMenuReport(DtoBundle dtoBundle) {
        this.dtoBundle = dtoBundle;
        context = ContextApp.context;

    }

    public void createNewReportSupervisor(Activity activity) {
        String version = "";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DtoReport dtoReport = new DtoReport();
        dtoReport.setIdPdv(1).setIdSchedule(1).setVersion(version).setDate(System.currentTimeMillis()).
                setTz(Config.getTimeZone()).setImei(Config.getIMEI()).
                setHash(Crypto.MD5(System.currentTimeMillis() + " " + Math.random())).
                setSend(0).setTypeReport(1).setActive(1).setTypePoll((int) dtoBundle.getIdTypeMenuReport());
        dtoBundle.setIdReportLocal(new DaoReport().insert(dtoReport));
        activity.startService(new Intent(ContextApp.context, ServiceCheck.class).
                putExtra(context.getString(R.string.app_bundle_name), dtoBundle).
                putExtra("typeCheck", context.getResources().getInteger(R.integer.type_check_in)));
    }


    public int isReportPollSup() {
        DaoEAEncuesta dao = new DaoEAEncuesta();
        DaoEaAnswerPdv daoAnswer = new DaoEaAnswerPdv();
        if (!dao.existPoll(ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor))) {
            return context.getResources().getInteger(R.integer.statusModuleReportWithOut);
        } else if (dao.isResponsePollById(ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)) ||
                daoAnswer.isResponsePollSupervisor()) {
            return context.getResources().getInteger(R.integer.statusModuleReportDone);
        } else {
            return context.getResources().getInteger(R.integer.statusModuleReportNotDone);
        }
    }

//    public int isReportSupComplete(){
//
//        DaoReport daoReport=new DaoReport();
//
//
//    }

    public int isReportPoll(long idPoll, long idReport) {
        DaoEAEncuesta dao = new DaoEAEncuesta();
        if (!dao.existPoll(idPoll)) {
            return context.getResources().getInteger(R.integer.statusModuleReportWithOut);
        } else if (dao.isResponsePollById(idReport, idPoll)) {
            return context.getResources().getInteger(R.integer.statusModuleReportDone);
        } else {
            return context.getResources().getInteger(R.integer.statusModuleReportNotDone);
        }
    }


}
