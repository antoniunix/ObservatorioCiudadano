package net.gshp.observatoriociudadano.listener;

/**
 * Created by gnu on 22/02/17.
 */

public interface OnProgressSync {

    void onProgresSync(int porcentOfProgress, int httpstatus, String service);
    void onNewVersionExist(boolean isExist);
    void onFinishSync(int httpstatus, String response, Object obj);
}
