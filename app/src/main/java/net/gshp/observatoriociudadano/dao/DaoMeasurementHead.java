package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoMeasurementHead;

import java.util.List;


/**
 * Created by leo on 28/08/17.
 */

public class DaoMeasurementHead extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;


    public String tableName;
    public static String PK_FIELD = "id";

    private final String ID = "id";
    private final String START_DATE = "startDate";
    private final String END_DATE = "endDate";
    private final String DESCRIPTION = "description";
    private final String LAST_UPDATE = "last_update";


    public DaoMeasurementHead(String tableName) {
        super(tableName, PK_FIELD);
    }

    /**
     * Insert
     */
    public int Insert(List<DtoMeasurementHead> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            String qry = "INSERT INTO " + TABLE_NAME + " (" + ID + "," + START_DATE + "," + END_DATE + "," + DESCRIPTION + "," + LAST_UPDATE + ")"
                    + "VALUES(?,?,?,?,?);";
            SQLiteStatement insStatement = db.compileStatement(qry);
            db.beginTransaction();
            for (DtoMeasurementHead dto : obj) {

                try {
                    insStatement.bindLong(1, dto.getId());
                } catch (Exception e) {
                    insStatement.bindNull(1);
                }
                try {
                    insStatement.bindString(2, dto.getStartDate());
                } catch (Exception e) {
                    insStatement.bindNull(2);
                }
                try {
                    insStatement.bindString(3, dto.getEndDate());
                } catch (Exception e) {
                    insStatement.bindNull(3);
                }
                try {
                    insStatement.bindString(4, dto.getDescription());
                } catch (Exception e) {
                    insStatement.bindNull(4);
                }
                try {
                    insStatement.bindLong(5, System.currentTimeMillis());
                } catch (Exception e) {
                    insStatement.bindNull(5);
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