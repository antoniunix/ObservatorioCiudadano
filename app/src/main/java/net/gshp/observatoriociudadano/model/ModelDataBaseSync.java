package net.gshp.observatoriociudadano.model;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.dao.DaoAgenda;
import net.gshp.observatoriociudadano.dao.DaoCTypeReport;
import net.gshp.observatoriociudadano.dao.DaoCanal;
import net.gshp.observatoriociudadano.dao.DaoCliente;
import net.gshp.observatoriociudadano.dao.DaoEAEncuesta;
import net.gshp.observatoriociudadano.dao.DaoEAOpcionPregunta;
import net.gshp.observatoriociudadano.dao.DaoEAPregunta;
import net.gshp.observatoriociudadano.dao.DaoEASeccion;
import net.gshp.observatoriociudadano.dao.DaoEATypeOpcionRespuesta;
import net.gshp.observatoriociudadano.dao.DaoEaAnswerPdv;
import net.gshp.observatoriociudadano.dao.DaoMeasurementFilter;
import net.gshp.observatoriociudadano.dao.DaoMeasurementHead;
import net.gshp.observatoriociudadano.dao.DaoMeasurementModule;
import net.gshp.observatoriociudadano.dao.DaoPdv;
import net.gshp.observatoriociudadano.dao.DaoRtm;
import net.gshp.observatoriociudadano.dto.DtoAgenda;
import net.gshp.observatoriociudadano.dto.DtoCatalog;
import net.gshp.observatoriociudadano.dto.DtoEAEncuesta;
import net.gshp.observatoriociudadano.dto.DtoEAOpcionPregunta;
import net.gshp.observatoriociudadano.dto.DtoEAPregunta;
import net.gshp.observatoriociudadano.dto.DtoEASeccion;
import net.gshp.observatoriociudadano.dto.DtoEATipoPregunta;
import net.gshp.observatoriociudadano.dto.DtoEaAnswerPdv;
import net.gshp.observatoriociudadano.dto.DtoMeasurementFilter;
import net.gshp.observatoriociudadano.dto.DtoMeasurementHead;
import net.gshp.observatoriociudadano.dto.DtoMeasurementModule;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.dto.DtoRtm;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModelDataBaseSync {

    private final static String TAG = "ModelDataBaseSync";

    private int STORAGE_OK = 1;


    private Handler handlerstorage;

    private ExecutorService executor;

    public ModelDataBaseSync(Handler handlerstorage) {
        executor = Executors.newSingleThreadExecutor();
        this.handlerstorage = handlerstorage;

    }

    protected void syncInsertion(final NetworkTask nt) {
        Runnable command = new Runnable() {

            @Override
            public void run() {
                Type typeObjectGson;
                STORAGE_OK = 1;
                try {

                    if (nt.getTag().equals("pdv_pdv")) {

                        String json="[{\n" +
                                "\"address\":\"Dir\",\n" +
                                "\"idClient\":\"1\",\n" +
                                "\"idClientFormat\":\"1\",\n" +
                                "\"idRtm\":\"1\",\n" +
                                "\"lon\":\"0.0\",\n" +
                                "\"idState\":\"1\",\n" +
                                "\"type\":\"1\",\n" +
                                "\"idCountry\":\"1\",\n" +
                                "\"pdvCode\":\"POS1\",\n" +
                                "\"idRegion\":\"1\",\n" +
                                "\"name\":\"POS1\",\n" +
                                "\"location\":\"1\",\n" +
                                "\"id\":\"1\",\n" +
                                "\"lat\":\"0.0\"\n" +
                                "}]";
                        nt.setResponse(json);

                        Log.d("SYNC", "pdv_pdv " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoPdvPdv>>() {
                        }.getType();

                        List<DtoPdvPdv> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoPdv().delete();
                        new DaoPdv().insert(lst);
                    } else if (nt.getTag().equals("schedule")) {
                        Log.d("SYNC", "schedule " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoAgenda>>() {
                        }.getType();

                        List<DtoAgenda> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoAgenda().delete();
                        new DaoAgenda().insert(lst);
                    } else if (nt.getTag().equals("c_client")) {
                        Log.d("SYNC", "c_client " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoCatalog>>() {
                        }.getType();

                        List<DtoCatalog> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoCliente().delete();
                        new DaoCliente().insert(lst);
                    } else if (nt.getTag().equals("c_rtm")) {
                        Log.d("SYNC", "c_rtm " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoRtm>>() {
                        }.getType();

                        List<DtoRtm> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoRtm().delete();
                        new DaoRtm().insert(lst);
                    } else if (nt.getTag().equals("c_canal")) {
                        Log.d("SYNC", "c_canal " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoCatalog>>() {
                        }.getType();

                        List<DtoCatalog> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoCanal().delete();
                        new DaoCanal().insert(lst);
                    } else if (nt.getTag().equals("c_type_report")) {
                        Log.d("SYNC", "c_type_report " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoCatalog>>() {
                        }.getType();
                        List<DtoCatalog> lst = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoCTypeReport().delete();
                        new DaoCTypeReport().Insert(lst);
                    } else if (nt.getTag().equals("mam_app_module")) {
                        Log.d(TAG, "inserting into measurement_module_head " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementHead>>() {
                        }.getType();
                        List<DtoMeasurementHead> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementHead("measurement_module_head").delete();
                        new DaoMeasurementHead("measurement_module_head").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_module")) {
                        Log.d(TAG, "inserting into measurement_module " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementModule>>() {
                        }.getType();
                        List<DtoMeasurementModule> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementModule().delete();
                        new DaoMeasurementModule().Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_canal")) {
                        Log.d(TAG, "inserting into measurement_module_canal " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_canal").delete();
                        new DaoMeasurementFilter("measurement_module_canal").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_client")) {
                        Log.d(TAG, "inserting into measurement_module_client " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_client").delete();
                        new DaoMeasurementFilter("measurement_module_client").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_format")) {
                        Log.d(TAG, "inserting into measurement_module_format " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_format").delete();
                        new DaoMeasurementFilter("measurement_module_format").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_pdv")) {
                        Log.d(TAG, "inserting into measurement_module_pdv " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_pdv").delete();
                        new DaoMeasurementFilter("measurement_module_pdv").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_rtm")) {
                        Log.d(TAG, "inserting into measurement_module_rtm " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_rtm").delete();
                        new DaoMeasurementFilter("measurement_module_rtm").Insert(lstCatalog);
                    } else if (nt.getTag().equals("mam_region")) {
                        Log.d(TAG, "inserting into measurement_module_region " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoMeasurementFilter>>() {
                        }.getType();
                        List<DtoMeasurementFilter> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoMeasurementFilter("measurement_module_region").delete();
                        new DaoMeasurementFilter("measurement_module_region").Insert(lstCatalog);
                    } else if (nt.getTag().equals("ea_poll")) {
                        Log.d(TAG, "inserting into ea_poll " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEAEncuesta>>() {
                        }.getType();
                        List<DtoEAEncuesta> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEAEncuesta().delete();
                        new DaoEAEncuesta().Insert(lstCatalog);
                    } else if (nt.getTag().equals("ea_question")) {
                        Log.d(TAG, "inserting into ea_question " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEAPregunta>>() {
                        }.getType();
                        List<DtoEAPregunta> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEAPregunta().delete();
                        new DaoEAPregunta().Insert(lstCatalog);
                    } else if (nt.getTag().equals("ea_question_type_cat")) {
                        Log.d(TAG, "inserting into ea_question_type_cat " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEATipoPregunta>>() {
                        }.getType();
                        List<DtoEATipoPregunta> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEATypeOpcionRespuesta().delete();
                        new DaoEATypeOpcionRespuesta().Insert(lstCatalog);
                    } else if (nt.getTag().equals("ea_question_option")) {
                        Log.d(TAG, "inserting into ea_question_option " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEAOpcionPregunta>>() {
                        }.getType();
                        List<DtoEAOpcionPregunta> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEAOpcionPregunta().delete();
                        new DaoEAOpcionPregunta().Insert(lstCatalog);
                    } else if (nt.getTag().equals("ea_section")) {
                        Log.d(TAG, "inserting into ea_section " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEASeccion>>() {
                        }.getType();
                        List<DtoEASeccion> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEASeccion().delete();
                        new DaoEASeccion().insertAllSeccion(lstCatalog);
                    } else if (nt.getTag().equals("ea_answers_pdv")) {
                        Log.d(TAG, "inserting into ea_answers_pdv " + nt.getResponse());
                        typeObjectGson = new TypeToken<List<DtoEaAnswerPdv>>() {
                        }.getType();
                        List<DtoEaAnswerPdv> lstCatalog = new Gson().fromJson(nt.getResponse(),
                                typeObjectGson);
                        new DaoEaAnswerPdv().delete();
                        new DaoEaAnswerPdv().Insert(lstCatalog);
                    }

                } catch (Exception e) {
                    STORAGE_OK = -1;
                    e.printStackTrace();
                }

                handlerstorage.sendEmptyMessage(STORAGE_OK);
            }
        };
        executor.execute(command);

    }

}
