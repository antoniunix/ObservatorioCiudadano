package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gshp.api.utils.Crypto;

import net.gshp.apiencuesta.model.DAO.DAOEncuestas;
import net.gshp.observatoriociudadano.dto.DtoEARespuesta;
import net.gshp.observatoriociudadano.dto.DtoSendPhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DaoEARespuesta extends DAO {

    private SQLiteDatabase db;

    private DAOEncuestas daoEncuestas;

    public DaoEARespuesta() {
        super("EARespuesta", "ida");
        daoEncuestas = new DAOEncuestas();
    }

    public int delete() {
        db = helper.getWritableDatabase();
        int resp = db.delete(TABLE_NAME, PK_FIELD, null);
        db.close();
        return resp;
    }


    public List<DtoEARespuesta> selectRespuestasbyReportAndEncuesta(long idEncuesta,
                                                                    long idReporteLocal) {
        String query = "SELECT DISTINCT\n" +
                "EARespuesta.respuesta,\n" +
                "EARespuesta.hash,\n" +
                "EARespuesta.idEncuesta,\n" +
                "EARespuesta.idPregunta,\n" +
                "EARespuesta.idReporteLocal,\n" +
                "EARespuesta.ida,\n" +
                "EARespuesta.numeroEncuesta,\n" +
                "EARespuesta.enviado,\n" +
                "EARespuesta.nombreEncuesta,\n" +
                "EARespuesta.campoExtra1,\n" +
                "EARespuesta.campoExtra2,\n" +
                "EARespuesta.timeStamp\n" +
                "FROM  EARespuesta\n" +
                "where EARespuesta.idEncuesta=" + idEncuesta + "   and EARespuesta.idReporteLocal=" + idReporteLocal + "\n" +
                "GROUP BY EARespuesta.numeroEncuesta\n" +
                "ORDER BY EARespuesta.numeroEncuesta";
        db = helper.getReadableDatabase();
        return toDto(db.rawQuery(query, null));
    }

    public List<DtoEARespuesta> selectRespuestasbyReportAndNumeroEncuesta(int numeroEncuesta,
                                                                          long idReporteLocal) {
        String query = "SELECT DISTINCT\n" +
                "EARespuesta.respuesta,\n" +
                "EARespuesta.hash,\n" +
                "EARespuesta.idEncuesta,\n" +
                "EARespuesta.idPregunta,\n" +
                "EARespuesta.idReporteLocal,\n" +
                "EARespuesta.ida,\n" +
                "EARespuesta.numeroEncuesta,\n" +
                "EARespuesta.enviado,\n" +
                "EARespuesta.nombreEncuesta,\n" +
                "EARespuesta.campoExtra1,\n" +
                "EARespuesta.campoExtra2,\n" +
                "EARespuesta.timeStamp\n" +
                "FROM  EARespuesta\n" +
                "where EARespuesta.numeroEncuesta=" + numeroEncuesta + "   and EARespuesta.idReporteLocal=" + idReporteLocal + "\n" +
                "GROUP BY EARespuesta.numeroEncuesta\n" +
                "ORDER BY EARespuesta.numeroEncuesta";
        db = helper.getReadableDatabase();
        return toDto(db.rawQuery(query, null));
    }

    public List<List<DtoEARespuesta>> selectToSend() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT DISTINCT\n" +
                "EARespuesta.ida, \n" +
                "EARespuesta.idPregunta, \n" +
                "EARespuesta.idReporteLocal, \n" +
                "EARespuesta.idEncuesta, \n" +
                "EARespuesta.nombreEncuesta, \n" +
                "EARespuesta.respuesta, \n" +
                "EARespuesta.hash, \n" +
                "EARespuesta.enviado, \n" +
                "EARespuesta.numeroEncuesta, \n" +
                "EARespuesta.campoExtra1, \n" +
                "EARespuesta.campoExtra2, \n" +
                "report.id_report_server, \n" +
                "EARespuesta.timeStamp \n" +
                "FROM \n" +
                "EARespuesta \n" +
                "INNER JOIN report ON report.id = EARespuesta.idReporteLocal AND report.id_report_server>0 AND EARespuesta.enviado=0 \n" +
                "ORDER BY EARespuesta.idReporteLocal ASC", null);
        List<List<DtoEARespuesta>> lst = new ArrayList<List<DtoEARespuesta>>();
        List<DtoEARespuesta> subLst = new ArrayList<DtoEARespuesta>();
        DtoEARespuesta catalogo;
        int tmpidReport;//variable para guardar el idreporte del cursor anterior
        if (cursor.moveToFirst()) {
            //guardamos el idReporte del primer row
            tmpidReport = cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal"));
            do {
                catalogo = new DtoEARespuesta();
                catalogo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ida")));
                catalogo.setIdReport(cursor.getInt(cursor.getColumnIndexOrThrow("id_report_server")));
                catalogo.setIdPregunta(cursor.getInt(cursor.getColumnIndexOrThrow("idPregunta")));
                catalogo.setIdEncuesta(cursor.getInt(cursor.getColumnIndexOrThrow("idEncuesta")));
                catalogo.setNombreEncuesta(cursor.getString(cursor.getColumnIndexOrThrow("nombreEncuesta")));
                catalogo.setNumeroEncuesta(cursor.getInt(cursor.getColumnIndexOrThrow("numeroEncuesta")));
                catalogo.setRespuesta(cursor.getString(cursor.getColumnIndexOrThrow("respuesta")));
                catalogo.setHash(cursor.getString(cursor.getColumnIndexOrThrow("hash")));
                catalogo.setCampoExtra1(cursor.getString(cursor.getColumnIndexOrThrow("campoExtra1")));
                catalogo.setCampoExtra2(cursor.getString(cursor.getColumnIndexOrThrow("campoExtra2")));
                //se invierten los id por cuestiones de la serializacion para el servidor
                catalogo.setIdReporteLocal(cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal")));
                catalogo.setEnviado(cursor.getInt(cursor.getColumnIndexOrThrow("enviado")));
                // si pertenece al mismo reporte se guardaran en la misma sublista
                if (tmpidReport == cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal")))
                    subLst.add(catalogo);
                else if (tmpidReport != cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal")))// si cambia el idReporte guardamos la sublista en la lista y creamos otra sublista
                {
                    //refrescamos el idReporte al actual id
                    tmpidReport = cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal"));
                    lst.add(subLst);
                    subLst = new ArrayList<DtoEARespuesta>();
                    subLst.add(catalogo);
                }
                if (cursor.isLast())
                    lst.add(subLst);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lst;
    }

    public DtoEARespuesta selectAnswer(int idQuestion, long idReportLocal) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "EARespuesta.ida,\n" +
                "EARespuesta.idPregunta,\n" +
                "EARespuesta.idReporte,\n" +
                "EARespuesta.idReporteLocal,\n" +
                "EARespuesta.idEncuesta,\n" +
                "EARespuesta.nombreEncuesta,\n" +
                "EARespuesta.respuesta,\n" +
                "EARespuesta.hash,\n" +
                "EARespuesta.enviado,\n" +
                "EARespuesta.numeroEncuesta,\n" +
                "EARespuesta.campoExtra1,\n" +
                "EARespuesta.campoExtra2,\n" +
                "EARespuesta.timeStamp\n" +
                "FROM\n" +
                "EARespuesta\n" +
                "WHERE \n" +
                "EARespuesta.idPregunta=" + idQuestion + "\n" +
                "AND EARespuesta.idReporteLocal=" + idReportLocal, null);
        DtoEARespuesta catalogo = new DtoEARespuesta();
        if (cursor.moveToFirst()) {
            catalogo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ida")));
            catalogo.setIdPregunta(cursor.getInt(cursor.getColumnIndexOrThrow("idPregunta")));
            catalogo.setIdReporteLocal(cursor.getInt(cursor.getColumnIndexOrThrow("idReporteLocal")));
            catalogo.setIdEncuesta(cursor.getInt(cursor.getColumnIndexOrThrow("idEncuesta")));
            catalogo.setNombreEncuesta(cursor.getString(cursor.getColumnIndexOrThrow("nombreEncuesta")));
            catalogo.setRespuesta(cursor.getString(cursor.getColumnIndexOrThrow("respuesta")));
            catalogo.setHash(cursor.getString(cursor.getColumnIndexOrThrow("hash")));
            catalogo.setNumeroEncuesta(cursor.getInt(cursor.getColumnIndexOrThrow("numeroEncuesta")));
        }
        cursor.close();
        db.close();
        return catalogo;
    }

    private List<DtoEARespuesta> toDto(Cursor cursor) {
        int id = cursor.getColumnIndexOrThrow("ida");
        int idPregunta = cursor.getColumnIndexOrThrow("idPregunta");
        int idReporteLocal = cursor.getColumnIndexOrThrow("idReporteLocal");
        int idEncuesta = cursor.getColumnIndexOrThrow("idEncuesta");
        int nombreEncuesta = cursor.getColumnIndexOrThrow("nombreEncuesta");
        int respuesta = cursor.getColumnIndexOrThrow("respuesta");
        int hash = cursor.getColumnIndexOrThrow("hash");
        int enviado = cursor.getColumnIndexOrThrow("enviado");
        int numeroEncuesta = cursor.getColumnIndexOrThrow("numeroEncuesta");
        int campoExtra1 = cursor.getColumnIndexOrThrow("campoExtra1");
        int campoExtra2 = cursor.getColumnIndexOrThrow("campoExtra2");

        List<DtoEARespuesta> result = new ArrayList<DtoEARespuesta>(cursor.getCount());
        while (cursor.moveToNext()) {
            DtoEARespuesta obj = new DtoEARespuesta();
            obj.setId(cursor.getInt(id));
            obj.setCampoExtra1(cursor.getString(campoExtra1));
            obj.setCampoExtra2(cursor.getString(campoExtra2));
            obj.setEnviado(cursor.getInt(enviado));
            obj.setHash(cursor.getString(hash));
            obj.setIdEncuesta(cursor.getInt(idEncuesta));
            obj.setIdPregunta(cursor.getInt(idPregunta));
            obj.setIdReporteLocal(cursor.getInt(idReporteLocal));
            obj.setNombreEncuesta(cursor.getString(nombreEncuesta));
            obj.setNumeroEncuesta(cursor.getInt(numeroEncuesta));
            obj.setRespuesta(cursor.getString(respuesta));

            result.add(obj);
        }
        cursor.close();

        return result;
    }


    /**
     * Select
     */
    public List<DtoEARespuesta> SelectToSendPhoto() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "				EARespuesta.ida,\n" +
                "				EARespuesta.idPregunta,\n" +
                "				EARespuesta.idReporteLocal,\n" +
                "				EARespuesta.idEncuesta,\n" +
                "				EARespuesta.respuesta,\n" +
                "				EARespuesta.hash,\n" +
                "				EARespuesta.enviado,\n" +
                "				report.id_report_server,\n" +
                "				report.id_pdv as place,\n" +
                "				EAPregunta.pregunta\n" +
                "				FROM\n" +
                "				EARespuesta\n" +
                "				INNER JOIN report ON report.id = EARespuesta.idReporteLocal AND report.id_report_server > 0 \n" +
                "				INNER JOIN EAPregunta ON EAPregunta.id = EARespuesta.idPregunta\n" +
                "				WHERE  EARespuesta.enviado =1  and  EARespuesta.respuesta LIKE '%.jpg'", null);
        List<DtoEARespuesta> obj = new ArrayList<DtoEARespuesta>();
        DtoEARespuesta catalogo;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("ida");
            int idPregunta = cursor.getColumnIndexOrThrow("idPregunta");
            int idReporteLocal = cursor.getColumnIndexOrThrow("idReporteLocal");
            int idEncuesta = cursor.getColumnIndexOrThrow("idEncuesta");
            int respuesta = cursor.getColumnIndexOrThrow("respuesta");
            int hash = cursor.getColumnIndexOrThrow("hash");
            int enviado = cursor.getColumnIndexOrThrow("enviado");
            int id_report_server = cursor.getColumnIndexOrThrow("id_report_server");
            int pregunta = cursor.getColumnIndexOrThrow("pregunta");
            int place = cursor.getColumnIndexOrThrow("place");
            do {
                catalogo = new DtoEARespuesta();
                catalogo.setId(cursor.getInt(id));
                catalogo.setIdReporteLocal(cursor.getInt(idReporteLocal));
                catalogo.setIdReport(cursor.getInt(id_report_server));
                catalogo.setIdEncuesta(cursor.getInt(idEncuesta));
                catalogo.setRespuesta(cursor.getString(respuesta));
                catalogo.setHash(cursor.getString(hash));
                catalogo.setDescription(cursor.getString(pregunta));
                catalogo.setPdv(cursor.getInt(place));
                catalogo.setEnviado(cursor.getInt(enviado));

                if (new File(catalogo.getRespuesta()).exists()) {
                    obj.add(catalogo);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    public List<DtoSendPhoto> SelectToSendPhotoV1() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "				EARespuesta.ida,\n" +
                "				EARespuesta.idPregunta,\n" +
                "				EARespuesta.idReporteLocal,\n" +
                "				EARespuesta.idEncuesta,\n" +
                "				EARespuesta.respuesta,\n" +
                "				EARespuesta.hash,\n" +
                "				EARespuesta.enviado,\n" +
                "				report.id_report_server,\n" +
                "				report.id_pdv as place,\n" +
                "				EAPregunta.pregunta\n" +
                "				FROM\n" +
                "				EARespuesta\n" +
                "				INNER JOIN report ON report.id = EARespuesta.idReporteLocal AND report.id_report_server > 0 \n" +
                "				INNER JOIN EAPregunta ON EAPregunta.id = EARespuesta.idPregunta\n" +
                "				WHERE  EARespuesta.enviado =1  and  EARespuesta.respuesta LIKE '%.jpg'", null);
        List<DtoSendPhoto> obj = new ArrayList<>();
        DtoSendPhoto catalogo;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("ida");
            int idPregunta = cursor.getColumnIndexOrThrow("idPregunta");
            int idReporteLocal = cursor.getColumnIndexOrThrow("idReporteLocal");
            int idEncuesta = cursor.getColumnIndexOrThrow("idEncuesta");
            int respuesta = cursor.getColumnIndexOrThrow("respuesta");
            int hash = cursor.getColumnIndexOrThrow("hash");
            int enviado = cursor.getColumnIndexOrThrow("enviado");
            int id_report_server = cursor.getColumnIndexOrThrow("id_report_server");
            int pregunta = cursor.getColumnIndexOrThrow("pregunta");
            int place = cursor.getColumnIndexOrThrow("place");
            do {
                catalogo = new DtoSendPhoto();
                catalogo.setId(cursor.getLong(id));
                catalogo.setPlaceId(cursor.getLong(place));
                catalogo.setPersonId("@person");
                catalogo.setUserId("@user");
                catalogo.setTableName("ea_answer");
                catalogo.setDescription(cursor.getString(pregunta));
                catalogo.setVersion("1");
                catalogo.setHash(cursor.getString(hash));
                catalogo.setPath(cursor.getString(respuesta));
                catalogo.setMd5(Crypto.MD5CheckSum(cursor.getString(respuesta)));

                if (new File(catalogo.getPath()).exists()) {
                    obj.add(catalogo);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    /**
     * Select
     */
    public boolean isEncuesta(int idReportLocal) {
        boolean isDone = false;
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "count(*) as count\n" +
                "FROM\n" +
                "EARespuesta\n" +
                "WHERE \n" +
                "EARespuesta.idReporteLocal=" + idReportLocal, null);
        if (cursor.moveToFirst()) {
            isDone = cursor.getInt(cursor.getColumnIndexOrThrow("count")) > 0;

        }
        cursor.close();
        db.close();
        return isDone;
    }


    public void updateEnviado(String idReporteLocal) {
        db = helper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("enviado", 1);
            db.update(TABLE_NAME, cv, "idReporteLocal=" + idReporteLocal, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void updatephoto(String id) {
        db = helper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("enviado", 2);
            db.update(TABLE_NAME, cv, "ida=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void update(int idReporteLocal, int idReporteServer) {
        daoEncuestas.updateReportServer((long) idReporteLocal,
                (long) idReporteServer);
    }

    public String selectUserName(int idQuestion, int idEncuesta, long idReportLocal) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "EARespuesta.ida,\n" +
                "EARespuesta.idPregunta,\n" +
                "EARespuesta.idReporte,\n" +
                "EARespuesta.idReporteLocal,\n" +
                "EARespuesta.idEncuesta,\n" +
                "EARespuesta.nombreEncuesta,\n" +
                "EARespuesta.respuesta,\n" +
                "EARespuesta.hash,\n" +
                "EARespuesta.enviado,\n" +
                "EARespuesta.numeroEncuesta,\n" +
                "EARespuesta.campoExtra1,\n" +
                "EARespuesta.campoExtra2,\n" +
                "EARespuesta.timeStamp\n" +
                "FROM\n" +
                "EARespuesta\n" +
                "WHERE \n" +
                "EARespuesta.idPregunta=" + idQuestion + "\n" +
                "AND EARespuesta.idReporteLocal=" + idReportLocal + "\n" +
                "AND EARespuesta.idEncuesta=" + idEncuesta, null);
        String userName = "";
        Log.w(TAG, "result: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("respuesta"));
        }
        cursor.close();
        db.close();
        return userName;
    }




}