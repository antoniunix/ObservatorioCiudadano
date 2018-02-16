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
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoPhoto;
import net.gshp.observatoriociudadano.dto.DtoPhoto;
import net.gshp.observatoriociudadano.faceDetection.camera.CameraSourcePreview;
import net.gshp.observatoriociudadano.faceDetection.camera.GraphicOverlay;
import net.gshp.observatoriociudadano.util.Exif;
import net.gshp.observatoriociudadano.util.ImageConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alejandro on 15/01/18.
 */

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private TestShutterCallback mShutterCallback = new TestShutterCallback();
    private JpegPictureCallback mJpegPictureCallback = new JpegPictureCallback();

    private SharedPreferences preferences;
    private String photoPath;
    private boolean shutter = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_photo);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

        //SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy - HH:mm");
        TextView timestamp = (TextView) findViewById(R.id.date);
        //timestamp.setText(df.format(System.currentTimeMillis()).toUpperCase());
        timestamp.setText(getIntent().getStringExtra(getString(R.string.PHOTO_TYPE)));

        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

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
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                String imageName = preferences.getString(getString(R.string.app_share_preference_user_account), "")
                        + "_" + System.currentTimeMillis() + ".jpg";
                File rawOutput = new File(getString(R.string.app_path_photo), imageName);

                FileOutputStream out = new FileOutputStream(rawOutput);
                bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();

                photoPath = rawOutput.getPath();

                DtoPhoto image = new DtoPhoto();

                image.setPath(photoPath);
                image.setName(imageName);
                image.setFace_id(getIntent().getIntExtra(getString(R.string.PICTURE_POSITION), 0));

                new DaoPhoto().insert(image);

                if (mCameraSource != null) {
                    mPreview.stop();
                    mCameraSource.release();
                    mCameraSource = null;
                }

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.PHOTO_PATH), photoPath);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                Log.v(TAG, e.toString());
            }
        }
    }

    @Override
    public void finish() {
        if (mCameraSource != null) {
            mPreview.stop();
            mCameraSource.release();
            mCameraSource = null;
        }

        super.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.PHOTO_PATH), photoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.PHOTO_PATH))) {
                photoPath = savedInstanceState.getString(getString(R.string.PHOTO_PATH));
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
}
