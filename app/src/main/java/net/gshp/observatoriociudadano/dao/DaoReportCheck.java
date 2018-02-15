package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.gshp.observatoriociudadano.dto.DtoCheckSend;
import net.gshp.observatoriociudadano.dto.DtoReportCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 22/01/18.
 */

public class DaoReportCheck extends DAO {
    private SQLiteDatabase db;
    private Cursor cursor;

    public static String TABLE_NAME = "report_check";
    public static String PK_FIELD = "id";


    private final String ID = "id";
    private final String ID_REPORT_LOCAL = "id_report_local";
    private final String DATE = "date";
    private final String TZ = "tz";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String ACCURACY = "accuracy";
    private final String IMEI = "imei";
    private final String SATELLITE_UTC = "satellite_utc";
    private final String TYPE_CHECK = "type";
    private final String SEND = "send";
    private final String PROVIDER = "provider";
    private final String DATE_INACTIVE = "date_inactive";
    private final String ACTIVE = "active";
    private final String HASH = "hash";


    public DaoReportCheck() {
        super(TABLE_NAME, PK_FIELD);
    }


    public int insert(DtoReportCheck obj) {
        db = helper.getWritableDatabase();
        ContentValues cv;
        int resp = 0;
        try {
            db.beginTransaction();
            cv = new ContentValues();

            cv.put(LATITUDE, obj.getLatitude());
            cv.put(LONGITUDE, obj.getLongitude());
            cv.put(SATELLITE_UTC, obj.getSatelliteUtc());
            cv.put(ACCURACY, obj.getAccuracy());
            cv.put(PROVIDER, obj.getProvider());

            resp = db.update(TABLE_NAME, cv,
                    ID_REPORT_LOCAL + "=" + obj.getIdReportLocal() + " AND " + TYPE_CHECK + "="
                            + obj.getTypeCheck(), null);
            if (resp == 0) {
                cv.put(ID_REPORT_LOCAL, obj.getIdReportLocal());
                cv.put(DATE, obj.getDate());
                cv.put(TZ, obj.getTz());
                cv.put(IMEI, obj.getImei());
                cv.put(TYPE_CHECK, obj.getTypeCheck());
                cv.put(SEND, obj.getSend());
                cv.put(DATE_INACTIVE, obj.getDateInactive());
                cv.put(ACTIVE, obj.getActive());
                cv.put(HASH, obj.getHash());
                resp = (int) db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }

    public boolean isCheckByIdAndType(long idReportLocal, int idTypeCheck) {
        db = helper.getReadableDatabase();
        boolean existCheck = false;
        String qry = "SELECT\n" +
                "count(report_check.id) count\n" +
                "FROM\n" +
                "report_check\n" +
                "WHERE  report_check.type=" + idTypeCheck + " and report_check .id_report_local=" + idReportLocal;
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            existCheck = cursor.getInt(cursor.getColumnIndexOrThrow("count")) > 0;
        }
        cursor.close();
        db.close();
        return existCheck;
    }

    /**
     * Select
     */
    public List<DtoCheckSend> SelectToSend() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "report.id_pdv,\n" +
                "report.id_schedule,\n" +
                "report.version,\n" +
                "report.hash,\n" +
                "report_check.id_report_local,\n" +
                "report_check.date,\n" +
                "report_check.tz,\n" +
                "report_check.latitude,\n" +
                "report_check.longitude,\n" +
                "report_check.accuracy,\n" +
                "report_check.imei,\n" +
                "report_check.satellite_utc\n" +
                "FROM\n" +
                "report\n" +
                "INNER JOIN report_check ON report_check.id_report_local = report.id\n" +
                "WHERE report_check.type=1 AND report_check.send=0 AND report.active=1", null);
        List<DtoCheckSend> obj = new ArrayList<DtoCheckSend>();
        DtoCheckSend catalogo;
        if (cursor.moveToFirst()) {
            int place = cursor.getColumnIndexOrThrow("id_pdv");
            int id_agenda = cursor.getColumnIndexOrThrow("id_schedule");
            int version = cursor.getColumnIndexOrThrow("version");
            int imei = cursor.getColumnIndexOrThrow(IMEI);
            int hash = cursor.getColumnIndexOrThrow(HASH);
            int id_report_local = cursor.getColumnIndexOrThrow(ID_REPORT_LOCAL);
            int date = cursor.getColumnIndexOrThrow(DATE);
            int tz = cursor.getColumnIndexOrThrow(TZ);
            int lat = cursor.getColumnIndexOrThrow(LATITUDE);
            int lon = cursor.getColumnIndexOrThrow(LONGITUDE);
            int accuracy = cursor.getColumnIndexOrThrow(ACCURACY);
            int satelliteUTC = cursor.getColumnIndexOrThrow(SATELLITE_UTC);
            do {
                catalogo = new DtoCheckSend();
                catalogo.setIdPdv(cursor.getInt(place));
                catalogo.setIdAgenda(cursor.getInt(id_agenda));
                catalogo.setVersion(cursor.getString(version));
                catalogo.setCheckInIMEI(cursor.getString(imei));
                catalogo.setHash(cursor.getString(hash));
                catalogo.setIdReportLocal(cursor.getInt(id_report_local));
                catalogo.setCheckInDate(cursor.getLong(date));
                catalogo.setCheckInTz(cursor.getString(tz));
                catalogo.setCheckInLat(cursor.getDouble(lat));
                catalogo.setCheckInLon(cursor.getDouble(lon));
                catalogo.setCheckInAccuracy(cursor.getString(accuracy));
                catalogo.setCheckInSatelliteUTC(cursor.getLong(satelliteUTC));
                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    public void markAsSent(String idReport) {
        db = helper.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET send=1 WHERE type=1 and id_report_local=" + idReport;
        try {
            db.beginTransaction();
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return;

    }

    /**
     * Comprueba que exista CheckIn del reporte ingresado
     *
     * @return true si existe.
     */
    public boolean isCheck(long idReporte, int type) {
        Log.e("ischeck", "idrepoort " + idReporte + " type " + type);
        boolean ischeck = false;
        db = helper.getReadableDatabase();
        StringBuilder sbQry = new StringBuilder();
        sbQry.append("SELECT\n" + "count(*)FROM\n" + "report_check\n"
                + "where id_report_local=? and type=?");
        cursor = db
                .rawQuery(
                        sbQry.toString(),
                        new String[]{String.valueOf(idReporte),
                                String.valueOf(type)});
        if (cursor.moveToFirst()) {
            ischeck = cursor.getInt(0) == 0 ? false : true;
        }
        cursor.close();
        db.close();
        return ischeck;
    }

}
