package net.gshp.observatoriociudadano.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.Network.NetworkConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
import net.gshp.observatoriociudadano.dao.DaoReport;
import net.gshp.observatoriociudadano.dao.DaoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoEARespuesta;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoReportToSend;
import net.gshp.observatoriociudadano.dto.DtoSendPhoto;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gnu on 16/02/18.
 */

public class ModelSend {
    private final int ENVIOFINALIZADO = -1000;
    private final int SINREPORTES = -2000;

    private NetworkConfig networkConfig;
    // este handler es el que regresa el api para avisar que ya se concluyó la
    // peticion al servicio regresandonos un httpstatus
    // y un httpResponse
    private HandlerTask handlerTask;

    // este handler es para cominicar a la vista de cualquier evento durante el
    // envio, asi como su termino.

    // estas variables son para saber cuantas cabeceras de reportes (check's)
    // deben ser enviados
    // y asi poder avisar a los subreportes que comiencen a enviar
    private Integer NUMREPORTES;
    private Integer REPORTESENVIADOS = 0;


    // estas variables son para saber cuantos subreportes se deberan enviar y
    // cuanso se han enviado
    // y asi poder informar a la vista que se a concluido el envio
    private Integer SubReportAEnviar = 0;
    private Integer SubReportesEnviados = 0;

    /* Estas variables son para el envío de subreportes dependientes de otros subreportes donde
    * necesitan que esos subreportes ya estén enviados para poder ligarlos en productivo */
    private Integer SubReportDependienteAEnviar = 0;
    private Integer SubReportDependienteEnviados = 0;

    // estas variables son para saber cuantas fotos se deberan enviar y cuando
    // se han enviado
    // y asi poder informar a la vista que se a concluido el envio
    private Integer SubReportFOTOAEnviar = 0;
    private Integer SubReportesFOTOEnviados = 0;

    // banderas para detectar si halgo salio mal
    private boolean UNAUTHORIZED = false;
    private boolean CONFLICT = false;
    /* Bandera que sirve para saber cuando exista algun registro por enviar */
    private boolean REPORT_TO_SEND = false;

    private DaoReport daoReportReport;
    private DaoEARespuesta daoeaRespuesta;
    private DaoReportCensus daoReportCensus;

    private List<DtoReportToSend> lstReports;

    private List<List<DtoEARespuesta>> respuestas;
    private List<List<DtoReportCensus>> lstReportCensus;

    private List<DtoSendPhoto> lstDtoReportRespuestasFotos;


    public ModelSend() {
        handlerTask = new HandlerTask(Looper.getMainLooper());
        networkConfig = new NetworkConfig(handlerTask, ContextApp.context);

        daoReportReport = new DaoReport();
        daoeaRespuesta = new DaoEARespuesta();
        daoReportCensus = new DaoReportCensus();
    }

    public void start() {
        lstReports = daoReportReport.SelectToSend();

        NUMREPORTES = lstReports.size();
        if (NUMREPORTES != 0) {
              /*
             * Se activa la bandera para poder notificar que hay algo por enviar
			 */
            REPORT_TO_SEND = true;
            new Thread() {
                public void run() {
                    /* Reportes de PDV's diferentes a uno nuevo*/
                    for (int j = 0; j < lstReports.size(); j++) {

                        String json = new Gson().toJson(lstReports.get(j));
                        Log.e(ContextApp.context.getResources().getString(R.string.app_name), "Report " + json);
                        Map<String, String> header = new HashMap<String, String>();
                        header.put(ContextApp.context.getString(R.string.network_header_name_application_json), ContextApp.context.getString(R.string.network_header_application_json));
                        networkConfig.POST("report/create", json, "rprt" + lstReports.get(j).getId(), header);
                    }
                }
            }.start();
        } else {
            Log.e("send", "sin cabecera");
            /*
             * En caso de no tener cabeceras por enviar entonces pasa a
			 * verificar subreportes por enviar
			 */
            sendSubReportes();
        }
    }

    private void sendSubReportes() {
        Log.e("send", "sendsubreportes");
        respuestas = daoeaRespuesta.selectToSend();
        lstReportCensus = daoReportCensus.selectToSend();

        SubReportAEnviar = respuestas.size() + lstReportCensus.size();

        if (SubReportAEnviar != 0) {
             /*
             * Se activa la bandera para poder notificar que hay algo por enviar
			 */
            REPORT_TO_SEND = true;
            new Thread() {
                public void run() {

                    // Encuesta
                    Log.e("Nestle", "Report Encuesta" + respuestas.size());
                    for (int i = 0; i < respuestas.size(); i++) {
                        String json = new Gson().toJson(respuestas.get(i));
                        System.out.println(json);
                        Map<String, String> header = new HashMap<>();
                        header.put(ContextApp.context.getString(R.string.network_header_name_application_json), ContextApp.context.getString(R.string.network_header_application_json));
                        networkConfig.POST("multireport/insertnt/poll/1", json, "rsaa" + respuestas.get(i).get(0)
                                .getIdReporteLocal(), header);
                    }

                    //Census
                    Log.e("Observador", "Report Census" + lstReportCensus.size());
                    for (int i = 0; i < lstReportCensus.size(); i++) {
                        String json = new Gson().toJson(lstReportCensus.get(i));
                        System.out.println(json);
                        Map<String, String> header = new HashMap<>();
                        header.put(ContextApp.context.getString(R.string.network_header_name_application_json),
                                ContextApp.context.getString(R.string.network_header_application_json));
                        networkConfig.POST("multireport/insertnt/rpdvcenso/1", json, "rsab" + lstReportCensus.get(i).get(0)
                                .getIdReporteLocal(), header);
                    }

                }

            }.start();
        } else {
            sendSubReportesDependientes();
        }
    }

    private void sendSubReportesDependientes() {
        Log.e("send", "Subreportenviar method");
//        lstLstNewExhibitionDetail = daoReportNewExhibitionDetail.SelectToSend();
//        lstLstExhibitionCause = daoReportExhibitionsCause.SelectToSend();

        SubReportDependienteAEnviar = 0;//lstLstNewExhibitionDetail.size() + lstLstExhibitionCause.size();

        if (SubReportDependienteAEnviar != 0) {
              /*
             * Se activa la bandera para poder notificar que hay algo por enviar
			 */
            REPORT_TO_SEND = true;
            new Thread() {
                public void run() {
                    // Report New Exhibition Detail
//                    Log.e(ContextApp.context.getResources().getString(R.string.app_name),
//                            "Report detail " + lstLstNewExhibitionDetail.size());
//                    for (int i = 0; i < lstLstNewExhibitionDetail.size(); i++) {
//                        System.out.println(new Gson().toJson(lstLstNewExhibitionDetail.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstLstNewExhibitionDetail.get(i))));
//                        networkConfig.POST("multireport/insertnt/raddexhdetail/1", nameValuePairs, "rsba" + lstLstNewExhibitionDetail.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//
//                    // Report Exhibition Cause
//                    Log.e(ContextApp.context.getResources().getString(R.string.app_name),
//                            "Report exhibitions cause " + lstLstExhibitionCause.size());
//                    for (int i = 0; i < lstLstExhibitionCause.size(); i++) {
//                        System.out.println(new Gson().toJson(lstLstExhibitionCause.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstLstExhibitionCause.get(i))));
//                        networkConfig.POST("multireport/insertnt/rexhcomment/1", nameValuePairs, "rsbb" + lstLstExhibitionCause.get(i).get(0)
//                                .getIdReportLocal());
//                    }
                }
            }.start();
        } else {
            Log.e("send", "foto else");
            sendFoto();
        }
    }

    private void sendFoto() {
        Log.e("send", "foto METHOD");
        lstDtoReportRespuestasFotos = daoeaRespuesta.SelectToSendPhotoV1();
        SubReportFOTOAEnviar = lstDtoReportRespuestasFotos.size();
        Log.e("photo", "size " + SubReportAEnviar);
        if (SubReportFOTOAEnviar != 0) {
            new Thread() {
                public void run() {

                    //FOTO Foto Respuesta
                    for (int i = 0; i < lstDtoReportRespuestasFotos.size(); i++) {
                        String json=new Gson().toJson(lstDtoReportRespuestasFotos.get(i));
                        Log.e("send","send "+json);
                        Map<String, String> header = new HashMap<>();
                        header.put(ContextApp.context.getString(R.string.network_header_name_application_json), ContextApp.context.getString(R.string.network_header_multipart_data));
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("json", json));
                        networkConfig.multipartFile("image/save",
                                lstDtoReportRespuestasFotos.get(i).getPath(),nameValuePairs, "rfaa" + lstDtoReportRespuestasFotos.get(i).getId(),true);
                    }

                }
            }.start();
        } else {
            Log.e(ContextApp.context.getResources().getString(R.string.app_name),
                    "@Sin fotos a enviar, ¿Se activó la bandera?: " + REPORT_TO_SEND);
            /*
             * Si paso por todos los métodos anteriores y la bandera no se
			 * activo en alguno de ellos significa que no hubo reportes por
			 * mandar por lo que se debe notificar al usuario que no tiene algo
			 * por mandar
			 */
            if (!REPORT_TO_SEND) {
//                handlerUI.sendEmptyMessage(SINREPORTES);
            }
        }

    }

    class HandlerTask extends Handler {

        public HandlerTask(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            NetworkTask completedTask = (NetworkTask) msg.obj;
            System.out.println("STATUS " + completedTask.getResponseStatus()
                    + "  " + completedTask.getTag());

            if (completedTask.getTag().contains("rprt")) {
                REPORTESENVIADOS++;
            } else if (completedTask.getTag().contains("rsaa")
                    || completedTask.getTag().contains("rsab")
                    )

                SubReportesEnviados++;

            else if (completedTask.getTag().contains("rsba")
                    || completedTask.getTag().contains("rsbb")
                    )
                SubReportDependienteEnviados++;
            else if (completedTask.getTag().contains("rfaa")

                    )
                SubReportesFOTOEnviados++;


            Log.e("send", "fotos enviadas " + SubReportesFOTOEnviados);

            if (completedTask.getResponseStatus() == HttpStatus.SC_OK
                    || completedTask.getResponseStatus() == HttpStatus.SC_CREATED) {
                if (completedTask.getTag().contains("rprt")) {
                    try {
                        // se valida que la respuesta del servidor sea un numero
                        Integer.parseInt(completedTask.getResponse());
                        daoReportReport.Update(completedTask.getTag().substring(4),
                                completedTask.getResponse());
                    } catch (NumberFormatException e) {
                        // TODO: handle exception
                        System.out.println("respuesta invalida "
                                + completedTask.getResponse());
                    }

                } else if (completedTask.getTag().contains("rsaa")) {
                    daoeaRespuesta.updateEnviado(completedTask.getTag().substring(4));
                } else if (completedTask.getTag().contains("rsab")) {
                    daoReportCensus.updateSend(completedTask.getTag().substring(4));
                }
                //fotos
                else if (completedTask.getTag().contains("rfaa")) {
                    daoeaRespuesta.updatephoto(completedTask.getTag().substring(4));
                }

            } else if (completedTask.getResponseStatus() == HttpStatus.SC_UNAUTHORIZED)
                UNAUTHORIZED = true;
            else if (completedTask.getResponseStatus() != HttpStatus.SC_OK
                    && completedTask.getResponseStatus() != HttpStatus.SC_CREATED
                    && completedTask.getResponseStatus() != HttpStatus.SC_UNAUTHORIZED)
                CONFLICT = true;

            // hasta que se hayan enviado todos los reportes se comenzara a
            // enviar los reportes de los nuevos PDV's.
            if (REPORTESENVIADOS.equals(NUMREPORTES)
                    && completedTask.getTag().contains("rprt")) {
                Log.e("send", "enviado cabecera " + " enviados " + REPORTESENVIADOS + " numero reportes " + NUMREPORTES);
                sendSubReportes();
            }

            // hasta que se hayan enviado los subreportes independientes se comenzara a enviar
            // los subreportes dependientes
            if (REPORTESENVIADOS.equals(NUMREPORTES)
                    && SubReportAEnviar.equals(SubReportesEnviados)
                    && completedTask.getTag().contains("rsa")) {
                Log.e("send", "enviado sub " + " enviados " + SubReportAEnviar + " numero reportes " + SubReportesEnviados);
                sendSubReportesDependientes();

            }


            /* Hasta que se heyan enviado los subreportes dependientes se comenzará
            * a enviar las fotos*/
            if (REPORTESENVIADOS.equals(NUMREPORTES)
                    && SubReportAEnviar.equals(SubReportesEnviados)
                    && SubReportDependienteAEnviar.equals(SubReportDependienteEnviados)
                    && completedTask.getTag().contains("rsb")) {

                Log.e("send", "enviado sub " + " enviados " + SubReportDependienteAEnviar + " numero reportes " + SubReportDependienteEnviados);
                sendFoto();

            }


            if (!UNAUTHORIZED && !CONFLICT && REPORT_TO_SEND
                    && REPORTESENVIADOS.equals(NUMREPORTES)
                    && SubReportAEnviar.equals(SubReportesEnviados)
                    && SubReportDependienteAEnviar.equals(SubReportDependienteEnviados)
                    && SubReportFOTOAEnviar.equals(SubReportesFOTOEnviados)) {
//                handlerUI.sendEmptyMessage(ENVIOFINALIZADO);
                NUMREPORTES = 0;
                REPORTESENVIADOS = 0;

                SubReportAEnviar = 0;
                SubReportesEnviados = 0;
                SubReportDependienteAEnviar = 0;
                SubReportDependienteEnviados = 0;
                SubReportFOTOAEnviar = 0;
                SubReportesFOTOEnviados = 0;
//                updateReportStatus();
            } else if (UNAUTHORIZED) {
//                handlerUI.sendEmptyMessage(HttpStatus.SC_UNAUTHORIZED);
                NUMREPORTES = 0;
                REPORTESENVIADOS = 0;
                SubReportAEnviar = 0;
                SubReportesEnviados = 0;
                SubReportDependienteAEnviar = 0;
                SubReportDependienteEnviados = 0;
                SubReportFOTOAEnviar = 0;
                SubReportesFOTOEnviados = 0;
            } else if (CONFLICT && SubReportAEnviar.equals(SubReportesEnviados)) {
//                handlerUI.sendEmptyMessage(HttpStatus.SC_CONFLICT);
                NUMREPORTES = 0;
                REPORTESENVIADOS = 0;
                SubReportAEnviar = 0;
                SubReportesEnviados = 0;
                SubReportDependienteAEnviar = 0;
                SubReportDependienteEnviados = 0;
                SubReportFOTOAEnviar = 0;
                SubReportesFOTOEnviados = 0;
//                updateReportStatus();
            }
        }
    }

    /* Este método verifica que todos los registros correspondientes a las cabeceras se hayan enviado para poder cambiar
        * el status del reporte de enviado(send=1) a enviado completamente(send=2) */
//    private void updateReportStatus() {
//        List<Integer> lstReportsSend = daoReportReport.selectAlreadySend();
//        for (int i = 0; i < lstReportsSend.size(); i++) {
//			/* Si todos los subreportes relacionados a ese reporte ya estan enviado entonces cambia el status=2 */
//            int idReportLocal = lstReportsSend.get(i);
//            if (!daoReportReport.hasSubreportToSend(idReportLocal) && !daoReportReport.hasSubreportDependentToSend(idReportLocal)
//                    && !daoReportReport.hasPhotosToSend(idReportLocal)) {
//                daoReportReport.updateStatusCompleteAll(idReportLocal);
//            }
//        }
//    }

}
