package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.Cursor;

import net.gshp.observatoriociudadano.dto.DtoEASeccion;

import java.util.ArrayList;
import java.util.List;

public class DaoEASeccion extends DAO {
	public DaoEASeccion() {
		super("EASeccion", "id");
		TAG="DaoEASeccion";
	}
	
	/**
	 * insert
	 */
	public long insert(DtoEASeccion obj)
	{
		ContentValues cv=new ContentValues();
		cv.put("id", obj.getId());
		cv.put("idEncuesta", obj.getIdEncuesta());
		cv.put("idParent", obj.getIdParent());
		cv.put("orden", obj.getOrden());
		cv.put("peso", obj.getPeso());
		cv.put("nombre", obj.getNombre());
		
		return insert(cv);
	}
	
	/**
	 * insert All
	 */
	public boolean insertAllSeccion( List<DtoEASeccion> obj)
	{
		List<ContentValues> cvs=new ArrayList<ContentValues>(obj.size());
		for (int i = 0; i < obj.size(); i++) {
			ContentValues cv=new ContentValues();
			cv.put("id", obj.get(i).getId());
			cv.put("idEncuesta", obj.get(i).getIdEncuesta());
			cv.put("idParent", obj.get(i).getIdParent());
			cv.put("orden", obj.get(i).getOrden());
			cv.put("peso", obj.get(i).getPeso());
			cv.put("nombre", obj.get(i).getNombre());
			cvs.add(cv);
		}
		return insertAll(cvs);
	}
	

	private List<DtoEASeccion> toDto(Cursor cursor){
		int id=cursor.getColumnIndexOrThrow("id");
		int idEncuesta=cursor.getColumnIndexOrThrow("idEncuesta");
		int idParent=cursor.getColumnIndexOrThrow("idParent");
		int orden=cursor.getColumnIndexOrThrow("orden");
		int peso=cursor.getColumnIndexOrThrow("peso");
		int nombre=cursor.getColumnIndexOrThrow("nombre");
		
		List<DtoEASeccion> result=new ArrayList<DtoEASeccion>(cursor.getCount());
		while (cursor.moveToNext()){
			DtoEASeccion obj = new DtoEASeccion();
			obj.setId(cursor.getInt(id));
			obj.setIdEncuesta(cursor.getInt(idEncuesta));
			obj.setIdParent(cursor.getInt(idParent));
			obj.setOrden(cursor.getInt(orden));
			obj.setPeso(cursor.getInt(peso));
			obj.setNombre(cursor.getString(nombre));
			result.add(obj);
		}
		cursor.close();
		
		return result;
	}
	
}
