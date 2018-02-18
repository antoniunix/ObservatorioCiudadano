package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alejandro on 15/02/18.
 */

public class DaoPhoto extends DAO {

    public static String TABLE_NAME = "photo";
    public static String PK_FIELD = "id";
    private final String id = "id";
    private final String path = "path";
    private final String name = "name";
    private final String face_id = "face_id";
    private final String sent = "sent";
    private final String rol = "rol";
    private final String user = "user";

    private SQLiteDatabase db;
    private Cursor cursor;

    public DaoPhoto() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * insert
     */
    public int insert(DtoPhoto dto) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            SQLiteStatement insStatement = db.compileStatement("INSERT INTO "
                    + TABLE_NAME + " (" + path + "," + name + "," + face_id + ","
                    + sent + "," + rol + "," + user + ")"
                    + "VALUES(?,?,?,?,?,?);");
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
                insStatement.bindLong(3, dto.getFace_id());
            } catch (Exception e) {
                insStatement.bindNull(3);
            }
            try {
                insStatement.bindLong(4, dto.getSent());
            } catch (Exception e) {
                insStatement.bindNull(4);
            }
            try {
                insStatement.bindLong(5, dto.getRol());
            } catch (Exception e) {
                insStatement.bindNull(5);
            }
            try {
                insStatement.bindString(6, dto.getUser());
            } catch (Exception e) {
                insStatement.bindNull(6);
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

    public List<DtoPhoto> selectAll(String userName) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "id,\n" +
                "path,\n" +
                "name,\n" +
                "face_id,\n" +
                "user\n" +
                "FROM\n" +
                "photo\n" +
                "WHERE user = \"" + userName + "\"", null);
        List<DtoPhoto> obj = new ArrayList<>(cursor.getCount());
        DtoPhoto dto;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int path = cursor.getColumnIndexOrThrow("path");
            int name = cursor.getColumnIndexOrThrow("name");
            int face_id = cursor.getColumnIndexOrThrow("face_id");
            int user = cursor.getColumnIndexOrThrow("user");

            do {
                dto = new DtoPhoto();
                dto.setId(cursor.getInt(id));
                dto.setPath(cursor.getString(path));
                dto.setName(cursor.getString(name));
                dto.setFace_id(cursor.getInt(face_id));
                dto.setUser(cursor.getString(user));
                obj.add(dto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    public DtoPhoto select(int idPhoto) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "id,\n" +
                "path,\n" +
                "name,\n" +
                "face_id,\n" +
                "user\n" +
                "FROM\n" +
                "photo\n" +
                "WHERE id = " + idPhoto, null);

        DtoPhoto dto = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int path = cursor.getColumnIndexOrThrow("path");
            int name = cursor.getColumnIndexOrThrow("name");
            int face_id = cursor.getColumnIndexOrThrow("face_id");
            int user = cursor.getColumnIndexOrThrow("user");

            dto = new DtoPhoto();
            dto.setId(cursor.getInt(id));
            dto.setPath(cursor.getString(path));
            dto.setName(cursor.getString(name));
            dto.setFace_id(cursor.getInt(face_id));
            dto.setUser(cursor.getString(user));
        }
        cursor.close();
        db.close();
        return dto;
    }
}
