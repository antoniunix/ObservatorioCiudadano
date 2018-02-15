package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.gshp.observatoriociudadano.sqlite.AppDb;

import java.util.List;

/**
 * Created by gnu on 22/02/17.
 */

public class DAO {
    protected String TAG = "DAO";

    protected AppDb helper;
    protected SQLiteDatabase db;
    protected Cursor cursor;

    public String TABLE_NAME;
    public String PK_FIELD;

    public DAO(String TABLE_NAME, String PK_FIELD) {
        this.TABLE_NAME = TABLE_NAME;
        this.PK_FIELD = PK_FIELD;
        helper = new AppDb();
    }

    public DAO() {
        helper = new AppDb();
    }



    /**
     * insert
     */
    public long insert(ContentValues cv) {
        db = helper.getWritableDatabase();
        long resp = 0;
        try {
            db.beginTransaction();
            resp = db.insert(TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return resp;
    }


    /**
     * insert All
     */
    public boolean insertAll(List<ContentValues> cvs) {
        db = helper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (ContentValues cv : cvs)
                db.insert(TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }

        return true;
    }

    /**
     * Delete
     */
    public int deleteById(long _id) {
        int resp = 0;
        try {
            db = helper.getWritableDatabase();
            resp = db.delete(TABLE_NAME, PK_FIELD + "=?", new String[]{String.valueOf(_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return resp;
    }

    public void updateReportSend(long idReport,byte value) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("send",value);
        db.update(TABLE_NAME, cv, PK_FIELD+"=" + idReport, null);
        db.close();
    }


    /**
     * Delete
     */
    public int delete() {

        int resp = 0;
        try {
            db = helper.getWritableDatabase();
            resp = db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return resp;
    }

    public Cursor selectBy(String column, String value){
        db = helper.getReadableDatabase();
        return db.query(TABLE_NAME, null, column+"="+value, null, null, null, null);
    }

    public Cursor selectBy(String column, int value){
        return selectBy(column, String.valueOf(value));
    }

    public void RunQuery(String query) {
        try {
            db = helper.getWritableDatabase();
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
