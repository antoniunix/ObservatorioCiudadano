package net.gshp.observatoriociudadano.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import net.gshp.APINetwork.APINetwork;
import net.gshp.APINetwork.NetworkTask;
import net.gshp.APINetwork.NetworkTask.TaskMode;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.util.Config;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkConfig {

    private Handler handler;
    private SharedPreferences mSharedPreferences;

    public NetworkConfig(Handler handler, Context context) {
        this.handler = handler;

        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        APINetwork.setUSERNAME(mSharedPreferences.getString(context.getString(R.string.app_share_preference_user_account), null));
        APINetwork.setPASSWORD(mSharedPreferences.getString(context.getString(R.string.app_share_preference_user_pass), null));
        APINetwork.setSOCKET_TIMEOUT(1000 * 20);

        APINetwork.setSERVICE_IP(context.getString(R.string.network_ip));
        APINetwork.setSERVICE_NAME(context.getString(R.string.network_context));
        Log.e("NETWORK", "INTENTOS User= " + APINetwork.getUSERNAME() + "  " + APINetwork.getPASSWORD());
    }

    public NetworkConfig(Handler handler, Context context, String serviceName) {
        this.handler = handler;

        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        APINetwork.setSOCKET_TIMEOUT(1000 * 20);

        APINetwork.setSERVICE_IP(context.getString(R.string.images_ip));
        APINetwork.setSERVICE_NAME(serviceName);
    }


    public void GET(String params, String tag) {
        Map<String, String> mapHeader = new HashMap<String, String>();
        mapHeader.put("GSHP-IMEI", Config.getIMEI());
        //mapHeader.put("GSHP-ATTEMPTS", mSharedPreferences.getInt(Config.SH_COUNT_UNAUTHORIZED, 0)+"");
        Log.e("NETWORK", "INTENTOS imei= " + mapHeader.get("GSHP-ATTEMPTS") + "   IMEI" + mapHeader.get("GSHP-IMEI"));
        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.GET)
                .setTag(tag).setParams(params).setBasicauth(true).setGzip(true);
        APINetwork.taskManager.addTask(Ntask);
    }

    public void GET_MEDIA(String params, String tag) {
        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.GET_MEDIA)
                .setTag(tag).setParams(params).setBasicauth(true).setGzip(true);
        APINetwork.taskManager.addTask(Ntask);
    }

    public void POST(String params, ArrayList<NameValuePair> nameValuePairs,
                     String tag) {
        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.POST)
                .setTag(tag).setPayload(nameValuePairs).setParams(params)
                .setBasicauth(true).setGzip(true);
        APINetwork.taskManager.addTask(Ntask);
    }

    public void POSTChangePass(String params, ArrayList<NameValuePair> nameValuePairs,
                               String tag, String lastPass) {
        APINetwork.setPASSWORD(lastPass);
        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.POST)
                .setTag(tag).setPayload(nameValuePairs).setParams(params)
                .setBasicauth(true).setGzip(true);
        APINetwork.taskManager.addTask(Ntask);
    }

    public void POST_GEO(String params, ArrayList<NameValuePair> nameValuePairs, String tag) {

        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.POST)
                .setTag(tag).setPayload(nameValuePairs).setParams(params)
                .setBasicauth(true).setGzip(true);
        APINetwork.taskManager.executePriorityTask(Ntask);
    }

    public void POST_IMAGE(String params, String Path, ArrayList<NameValuePair> nameValuePairs, String tag) {

        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.POST_MULTIPART)
                .setTag(tag).setPayload(nameValuePairs).setParams(params).setFilepath(Path)
                .setBasicauth(true).setGzip(true);
        APINetwork.taskManager.addTask(Ntask);
    }

    public void POST_MULTIPART_FILE(String params, String path, ArrayList<NameValuePair> nameValuePairs, String tag) {
        NetworkTask Ntask = new NetworkTask(handler).setMode(TaskMode.POST_MULTIPART_FILE)
                .setTag(tag).setPayload(nameValuePairs).setFilepath(path).setParams(params)
                .setBasicauth(true);
        APINetwork.taskManager.addTask(Ntask);
    }


}
