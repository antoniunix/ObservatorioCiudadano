package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.gshp.observatoriociudadano.dto.DtoEaRespuestaPdv;

import java.util.List;

/**
 * Created by omara on 06/04/2017.
 */

public class DaoEaRespuestaPdv extends DAO {

    private SQLiteDatabase db;

    public final static String id_pdv = "id_pdv";
    public final static String id_poll = "id_poll";
    public final static String last_update = "last_update";

    public static String TABLE_NAME = "EARespuestaPdv";
    public static String PK_FIELD = "id";

    public DaoEaRespuestaPdv() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * Insert
     */
    public int Insert(List<DtoEaRespuestaPdv> obj) {
        db = helper.getWritableDatabase();
        ContentValues cv;
        int resp = 0;
        try {
            db.beginTransaction();
            for (int i = 0; i < obj.size(); i++) {
                cv = new ContentValues();
                cv.put(id_pdv, obj.get(i).getIdPdv());
                cv.put(id_poll, obj.get(i).getIdPoll());
                cv.put(last_update, System.currentTimeMillis());
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

    /**
     * Si existe o no encuesta respondida por pdv
     */
    public boolean exists(long idPdv, long idPoll) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *" + " FROM " + TABLE_NAME
                + " WHERE id_pdv = " + idPdv + " AND id_poll=" + idPoll, null);
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean supervisorRegistered() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "rowid\n" +
                "FROM\n" +
                "EAAnswerPdv\n" +
                "WHERE \n" +
                id_poll + "=1", null);
        int results = cursor.getCount();
        Log.w(TAG, "result: " + results);
        cursor.close();
        db.close();
        return results >= 1;
    }
}
