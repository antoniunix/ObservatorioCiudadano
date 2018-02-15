package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.gshp.api.utils.Crypto;
import com.gshp.api.utils.Util;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoEAEncuesta;
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
        if (!new DaoEAEncuesta().isResponsePollById(context.getResources().getInteger(R.integer.idPollSupervisor))) {
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
                    setSend(0).setTypeReport(1).setActive(1);
            dtoBundle.setIdReportLocal(new DaoReport().insert(dtoReport));
            activity.startService(new Intent(ContextApp.context, ServiceCheck.class).
                    putExtra(context.getString(R.string.app_bundle_name), dtoBundle).
                    putExtra("typeCheck", context.getResources().getInteger(R.integer.type_check_in)));
        }
    }
}
