package net.gshp.observatoriociudadano.model;

import net.gshp.observatoriociudadano.dao.DaoSepomex;
import net.gshp.observatoriociudadano.listener.OnFinishThread;

import java.io.IOException;

/**
 * Created by leo on 15/02/18.
 */

public class ModelSplash {

    private DaoSepomex daoSepomex;
    private OnFinishThread onFinishThread;

    public ModelSplash(OnFinishThread onFinishThread) {
        daoSepomex = new DaoSepomex();
        this.onFinishThread = onFinishThread;
    }

    /**
     * se insertara la informacion csv contenido en assets, esta solo se insertara cuando la base es creada
     *
     * @return true si la tabla esta vaci y tiene que ser llenada
     */
    public boolean fillSepomex() {
        boolean isempty = daoSepomex.isSpomexFill();
        if (isempty) {
            new Thread() {
                public void run() {
                    try {
                        daoSepomex.importCsvToDB();
                        onFinishThread.onFinishThread();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return isempty;
    }

}
