package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.gshp.observatoriociudadano.dto.DtoEAOpcionPregunta;

import java.util.List;

public class DaoEAOpcionPregunta extends DAO {

	private SQLiteDatabase db;

	public static String TABLE_NAME = "EAOpcionPregunta";
	public static String PK_FIELD = "";

	public static final String idPregunta="idPregunta";
	public static final String opcion="opcion";
	public static final String image="image";
	public static final String last_update="last_update";

	public DaoEAOpcionPregunta() {
		super(TABLE_NAME, PK_FIELD);
	}
	
	/**
	 * insert
	 */
	public int Insert( List<DtoEAOpcionPregunta> obj)
	{
		db=helper.getWritableDatabase();
		ContentValues cv;
		int resp=0;
		try {
			db.beginTransaction();
			for (int i = 0; i < obj.size(); i++) {
				cv=new ContentValues();
				
				cv.put(idPregunta, obj.get(i).question);
				cv.put(opcion, obj.get(i).option_value);
				cv.put(image, obj.get(i).image);
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


}
