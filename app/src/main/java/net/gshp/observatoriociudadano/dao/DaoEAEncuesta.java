package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoCheckIsPoll;
import net.gshp.observatoriociudadano.dto.DtoEAEncuesta;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;

import java.util.ArrayList;
import java.util.List;

public class DaoEAEncuesta extends DAO {

    private SQLiteDatabase db;
    private final static int ENABLE = 1, DISABLE = 0;//la preciosa fanieta no usa numeros magicos

    public final static String id = "id";
    public final static String nombre = "nombre";
    public final static String vigenciaInicial = "vigenciaInicial";
    public final static String vigenciaFinal = "vigenciaFinal";
    public final static String repeticiones = "repeticiones";
    public final static String canal = "canal";
    public final static String rtm = "rtm";
    public final static String cliente = "cliente";
    public final static String pdv = "pdv";
    public final static String query = "query";
    public final static String descripcion = "descripcion";
    public final static String rtEnabled = "rtEnabled";
    public final static String estado = "estado";
    public final static String region = "region";
    public final static String restriction = "restriction";

    public static String TABLE_NAME = "EAEncuesta";
    public static String PK_FIELD = "id";

    public DaoEAEncuesta() {
        super(TABLE_NAME, PK_FIELD);
    }

    public DtoEAEncuesta selectByIdEncuesta(int id) {
        List<DtoEAEncuesta> result = toDto(super.selectBy("id", id));
        if (result.isEmpty())
            return null;
        else
            return result.get(0);
    }

    /**
     * API
     *
     * @param idReporte
     * @param idCanal
     * @param idCliente
     * @param idPdv
     * @param idRtm
     * @return
     */
    public List<DtoEAEncuesta> selectTipoEncuestas(long idReporte, long idCanal,
                                                   long idCliente, long idPdv, long idRtm, long region) {

        String query = "SELECT DISTINCT\n" +
                "*\n" + "FROM\n" + "EAEncuesta \n"
                + "LEFT JOIN EAAnswerPdv ON EAAnswerPdv.id_poll=EAEncuesta.id AND EAAnswerPdv.id_pdv=" + idPdv + " \n"
                + " WHERE (EAEncuesta.restriction=1 OR EAAnswerPdv.id_poll ISNULL  ) and \n" +
                +System.currentTimeMillis() + "\n"
                + "BETWEEN vigenciaInicial and vigenciaFinal and\n" + "(\n"
                + "EAEncuesta.canal like \"%@" + idCanal + "@%\" or \n"
                + "EAEncuesta.cliente like \"%@" + idCliente + "@%\" or \n"
                + "EAEncuesta.pdv like \"%@" + idPdv + "@%\" or \n"
                + "EAEncuesta.region like \"%@" + region + "@%\" or \n"
                + "EAEncuesta.rtm like \"%@" + idRtm + "@%\" or \n" + "(\n"
                + "(EAEncuesta.canal is null or canal='') and\n"
                + "(EAEncuesta.region is null or region='') and\n"
                + "(EAEncuesta.cliente is null or cliente='') and\n"
                + "(EAEncuesta.pdv is null or pdv='') and\n" + "(EAEncuesta.rtm is null or rtm='') \n"
                + ")\n" + ")\n" +
                "AND EAEncuesta.id <> 1000 AND EAEncuesta.id <> 1001 AND EAEncuesta.id<>1002";


        db = helper.getReadableDatabase();
        return toDto(db.rawQuery(query, null));
    }

    private List<DtoEAEncuesta> toDto(Cursor cursor) {
        int id = cursor.getColumnIndexOrThrow("id");
        int nombre = cursor.getColumnIndexOrThrow("nombre");
        int vigenciaInicial = cursor.getColumnIndexOrThrow("vigenciaInicial");
        int vigenciaFinal = cursor.getColumnIndexOrThrow("vigenciaFinal");
        int repeticiones = cursor.getColumnIndexOrThrow("repeticiones");
        int canal = cursor.getColumnIndexOrThrow("canal");
        int rtm = cursor.getColumnIndexOrThrow("rtm");
        int cliente = cursor.getColumnIndexOrThrow("cliente");
        int pdv = cursor.getColumnIndexOrThrow("pdv");
        int query = cursor.getColumnIndexOrThrow("query");
        int descripcion = cursor.getColumnIndexOrThrow("descripcion");
        int region = cursor.getColumnIndexOrThrow("region");

        List<DtoEAEncuesta> result = new ArrayList<DtoEAEncuesta>(cursor.getCount());
        while (cursor.moveToNext()) {
            DtoEAEncuesta obj = new DtoEAEncuesta();
            obj.setId(cursor.getInt(id));
            obj.setName(cursor.getString(nombre));
            obj.setCanal(cursor.getString(canal));
            obj.setCliente(cursor.getString(cliente));
            obj.setDate_start(cursor.getLong(vigenciaFinal));
            obj.setDate_end(cursor.getLong(vigenciaInicial));
            obj.setDescription(cursor.getString(descripcion));
            obj.setPdv(cursor.getString(pdv));
            obj.setQuery(cursor.getString(query));
            obj.setRepeat(cursor.getInt(repeticiones));
            obj.setRtm(cursor.getString(rtm));
//            obj.setRegion(cursor.getString(region));
            result.add(obj);
        }
        cursor.close();

        return result;
    }


    /**
     * Insert
     */
    public int Insert(List<DtoEAEncuesta> obj) {
        db = helper.getWritableDatabase();
        ContentValues cv;
        int resp = 0;
        try {
            db.beginTransaction();
            for (int i = 0; i < obj.size(); i++) {
                cv = new ContentValues();
                cv.put(id, obj.get(i).id);
                cv.put(nombre, obj.get(i).name);
                cv.put(vigenciaInicial, obj.get(i).date_start);
                cv.put(vigenciaFinal, obj.get(i).date_end);
                cv.put(repeticiones, obj.get(i).repeat);
                cv.put(canal, obj.get(i).canal);
                cv.put(rtm, obj.get(i).rtm);
                cv.put(cliente, obj.get(i).cliente);
                cv.put(pdv, obj.get(i).pdv);
                cv.put(query, obj.get(i).query);
                cv.put(descripcion, obj.get(i).description);
                cv.put(rtEnabled, obj.get(i).rtEnabled ? ENABLE : DISABLE);
                cv.put(estado, obj.get(i).estado);
                cv.put(region, obj.get(i).region);
                cv.put(restriction, obj.get(i).getRestriction());
                resp = (int) db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error db");
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }


    public List<DtoCheckIsPoll> selectIsPollComplete(DtoBundle dtoBundle, DtoPdvPdv dto, int status) {
        db = helper.getReadableDatabase();
        String qry = "SELECT\n" +
                "EAEncuesta.id,\n" +
                "EAEncuesta.nombre,\n" +
                "EAEncuesta.estado,\n" +
                "count(EAPregunta.id) as preguntas,\n" +
                "count(EARespuesta.ida) as respuestas\n" +
                "FROM\n" +
                "EAEncuesta\n" +
                "LEFT JOIN EAAnswerPdv ON EAAnswerPdv.id_poll=EAEncuesta.id AND EAAnswerPdv.id_pdv=" + dto.getId() + "\n" +
                "LEFT JOIN EARespuesta ON EARespuesta.idEncuesta=EAEncuesta.id AND  EARespuesta.idReporteLocal=" + dtoBundle.getIdReportLocal() + "\n" +
                "INNER JOIN EAPregunta ON EAPregunta.idEncuesta=EAEncuesta.id\n" +
                "WHERE " + System.currentTimeMillis() + " BETWEEN vigenciaInicial and vigenciaFinal and    \n" +
                "(EAEncuesta.canal like '%@" + dto.getIdCanal() + "@%' or EAEncuesta.canal ISNULL or EAEncuesta.canal= '' ) and    \n" +
                "(EAEncuesta.cliente like '%@" + dto.getIdClient() + "@%' or EAEncuesta.cliente ISNULL or EAEncuesta.cliente='' ) and    \n" +
                "(EAEncuesta.pdv like '%@" + dto.getId() + "@%' or  EAEncuesta.pdv ISNULL or EAEncuesta.pdv='' ) and    \n" +
                "(EAEncuesta.region like '%@" + dto.getIdRegion() + "@%' or  EAEncuesta.region ISNULL or EAEncuesta.region='' ) and    \n" +
                "( EAEncuesta.rtm like '%@" + dto.getIdRtm() + "@%' or  EAEncuesta.rtm ISNULL or EAEncuesta.rtm='') \n" +
                "AND EAEncuesta.estado=" + status + "\n" +
                "AND (EAEncuesta.restriction=1 OR EAAnswerPdv.id_poll ISNULL ) \n" +
                "GROUP BY EAEncuesta.id";
        Log.e("qry", "qry poll " + qry);
        cursor = db.rawQuery(qry, null);
        List<DtoCheckIsPoll> obj = new ArrayList<DtoCheckIsPoll>();
        DtoCheckIsPoll catalogo;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int nombre = cursor.getColumnIndexOrThrow("nombre");
            int estado = cursor.getColumnIndexOrThrow("estado");
            int preguntas = cursor.getColumnIndexOrThrow("preguntas");
            int respuestas = cursor.getColumnIndexOrThrow("respuestas");
            do {
                catalogo = new DtoCheckIsPoll();
                catalogo.setId(cursor.getInt(id));
                catalogo.setNombre(cursor.getString(nombre));
                catalogo.setEstado(cursor.getInt(estado));
                catalogo.setPreguntas(cursor.getInt(preguntas));
                catalogo.setRespuestas(cursor.getInt(respuestas));
                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    /**
     * Delete
     */
    public int deleteById(int _id) {
        db = helper.getWritableDatabase();
        int resp = db.delete(TABLE_NAME, PK_FIELD + "=?",
                new String[]{String.valueOf(_id)});
        db.close();
        return resp;
    }

    /**
     * Delete
     */
    public int delete() {
        db = helper.getWritableDatabase();
        int resp = db.delete(TABLE_NAME, PK_FIELD, null);
        db.close();
        return resp;
    }


    public boolean isResponsePollById(long idPoll) {
        db = helper.getReadableDatabase();
        String qry = "SELECT \n" +
                "COUNT(*) count\n" +
                "FROM\n" +
                "EARespuesta\n" +
                "WHERE EARespuesta.idEncuesta=" + idPoll;
        Log.e("qry", "qry poll " + qry);
        cursor = db.rawQuery(qry, null);
        boolean isResponsPoll = false;
        if (cursor.moveToFirst()) {
            isResponsPoll = cursor.getInt(cursor.getColumnIndexOrThrow("count")) > 0;
        }
        cursor.close();
        db.close();
        return isResponsPoll;
    }

    /**
     * Select
     */
    public boolean isResponsePollById(long idReportLocal,long idPoll)
    {
        boolean isDone=false;
        db=helper.getReadableDatabase();
        cursor=db.rawQuery("SELECT\n" +
                "count(*) as count\n" +
                "FROM\n" +
                "EARespuesta\n" +
                "WHERE \n" +
                "EARespuesta.idReporteLocal="+idReportLocal+" AND EARespuesta.idEncuesta="+idPoll,null);
        if(cursor.moveToFirst())
        {
            isDone=cursor.getInt(cursor.getColumnIndexOrThrow("count"))>0;

        }
        cursor.close();
        db.close();
        return isDone;
    }

    public boolean existPoll(long idPoll) {
        db = helper.getReadableDatabase();
        String qry = "SELECT\n" +
                "COUNT(*) count\n" +
                "FROM\n" +
                "EAEncuesta\n" +
                "WHERE EAEncuesta.id=" + idPoll;
        Log.e("qry", "qry poll " + qry);
        cursor = db.rawQuery(qry, null);
        boolean isResponsPoll = false;
        if (cursor.moveToFirst()) {
            isResponsPoll = cursor.getInt(cursor.getColumnIndexOrThrow("count")) > 0;
        }
        cursor.close();
        db.close();
        return isResponsPoll;
    }
}