package net.gshp.observatoriociudadano.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.gshp.observatoriociudadano.dto.DtoEAPregunta;

import java.util.List;

public class DaoEAPregunta extends DAO {

	private SQLiteDatabase db;

	public static String TABLE_NAME = "EAPregunta";
	public static String PK_FIELD = "id";

	static final String id = "id";
	static final String idSeccion = "idSeccion";
	static final String idEncuesta = "idEncuesta";
	static final String idGrupo = "idGrupo";
	static final String pregunta = "pregunta";
	static final String parentId = "parentId";
	static final String idTipoPregunta = "idTipoPregunta";
	static final String obligatoria = "obligatoria";
	static final String RangoMinimo = "RangoMinimo";
	static final String RangoMaximo = "RangoMaximo";
	static final String orden = "orden";
	static final String peso = "peso";
	static final String operadorDependencia = "operadorDependencia";
	static final String valorDependencia1 = "valorDependencia1";
	static final String valorDependencia2 = "valorDependencia2";
	static final String queryOpcionesDependencia = "queryOpcionesDependencia";
	static final String queryOpciones = "queryOpciones";

	public DaoEAPregunta() {
		super(TABLE_NAME, PK_FIELD);
	}

	/**
	 * insert
	 */
	public int Insert(List<DtoEAPregunta> obj) {
		db = helper.getWritableDatabase();
		ContentValues cv;
		int resp = 0;
		try {
			db.beginTransaction();
			for (int i = 0; i < obj.size(); i++) {
				cv = new ContentValues();
				cv.put(id, obj.get(i).id);
				cv.put(idSeccion, obj.get(i).section);
				cv.put(idEncuesta, obj.get(i).poll);
				cv.put(idGrupo, obj.get(i).group_parent);
				cv.put(pregunta, obj.get(i).question);
				cv.put(parentId, obj.get(i).parent);
				cv.put(idTipoPregunta, obj.get(i).type_question);
				cv.put(obligatoria, obj.get(i).required);
				cv.put(RangoMinimo, obj.get(i).range_min);
				cv.put(RangoMaximo, obj.get(i).range_max);
				cv.put(orden, obj.get(i).order_by);
				cv.put(peso, obj.get(i).weight);
				cv.put(operadorDependencia, obj.get(i).operator_dependency);
				cv.put(valorDependencia1, obj.get(i).value_dependency);
				cv.put(valorDependencia2, obj.get(i).value_dependency2);
				cv.put(queryOpcionesDependencia, obj.get(i).query_dependency);
				cv.put(queryOpciones, obj.get(i).query_option);
				resp = (int) db.insert(TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			System.out.println("error db");
		} finally {
			db.endTransaction();
		}
		db.close();
		return resp;
	}

	public int delete() {
		db = helper.getWritableDatabase();
		int resp = db.delete(TABLE_NAME, PK_FIELD, null);
		db.close();
		return resp;
	}


}
