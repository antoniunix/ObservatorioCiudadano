package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoImageLogin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alejandro on 16/02/18.
 */

public class DaoImageLogin extends DAO {

    public static String TABLE_NAME = "image_login";
    public static String PK_FIELD = "id";
    private final String id = "id";
    private final String path = "path";
    private final String name = "name";
    private final String sent = "sent";
    private final String rol = "rol";

    private SQLiteDatabase db;
    private Cursor cursor;

    public DaoImageLogin() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * insert
     */
    public int insert(DtoImageLogin dto) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            SQLiteStatement insStatement = db.compileStatement("INSERT INTO "
                    + TABLE_NAME + " (" + path + "," + name + ","
                    + sent + "," + rol + ")"
                    + "VALUES(?,?,?,?);");
            db.beginTransaction();

            try {
                insStatement.bindString(1, dto.getPath());
            } catch (Exception e) {
                insStatement.bindNull(1);
            }
            try {
                insStatement.bindString(2, dto.getName());
            } catch (Exception e) {
                insStatement.bindNull(2);
            }
            try {
                insStatement.bindLong(3, dto.getSent());
            } catch (Exception e) {
                insStatement.bindNull(3);
            }
            try {
                insStatement.bindLong(4, dto.getRol());
            } catch (Exception e) {
                insStatement.bindNull(4);
            }
            insStatement.executeInsert();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }

    public void update(DtoImageLogin dto) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(path, dto.getPath());
        cv.put(name, dto.getName());
        cv.put(rol, dto.getRol());
        cv.put(sent, dto.getSent());
        db.update(TABLE_NAME, cv, "id=" + dto.getId(), null);
        db.close();
    }

    public void insertOrReplace(DtoImageLogin dto) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "id\n" +
                "FROM\n" +
                TABLE_NAME, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndexOrThrow("id");
                dto.setId(cursor.getInt(id));

                update(dto);
            }
        } else {
            insert(dto);
        }
    }

    public List<DtoImageLogin> selectAll() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "id,\n" +
                "path,\n" +
                "name\n" +
                "FROM\n" +
                TABLE_NAME, null);
        List<DtoImageLogin> obj = new ArrayList<>(cursor.getCount());
        DtoImageLogin dto;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int path = cursor.getColumnIndexOrThrow("path");
            int name = cursor.getColumnIndexOrThrow("name");

            do {
                dto = new DtoImageLogin();
                dto.setId(cursor.getInt(id));
                dto.setPath(cursor.getString(path));
                dto.setName(cursor.getString(name));
                obj.add(dto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    public DtoImageLogin selectLast() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "max(id) id,\n" +
                "path,\n" +
                "name\n" +
                "FROM\n" +
                TABLE_NAME, null);
        DtoImageLogin dto = new DtoImageLogin();
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int path = cursor.getColumnIndexOrThrow("path");
            int name = cursor.getColumnIndexOrThrow("name");

            dto.setId(cursor.getInt(id));
            dto.setPath(cursor.getString(path));
            dto.setName(cursor.getString(name));
        }
        cursor.close();
        db.close();
        return dto;
    }

    public DtoImageLogin select(int idPhoto) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "id,\n" +
                "path,\n" +
                "name,\n" +
                "face_id\n" +
                "FROM\n" +
                TABLE_NAME + "\n" +
                "WHERE id = " + idPhoto, null);

        DtoImageLogin dto = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int path = cursor.getColumnIndexOrThrow("path");
            int name = cursor.getColumnIndexOrThrow("name");
            int face_id = cursor.getColumnIndexOrThrow("face_id");

            dto = new DtoImageLogin();
            dto.setId(cursor.getInt(id));
            dto.setPath(cursor.getString(path));
            dto.setName(cursor.getString(name));
        }
        cursor.close();
        db.close();
        return dto;
    }
}
