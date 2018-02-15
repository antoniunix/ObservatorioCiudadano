package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoRtm;

import java.util.List;

public class DaoRtm extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;

    public static String TABLE_NAME = "c_rtm";
    public static String PK_FIELD = "id";

    private final String ID = "id";
    private final String ID_CANAL = "id_canal";
    private final String VALUE = "value";

    public DaoRtm() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * insert
     */
    public int insert(List<DtoRtm> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            SQLiteStatement inssStatement = db.compileStatement("INSERT INTO "
                    + TABLE_NAME + " (" + ID + "," + ID_CANAL + "," + VALUE
                    + ") VALUES(?,?,?);");
            db.beginTransaction();

            for (DtoRtm dtOrtm : obj) {
                try {
                    inssStatement.bindDouble(1, dtOrtm.getId());
                } catch (Exception e) {
                    inssStatement.bindNull(1);
                }
                try {
                    inssStatement.bindDouble(2, dtOrtm.getIdCanal());
                } catch (Exception e) {
                    inssStatement.bindNull(2);
                }
                try {
                    inssStatement.bindString(3, dtOrtm.getValue());
                } catch (Exception e) {
                    inssStatement.bindNull(3);
                }

                inssStatement.executeInsert();
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
