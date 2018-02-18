package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gshp.api.utils.Crypto;

import net.gshp.observatoriociudadano.dto.DtoReportCensus;

/**
 * Created by leo on 17/02/18.
 */

public class DaoReportCensus extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;
    public static String TABLE_NAME = "report_census";

    public static String PK_FIELD = "id";
    private final String ID = "id";
    private final String IDREPORTLOCAL = "id_report_local";
    private final String STATE = "state";
    private final String TOWN = "town";
    private final String SUBURB = "suburb";
    private final String ADDRESS = "address";
    private final String EXTERNAL_NUMBER = "external_number";
    private final String LAT = "lat";
    private final String LON = "lon";
    private final String HASH = "hash";
    private final String SEND = "send";
    private final String PROVIDER = "provider";
    private final String NAMESTREET = "name_street";
    private final String CP = "cp";

    public DaoReportCensus() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * INSERT
     */

    public int insert(DtoReportCensus obj) {
        db = helper.getWritableDatabase();
        ContentValues cv;
        int resp = 0;
        try {
            db.beginTransaction();

            cv = new ContentValues();
            cv.put(IDREPORTLOCAL,obj.getIdReporteLocal());
            cv.put(STATE, obj.getState());
            cv.put(TOWN, obj.getTown());
            cv.put(SUBURB, obj.getSuburb());
            cv.put(ADDRESS, obj.getAddress());
            cv.put(NAMESTREET, obj.getName_street());
            cv.put(EXTERNAL_NUMBER, obj.getExternalNumber());
            cv.put(CP, obj.getCp());
            cv.put(LAT, obj.getLat());
            cv.put(LON, obj.getLon());
            cv.put(HASH, Crypto.MD5(System.currentTimeMillis() + obj.getIdReporteLocal()+" " + Math.random()));
            cv.put(SEND, obj.getSend());
            cv.put(PROVIDER, obj.getProvider());
            resp = (int) db.insert(TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }

}
