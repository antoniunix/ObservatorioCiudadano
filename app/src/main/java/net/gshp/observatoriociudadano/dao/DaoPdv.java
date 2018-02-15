package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoPdvPdv;

import java.util.List;

public class DaoPdv extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;

    public static String TABLE_NAME = "pdv";
    public static String PK_FIELD = "id";

    private final String ID = "id";
    private final String ID_CLIENT = "id_client";
    private final String ID_RTM = "id_rtm";
    private final String NAME = "name";
    private final String ADDRESS = "address";
    private final String PDV_CODE = "pdv_code";
    private final String TOWN = "town";
    private final String ID_FORMAT = "id_format";
    private final String LAT = "lat";
    private final String LON = "lon";
    private final String EXTRA_FIELD1 = "extra_field1";
    private final String EXTRA_FIELD2 = "extra_field2";
    private final String EXTRA_FIELD3 = "extra_field3";
    private final String TYPE = "type";
    private final String ID_REGION = "id_region";
    private final String ID_ROL = "id_rol";


    public DaoPdv() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * insert
     */
    public int insert(List<DtoPdvPdv> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            String qry = "INSERT INTO " + TABLE_NAME + " (" + ID + ","
                    + ID_CLIENT + "," + ID_RTM + "," + NAME + "," + ADDRESS
                    + "," + PDV_CODE + "," + TOWN + "," + LAT + "," + LON + "," + EXTRA_FIELD1 + ","
                    + EXTRA_FIELD2 + "," + EXTRA_FIELD3 + "," + ID_FORMAT + "," + TYPE + ","
                    + ID_REGION + ")"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();
            for (DtoPdvPdv dtoPdv : obj) {

                try {
                    insStatement.bindLong(1, dtoPdv.getId());
                } catch (Exception e) {
                    insStatement.bindNull(1);
                }
                try {
                    insStatement.bindLong(2, dtoPdv.getIdClient());
                } catch (Exception e) {
                    insStatement.bindNull(2);
                }
                try {
                    insStatement.bindLong(3, dtoPdv.getIdRtm());
                } catch (Exception e) {
                    insStatement.bindNull(3);
                }
                try {
                    insStatement.bindString(4, dtoPdv.getName());
                } catch (Exception e) {
                    insStatement.bindNull(4);
                }
                try {
                    insStatement.bindString(5, dtoPdv.getAddress());
                } catch (Exception e) {
                    insStatement.bindNull(5);
                }
                try {
                    insStatement.bindString(6, dtoPdv.getPdvCode());
                } catch (Exception e) {
                    insStatement.bindNull(6);
                }
                try {
                    insStatement.bindLong(7, dtoPdv.getTown());
                } catch (Exception e) {
                    insStatement.bindNull(7);
                }
                try {
                    insStatement.bindDouble(8, dtoPdv.getLat());
                } catch (Exception e) {
                    insStatement.bindNull(8);
                }
                try {
                    insStatement.bindDouble(9, dtoPdv.getLon());
                } catch (Exception e) {
                    insStatement.bindNull(9);
                }
                try {
                    insStatement.bindString(10, dtoPdv.getExtraField1());
                } catch (Exception e) {
                    insStatement.bindNull(10);
                }
                try {
                    insStatement.bindString(11, dtoPdv.getExtraField2());
                } catch (Exception e) {
                    insStatement.bindNull(11);
                }
                try {
                    insStatement.bindString(12, dtoPdv.getExtraField3());
                } catch (Exception e) {
                    insStatement.bindNull(12);
                }
                try {
                    insStatement.bindLong(13, dtoPdv.getIdFormat());
                } catch (Exception e) {
                    insStatement.bindNull(13);
                }
                try {
                    insStatement.bindLong(14, dtoPdv.getType());
                } catch (Exception e) {
                    insStatement.bindNull(14);
                }
                try {
                    insStatement.bindLong(15, dtoPdv.getIdRegion());
                } catch (Exception e) {
                    insStatement.bindNull(15);
                }

                insStatement.executeInsert();
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


    /**
     * Select
     */
    public DtoPdvPdv SelectById(long idPdv) {
        db = helper.getReadableDatabase();
        String qry = "SELECT \n" +
                "				pdv.id, \n" +
                "				pdv.id_client, \n" +
                "				pdv.id_rtm, \n" +
                "				pdv.name, \n" +
                "				pdv.address, \n" +
                "				pdv.pdv_code, \n" +
                "				pdv.town, \n" +
                "				pdv.id_format, \n" +
                "				pdv.lat, \n" +
                "				pdv.lon, \n" +
                "				pdv.extra_field1, \n" +
                "				pdv.extra_field2, \n" +
                "				pdv.extra_field3, \n" +
                "				pdv.type, \n" +
                "				pdv.id_region, \n" +
                "				c_rtm.id_canal \n" +
                "				FROM \n" +
                "				pdv \n" +
                "				INNER JOIN c_rtm ON pdv.id_rtm = c_rtm.id \n" +
                "				WHERE pdv.id=" + idPdv;
        cursor = db.rawQuery(qry, null);
        DtoPdvPdv catalogo;
        catalogo = new DtoPdvPdv();
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int id_client = cursor.getColumnIndexOrThrow("id_client");
            int id_rtm = cursor.getColumnIndexOrThrow("id_rtm");
            int name = cursor.getColumnIndexOrThrow("name");
            int address = cursor.getColumnIndexOrThrow("address");
            int pdvCode = cursor.getColumnIndexOrThrow("pdv_code");
            int lat = cursor.getColumnIndexOrThrow("lat");
            int lon = cursor.getColumnIndexOrThrow("lon");
            int town = cursor.getColumnIndexOrThrow("town");
            int extraField1 = cursor.getColumnIndexOrThrow("extra_field1");
            int extraField2 = cursor.getColumnIndexOrThrow("extra_field2");
            int extraField3 = cursor.getColumnIndexOrThrow("extra_field3");
            int idCanal = cursor.getColumnIndexOrThrow("id_canal");
            int id_format = cursor.getColumnIndexOrThrow("id_format");
            int type = cursor.getColumnIndexOrThrow("type");
            int id_region = cursor.getColumnIndexOrThrow("id_region");
            do {
                catalogo.setId(cursor.getInt(id));
                catalogo.setIdClient(cursor.getInt(id_client));
                catalogo.setIdRtm(cursor.getInt(id_rtm));
                catalogo.setName(cursor.getString(name));
                catalogo.setAddress(cursor.getString(address));
                catalogo.setPdvCode(cursor.getString(pdvCode));
                catalogo.setLat(cursor.getDouble(lat));
                catalogo.setLon(cursor.getDouble(lon));
                catalogo.setTown(cursor.getInt(town));
                catalogo.setExtraField1(cursor.getString(extraField1));
                catalogo.setExtraField2(cursor.getString(extraField2));
                catalogo.setExtraField3(cursor.getString(extraField3));
                catalogo.setIdCanal(cursor.getInt(idCanal));
                catalogo.setIdFormat(cursor.getInt(id_format));
                catalogo.setType(cursor.getInt(type));
                catalogo.setIdRegion(cursor.getInt(id_region));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return catalogo;
    }

}
