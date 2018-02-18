package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoSepomex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 15/02/18.
 */

public class DaoSepomex extends DAO {

    private SQLiteDatabase db;
    private Cursor cursor;

    public static String TABLE_NAME = "sepomex";
    public static String PK_FIELD = "postal_code";

    private final String POSTAL_CODE = "postal_code";
    private final String SUBURB = "suburb";
    private final String TYPE_SUBURB = "type_suburb";
    private final String TOWN = "town";
    private final String STATE = "state";

    public DaoSepomex() {
        super(TABLE_NAME, PK_FIELD);
    }

    public void importCsvToDB() throws IOException {
        InputStreamReader is = new InputStreamReader((ContextApp.context.getAssets().open("sepomex.csv")));
        BufferedReader reader = new BufferedReader(is);
        reader.readLine();
        String line;
        String qry = "INSERT INTO " + TABLE_NAME + " (" + POSTAL_CODE + "," + SUBURB + "," + TYPE_SUBURB + "," + TOWN + "," + STATE + ")"
                + "VALUES(?,?,?,?,?);";
        db = helper.getWritableDatabase();
        SQLiteStatement insStatement = db.compileStatement(qry);
        db.beginTransaction();
        while ((line = reader.readLine()) != null) {
            String[] str = line.split(",");
            insStatement.bindString(1, str[0]);
            insStatement.bindString(2, str[1]);
            insStatement.bindString(3, str[2]);
            insStatement.bindString(4, str[3]);
            insStatement.bindString(5, str[4]);
            insStatement.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * se verifica que la tabla sepomex contenga informacion
     *
     * @return true si esta vacio
     */
    public boolean isSpomexFill() {
        db = helper.getReadableDatabase();
        boolean existCheck = false;
        String qry = "SELECT\n" +
                "count(*) count\n" +
                "FROM sepomex";
        cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            existCheck = cursor.getInt(cursor.getColumnIndexOrThrow("count")) == 0;
        }
        cursor.close();
        db.close();
        return existCheck;
    }

    /**
     * Select
     */
    public List<DtoSepomex> Select(String postalCode) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *\n" +
                "FROM\n" +
                TABLE_NAME + "\n" +
                "WHERE " + POSTAL_CODE + "='" + postalCode + "'\n" +
                "ORDER BY suburb ASC", null);
        List<DtoSepomex> obj = new ArrayList<DtoSepomex>();
        DtoSepomex catalogo;
        if (cursor.moveToFirst()) {
            do {
                catalogo = new DtoSepomex();
                catalogo.setPostalCode(cursor.getString(cursor.getColumnIndexOrThrow(POSTAL_CODE)));
                catalogo.setSuburb(cursor.getString(cursor.getColumnIndexOrThrow(SUBURB)));
                catalogo.setTown(cursor.getString(cursor.getColumnIndexOrThrow(TOWN)));
                catalogo.setState(cursor.getString(cursor.getColumnIndexOrThrow(STATE)));
                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    /**
     * Select
     */
    public List<String> selectAutoComplete() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *\n" +
                "FROM\n" +
                TABLE_NAME + "\n" +
                "ORDER BY suburb ASC", null);
        List<String> obj = new ArrayList<String>();
        DtoSepomex catalogo;
        String address = "";
        if (cursor.moveToFirst()) {
            do {
                catalogo = new DtoSepomex();
                catalogo.setPostalCode(cursor.getString(cursor.getColumnIndexOrThrow(POSTAL_CODE)));
                catalogo.setSuburb(cursor.getString(cursor.getColumnIndexOrThrow(SUBURB)));
                catalogo.setTown(cursor.getString(cursor.getColumnIndexOrThrow(TOWN)));
                catalogo.setState(cursor.getString(cursor.getColumnIndexOrThrow(STATE)));
                address = "" + catalogo.getPostalCode() + "," + catalogo.getTown() + "," + catalogo.getState();
                obj.add(address);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    /**
     * Select
     */
    public List<String> SelectCp() {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT DISTINCT\n" +
                POSTAL_CODE + "\n" +
                "FROM\n" +
                TABLE_NAME + "\n" +
                "ORDER BY " + POSTAL_CODE + " ASC", null);
        List<String> obj = new ArrayList<String>();
        String catalogo = "";
        if (cursor.moveToFirst()) {
            do {
                catalogo = cursor.getString(cursor.getColumnIndexOrThrow(POSTAL_CODE));
                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }


}
