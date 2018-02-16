//package net.gshp.observatoriociudadano.model;
//
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.google.gson.Gson;
//
//import net.gshp.APINetwork.NetworkTask;
//import net.gshp.observatoriociudadano.Network.NetworkConfig;
//import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
//import net.gshp.observatoriociudadano.dao.DaoReport;
//import net.gshp.observatoriociudadano.dto.DtoReportToSend;
//
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by gnu on 16/02/18.
// */
//
//public class ModelSend {
//    private final int ENVIOFINALIZADO = -1000;
//    private final int SINREPORTES = -2000;
//
//    private NetworkConfig networkConfig;
//    // este handler es el que regresa el api para avisar que ya se concluyó la
//    // peticion al servicio regresandonos un httpstatus
//    // y un httpResponse
//    private HandlerTask handlerTask;
//
//    // este handler es para cominicar a la vista de cualquier evento durante el
//    // envio, asi como su termino.
//
//    // estas variables son para saber cuantas cabeceras de reportes (check's)
//    // deben ser enviados
//    // y asi poder avisar a los subreportes que comiencen a enviar
//    private Integer NUMREPORTES;
//    private Integer REPORTESENVIADOS = 0;
//
//
//    // estas variables son para saber cuantos subreportes se deberan enviar y
//    // cuanso se han enviado
//    // y asi poder informar a la vista que se a concluido el envio
//    private Integer SubReportAEnviar = 0;
//    private Integer SubReportesEnviados = 0;
//
//    /* Estas variables son para el envío de subreportes dependientes de otros subreportes donde
//    * necesitan que esos subreportes ya estén enviados para poder ligarlos en productivo */
//    private Integer SubReportDependienteAEnviar = 0;
//    private Integer SubReportDependienteEnviados = 0;
//
//    // estas variables son para saber cuantas fotos se deberan enviar y cuando
//    // se han enviado
//    // y asi poder informar a la vista que se a concluido el envio
//    private Integer SubReportFOTOAEnviar = 0;
//    private Integer SubReportesFOTOEnviados = 0;
//
//    // banderas para detectar si halgo salio mal
//    private boolean UNAUTHORIZED = false;
//    private boolean CONFLICT = false;
//    /* Bandera que sirve para saber cuando exista algun registro por enviar */
//    private boolean REPORT_TO_SEND = false;
//
//    private DaoReport daoReportReport;
//    private DaoEARespuesta daoeaRespuesta;
//
//    private List<DtoReportToSend> lstReports;
//
//    private List<List<DtoEARespuesta>> respuestas;
//
//    private List<DtoReportPhotoActivity> lstDtoReportPhotoActivities;
//    private List<DtoReportCompetition> lstCompetitions;
//    private List<DtoEARespuesta> lstDtoReportRespuestasFotos;
//    private List<DtoReportPhoto> lstReportPhotos;
//
//
//    public ModelSend() {
//        handlerTask = new HandlerTask();
//        networkConfig = new NetworkConfig(handlerTask, ContextApp.context);
//
//        daoReportReport = new DaoReport();
//        daoReportAnswerTask = new DaoReportAnswerTask();
//        daoReportActivity = new DaoReportActivity();
//        daoReportActivityComment = new DaoReportActivityComment();
//        daoReportAvailability = new DaoReportAvailability();
//        daoReportExpiration = new DaoReportExpiration();
//        daoReportCompetition = new DaoReportCompetition();
//        daoReportExhibition = new DaoReportExhibition();
//        daoReportPrice = new DaoReportPrice();
//        daoReportScannAlert = new DaoReportScannAlert();
//        daoeaRespuesta = new DaoEARespuesta();
//        daoReportPhotoActivity = new DaoReportPhotoActivity();
//        daoReportPhoto = new DaoReportPhoto();
//        daoReportSkuShelf = new DaoReportSkuShelf();
//        daoReportSos = new DaoReportSos();
//        daoReportTask = new DaoReportTask();
//    }
//
//    public void start() {
//        lstReports = daoReportReport.SelectToSend();
//
//        NUMREPORTES = lstReports.size();
//        if (NUMREPORTES != 0) {
//              /*
//             * Se activa la bandera para poder notificar que hay algo por enviar
//			 */
//            REPORT_TO_SEND = true;
//            new Thread() {
//                public void run() {
//                    /* Reportes de PDV's diferentes a uno nuevo*/
//                    for (int j = 0; j < lstReports.size(); j++) {
//                        Log.e(ContextApp.context.getResources().getString(R.string.app_name),
//                                "Report " + new Gson().toJson(lstReports.get(j)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstReports.get(j))));
//                        networkConfig.POST("report", nameValuePairs, "rprt" + lstReports.get(j).getId());
//                        System.out.println("cabecera  " + new Gson().toJson(lstReports.get(j)));
//                    }
//                }
//            }.start();
//        } else {
//            Log.e("send", "sin cabecera");
//            /*
//             * En caso de no tener cabeceras por enviar entonces pasa a
//			 * verificar subreportes por enviar
//			 */
//            sendSubReportes();
//        }
//    }
//
//    private void sendSubReportes() {
//        Log.e("send", "sendsubreportes");
//        lstLstReportAnswerTask = daoReportAnswerTask.SelectToSend();
//        lstlstreportActivity = daoReportActivity.SelecttoSend();
//        lstlstReportComment = daoReportActivityComment.SelecttoSend();
//        lstlstReportAvailability = daoReportAvailability.SelecttoSend();
//        lstlstReportExpiration = daoReportExpiration.SelecttoSend();
//        lstlstReportCompetition = daoReportCompetition.SelecttoSend();
//        lstlstreportExhibition = daoReportExhibition.SelecttoSend();
//        lstlstReportPrice = daoReportPrice.SelecttoSend();
//        respuestas = daoeaRespuesta.selectToSend();
//        lstlstReportPhoto = daoReportPhoto.SelecttoSend();
//        lstlstReportSacanAlert = daoReportScannAlert.SelecttoSend();
//        lstReportSkuShelf = daoReportSkuShelf.SelecttoSend();
//        lstReportSos = daoReportSos.SelecttoSend();
//        lstReportTask = daoReportTask.selectToSend();
//
//        SubReportAEnviar = lstLstReportAnswerTask.size()
//                + lstlstreportActivity.size()
//                + lstlstReportComment.size()
//                + lstlstReportAvailability.size()
//                + lstlstReportExpiration.size()
//                + lstlstReportCompetition.size()
//                + lstlstreportExhibition.size()
//                + lstlstReportPrice.size()
//                + respuestas.size()
//                + lstlstReportPhoto.size()
//                + lstlstReportSacanAlert.size()
//                + lstReportSkuShelf.size()
//                + lstReportSos.size()
//                + lstReportTask.size();
//
//        if (SubReportAEnviar != 0) {
//             /*
//             * Se activa la bandera para poder notificar que hay algo por enviar
//			 */
//            REPORT_TO_SEND = true;
//            new Thread() {
//                public void run() {
//                    // Report Answer task
//                    Log.e(ContextApp.context.getResources().getString(R.string.app_name),
//                            "Report answer task " + lstLstReportAnswerTask.size());
//                    for (int i = 0; i < lstLstReportAnswerTask.size(); i++) {
//                        System.out.println(new Gson().toJson(lstLstReportAnswerTask.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstLstReportAnswerTask.get(i))));
//                        networkConfig.POST("task/report", nameValuePairs, "rsaa" + lstLstReportAnswerTask.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    Log.e("Nestle", "Report Activity " + lstlstreportActivity.size());
//                    for (int i = 0; i < lstlstreportActivity.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstreportActivity.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstreportActivity.get(i))));
//                        networkConfig.POST("multireport/insertnt/ractivity/1", nameValuePairs, "rsab" + lstlstreportActivity.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Report Activity Comment
//                    Log.e("Nestle", "Report Activity comment" + lstlstReportComment.size());
//                    for (int i = 0; i < lstlstReportComment.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportComment.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportComment.get(i))));
//                        networkConfig.POST("multireport/insertnt/rcomment/1", nameValuePairs, "rsac" + lstlstReportComment.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Report Availability
//                    Log.e("Nestle", "Report availability" + lstlstReportAvailability.size());
//                    for (int i = 0; i < lstlstReportAvailability.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportAvailability.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportAvailability.get(i))));
//                        networkConfig.POST("multireport/insertnt/ravailability/1", nameValuePairs, "rsad" + lstlstReportAvailability.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//
//                    // Report Caducidad
//                    Log.e("Nestle", "Report Caducidad" + lstlstReportExpiration.size());
//                    for (int i = 0; i < lstlstReportExpiration.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportExpiration.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportExpiration.get(i))));
//                        networkConfig.POST("multireport/insertnt/rcaducity/1", nameValuePairs, "rsae" + lstlstReportExpiration.get(i).get(0)
//                                .getId_report_local());
//                    }
//                    // Report Competition
//                    Log.e("Nestle", "Report Competition" + lstlstReportCompetition.size());
//                    for (int i = 0; i < lstlstReportCompetition.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportCompetition.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportCompetition.get(i))));
//                        networkConfig.POST("multireport/insertnt/rcompetition/1", nameValuePairs, "rsaf" + lstlstReportCompetition.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Report Exhibition
//                    Log.e("Nestle", "Report Exhibition" + lstlstreportExhibition.size());
//                    for (int i = 0; i < lstlstreportExhibition.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstreportExhibition.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstreportExhibition.get(i))));
//                        networkConfig.POST("multireport/insertnt/rexhibition/1", nameValuePairs, "rsag" + lstlstreportExhibition.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Report Price
//                    Log.e("Nestle", "Report Price" + lstlstReportPrice.size());
//                    for (int i = 0; i < lstlstReportPrice.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportPrice.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportPrice.get(i))));
//                        networkConfig.POST("multireport/insertnt/rprice/1", nameValuePairs, "rsah" + lstlstReportPrice.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//
//                    // Encuesta
//                    Log.e("Nestle", "Report Encuesta" + respuestas.size());
//                    for (int i = 0; i < respuestas.size(); i++) {
//                        System.out.println(new Gson().toJson(respuestas.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(respuestas.get(i))));
//                        networkConfig.POST("multireport/insertnt/poll/1", nameValuePairs, "rsai" + respuestas.get(i).get(0)
//                                .getIdReporteLocal());
//                    }
//                    // Photo
//                    Log.e("Nestle", "Report photo " + lstlstReportPhoto.size());
//                    for (int i = 0; i < lstlstReportPhoto.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportPhoto.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportPhoto.get(i))));
//                        networkConfig.POST("multireport/insertnt/rtypephoto/1", nameValuePairs, "rsaj" + lstlstReportPhoto.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // scan alert
//                    Log.e("Nestle", "Report scan alert" + lstlstReportSacanAlert.size());
//                    for (int i = 0; i < lstlstReportSacanAlert.size(); i++) {
//                        System.out.println(new Gson().toJson(lstlstReportSacanAlert.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstlstReportSacanAlert.get(i))));
//                        networkConfig.POST("multireport/insertnt/scanalert/1", nameValuePairs, "rsak" + lstlstReportSacanAlert.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Anaquel
//                    Log.e("Nestle", "Report Anaquel" + lstReportSkuShelf.size());
//                    for (int i = 0; i < lstReportSkuShelf.size(); i++) {
//                        System.out.println(new Gson().toJson(lstReportSkuShelf.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstReportSkuShelf.get(i))));
//                        networkConfig.POST("multireport/insertnt/rshelf/1", nameValuePairs, "rsal" + lstReportSkuShelf.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                    // Sos
//                    Log.e("Nestle", "Report Sos" + lstReportSos.size());
//                    for (int i = 0; i < lstReportSos.size(); i++) {
//                        System.out.println(new Gson().toJson(lstReportSos.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstReportSos.get(i))));
//                        networkConfig.POST("multireport/insertnt/rsos/1", nameValuePairs, "rsam" + lstReportSos.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//
//                    // Task
//                    Log.e(ContextApp.context.getResources().getString(R.string.app_name), "Report TASK" + respuestas.size());
//                    for (int i = 0; i < lstReportTask.size(); i++) {
//                        System.out.println(new Gson().toJson(lstReportTask.get(i)));
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstReportTask.get(i))));
//                        networkConfig.POST("task/add", nameValuePairs, "rsan" + lstReportTask.get(i).get(0)
//                                .getIdReportLocal());
//                    }
//                }
//
//            }.start();
//        } else {
//            sendSubReportesDependientes();
//        }
//    }
//
//    private void sendSubReportesDependientes() {
//        Log.e("send", "Subreportenviar method");
////        lstLstNewExhibitionDetail = daoReportNewExhibitionDetail.SelectToSend();
////        lstLstExhibitionCause = daoReportExhibitionsCause.SelectToSend();
//
//        SubReportDependienteAEnviar = 0;//lstLstNewExhibitionDetail.size() + lstLstExhibitionCause.size();
//
//        if (SubReportDependienteAEnviar != 0) {
//              /*
//             * Se activa la bandera para poder notificar que hay algo por enviar
//			 */
//            REPORT_TO_SEND = true;
//            new Thread() {
//                public void run() {
//                    // Report New Exhibition Detail
////                    Log.e(ContextApp.context.getResources().getString(R.string.app_name),
////                            "Report detail " + lstLstNewExhibitionDetail.size());
////                    for (int i = 0; i < lstLstNewExhibitionDetail.size(); i++) {
////                        System.out.println(new Gson().toJson(lstLstNewExhibitionDetail.get(i)));
////                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
////                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstLstNewExhibitionDetail.get(i))));
////                        networkConfig.POST("multireport/insertnt/raddexhdetail/1", nameValuePairs, "rsba" + lstLstNewExhibitionDetail.get(i).get(0)
////                                .getIdReportLocal());
////                    }
////
////                    // Report Exhibition Cause
////                    Log.e(ContextApp.context.getResources().getString(R.string.app_name),
////                            "Report exhibitions cause " + lstLstExhibitionCause.size());
////                    for (int i = 0; i < lstLstExhibitionCause.size(); i++) {
////                        System.out.println(new Gson().toJson(lstLstExhibitionCause.get(i)));
////                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
////                        nameValuePairs.add(new BasicNameValuePair("json", new Gson().toJson(lstLstExhibitionCause.get(i))));
////                        networkConfig.POST("multireport/insertnt/rexhcomment/1", nameValuePairs, "rsbb" + lstLstExhibitionCause.get(i).get(0)
////                                .getIdReportLocal());
////                    }
//                }
//            }.start();
//        } else {
//            Log.e("send", "foto else");
//            sendFoto();
//        }
//    }
//
//    private void sendFoto() {
//        Log.e("send", "foto METHOD");
//        lstDtoReportPhotoActivities = daoReportPhotoActivity.SelectToSend();
//        lstCompetitions = daoReportCompetition.SelectToSendPhoto();
//        lstDtoReportRespuestasFotos = daoeaRespuesta.SelectToSendPhoto();
//        lstReportPhotos = daoReportPhoto.SelecttoSendPhoto();
//
//        SubReportFOTOAEnviar = lstDtoReportPhotoActivities.size() +
//                lstCompetitions.size() +
//                lstDtoReportRespuestasFotos.size() +
//                lstReportPhotos.size();
//        ;
//        Log.e("photo", "size " + SubReportAEnviar);
//        if (SubReportFOTOAEnviar != 0) {
//            new Thread() {
//                public void run() {
//
////                    // foto actividades
//                    for (int i = 0; i < lstDtoReportPhotoActivities.size(); i++) {
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(3);
//                        nameValuePairs.add(new BasicNameValuePair("hash", lstDtoReportPhotoActivities.get(i).getHash() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("pdv", lstDtoReportPhotoActivities.get(i).getIdPdv() + ""));
//                        Log.e("Description", lstDtoReportPhotoActivities.get(i).getDescription());
//                        nameValuePairs.add(new BasicNameValuePair("description", lstDtoReportPhotoActivities.get(i).getDescription() + ""));
//                        networkConfig.POST_IMAGE("multireport/image/ractivity/1",
//                                lstDtoReportPhotoActivities.get(i).getPath(), nameValuePairs, "rfaa" + lstDtoReportPhotoActivities.get(i).getId());
//                    }
//                    //FOTO Competencia
//                    for (int i = 0; i < lstCompetitions.size(); i++) {
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(3);
//                        nameValuePairs.add(new BasicNameValuePair("hash", lstCompetitions.get(i).getHash() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("pdv", lstCompetitions.get(i).getPdv() + ""));
//                        Log.e("Description", lstCompetitions.get(i).getDescription());
//                        nameValuePairs.add(new BasicNameValuePair("description", lstCompetitions.get(i).getDescription() + ""));
//                        networkConfig.POST_IMAGE("multireport/image/rcompetition/1",
//                                lstCompetitions.get(i).getPath(), nameValuePairs, "rfab" + lstCompetitions.get(i).getId());
//                    }
//                    //FOTO Foto Respuesta
//                    for (int i = 0; i < lstDtoReportRespuestasFotos.size(); i++) {
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("hash", lstDtoReportRespuestasFotos.get(i).getHash() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("pdv", lstDtoReportRespuestasFotos.get(i).getPdv() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("description", lstDtoReportRespuestasFotos.get(i).getDescription() + ""));
//                        networkConfig.POST_IMAGE("multireport/image/poll/1",
//                                lstDtoReportRespuestasFotos.get(i).getRespuesta(), nameValuePairs, "rfac" + lstDtoReportRespuestasFotos.get(i).getId());
//                    }
//                    //FOTO Report photo
//                    for (int i = 0; i < lstReportPhotos.size(); i++) {
//                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                        nameValuePairs.add(new BasicNameValuePair("hash", lstReportPhotos.get(i).getHash() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("pdv", lstReportPhotos.get(i).getIdPdv() + ""));
//                        nameValuePairs.add(new BasicNameValuePair("description", lstReportPhotos.get(i).getDescription() + ""));
//                        networkConfig.POST_IMAGE("multireport/image/rtypephoto/1",
//                                lstReportPhotos.get(i).getPath(), nameValuePairs, "rfad" + lstReportPhotos.get(i).getId());
//                    }
//
//                }
//            }.start();
//        } else {
//            Log.e(ContextApp.context.getResources().getString(R.string.app_name),
//                    "@Sin fotos a enviar, ¿Se activó la bandera?: " + REPORT_TO_SEND);
//            /*
//             * Si paso por todos los métodos anteriores y la bandera no se
//			 * activo en alguno de ellos significa que no hubo reportes por
//			 * mandar por lo que se debe notificar al usuario que no tiene algo
//			 * por mandar
//			 */
//            if (!REPORT_TO_SEND) {
////                handlerUI.sendEmptyMessage(SINREPORTES);
//            }
//        }
//
//    }
//
//    class HandlerTask extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            NetworkTask completedTask = (NetworkTask) msg.obj;
//            System.out.println("STATUS " + completedTask.getResponseStatus()
//                    + "  " + completedTask.getTag());
//
//            if (completedTask.getTag().contains("rprt")) {
//                REPORTESENVIADOS++;
//            } else if (completedTask.getTag().contains("rsaa")
//                    || completedTask.getTag().contains("rsab")
//                    || completedTask.getTag().contains("rsac")
//                    || completedTask.getTag().contains("rsad")
//                    || completedTask.getTag().contains("rsae")
//                    || completedTask.getTag().contains("rsaf")
//                    || completedTask.getTag().contains("rsag")
//                    || completedTask.getTag().contains("rsah")
//                    || completedTask.getTag().contains("rsai")
//                    || completedTask.getTag().contains("rsaj")
//                    || completedTask.getTag().contains("rsak")
//                    || completedTask.getTag().contains("rsal")
//                    || completedTask.getTag().contains("rsam")
//                    || completedTask.getTag().contains("rsan"))
//
//                SubReportesEnviados++;
//
//            else if (completedTask.getTag().contains("rsba")
//                    || completedTask.getTag().contains("rsbb")
//                    )
//                SubReportDependienteEnviados++;
//            else if (completedTask.getTag().contains("rfaa")
//                    || completedTask.getTag().contains("rfab")
//                    || completedTask.getTag().contains("rfac")
//                    || completedTask.getTag().contains("rfad")
//                    )
//                SubReportesFOTOEnviados++;
//
//
//            Log.e("send", "fotos enviadas " + SubReportesFOTOEnviados);
//
//            if (completedTask.getResponseStatus() == HttpStatus.SC_OK
//                    || completedTask.getResponseStatus() == HttpStatus.SC_CREATED) {
//                if (completedTask.getTag().contains("rprt")) {
//                    try {
//                        // se valida que la respuesta del servidor sea un numero
//                        Integer.parseInt(completedTask.getResponse());
//                        daoReportReport.Update(completedTask.getTag().substring(4),
//                                completedTask.getResponse());
//                    } catch (NumberFormatException e) {
//                        // TODO: handle exception
//                        System.out.println("respuesta invalida "
//                                + completedTask.getResponse());
//                    }
//
//                } else if (completedTask.getTag().contains("rsaa")) {
//                    daoReportAnswerTask.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsab")) {
//                    daoReportActivity.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsac")) {
//                    daoReportActivityComment.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsad")) {
//                    daoReportAvailability.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsae")) {
//                    daoReportExpiration.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsaf")) {
//                    daoReportCompetition.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsag")) {
//                    daoReportExhibition.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsah")) {
//                    daoReportPrice.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsai")) {
//                    daoeaRespuesta.updateEnviado(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsaj")) {
//                    daoReportPhoto.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsak")) {
//                    daoReportScannAlert.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsal")) {
//                    daoReportSkuShelf.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rsam")) {
//                    daoReportSos.Update(completedTask.getTag().substring(4));
//                }else if (completedTask.getTag().contains("rsan")) {
//                    daoReportTask.update(completedTask.getTag().substring(4));
//                }
//                //fotos
//                else if (completedTask.getTag().contains("rfaa")) {
//                    daoReportPhotoActivity.Update(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rfab")) {
//                    daoReportCompetition.UpdatePhoto(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rfac")) {
//                    daoeaRespuesta.updatephoto(completedTask.getTag().substring(4));
//                } else if (completedTask.getTag().contains("rfad")) {
//                    daoReportPhoto.UpdatePhoto(completedTask.getTag().substring(4));
//                }
//
//            } else if (completedTask.getResponseStatus() == HttpStatus.SC_UNAUTHORIZED)
//                UNAUTHORIZED = true;
//            else if (completedTask.getResponseStatus() != HttpStatus.SC_OK
//                    && completedTask.getResponseStatus() != HttpStatus.SC_CREATED
//                    && completedTask.getResponseStatus() != HttpStatus.SC_UNAUTHORIZED)
//                CONFLICT = true;
//
//            // hasta que se hayan enviado todos los reportes se comenzara a
//            // enviar los reportes de los nuevos PDV's.
//            if (REPORTESENVIADOS.equals(NUMREPORTES)
//                    && completedTask.getTag().contains("rprt")) {
//                Log.e("send", "enviado cabecera " + " enviados " + REPORTESENVIADOS + " numero reportes " + NUMREPORTES);
//                sendSubReportes();
//            }
//
//            // hasta que se hayan enviado los subreportes independientes se comenzara a enviar
//            // los subreportes dependientes
//            if (REPORTESENVIADOS.equals(NUMREPORTES)
//                    && SubReportAEnviar.equals(SubReportesEnviados)
//                    && completedTask.getTag().contains("rsa")) {
//                Log.e("send", "enviado sub " + " enviados " + SubReportAEnviar + " numero reportes " + SubReportesEnviados);
//                sendSubReportesDependientes();
//
//            }
//
//
//            /* Hasta que se heyan enviado los subreportes dependientes se comenzará
//            * a enviar las fotos*/
//            if (REPORTESENVIADOS.equals(NUMREPORTES)
//                    && SubReportAEnviar.equals(SubReportesEnviados)
//                    && SubReportDependienteAEnviar.equals(SubReportDependienteEnviados)
//                    && completedTask.getTag().contains("rsb")) {
//
//                Log.e("send", "enviado sub " + " enviados " + SubReportDependienteAEnviar + " numero reportes " + SubReportDependienteEnviados);
//                sendFoto();
//
//            }
//
//
//            if (!UNAUTHORIZED && !CONFLICT && REPORT_TO_SEND
//                    && REPORTESENVIADOS.equals(NUMREPORTES)
//                    && SubReportAEnviar.equals(SubReportesEnviados)
//                    && SubReportDependienteAEnviar.equals(SubReportDependienteEnviados)
//                    && SubReportFOTOAEnviar.equals(SubReportesFOTOEnviados)) {
////                handlerUI.sendEmptyMessage(ENVIOFINALIZADO);
//                NUMREPORTES = 0;
//                REPORTESENVIADOS = 0;
//
//                SubReportAEnviar = 0;
//                SubReportesEnviados = 0;
//                SubReportDependienteAEnviar = 0;
//                SubReportDependienteEnviados = 0;
//                SubReportFOTOAEnviar = 0;
//                SubReportesFOTOEnviados = 0;
////                updateReportStatus();
//            } else if (UNAUTHORIZED) {
////                handlerUI.sendEmptyMessage(HttpStatus.SC_UNAUTHORIZED);
//                NUMREPORTES = 0;
//                REPORTESENVIADOS = 0;
//                SubReportAEnviar = 0;
//                SubReportesEnviados = 0;
//                SubReportDependienteAEnviar = 0;
//                SubReportDependienteEnviados = 0;
//                SubReportFOTOAEnviar = 0;
//                SubReportesFOTOEnviados = 0;
//            } else if (CONFLICT && SubReportAEnviar.equals(SubReportesEnviados)) {
////                handlerUI.sendEmptyMessage(HttpStatus.SC_CONFLICT);
//                NUMREPORTES = 0;
//                REPORTESENVIADOS = 0;
//                SubReportAEnviar = 0;
//                SubReportesEnviados = 0;
//                SubReportDependienteAEnviar = 0;
//                SubReportDependienteEnviados = 0;
//                SubReportFOTOAEnviar = 0;
//                SubReportesFOTOEnviados = 0;
////                updateReportStatus();
//            }
//        }
//    }
//
//    /* Este método verifica que todos los registros correspondientes a las cabeceras se hayan enviado para poder cambiar
//        * el status del reporte de enviado(send=1) a enviado completamente(send=2) */
////    private void updateReportStatus() {
////        List<Integer> lstReportsSend = daoReportReport.selectAlreadySend();
////        for (int i = 0; i < lstReportsSend.size(); i++) {
////			/* Si todos los subreportes relacionados a ese reporte ya estan enviado entonces cambia el status=2 */
////            int idReportLocal = lstReportsSend.get(i);
////            if (!daoReportReport.hasSubreportToSend(idReportLocal) && !daoReportReport.hasSubreportDependentToSend(idReportLocal)
////                    && !daoReportReport.hasPhotosToSend(idReportLocal)) {
////                daoReportReport.updateStatusCompleteAll(idReportLocal);
////            }
////        }
////    }
//
//}
