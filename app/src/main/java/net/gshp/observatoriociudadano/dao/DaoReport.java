package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoCatalog;
import net.gshp.observatoriociudadano.dto.DtoReport;
import net.gshp.observatoriociudadano.dto.DtoReportToSend;
import net.gshp.observatoriociudadano.dto.DtoReportVisit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 22/01/18.
 */

public class DaoReport extends DAO {

    private SQLiteDatabase db;
    private String query = "";

    private Cursor cursor;
    public static String TABLE_NAME = "report";

    public static String PK_FIELD = "id";
    private final String ID = "id";
    private final String ID_PDV = "id_pdv";
    private final String ID_SCHEDULE = "id_agenda";
    private final String VERSION = "version";
    private final String DATE = "date";
    private final String TZ = "tz";
    private final String IMEI = "imei";
    private final String HASH = "hash";
    private final String SEND = "send";
    private final String TYPE_REPORT = "type_report";
    private final String ID_REPORT_SERVER = "id_report_server";
    private final String DATE_INACTIVE = "date_inactive";
    private final String ACTIVE = "active";

    public DaoReport() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * Insert
     */
    public long insert(DtoReport dto) {
        db = helper.getWritableDatabase();
        long resp = -1;
        try {
            String qry = "INSERT INTO " + TABLE_NAME + " (" + ID_PDV + "," + ID_SCHEDULE +
                    "," + VERSION + "," + DATE + "," + TZ + "," + IMEI + "," + HASH + "," + SEND + "," + TYPE_REPORT +
                    "," + ID_REPORT_SERVER + "," + DATE_INACTIVE + "," + ACTIVE + ")"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();

            try {
                insStatement.bindLong(1, dto.getIdPdv());
            } catch (Exception e) {
                insStatement.bindNull(1);
            }
            try {
                insStatement.bindLong(2, dto.getIdSchedule());
            } catch (Exception e) {
                insStatement.bindNull(2);
            }
            try {
                insStatement.bindString(3, dto.getVersion());
            } catch (Exception e) {
                insStatement.bindNull(3);
            }
            try {
                insStatement.bindLong(4, dto.getDate());
            } catch (Exception e) {
                insStatement.bindNull(4);
            }
            try {
                insStatement.bindString(5, dto.getTz());
            } catch (Exception e) {
                insStatement.bindNull(5);
            }
            try {
                insStatement.bindString(6, dto.getImei());
            } catch (Exception e) {
                insStatement.bindNull(6);
            }
            try {
                insStatement.bindString(7, dto.getHash());
            } catch (Exception e) {
                insStatement.bindNull(7);
            }
            try {
                insStatement.bindLong(8, dto.getSend());
            } catch (Exception e) {
                insStatement.bindNull(8);
            }
            try {
                insStatement.bindLong(9, dto.getTypeReport());
            } catch (Exception e) {
                insStatement.bindNull(9);
            }
            try {
                insStatement.bindLong(10, dto.getIdReportServer());
            } catch (Exception e) {
                insStatement.bindNull(10);
            }
            try {
                insStatement.bindString(11, dto.getDateInactive());
            } catch (Exception e) {
                insStatement.bindNull(11);
            }
            try {
                insStatement.bindLong(12, dto.getActive());
            } catch (Exception e) {
                insStatement.bindNull(12);
            }
            resp = insStatement.executeInsert();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }


    public boolean isReportIncomplete() {
        boolean isReport = false;
        db = helper.getReadableDatabase();
        String query = "SELECT  * FROM(\n"
                + "SELECT DISTINCT\n"
                + "c_client.id,\n"
                + "c_client.value,\n"
                + "pdv.id as id_pdv,\n"
                + "pdv.id_client,\n"
                + "pdv.id_rtm,				 \n"
                + "pdv.name,\n"
                + "pdv.address,\n"
                + "report.id as id_report,\n"
                + "CHECK_in.date as datecheckin,\n"
                + "CHECK_out.date as datecheckout\n"
                + "FROM\n"
                + "c_client\n"
                + "INNER JOIN pdv ON pdv.id_client = c_client.id \n"
                + "INNER JOIN report on report.id_pdv=pdv.id and report.send==0 AND report.active=1\n"
                + "LEFT JOIN report_check  as CHECK_in ON CHECK_in.id_report_local = report.id   and CHECK_in.type=1\n"
                + "LEFT JOIN report_check as CHECK_out on  CHECK_out.id_report_local = report.id   and CHECK_out.type=2\n"
                + ") as q1\n"
                + "where    q1.datecheckin   is not NULL  and  q1.datecheckout   is  NULL  ";
        cursor = db.rawQuery(query, null);
        isReport = cursor.getCount() != 0;
        cursor.close();
        db.close();
        return isReport;
    }

    /**
     * Select
     */
    public List<DtoReportToSend> SelectToSend() {
        db = helper.getReadableDatabase();
        String qry = "SELECT  * FROM(\n" +
                "SELECT\n" +
                "report.id,\n" +
                "report.id_schedule,\n" +
                "report.version ,\n" +
                "report.type_report AS tipo_report ,\n" +
                "report.id_pdv AS place ,\n" +
                "report.hash ,\n" +
                "report.date as report_date ,\n" +
                "report.tz as report_tz,\n" +
                "report.IMEI ,\n" +
                "report.send as report_enviado,\n" +
                "CHECK_in.id as CHECKIn_id,\n" +
                "CHECK_in.id_report_local as CHECKIn_id_report,\n" +
                "CHECK_in.date as CHECKIn_date,\n" +
                "CHECK_in.tz as CHECKIn_tz,\n" +
                "CHECK_in.latitude as CHECKIn_lat,\n" +
                "CHECK_in.longitude as CheckIn_lon,\n" +
                "CHECK_in.imei as CheckIn_imei,\n" +
                "CHECK_in.satellite_utc as CheckIn_satelliteUTC,\n" +
                "CHECK_in.accuracy as CheckIn_accuracy,\n" +
                "CHECK_in.type as CHECKIn_type,\n" +
                "CHECK_out.id as CHECKOut_id,\n" +
                "CHECK_out.id_report_local as CHECKOut_id_report,\n" +
                "CHECK_out.date as CHECKOut_date,\n" +
                "CHECK_out.tz as CHECKOut_tz,\n" +
                "CHECK_out.latitude as CHECKOut_lat,\n" +
                "CHECK_out.longitude as CheckOut_lon,\n" +
                "CHECK_out.imei as CheckOut_imei,\n" +
                "CHECK_out.satellite_utc as CheckOut_satelliteUTC,\n" +
                "CHECK_out.accuracy as CheckOut_accuracy,\n" +
                "CHECK_out.type as CHECKOut_type\n" +
                "FROM \n" +
                "report\n" +
                "LEFT JOIN report_check AS CHECK_in ON CHECK_in.id_report_local = report.id AND CHECK_in.type=1 \n" +
                "LEFT JOIN report_check AS CHECK_out ON CHECK_out.id_report_local = report.id AND CHECK_out.type=2\n" +
                "WHERE report.send=0 AND report.active=1 \n" +
                ") AS q1\n" +
                "WHERE q1.CHECKIn_date IS NOT NULL AND q1.CHECKOut_date IS NOT NULL";
        cursor = db.rawQuery(qry, null);
        Log.e("qry", "qry  " + qry);
        List<DtoReportToSend> obj = new ArrayList<DtoReportToSend>();
        DtoReportToSend catalogo;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int id_schedule = cursor.getColumnIndexOrThrow("id_schedule");
            int version = cursor.getColumnIndexOrThrow("version");
            int tipo_report = cursor.getColumnIndexOrThrow("tipo_report");
            int place = cursor.getColumnIndexOrThrow("place");
            int hash = cursor.getColumnIndexOrThrow("hash");
            int CHECKIn_lat = cursor.getColumnIndexOrThrow("CHECKIn_lat");
            int CheckIn_lon = cursor.getColumnIndexOrThrow("CheckIn_lon");
            int CHECKIn_date = cursor.getColumnIndexOrThrow("CHECKIn_date");
            int CHECKIn_tz = cursor.getColumnIndexOrThrow("CHECKIn_tz");
            int CHECKIn_imei = cursor.getColumnIndexOrThrow("CheckIn_imei");
            int CHECKIn_satelliteUTC = cursor.getColumnIndexOrThrow("CheckIn_satelliteUTC");
            int CHECKIn_accuracy = cursor.getColumnIndexOrThrow("CheckIn_accuracy");
            int CHECKOut_lat = cursor.getColumnIndexOrThrow("CHECKOut_lat");
            int CheckOut_lon = cursor.getColumnIndexOrThrow("CheckOut_lon");
            int CHECKOut_date = cursor.getColumnIndexOrThrow("CHECKOut_date");
            int CHECKOut_tz = cursor.getColumnIndexOrThrow("CHECKOut_tz");
            int CHECKOut_imei = cursor.getColumnIndexOrThrow("CheckOut_imei");
            int CHECKOut_satelliteUTC = cursor.getColumnIndexOrThrow("CheckOut_satelliteUTC");
            int CHECKOut_accuracy = cursor.getColumnIndexOrThrow("CheckOut_accuracy");
            do {
                catalogo = new DtoReportToSend();
                catalogo.setPlace(cursor.getInt(place));
                catalogo.setId(cursor.getInt(id));
                catalogo.setIdSchedule(cursor.getInt(id_schedule));
                catalogo.setVersion(cursor.getString(version));
                catalogo.setHash(cursor.getString(hash));
                catalogo.setIdTipo(cursor.getInt(tipo_report));
                catalogo.setCheckIn(cursor.getLong(CHECKIn_date));
                catalogo.setCheckInTz(cursor.getString(CHECKIn_tz));
                catalogo.setCheckInLat(cursor.getDouble(CHECKIn_lat));
                catalogo.setCheckInLon(cursor.getDouble(CheckIn_lon));
                catalogo.setCheckInImei(cursor.getString(CHECKIn_imei));
                catalogo.setCheckInAccuracy(cursor.getString(CHECKIn_accuracy));
                catalogo.setCheckInSateliteUTC(cursor.getLong(CHECKIn_satelliteUTC));
                catalogo.setCheckOut(cursor.getLong(CHECKOut_date));
                catalogo.setCheckOutTz(cursor.getString(CHECKOut_tz));
                catalogo.setCheckOutLat(cursor.getDouble(CHECKOut_lat));
                catalogo.setCheckOutLon(cursor.getDouble(CheckOut_lon));
                catalogo.setCheckOutImei(cursor.getString(CHECKOut_imei));
                catalogo.setCheckOutAccuracy(cursor.getString(CHECKOut_accuracy));
                catalogo.setCheckOutSateliteUTC(cursor.getLong(CHECKOut_satelliteUTC));

                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }


    /**
     * SELECT
     */
    public boolean isReportDone(long idPdv) {
        boolean reportExists = false;
        db = helper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT * FROM report\n" + "WHERE report.id_pdv = " + idPdv + " AND report.active = 1\n"
                        + "AND CAST(strftime('%Y-%m-%d', report.date / 1000 , 'unixepoch', 'localtime') AS 'TEXT') "
                        + "= CAST(strftime('%Y-%m-%d', date('now', 'localtime'), 'utc') AS 'TEXT')",
                null);
        if (cursor.moveToFirst()) {
            reportExists = cursor.getInt(0) != 0;
        }
        return reportExists;
    }

    /**
     * UPDATE
     */
    public void Update(String id_reporte) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SEND, true);
        db.update(TABLE_NAME, cv, "id=" + id_reporte, null);
        db.close();
    }

    /**
     * UPDATE
     */
    public void UpdateType(DtoBundle dtoBundle, DtoCatalog dtoCatalog) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TYPE_REPORT, dtoCatalog.getId());
        db.update(TABLE_NAME, cv, "id=" + dtoBundle.getIdReportLocal(), null);
        db.close();
    }

    /**
     * UPDATE
     */
    public void Update(String id_reporte, String id_reporte_server) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_REPORT_SERVER, id_reporte_server);
        db.update(TABLE_NAME, cv, "id=" + id_reporte, null);
        db.close();
    }

    /**
     * UPDATE
     */
    public void inactivate(long id_reporte, String hash) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DATE_INACTIVE, System.currentTimeMillis());
        cv.put(ACTIVE, 2);

        db.update(TABLE_NAME, cv, "id=" + id_reporte, null);
        db.update("report_check", cv, "id_report_local=" + id_reporte, null);

        db.close();
    }

    public void updateTypeReport(DtoBundle dtoBundle, DtoCatalog dtoCatalog) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TYPE_REPORT, dtoCatalog.getId());
        db.update(TABLE_NAME, cv, "id=" + dtoBundle.getIdReportLocal(), null);
        db.close();
    }


    public boolean existsPdv(long idPDV) {
        db = helper.getReadableDatabase();
        String qry = "SELECT\n" +
                "report.id\n" +
                "from report\n" +
                "WHERE\n" +
                "strftime('%Y-%m-%d',datetime(report.date/1000, 'unixepoch'),'localtime')=date('now','localtime') AND report.active=1 AND\n" +
                "report.id_pdv =" + idPDV;
        cursor = db.rawQuery(qry, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0 ? true : false;
    }

    public List<DtoReportVisit> SelectReportVisit() {
        db = helper.getReadableDatabase();

        String queryIncomplete = "SELECT  * FROM( \n" +
                "                SELECT DISTINCT \n" +
                "                report.id, \n" +
                "                report.send, \n" +
                "                pdv.name, \n" +
                "                report.id_pdv,                \n" +
                "                CHECK_in.date  datecheckin, \n" +
                "                CHECK_out.date datecheckout \n" +
                "                FROM \n" +
                "                report  \n" +
                "                LEFT JOIN pdv ON pdv.id=report.id_pdv \n" +
                "                LEFT JOIN report_check  as CHECK_in ON CHECK_in.id_report_local = report.id   and CHECK_in.type=1 \n" +
                "                LEFT JOIN report_check as CHECK_out on  CHECK_out.id_report_local = report.id   and CHECK_out.type=2 \n" +
                "                ) as q1 \n" +
                "                where    q1.datecheckin   is not NULL  and  q1.datecheckout   is NULL";

        String queryCompleteNotSend = "SELECT  * FROM( \n" +
                "                SELECT DISTINCT \n" +
                "                report.id, \n" +
                "                report.send, \n" +
                "                pdv.name, \n" +
                "                report.id_pdv,                \n" +
                "                CHECK_in.date  datecheckin, \n" +
                "                CHECK_out.date datecheckout \n" +
                "                FROM \n" +
                "                report  \n" +
                "                LEFT JOIN pdv ON pdv.id=report.id_pdv \n" +
                "                LEFT JOIN report_check  as CHECK_in ON CHECK_in.id_report_local = report.id   and CHECK_in.type=1 \n" +
                "                LEFT JOIN report_check as CHECK_out on  CHECK_out.id_report_local = report.id   and CHECK_out.type=2 \n" +
                "                ) as q1 \n" +
                "                where    q1.datecheckin   is not NULL  and  q1.datecheckout   is not NULL  and q1.send=0";


        String queryCompleteSend = "SELECT  * FROM( \n" +
                "                SELECT DISTINCT \n" +
                "                report.id, \n" +
                "                report.send, \n" +
                "                pdv.name, \n" +
                "                report.id_pdv,                \n" +
                "                CHECK_in.date  datecheckin, \n" +
                "                CHECK_out.date datecheckout \n" +
                "                FROM \n" +
                "                report  \n" +
                "                LEFT JOIN pdv ON pdv.id=report.id_pdv \n" +
                "                LEFT JOIN report_check  as CHECK_in ON CHECK_in.id_report_local = report.id   and CHECK_in.type=1 \n" +
                "                LEFT JOIN report_check as CHECK_out on  CHECK_out.id_report_local = report.id   and CHECK_out.type=2 \n" +
                "                ) as q1 \n" +
                "                where    q1.datecheckin   is not NULL  and  q1.datecheckout   is not NULL  and q1.send>0";


        cursor = db.rawQuery(queryIncomplete, null);
        List<DtoReportVisit> obj = new ArrayList<>();
        DtoReportVisit dtoReportVisit;
        int id = cursor.getColumnIndexOrThrow("id");
        int send = cursor.getColumnIndexOrThrow("send");
        int nombrePdv = cursor.getColumnIndexOrThrow("name");
        int idPdv = cursor.getColumnIndexOrThrow("id_pdv");
        int dateCheckIn = cursor.getColumnIndexOrThrow("datecheckin");
        int dateCheckOut = cursor.getColumnIndexOrThrow("datecheckout");
        if (cursor.moveToFirst()) {
            do {
                dtoReportVisit = new DtoReportVisit();
                dtoReportVisit.setId(cursor.getLong(id));
                dtoReportVisit.setSend(cursor.getInt(send));
                dtoReportVisit.setIdPdv(cursor.getLong(idPdv));
                dtoReportVisit.setName(cursor.getString(nombrePdv));
                dtoReportVisit.setDateCheckIn(cursor.getLong(dateCheckIn));
                dtoReportVisit.setDateCheckOut(cursor.getLong(dateCheckOut));

                obj.add(dtoReportVisit);
            } while (cursor.moveToNext());
        }
        cursor = db.rawQuery(queryCompleteNotSend, null);
        if (cursor.moveToFirst()) {
            do {
                dtoReportVisit = new DtoReportVisit();
                dtoReportVisit.setId(cursor.getInt(id));
                dtoReportVisit.setSend(cursor.getInt(send));
                dtoReportVisit.setIdPdv(cursor.getLong(idPdv));
                dtoReportVisit.setName(cursor.getString(nombrePdv));
                dtoReportVisit.setDateCheckIn(cursor.getLong(dateCheckIn));
                dtoReportVisit.setDateCheckOut(cursor.getLong(dateCheckOut));

                obj.add(dtoReportVisit);
            } while (cursor.moveToNext());
        }
        cursor = db.rawQuery(queryCompleteSend, null);
        if (cursor.moveToFirst()) {
            do {
                dtoReportVisit = new DtoReportVisit();
                dtoReportVisit.setId(cursor.getInt(id));
                dtoReportVisit.setSend(cursor.getInt(send));
                dtoReportVisit.setIdPdv(cursor.getLong(idPdv));
                dtoReportVisit.setName(cursor.getString(nombrePdv));
                dtoReportVisit.setDateCheckIn(cursor.getLong(dateCheckIn));
                dtoReportVisit.setDateCheckOut(cursor.getLong(dateCheckOut));

                obj.add(dtoReportVisit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }


    /**
     * Select
     */
    public List<Integer> selectAlreadySend() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT id FROM report WHERE send=1 AND id_report_server>0 AND active=1", null);
        List<Integer> lst = new ArrayList<Integer>();
        Integer id;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                lst.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lst;
    }

    public boolean hasSubreportToSend(int idReportLocal) {
        boolean hasSubreportToSend = false;
        db = helper.getReadableDatabase();
        String qry = "SELECT DISTINCT    \n" +
                "(SELECT count(F.ida) FROM report    \n" +
                "LEFT JOIN EARespuesta F ON F.idReporteLocal = report.id AND F.idReporteLocal = " + idReportLocal + " AND F.enviado = 0) AS countPoll    \n" +
                "FROM report";
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            int countPoll = cursor.getColumnIndexOrThrow("countPoll");
            if (cursor.getInt(countPoll) > 0) hasSubreportToSend = true;
        }
        cursor.close();
        db.close();
        return hasSubreportToSend;
    }

    public boolean hasPhotosToSend(int idReportLocal) {
        boolean hasPhotosToSend = false;
        db = helper.getReadableDatabase();
        String qry = "SELECT DISTINCT\n" +
                "(SELECT count(A.ida) FROM report\n" +
                "LEFT JOIN EARespuesta A ON A.idReporteLocal = report.id AND A.idReporteLocal=" + idReportLocal + "\n" +
                "INNER JOIN EAPregunta ON EAPregunta.id = A.idPregunta\n" +
                "WHERE A.enviado<>2 AND A.respuesta LIKE '%.jpg') AS countPhotoPoll\n" +
                "FROM report";
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            int countPhotoPoll = cursor.getColumnIndexOrThrow("countPhotoPoll");

            if (cursor.getInt(countPhotoPoll) > 0) hasPhotosToSend = true;
        }
        cursor.close();
        db.close();
        return hasPhotosToSend;
    }

    /**
     * UPDATE
     */
    public void updateStatusCompleteAll(int id_reporte) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SEND, 2);
        db.update(TABLE_NAME, cv, "id=" + id_reporte, null);
        db.close();
    }


}
