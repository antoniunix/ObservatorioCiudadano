package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.gshp.observatoriociudadano.dto.DtoEATipoPregunta;

import java.util.List;

public class DaoEATypeOpcionRespuesta extends DAO {

	private SQLiteDatabase db;

	public static String TABLE_NAME = "EATipoPregunta";
	public static String PK_FIELD = "id";
	
	public static final String id="id";
	public static final String tipo="tipo";

	public DaoEATypeOpcionRespuesta() {
		super(TABLE_NAME, PK_FIELD);
	}
	
	/**
	 * insert
	 */
	public int Insert( List<DtoEATipoPregunta> obj)
	{
		db=helper.getWritableDatabase();
		ContentValues cv;
		int resp=0;
		try {
			db.beginTransaction();
			for (int i = 0; i < obj.size(); i++) {
				cv=new ContentValues();
				cv.put(id, obj.get(i).id);
				cv.put(tipo, obj.get(i).type);
				resp=(int)db.insert(TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error db");
		}
		finally
		{
			db.endTransaction();
		}
		db.close();
		return resp;
	}
	
	/**
	 * Delete
	 */
	public int delete()
	{
		db=helper.getWritableDatabase();
		int resp=db.delete(TABLE_NAME, PK_FIELD, null);
		db.close();
		return resp;
	}
	/**
	 * Cursor
	 */
	public Cursor list(){
		db=helper.getReadableDatabase();
		Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
		return c;
	}


}
