package net.gshp.observatoriociudadano.faceDetection;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import net.gshp.observatoriociudadano.Home;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoImageLogin;
import net.gshp.observatoriociudadano.dto.DtoImageLogin;
import net.gshp.observatoriociudadano.faceDetection.camera.CameraSourcePreview;
import net.gshp.observatoriociudadano.faceDetection.camera.GraphicOverlay;
import net.gshp.observatoriociudadano.util.Exif;
import net.gshp.observatoriociudadano.util.ImageConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FaceDetectionActivity extends AppCompatActivity {
    private static final String TAG = "FaceDetectionActivity";
    private static final String PHOTO_PATH = "PHOTO_PATH";

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    //private FaceGraphic mFaceGraphic;
    //private GraphicOverlay mOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private TestShutterCallback mShutterCallback = new TestShutterCallback();
    private JpegPictureCallback mJpegPictureCallback = new JpegPictureCallback();
    private boolean shutter = false;

    private SharedPreferences preferences;
    private String photoPath;
    private String imageName;

    private ImageView myPhoto;
    private ProgressBar progress;
    private RelativeLayout progressView;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_face_detection);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy - HH:mm");
        TextView timestamp = (TextView) findViewById(R.id.date);
        timestamp.setText(df.format(System.currentTimeMillis()).toUpperCase());

        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        progressView = findViewById(R.id.progress_view);
        myPhoto = findViewById(R.id.my_photo);
        progress = findViewById(R.id.progress);
        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.faceOverlay);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private final class TestShutterCallback implements CameraSource.ShutterCallback {
        @Override
        public void onShutter() {
            shutter = true;

            //AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }

    private final class JpegPictureCallback implements CameraSource.PictureCallback {
        @Override
        public void onPictureTaken(byte[] rawData) {
            int orientation = Exif.getOrientation(rawData);
            Bitmap bitmap = BitmapFactory.decodeByteArray(rawData, 0, rawData.length);

            Bitmap bitmapPicture = bitmap;
            switch (orientation) {
                case 90:
                    bitmapPicture = ImageConverter.rotateImage(bitmap, 90);

                    break;
                case 180:
                    bitmapPicture = ImageConverter.rotateImage(bitmap, 180);

                    break;
                case 270:
                    bitmapPicture = ImageConverter.rotateImage(bitmap, 270);

                    break;
            }

            try {
                imageName = preferences.getString(getString(R.string.app_share_preference_user_account), "")
                        + "_" + System.currentTimeMillis() + ".jpg";
                File rawOutput = new File(getString(R.string.app_path_photo), imageName);

                FileOutputStream out = new FileOutputStream(rawOutput);
                bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();

                photoPath = rawOutput.getPath();
                saveImage();

                ImageConverter.roundedCornerBitmap(FaceDetectionActivity.this, photoPath, "myPhoto",
                        preferences.getInt(getString(R.string.IMAGE_SIZE), 60));
                myPhoto.setImageBitmap(ImageConverter.getBitmap(FaceDetectionActivity.this, "myPhoto"));
                progressView.setVisibility(View.VISIBLE);

                if (mCameraSource != null) {
                    mPreview.stop();
                    mCameraSource.release();
                    mCameraSource = null;
                }

                //final Canvas canvas = mFaceGraphic.getCanvas();
                //sendImage();

                progress.setProgress(10);
                finishProcess();

            } catch (Exception e) {
                Log.v(TAG, e.toString());
            }
        }
    }

    private void finishProcess() {
        new Thread(new Runnable() {
            int i = 0;
            int progressStatus = 10;

            public void run() {
                while (progressStatus < 100) {
                    progressStatus += doWork();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            //mFaceGraphic.animate(canvas);
                            //mOverlay.invalidate();
                            progress.setProgress(progressStatus);
                            i++;
                        }
                    });
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        //intent.putExtra(Constants.PHOTO_PATH, path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }

            private int doWork() {
                return i * 3;
            }

        }).start();
    }

    @Override
    public void finish() {
        if (mCameraSource != null) {
            mPreview.stop();
            mCameraSource.release();
            mCameraSource = null;
        }

        startActivity(new Intent(this, Home.class));

        super.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHOTO_PATH, photoPath);
        //outState.putInt(Constants.GEO_TYPE, geoType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PHOTO_PATH)) {
                photoPath = savedInstanceState.getString(PHOTO_PATH);
                myPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                //geoType = savedInstanceState.getInt(Constants.GEO_TYPE);
            }
        }
    }

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay, getApplicationContext());
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
            if (mFaceGraphic.centered()) {
                if (!shutter) {
                    //AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    //mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                    shutter = true;
                    mCameraSource.takePicture(mShutterCallback, mJpegPictureCallback);
                }
            }
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    private void saveImage() {
        DtoImageLogin image = new DtoImageLogin(photoPath, imageName, 0, 1);
        new DaoImageLogin().insertOrReplace(image);
    }

    /*private void sendImage() {
        ImageSender imageSender = new ImageSender(image);
        try {
            imageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onBackPressed() {
    }
}