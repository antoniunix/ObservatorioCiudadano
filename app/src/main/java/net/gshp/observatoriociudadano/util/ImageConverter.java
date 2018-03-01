package net.gshp.observatoriociudadano.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by alejandro on 30/03/17.
 */

public class ImageConverter {

    private static final String TAG = "ImageConverter";

    public static Bitmap getBitmap(Context context, String name) {
        /*File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");*/
        /*File mediaStorageDir;
        if (isExternalStorageWritable()) {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/GeoCore/data");
        } else {*/
        File mediaStorageDir = context.getFilesDir();
        //}

        File file = new File(mediaStorageDir.getPath() + File.separator + name + ".png");
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static String saveImage(Context context, Bitmap bitmap, String name) {
        /*File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");*/

        /*File mediaStorageDir;
        if (isExternalStorageWritable()) {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/GeoCore/data");
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
        } else {*/
        File mediaStorageDir = context.getFilesDir();
        //}

        File file = new File(mediaStorageDir.getPath() + File.separator + name + ".png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    public static String roundedCornerBitmap(Context context, String path, String name, int size) {
        Log.w(TAG, "make icon image");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        bmp = Bitmap.createScaledBitmap(bmp, size, size, false);
        Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bmp.getWidth();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);

        return saveImage(context, output, name);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight, byte[] rawData) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeByteArray(rawData, 0, rawData.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeByteArray(rawData, 0, rawData.length, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
