package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoReportGeolocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 20/08/17.
 */

public class DaoReportGeolocation extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;
    public static String TABLE_NAME = "report_geolocation";
    public static String PK_FIELD = "id";

    private final String ID = "id";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String BATERRY = "battery";
    private final String ACCURACY = "accuracy";
    private final String IMEI = "imei";
    private final String SATELLITE_UTC = "satellite_utc";
    private final String DATE = "date";
    private final String TZ = "tz";
    private final String VERSION = "version";
    private final String SEND = "send";
    private final String HASH = "hash";
    private final String PROVIDER = "provider";
    private final String FAKELOCATION_ENABLED = "fakelocation_enabled";

    public DaoReportGeolocation(){super(TABLE_NAME,PK_FIELD);}

    /**
     * insert
     */
    public long Insert(DtoReportGeolocation dto) {
        db = null;
        long resp = 0;
        try {
            db = helper.getWritableDatabase();
            String qry = "INSERT INTO " + TABLE_NAME + " (" + LATITUDE + "," + LONGITUDE
                    + "," + BATERRY + "," + ACCURACY + "," + IMEI + "," + SATELLITE_UTC + "," + DATE + ","
                    + TZ + "," + VERSION + "," + SEND + "," + HASH + "," + PROVIDER + "," + FAKELOCATION_ENABLED + ")"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();
            try {
                insStatement.bindDouble(1, dto.getLatitude());
            } catch (Exception e) {
                insStatement.bindNull(1);
            }
            try {
                insStatement.bindDouble(2, dto.getLongitude());
            } catch (Exception e) {
                insStatement.bindNull(2);
            }
            try {
                insStatement.bindString(3, dto.getBattery());
            } catch (Exception e) {
                insStatement.bindNull(3);
            }
            try {
                insStatement.bindString(4, dto.getAccuracy());
            } catch (Exception e) {
                insStatement.bindNull(4);
            }
            try {
                insStatement.bindString(5, dto.getImei());
            } catch (Exception e) {
                insStatement.bindNull(5);
            }
            try {
                insStatement.bindString(6, dto.getSatelliteUtc());
            } catch (Exception e) {
                insStatement.bindNull(6);
            }
            try {
                insStatement.bindString(7, dto.getDate());
            } catch (Exception e) {
                insStatement.bindNull(7);
            }
            try {
                insStatement.bindString(8, dto.getTz());
            } catch (Exception e) {
                insStatement.bindNull(8);
            }
            try {
                insStatement.bindString(9, dto.getVersion());
            } catch (Exception e) {
                insStatement.bindNull(9);
            }
            try {
                insStatement.bindLong(10, dto.getSend());
            } catch (Exception e) {
                insStatement.bindNull(10);
            }

            try {
                insStatement.bindString(11, dto.getHash());
            } catch (Exception e) {
                insStatement.bindNull(11);
            }
            try {
                insStatement.bindString(12, dto.getProvider());
            } catch (Exception e) {
                insStatement.bindNull(12);
            }
            try {
                insStatement.bindLong(13, dto.isFakelocationEnabled() ? 1 : 0);
            } catch (Exception e) {
                insStatement.bindNull(13);
            }
            resp = insStatement.executeInsert();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return resp;
    }

    public List<DtoReportGeolocation> selectAll() {

        db = null;
        cursor = null;
        List<DtoReportGeolocation> obj = new ArrayList<>();
        try {
            db = helper.getReadableDatabase();
            String qry = "SELECT\n" +
                    "report_geolocation.id,\n" +
                    "report_geolocation.latitude,\n" +
                    "report_geolocation.longitude,\n" +
                    "report_geolocation.battery,\n" +
                    "report_geolocation.accuracy,\n" +
                    "report_geolocation.imei,\n" +
                    "report_geolocation.satellite_utc,\n" +
                    "report_geolocation.date,\n" +
                    "report_geolocation.tz,\n" +
                    "report_geolocation.version,\n" +
                    "report_geolocation.send,\n" +
                    "report_geolocation.hash,\n" +
                    "report_geolocation.provider,\n" +
                    "report_geolocation.fakelocation_enabled\n" +
                    "FROM\n" +
                    "report_geolocation\n" +
                    "WHERE report_geolocation.send=0";
            cursor = db.rawQuery(qry, null);

            DtoReportGeolocation catalogo;
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndexOrThrow(ID);
                int latitude = cursor.getColumnIndexOrThrow(LATITUDE);
                int longitude = cursor.getColumnIndexOrThrow(LONGITUDE);
                int battery = cursor.getColumnIndexOrThrow(BATERRY);
                int accuracy = cursor.getColumnIndexOrThrow(ACCURACY);
                int imei = cursor.getColumnIndexOrThrow(IMEI);
                int satelliteUtc = cursor.getColumnIndexOrThrow(SATELLITE_UTC);
                int date = cursor.getColumnIndexOrThrow(DATE);
                int tz = cursor.getColumnIndexOrThrow(TZ);
                int version = cursor.getColumnIndexOrThrow(VERSION);
                int send = cursor.getColumnIndexOrThrow(SEND);
                int hash = cursor.getColumnIndexOrThrow(HASH);
                int provider = cursor.getColumnIndexOrThrow(PROVIDER);
                int fakelocationEnabled = cursor.getColumnIndexOrThrow(FAKELOCATION_ENABLED);
                do {
                    catalogo = new DtoReportGeolocation();
                    catalogo.setId(cursor.getInt(id));
                    catalogo.setLatitude(cursor.getDouble(latitude));
                    catalogo.setLongitude(cursor.getDouble(longitude));
                    catalogo.setBattery(cursor.getString(battery));
                    catalogo.setAccuracy(cursor.getString(accuracy));
                    catalogo.setImei(cursor.getString(imei));
                    catalogo.setSatelliteUtc(cursor.getString(satelliteUtc));
                    catalogo.setDate(cursor.getString(date));
                    catalogo.setTz(cursor.getString(tz));
                    catalogo.setVersion(cursor.getString(version));
                    catalogo.setSend(cursor.getInt(send));
                    catalogo.setHash(cursor.getString(hash));
                    catalogo.setProvider(cursor.getString(provider));
                    catalogo.setFakelocationEnabled(cursor.getInt(fakelocationEnabled) == 1);

                    obj.add(catalogo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return obj;
    }

    public DtoReportGeolocation Select() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT \n"
                + "max(report_geolocation.date) as max, \n"
                + "report_geolocation.id, \n"
                + "report_geolocation.latitude, \n"
                + "report_geolocation.longitude, \n"
                + "report_geolocation.battery, \n"
                + "report_geolocation.accuracy, \n"
                + "report_geolocation.imei, \n"
                + "report_geolocation.satellite_utc, \n"
                + "report_geolocation.date, \n"
                + "report_geolocation.tz, \n"
                + "report_geolocation.version, \n"
                + "report_geolocation.hash, \n"
                + "report_geolocation.provider, \n"
                + "report_geolocation.send \n"
                + "FROM \n"
                + "report_geolocation\n"
                + "ORDER BY report_geolocation.id ASC", null);
        DtoReportGeolocation catalogo = new DtoReportGeolocation();
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow(ID);
            int lat = cursor.getColumnIndexOrThrow(LATITUDE);
            int lon = cursor.getColumnIndexOrThrow(LONGITUDE);
            int battery = cursor.getColumnIndexOrThrow(BATERRY);
            int accuracy = cursor.getColumnIndexOrThrow(ACCURACY);
            int imei = cursor.getColumnIndexOrThrow(IMEI);
            int satelliteUTC = cursor.getColumnIndexOrThrow(SATELLITE_UTC);
            int date = cursor.getColumnIndexOrThrow(DATE);
            int tz = cursor.getColumnIndexOrThrow(TZ);
            int version = cursor.getColumnIndexOrThrow(VERSION);
            int hash = cursor.getColumnIndexOrThrow(HASH);
            int provider = cursor.getColumnIndexOrThrow(PROVIDER);
            int send = cursor.getColumnIndexOrThrow(SEND);
            catalogo.setId(cursor.getInt(id));
            catalogo.setLatitude(cursor.getDouble(lat));
            catalogo.setLongitude(cursor.getDouble(lon));
            catalogo.setBattery(cursor.getString(battery));
            catalogo.setAccuracy(cursor.getString(accuracy));
            catalogo.setImei(cursor.getString(imei));
            catalogo.setSatelliteUtc(cursor.getString(satelliteUTC));
            catalogo.setDate(cursor.getString(date));
            catalogo.setTz(cursor.getString(tz));
            catalogo.setVersion(cursor.getString(version));
            catalogo.setProvider(cursor.getString(provider));
            catalogo.setHash(cursor.getString(hash));
            catalogo.setSend(cursor.getInt(send));
        }
        cursor.close();
        db.close();
        return catalogo;
    }

    public void updateReportSend(byte value) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("send", value);
        db.update(TABLE_NAME, cv, null, null);
        db.close();
    }
}
