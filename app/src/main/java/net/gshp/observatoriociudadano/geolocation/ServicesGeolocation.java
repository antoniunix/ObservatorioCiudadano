package net.gshp.observatoriociudadano.geolocation;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.gshp.api.utils.Crypto;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.Network.NetworkConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoReportGeolocation;
import net.gshp.observatoriociudadano.dto.DtoReportGeolocation;
import net.gshp.observatoriociudadano.util.Config;
import net.panamiur.geolocation.Geolocation;
import net.panamiur.geolocation.interfaces.OnApiGeolocation;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by gnu on 28/02/17.
 */

public class ServicesGeolocation extends IntentService implements OnApiGeolocation {
    private final int ENVIOFINALIZADO = -1000;
    private final int SINREPORTES = -2000;
    private DtoReportGeolocation dto;
    private DaoReportGeolocation daoReportGeolocation;
    private Geolocation geolocation;
    private NetworkConfig networkConfig;

    public ServicesGeolocation() {
        super("geolocation");
        Log.e("GEO","Geo Service ");
        daoReportGeolocation = new DaoReportGeolocation();
        networkConfig = new NetworkConfig(new HandlerTask(), ContextApp.context);
        geolocation = new Geolocation(ServicesGeolocation.class);
        geolocation.setOnApiGeolocationListener(this)
                .setContext(ContextApp.context)
                .setTimeUpdateLocation(ContextApp.context.getResources().getInteger(R.integer.geolocation_time_update));

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("GEO","Geo Service resume");
        geolocation.stopGeo();
        geolocation.startGeo();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onApiGeolocationChange(Location location) {

        Log.e("GEO","Geo se inserta ");
        new DaoReportGeolocation().Insert(new DtoReportGeolocation()
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude())
                .setBattery(String.valueOf(Config.getBatteryLevel()))
                .setAccuracy(String.valueOf(location.getAccuracy()))
                .setImei(Config.getIMEI())
                .setSatelliteUtc(String.valueOf(location.getTime()))
                .setDate(String.valueOf(System.currentTimeMillis()))
                .setTz(Config.getTimeZone())
                .setVersion("1")
                .setHash(Crypto.MD5(System.currentTimeMillis() + " " + Math.random()))
                .setProvider(location.getProvider()));
        geolocation.stopGeo();
        sendLocation();

    }

    private void sendLocation() {
        dto = daoReportGeolocation.Select();
        new Thread() {
            public void run() {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(dto)));
                networkConfig.POST_GEO("geo", nameValuePairs, "geo");
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class HandlerTask extends Handler {
        @Override
        public void handleMessage(Message msg) {
            NetworkTask completedTask = (NetworkTask) msg.obj;
            if (completedTask != null && (completedTask.getTag().equals("geo"))) {

                Log.i("geo", "status GEO" + completedTask.getResponseStatus());
                if (completedTask.getResponseStatus() == HttpStatus.SC_CREATED) {
                    new DaoReportGeolocation().deleteById(dto.getId());
                    dto = daoReportGeolocation.Select();
                    if (dto.getId()>0) {
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(dto)));
                        //asegurarnos de no mandar datos null
                        if (dto.getTz() != null)
                            networkConfig.POST_GEO("geo", nameValuePairs, "geo");
                    }
                }
                stopSelf();
            }
        }
    }

    class HandlerTaskReportsSend extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SINREPORTES:
                    Log.e("ServiceGeolocation:211", "Sin reportes a enviar");
                    break;
                case ENVIOFINALIZADO:
                    Log.e("ServiceGeolocation:214", "Reportes enviados");
                    break;
                case HttpStatus.SC_CONFLICT:
                    Log.e("ServiceGeolocation:217", "No se pudieron enviar sus reportes,\\nintentelo más tarde");
                    break;
                case HttpStatus.SC_FORBIDDEN:
                    Log.e("ServiceGeolocation:217", "El usuario que ingresó tiene estatus inactivo.\\n No puede hacer uso de la aplicación, para mayor información consultelo con su supervisor.");
                    break;
                case HttpStatus.SC_UNAUTHORIZED:
                    Log.e("ServiceGeolocation:217", "Su nombre de usuario y/o contraseña son incorrectos");
                    break;
                default:
                    break;
            }
        }
    }
}
