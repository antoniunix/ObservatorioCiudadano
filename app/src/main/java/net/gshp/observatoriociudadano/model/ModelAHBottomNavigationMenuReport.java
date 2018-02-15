package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import net.gshp.observatoriociudadano.R;

/**
 * Created by Antoniunix on 13/11/17.
 */

public class ModelAHBottomNavigationMenuReport {

    private Activity activity;
    private AHBottomNavigation ahBottomNavigation;
    private AHBottomNavigationItem ahPollSup,ahCensus,ahPhoto, ahCheckOut;
    private ModelMenuReport model;
    private AHBottomNavigation.OnTabSelectedListener onTabSelectedListener;

    public ModelAHBottomNavigationMenuReport(Activity activity, ModelMenuReport model, AHBottomNavigation.OnTabSelectedListener onTabSelectedListener) {
        this.activity = activity;
        this.model = model;
        this.onTabSelectedListener = onTabSelectedListener;
        ahBottomNavigation =  activity.findViewById(R.id.bottom_navigation);


        ahPollSup = new AHBottomNavigationItem( null, R.drawable.agenda, R.color.colorAHBottonDefaul);
        ahCensus = new AHBottomNavigationItem( null, R.drawable.agenda, R.color.colorAHBottonDefaul);
        ahPhoto = new AHBottomNavigationItem( null, R.drawable.agenda, R.color.colorAHBottonDefaul);
        ahCheckOut = new AHBottomNavigationItem(null, R.drawable.salir2, R.color.colorAHBottonDefaul);


        ahBottomNavigation.setAccentColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAHBottonAccentColor));
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAHBottonInactive));

        ahBottomNavigation.addItem(ahPollSup);
        ahBottomNavigation.addItem(ahCensus);
        ahBottomNavigation.addItem(ahPhoto);
        ahBottomNavigation.addItem(ahCheckOut);


        ahBottomNavigation.setOnTabSelectedListener(onTabSelectedListener);
    }

    public void onResume() {
//        if (model.isCheck(1)) {
//            ahBottomNavigation.setNotification("H", 0);
//        }
//        if (model.isCheck(2)) {
//            ahBottomNavigation.setNotification("H", 1);
//        }

    }


    public AHBottomNavigation getView() {
        return ahBottomNavigation;
    }
}
