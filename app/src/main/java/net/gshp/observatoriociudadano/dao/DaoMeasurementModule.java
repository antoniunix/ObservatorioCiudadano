package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import net.gshp.observatoriociudadano.dto.DtoMeasurementModule;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 28/08/17.
 */

public class DaoMeasurementModule extends DAO {
    private SQLiteDatabase db;
    private Cursor cursor;


    public static String TABLE_NAME = "measurement_module";
    public static String PK_FIELD = "id";

    private final String ID = "id";
    private final String ID_ITEM = "id_item";
    private final String ID_MEASUREMENT = "id_measurement";
    private final String VALUE = "value";
    private final String REQUIRED = "required";
    private final String ORDEN = "_orden";
    private final String LAST_UPDATE = "last_update";


    public DaoMeasurementModule() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * Insert
     */
    public int Insert(List<DtoMeasurementModule> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            String qry = "INSERT INTO " + TABLE_NAME + " (" + ID + "," + ID_ITEM + "," + VALUE + "," + REQUIRED + "," + LAST_UPDATE
                    + "," + ID_MEASUREMENT + "," + ORDEN + ")"
                    + "VALUES(?,?,?,?,?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();
            for (DtoMeasurementModule dto : obj) {

                try {
                    insStatement.bindLong(1, dto.getId());
                } catch (Exception e) {
                    insStatement.bindNull(1);
                }
                try {
                    insStatement.bindLong(2, dto.getIdItemRelation());
                } catch (Exception e) {
                    insStatement.bindNull(2);
                }
                try {
                    insStatement.bindString(3, dto.getValue());
                } catch (Exception e) {
                    insStatement.bindNull(3);
                }
                try {
                    insStatement.bindLong(4, dto.getRequired());
                } catch (Exception e) {
                    insStatement.bindNull(4);
                }
                try {
                    insStatement.bindLong(5, System.currentTimeMillis());
                } catch (Exception e) {
                    insStatement.bindNull(5);
                }
                try {
                    insStatement.bindLong(6, dto.getIdMeasurement());
                } catch (Exception e) {
                    insStatement.bindNull(6);
                }
                try {
                    insStatement.bindLong(7, dto.get_orden());
                } catch (Exception e) {
                    insStatement.bindNull(7);
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

    public List<DtoMeasurementModule> selectModule(DtoPdvPdv dtoPdvPdv) {
        db = helper.getReadableDatabase();
        String qry = "SELECT DISTINCT\n" +
                "measurement_module.id,\n" +
                "measurement_module.id_item,\n" +
                "measurement_module.value,\n" +
                "measurement_module.required\n" +
                "FROM\n" +
                "measurement_module_head\n" +
                "INNER JOIN measurement_module ON measurement_module.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_canal ON measurement_module_canal.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_client ON measurement_module_client.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_format ON measurement_module_format.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_pdv ON measurement_module_pdv.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_region ON measurement_module_region.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_rtm ON measurement_module_rtm.id_measurement = measurement_module_head.id\n" +
                "WHERE  (measurement_module_canal.id_item_relation= " + dtoPdvPdv.getIdCanal() + " OR measurement_module_canal.id_item_relation ISNULL) AND\n" +
                "(measurement_module_client.id_item_relation= " + dtoPdvPdv.getIdClient() + " OR measurement_module_client.id_item_relation ISNULL) AND\n" +
                "(measurement_module_format.id_item_relation= " + dtoPdvPdv.getIdFormat() + " OR measurement_module_format.id_item_relation ISNULL) AND\n" +
                "(measurement_module_pdv.id_item_relation= " + dtoPdvPdv.getId() + " OR measurement_module_pdv.id_item_relation ISNULL) AND\n" +
                "(measurement_module_region.id_item_relation= " + dtoPdvPdv.getIdRegion() + " OR measurement_module_region.id_item_relation ISNULL) AND\n" +
                "(measurement_module_rtm.id_item_relation= " + dtoPdvPdv.getIdRtm() + " OR measurement_module_rtm.id_item_relation ISNULL) AND\n" +
                " strftime('%Y-%m-%d', date('now', 'localtime'), 'utc') BETWEEN startDate AND endDate  AND measurement_module.id_item<>17 AND measurement_module.id_item<>23\n" +
                "ORDER BY measurement_module._orden ASC";
        Log.e("modulos", " " + qry);
        cursor = db.rawQuery(qry, null);
        List<DtoMeasurementModule> lst = new ArrayList<>(cursor.getCount());
        DtoMeasurementModule catalogo;
        if (cursor.moveToFirst()) {

            int id = cursor.getColumnIndexOrThrow(ID);
            int id_item = cursor.getColumnIndexOrThrow(ID_ITEM);
            int value = cursor.getColumnIndexOrThrow(VALUE);
            int required = cursor.getColumnIndexOrThrow(REQUIRED);
            do {
                catalogo = new DtoMeasurementModule();
                catalogo.setId(cursor.getInt(id));
                catalogo.setIdItemRelation(cursor.getInt(id_item));
                catalogo.setValue(cursor.getString(value));
                catalogo.setRequired(cursor.getInt(required));
                lst.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lst;
    }

    public boolean existModule(int idModule, DtoPdvPdv dtoPdvPdv) {
        boolean exist = false;
        db = helper.getReadableDatabase();
        String qry = "SELECT\n" +
                "count(measurement_module.id_item) as count\n" +
                "FROM\n" +
                "measurement_module_head\n" +
                "INNER JOIN measurement_module ON measurement_module.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_canal ON measurement_module_canal.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_client ON measurement_module_client.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_format ON measurement_module_format.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_pdv ON measurement_module_pdv.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_region ON measurement_module_region.id_measurement = measurement_module_head.id\n" +
                "LEFT JOIN measurement_module_rtm ON measurement_module_rtm.id_measurement = measurement_module_head.id\n" +
                "WHERE  (measurement_module_canal.id_item_relation= " + dtoPdvPdv.getIdCanal() + " OR measurement_module_canal.id_item_relation ISNULL) AND\n" +
                "(measurement_module_client.id_item_relation= " + dtoPdvPdv.getIdClient() + " OR measurement_module_client.id_item_relation ISNULL) AND\n" +
                "(measurement_module_format.id_item_relation= " + dtoPdvPdv.getIdFormat() + " OR measurement_module_format.id_item_relation ISNULL) AND\n" +
                "(measurement_module_pdv.id_item_relation= " + dtoPdvPdv.getId() + " OR measurement_module_pdv.id_item_relation ISNULL) AND\n" +
                "(measurement_module_region.id_item_relation= " + dtoPdvPdv.getIdRegion() + " OR measurement_module_region.id_item_relation ISNULL) AND\n" +
                "(measurement_module_rtm.id_item_relation= " + dtoPdvPdv.getIdRtm() + " OR measurement_module_rtm.id_item_relation ISNULL) AND\n" +
                " strftime('%Y-%m-%d', date('now', 'localtime'), 'utc') BETWEEN startDate AND endDate AND measurement_module.id_item=" + idModule;
        Log.e("count", " " + qry);
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            exist = cursor.getInt(cursor.getColumnIndexOrThrow("count")) > 0;
        }

        cursor.close();
        db.close();
        Log.e("count", "exist " + exist);
        return exist;
    }
}
