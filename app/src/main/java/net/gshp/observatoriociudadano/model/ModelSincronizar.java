package net.gshp.observatoriociudadano.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.Network.NetworkConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dto.DtoAuthentication;
import net.gshp.observatoriociudadano.dto.DtoStatus;
import net.gshp.observatoriociudadano.dto.DtoUpdate;
import net.gshp.observatoriociudadano.listener.OnProgressSync;
import net.gshp.observatoriociudadano.util.Config;

import org.apache.http.HttpStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModelSincronizar {

    private final String regId;
    private final int STORAGE_OK = 1;
    private final int STORAGE_ERROR = -1;
    private final int ACTUALIZAR = 2000;
    private NetworkConfig networkConfig;
    private HandlerTask handlerTask;
    private OnProgressSync listenerOnProgressSync;
    private boolean SC_UNAUTHORIZED = false;
    private boolean SC_CONFLICT = false;
    private boolean NUEVA_ACTUALIZACION = false;
    private boolean SC_NO_CONTENT = false;
    private boolean SC_FORBIDDEN = false;
    private boolean SC_PAYMENT_REQUIRED = false;
    private boolean SC_METHOD_FAILURE = false;
    private String SC_FORBIDDEN_RESPONSE = "";
    private String SC_METHOD_FAILURE_RESPONSE = "";
    private ModelDataBaseSync modelDataBaseSync;
    private HandlerStorage handlerStorage;
    private DtoUpdate dtoUpdate;
    private List<String> lstNoContent;

    private DtoStatus dtoStatus;
    private Context context;

    private boolean flag = false;
    private int numReportGuardados = 0;
    private SharedPreferences mySharedPreferences;

    private final int NUMCATALOGOS = 7; //se usa para saber cuando ya se descargaron todos los catalogos y enviar mensaje de terminado
    private int numReportDownload = 0;


    public ModelSincronizar(OnProgressSync listenerOnProgressSync, Context context) {
        handlerTask = new HandlerTask();
        this.listenerOnProgressSync = listenerOnProgressSync;
        this.context = context;
        handlerStorage = new HandlerStorage();
        modelDataBaseSync = new ModelDataBaseSync(handlerStorage);
        networkConfig = new NetworkConfig(handlerTask, context);
        lstNoContent = new ArrayList<>();
        mySharedPreferences = context.getSharedPreferences(context.getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
        regId = FirebaseInstanceId.getInstance().getToken();
        Log.e("RegId", "RegId " + regId);
    }


    public void setAuthentication() {
        new Thread() {
            public void run() {
                String json = new Gson().toJson(getDataToAuthentication());
                networkConfig.POST("login/authentication", json, "token", null);
            }
        }.start();
    }

    private void Actualizar() {
        new Thread() {
            public void run() {
                networkConfig.GET("version/android", "version");
            }

            ;
        }.start();
    }

    private void Sincronizar() {
        new Thread() {
            public void run() {
                networkConfig.GET("multireport/catalog/pdv_pdv", "pdv_pdv");

                //POLL
                networkConfig.GET("multireport/catalog/ea_poll", "ea_poll");
                networkConfig.GET("multireport/catalog/ea_question", "ea_question");
                networkConfig.GET("multireport/catalog/ea_question_type_cat", "ea_question_type_cat");
                networkConfig.GET("multireport/catalog/ea_question_option", "ea_question_option");
                networkConfig.GET("multireport/catalog/ea_section", "ea_section");
                networkConfig.GET("multireport/catalog/ea_answers_pdv", "ea_answers_pdv");

//                networkConfig.GET("user/regId/" + regId, "regid");
            }
        }.start();
    }

    class HandlerTask extends Handler {
        @Override
        public void handleMessage(Message msg) {
            NetworkTask nt = (NetworkTask) msg.obj;


            if (!nt.getTag().equals("version") && !nt.getTag().equals("STATUS") && !nt.getTag().equals("token")) {
                numReportDownload++;
            }
            if (nt.getResponseStatus() == HttpStatus.SC_OK || nt.getResponseStatus() == HttpStatus.SC_CREATED) {
                if (nt.getTag().equals("token")) {
                    SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
                    prefs.edit().putString(context.getString(R.string.app_share_preference_toke_webservices), nt.getResponse()).apply();
                    Actualizar();
                } else if (nt.getTag().equals("version")) {

                    String version = "";
                    try {
                        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.replace(" TEMP", "");
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Type typeObjectGson = new TypeToken<DtoUpdate>() {
                    }.getType();
                    dtoUpdate = new Gson().fromJson(nt.getResponse(),
                            typeObjectGson);
                    if (dtoUpdate.getVersion().equals(version)) {
                        Sincronizar();
                    } else {
                        SharedPreferences sp = ContextApp.context
                                .getSharedPreferences(context.getString(R.string.app_share_preference_name),
                                        Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("MD5", dtoUpdate.getMd5());
                        editor.commit();
                        NUEVA_ACTUALIZACION = true;
                        infoUI();

                    }
                } else {
                    // enviamos los datos recividos a base
                    modelDataBaseSync.syncInsertion(nt);
                }
            } else if (nt.getResponseStatus() == HttpStatus.SC_UNAUTHORIZED) {
                numReportDownload = NUMCATALOGOS;
                numReportGuardados = NUMCATALOGOS;

                SC_UNAUTHORIZED = true;
            } else if (nt.getResponseStatus() == HttpStatus.SC_FORBIDDEN) {
                SC_FORBIDDEN = true;
                SC_FORBIDDEN_RESPONSE = nt.getResponse();
                numReportDownload = NUMCATALOGOS;
                numReportGuardados = NUMCATALOGOS;

            } else if (nt.getResponseStatus() == HttpStatus.SC_PAYMENT_REQUIRED) {
                SC_PAYMENT_REQUIRED = true;
                numReportDownload = NUMCATALOGOS;
                numReportGuardados = NUMCATALOGOS;

            } else if (nt.getResponseStatus() == HttpStatus.SC_METHOD_FAILURE) {
                SC_METHOD_FAILURE_RESPONSE = nt.getResponse();
                SC_METHOD_FAILURE = true;
                numReportDownload = NUMCATALOGOS;
                numReportGuardados = NUMCATALOGOS;

            } else if (nt.getResponseStatus() == HttpStatus.SC_NO_CONTENT) {
                //Se tiene que elimiar lo que contenga la tabla
                nt.setTag("DELETE" + nt.getTag());
                modelDataBaseSync.syncInsertion(nt);
                SC_NO_CONTENT = true;
                lstNoContent.add(nt.getTag().substring("DELETE".length()));
            } else {
                if (nt.getTag().equals("version") || nt.getTag().equals("STATUS") || nt.getTag().equals("token")) {
                    //si los servicios VERSION y/o MD5 fallan no deben descargarse los demas
                    //servicios, por eso se igualan las variables
                    numReportDownload = NUMCATALOGOS;
                    numReportGuardados = NUMCATALOGOS;
                    SC_CONFLICT = true;
                } else {
                    //como no se pudo descargar el catalogo, se tiene que aumentar al contador de base
                    //ya que si no nunca se guardara a la base y no se podra completar la validacion
                    // de catalogos guardados.
                    numReportGuardados++;
                    SC_CONFLICT = true;
                }
            }
            infoUI();

        }
    }

    class HandlerStorage extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == STORAGE_OK) {
                numReportGuardados++;
            } else if (msg.what == STORAGE_ERROR) {
                SC_CONFLICT = true;
                numReportGuardados++;
            }
            infoUI();
        }
    }

    /**
     * comprueba los resultados de la sincronizacion y los envia a la vista
     */
    private void infoUI() {
        int porcent = (numReportGuardados * 100) / NUMCATALOGOS;
        if (!SC_UNAUTHORIZED && !SC_CONFLICT) {
            listenerOnProgressSync.onProgresSync(porcent, 0, null);
        }
        if (NUEVA_ACTUALIZACION && !flag) {
            listenerOnProgressSync.onNewVersionExist(true);
            flag = true;
        } else if (numReportDownload == NUMCATALOGOS && numReportGuardados == NUMCATALOGOS) {
            numReportDownload = 0; //reseteamos el contador
            numReportGuardados = 0;
            if (!SC_UNAUTHORIZED && !SC_CONFLICT && !SC_NO_CONTENT && !SC_FORBIDDEN && !SC_PAYMENT_REQUIRED && !SC_METHOD_FAILURE) {
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_OK, null, null);
            } else if (SC_UNAUTHORIZED) {
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_UNAUTHORIZED, null, null);
                SC_UNAUTHORIZED = false;
            } else if (SC_CONFLICT) {
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_CONFLICT, null, null);
                SC_CONFLICT = false;
            } else if (SC_NO_CONTENT) {
                SC_NO_CONTENT = false;
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_NO_CONTENT, null, null);
            } else if (SC_FORBIDDEN) {
                SC_FORBIDDEN = false;
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_FORBIDDEN, null, SC_FORBIDDEN_RESPONSE);
            } else if (SC_PAYMENT_REQUIRED) {
                SC_PAYMENT_REQUIRED = false;
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_PAYMENT_REQUIRED, null, null);
            } else if (SC_METHOD_FAILURE) {
                SC_FORBIDDEN = false;
                listenerOnProgressSync.onFinishSync(HttpStatus.SC_METHOD_FAILURE, SC_METHOD_FAILURE_RESPONSE, SC_METHOD_FAILURE_RESPONSE);

            }

        }
    }

    private DtoAuthentication getDataToAuthentication() {
        return new DtoAuthentication()
                .setUsername(mySharedPreferences.getString(context.getString(R.string.app_share_preference_user_account), ""))
                .setPassword(mySharedPreferences.getString(context.getString(R.string.app_share_preference_user_pass), ""))
                .setImei(Config.getIMEI())
                .setBrand(Config.getBrandDevice())
                .setOs(Config.getOs())
                .setOsVersion(Config.getOsVersion())
                .setPhone(Config.getPhoneNumber())
                .setModel(Config.getModel())
                .setDeviceId(FirebaseInstanceId.getInstance().getToken());
    }
}
