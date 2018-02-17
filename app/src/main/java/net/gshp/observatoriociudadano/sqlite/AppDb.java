package net.gshp.observatoriociudadano.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;

/**
 * Created by gnu on 22/02/17.
 */

public class AppDb extends SQLiteOpenHelper {

    private Tables tables;
    private Context context;

    public AppDb() {
        super(ContextApp.context, ContextApp.context.getString(R.string.app_db_name), null, ContextApp.context.getResources().getInteger(R.integer.app_version_db));
        tables = new Tables();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tables.TableSepomex);
        db.execSQL(tables.TABLE_PDV);
        db.execSQL(tables.TABLE_SCHEDULE);
        db.execSQL(tables.TableReportGeo);
        db.execSQL(tables.TablePolitics);
        db.execSQL(tables.Table_CClient);
        db.execSQL(tables.Table_CCanal);
        db.execSQL(tables.Table_CTypeReport);
        db.execSQL(tables.Table_CRtm);
        db.execSQL(tables.TableReportReport);
        db.execSQL(tables.TableReportCheck);
        db.execSQL(tables.TableModule);

        db.execSQL(tables.Table_EAEncuesta);
        db.execSQL(tables.Table_EAGrupo);
        db.execSQL(tables.Table_EAOpcionPregunta);
        db.execSQL(tables.Table_EAPregunta);
        db.execSQL(tables.Table_EARespuesta);
        db.execSQL(tables.Table_EASeccion);
        db.execSQL(tables.Table_EATipoPregunta);
        db.execSQL(tables.Table_EARespuestaRT);
        db.execSQL(tables.TableEAAnswerPdv);

        db.execSQL(tables.TableMeasurementModuleHead);
        db.execSQL(tables.TableMeasurementModule);
        db.execSQL(tables.TableMeasurementModuleClient);
        db.execSQL(tables.TableMeasurementModuleCanal);
        db.execSQL(tables.TableMeasurementModuleFormat);
        db.execSQL(tables.TableMeasurementModulePdv);
        db.execSQL(tables.TableMeasurementModuleRtm);
        db.execSQL(tables.TableMeasurementModuleRegion);

        db.execSQL(tables.TablePhoto);
        db.execSQL(tables.TableImageLogin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 1:

            default:
                break;
        }

    }
}
