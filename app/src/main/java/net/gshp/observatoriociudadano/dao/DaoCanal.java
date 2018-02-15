package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoCatalog;

import java.util.List;

public class DaoCanal extends DAO {

	private SQLiteDatabase db;
	private Cursor cursor;

	public static String TABLE_NAME = "c_canal";
	public static String PK_FIELD = "id";

	private final String ID = "id";
	private final String VALUE = "value";

	public DaoCanal() {
		super(TABLE_NAME, PK_FIELD);
	}

	/**
	 * insert
	 */
	public int insert(List<DtoCatalog> obj) {
		db = helper.getWritableDatabase();
		int resp = 0;
		try {
			SQLiteStatement insStmnt = db.compileStatement("" + "INSERT INTO "
					+ TABLE_NAME + " (" + ID + "," + VALUE + ") VALUES(?,?);");
			db.beginTransaction();
			for (DtoCatalog DtoCatalog : obj) {
				try {
					insStmnt.bindLong(1, DtoCatalog.getId());
				} catch (Exception e) {
					insStmnt.bindNull(1);
				}
				try {
					insStmnt.bindString(2, DtoCatalog.getValue());
				} catch (Exception e) {
					insStmnt.bindNull(2);
				}
				insStmnt.executeInsert();
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
