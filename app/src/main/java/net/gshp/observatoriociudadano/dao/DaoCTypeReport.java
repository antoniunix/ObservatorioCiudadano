package net.gshp.observatoriociudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoCatalog;

import java.util.ArrayList;
import java.util.List;

public class DaoCTypeReport extends DAO {

	private SQLiteDatabase db;
	private Cursor cursor;

	public static String TABLE_NAME = "c_type_report";
	public static String PK_FIELD = "id";

	private final String ID = "id";
	private final String VALUE = "value";

	public DaoCTypeReport() {
		super(TABLE_NAME, PK_FIELD);
	}

	/**
	 * Insert
	 */
	public int Insert(List<DtoCatalog> obj) {
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
			db.close();
		}
		return resp;
	}

	public List<DtoCatalog> select(int status) {
		db = helper.getReadableDatabase();
		String qry = "SELECT DISTINCT\n"
				+ "c_type_report.id,\n"
				+ "c_type_report.value\n"
				+ "FROM\n" + TABLE_NAME + "\n"
				+ "WHERE id<>1\n"
				+ "ORDER BY c_type_report.id ASC";
		cursor = db.rawQuery(qry, null);
		List<DtoCatalog> obj = new ArrayList<>();
		DtoCatalog catalogo;
		if (cursor.moveToFirst()) {
			int id = cursor.getColumnIndexOrThrow(ID);
			int value = cursor.getColumnIndexOrThrow(VALUE);
			do {
				catalogo = new DtoCatalog();
				catalogo.setId(cursor.getInt(id));
				catalogo.setValue(cursor.getString(value));
				obj.add(catalogo);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return obj;
	}
}
