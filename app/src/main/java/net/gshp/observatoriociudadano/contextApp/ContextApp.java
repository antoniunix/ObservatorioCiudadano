package net.gshp.observatoriociudadano.contextApp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by gnu on 13/02/18.
 */

public class ContextApp extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        Stetho.initializeWithDefaults(this);
//        AlarmGeolocation.getInstance();

//        APIEncuesta.setPATH_FOTO(getString(R.string.app_path_photo));

        /*Dbapi*/
//        DBAPI dbapi = DBAPI.getInstance();
//        dbapi.loadPropertiesFromFile(this.getApplicationContext().getResources());
//        dbapi.createDB(this.getApplicationContext());
    }
}