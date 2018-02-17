package net.gshp.observatoriociudadano.geolocation;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.gshp.api.utils.Crypto;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.Network.NetworkConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoReportCheck;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoCheckSend;
import net.gshp.observatoriociudadano.dto.DtoReportCheck;
import net.gshp.observatoriociudadano.model.ModelSend;
import net.gshp.observatoriociudadano.util.Config;
import net.panamiur.geolocation.Geolocation;
import net.panamiur.geolocation.interfaces.OnApiGeolocation;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by leo on 2/02/18.
 */

public class ServiceCheck extends IntentService implements OnApiGeolocation {

    private Geolocation geolocation;
    private DtoBundle dtoBundle;
    private List<DtoCheckSend> lstCheckSends;
    private NetworkConfig networkConfig;
    private Timer timer;
    private int typeCheck;
    private float bestAccuracy = 1000;

    public ServiceCheck() {
        super("ServiceCheck");
        Log.e("service check", "service check");
        geolocation = new Geolocation(ServiceCheck.class);
        geolocation.setOnApiGeolocationListener(this)
                .setContext(ContextApp.context)
                .setTimeUpdateLocation(ContextApp.context.getResources().getInteger(R.integer.geolocation_time_update));
        geolocation.stopGeo();
        geolocation.startGeo();
        networkConfig = new NetworkConfig(new HandlerSendCheck(), ContextApp.context);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        timer = new Timer();
        dtoBundle = (DtoBundle) intent.getExtras().get(ContextApp.context.getString(R.string.app_bundle_name));
        typeCheck = intent.getExtras().getInt("typeCheck");

        if (typeCheck > 0) {
            DtoReportCheck dtoCheck = new DtoReportCheck();
            dtoCheck.setIdReportLocal(dtoBundle.getIdReportLocal());
            dtoCheck.setDate(System.currentTimeMillis());
            dtoCheck.setTz(Config.getTimeZone());
            dtoCheck.setLatitude(0);
            dtoCheck.setLongitude(0);
            dtoCheck.setAccuracy(String.valueOf(0));
            dtoCheck.setImei(Config.getIMEI());
            dtoCheck.setSatelliteUtc(String.valueOf(0));
            dtoCheck.setTypeCheck(typeCheck);
            dtoCheck.setSend(0);
            dtoCheck.setProvider(ContextApp.context.getString(R.string.report_check_dont_provider));
            dtoCheck.setDateInactive(String.valueOf(0));
            dtoCheck.setActive(1);
            dtoCheck.setHash(Crypto.MD5(System.currentTimeMillis() + " " + Math.random()));
            new DaoReportCheck().insert(dtoCheck);
        }
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                geolocation.stopGeo();
                if (typeCheck == 2) {
                    new ModelSend().start();
                }
                stopSelf();
            }
        };
        timer.schedule(timerTask, ContextApp.context.getResources().getInteger(R.integer.time_check_max));
    }

    @Override
    public void onApiGeolocationChange(Location location) {
        if (location.getAccuracy() < bestAccuracy) {

            if (typeCheck > 0) {
                DtoReportCheck dtoCheck = new DtoReportCheck();
                dtoCheck.setIdReportLocal(dtoBundle.getIdReportLocal());
                dtoCheck.setDate(System.currentTimeMillis());
                dtoCheck.setTz(Config.getTimeZone());
                dtoCheck.setLatitude(location.getLatitude());
                dtoCheck.setLongitude(location.getLongitude());
                dtoCheck.setAccuracy(location.getAccuracy() + "");
                dtoCheck.setImei(Config.getIMEI());
                dtoCheck.setSatelliteUtc(location.getTime() + "");
                dtoCheck.setTypeCheck(typeCheck);
                dtoCheck.setProvider(location.getProvider());
                dtoCheck.setSend(0);
                new DaoReportCheck().insert(dtoCheck);

            }
            bestAccuracy = location.getAccuracy();
        }
        sendCheck();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendCheck() {
        lstCheckSends = new DaoReportCheck().SelectToSend();
        Log.e("status", "size " + lstCheckSends.size());
        new Thread() {
            public void run() {
                for (int i = 0; i < lstCheckSends.size(); i++) {
                    Log.e("Lista", "Lista " + "[" + new Gson().toJson(lstCheckSends.get(i)) + "]");
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("json", "[" + new Gson().toJson(lstCheckSends.get(i)) + "]"));
                    networkConfig.POST("multireport/insertnt/rcheckin/1", nameValuePairs,
                            "rsch" + lstCheckSends.get(i).getIdReportLocal());
                }
            }

            ;
        }.start();
    }

    class HandlerSendCheck extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("kkk", "hhhh");
            NetworkTask nt = (NetworkTask) msg.obj;
            if (nt.getResponseStatus() == HttpStatus.SC_OK || nt.getResponseStatus() == HttpStatus.SC_CREATED) {
                new DaoReportCheck().markAsSent(nt.getTag().substring(4));
            }
        }
    }
}
