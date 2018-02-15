package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoMeasurementFilter;

import java.util.List;

/**
 * Created by leo on 28/08/17.
 */

public class DaoMeasurementFilter extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;


    public String tableName;
    public static String PK_FIELD = "id";

    private final String ID_ITEM_RELATION = "id_item_relation";
    private final String ID_MEASUREMENT = "id_measurement";
    private final String VALUE = "value";
    private final String LAST_UPDATE = "last_update";


    public DaoMeasurementFilter(String tableName) {
        super(tableName, PK_FIELD);
    }

    /**
     * Insert
     */
    public int Insert(List<DtoMeasurementFilter> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            String qry = "INSERT INTO " + TABLE_NAME + " (" + ID_ITEM_RELATION + "," + ID_MEASUREMENT + "," + LAST_UPDATE + ")"
                    + "VALUES(?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();
            for (DtoMeasurementFilter dto : obj) {

                try {
                    insStatement.bindLong(1, dto.getIdItemRelation());
                } catch (Exception e) {
                    insStatement.bindNull(1);
                }
                try {
                    insStatement.bindLong(2, dto.getIdMeasurement());
                } catch (Exception e) {
                    insStatement.bindNull(2);
                }
                try {
                    insStatement.bindLong(3, System.currentTimeMillis());
                } catch (Exception e) {
                    insStatement.bindNull(3);
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


}
