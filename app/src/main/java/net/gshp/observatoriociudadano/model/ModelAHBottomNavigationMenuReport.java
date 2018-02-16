package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoBundle;

/**
 * Created by Antoniunix on 13/11/17.
 */

public class ModelAHBottomNavigationMenuReport {

    private Activity activity;
    private AHBottomNavigation ahBottomNavigation;
    private AHBottomNavigationItem ahPollSup, ahPollCasilla, ahPollRepresentante, ahCensus, ahPhoto, ahCheckOut;
    private ModelMenuReport model;
    private AHBottomNavigation.OnTabSelectedListener onTabSelectedListener;
    private DtoBundle dtoBundle;

    public ModelAHBottomNavigationMenuReport(Activity activity, ModelMenuReport model,
                                             AHBottomNavigation.OnTabSelectedListener onTabSelectedListener, DtoBundle dtoBundle) {
        this.activity = activity;
        this.model = model;
        this.onTabSelectedListener = onTabSelectedListener;
        ahBottomNavigation = activity.findViewById(R.id.bottom_navigation);
        this.dtoBundle = dtoBundle;


        ahPollSup = new AHBottomNavigationItem(null, R.drawable.supervisor, R.color.colorAHBottonDefaul);
        ahCensus = new AHBottomNavigationItem(null, R.drawable.censo, R.color.colorAHBottonDefaul);
        ahPollCasilla = new AHBottomNavigationItem(null, R.drawable.e_casilla, R.color.colorAHBottonDefaul);
        ahPollRepresentante = new AHBottomNavigationItem(null, R.drawable.e_rep, R.color.colorAHBottonDefaul);
        ahPhoto = new AHBottomNavigationItem(null, R.drawable.foto, R.color.colorAHBottonDefaul);
        ahCheckOut = new AHBottomNavigationItem(null, R.drawable.checkout, R.color.colorAHBottonDefaul);


        ahBottomNavigation.setAccentColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAHBottonAccentColor));
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAHBottonInactive));

        if (dtoBundle.getIdTypeMenuReport() == activity.getResources().getInteger(R.integer.idPollSupervisor)) {
            ahBottomNavigation.addItem(ahPollSup);
        } else if (dtoBundle.getIdTypeMenuReport() == activity.getResources().getInteger(R.integer.idPollRepresentanteCasilla)) {
            ahBottomNavigation.addItem(ahPollCasilla);
            ahBottomNavigation.addItem(ahPollRepresentante);

        }

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
