package net.gshp.observatoriociudadano.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by LEONARDO on 20/08/2017.
 */

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private int progress_bar_type;
    private Handler handler;
    private Message msg;
    private int ConnectTimeout=15000;
    private int readTimeout=10000;
    private String nameFile;

    public DownloadFileFromURL(Handler h, String nameFile)
    {
        this.handler=h;
        this.nameFile = nameFile;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        msg = handler.obtainMessage();
        msg.what=1;
        msg.arg2=progress_bar_type;
        handler.sendMessage(msg);
    }
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.setConnectTimeout(ConnectTimeout);
            conection.setReadTimeout(readTimeout);
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();
            // input stream to read file - with 8k buffer
            InputStream input = conection.getInputStream();//new BufferedInputStream(url.openStream(), 8192);
            // Output stream to write file
            OutputStream output;

            // output = new FileOutputStream(AppContext.context.getString(R.string.app_path_apk));
            output = new FileOutputStream(nameFile);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                // writing data to file
                output.write(data, 0, count);

            }
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//        pDialog.setProgress(Integer.parseInt(progress[0]));
        msg = handler.obtainMessage();
        msg.what=2;
        msg.arg2= Integer.parseInt(progress[0]);
        handler.sendMessage(msg);
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        handler.sendEmptyMessage(3);
    }
}

