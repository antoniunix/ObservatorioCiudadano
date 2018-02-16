package net.gshp.observatoriociudadano.faceDetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.faceDetection.camera.GraphicOverlay;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final String TAG = "FaceGraphic";

    private volatile Face mFace;
    private int mFaceId;

    private boolean centered;
    private float centerX;
    private float centerY;
    private int marks;

    private Context context;
    private Canvas mCanvas;
    private Paint ring;

    private float[] points;

    FaceGraphic(GraphicOverlay overlay, Context context) {
        super(overlay, context);

        ring = new Paint();
        ring.setColor(Color.WHITE);
        ring.setStyle(Paint.Style.STROKE);
        ring.setStrokeWidth(3);

        centered = false;
        this.context = context;
    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        mCanvas = canvas;
        Face face = mFace;
        if (face == null) {
            return;
        }

        Paint center = new Paint();
        center.setColor(Color.parseColor("#00FF00"));
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        canvas.drawCircle(centerX, centerY, 6, center);

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, 6, ring);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f) + 10;
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;

        Drawable d = context.getResources().getDrawable(R.drawable.fondo_reco);
        d.setBounds((int) left, (int) top, (int) right, (int) bottom + 10);
        d.draw(canvas);

        marks = face.getLandmarks().size();

        for (Landmark landmark : face.getLandmarks()) {
            float cx = translateX(landmark.getPosition().x);
            float cy = translateY(landmark.getPosition().y);
            canvas.drawCircle(cx, cy, 6, ring);

            if (landmark.getType() == Landmark.NOSE_BASE)
                checkCenter(cx, cy);

            /*Drawable lm = context.getResources().getDrawable(R.drawable.land_mark);
            lm.setBounds((int)cx, (int)cy, (int)cx+10, (int)cy+10);
            lm.draw(canvas);*/
        }
    }

    @Override
    public boolean centered() {
        return centered;
    }

    @Override
    public Canvas getCanvas() {
        points = new float[8 * 8 * 4];
        int index = 0;
        for (Landmark landmark : mFace.getLandmarks()) {
            float cx = translateX(landmark.getPosition().x);
            float cy = translateY(landmark.getPosition().y);

            for (Landmark landmark1 : mFace.getLandmarks()) {
                float cx1 = translateX(landmark1.getPosition().x);
                float cy1 = translateY(landmark1.getPosition().y);

                points[index] = cx;
                points[index + 1] = cy;
                points[index + 2] = cx1;
                points[index + 3] = cy1;
                index += 4;
            }
        }

        return mCanvas;
    }

    @Override
    public void animate(Canvas canvas) {
        ring.setStrokeWidth(1);
        canvas.drawLines(points, ring);
    }

    private void checkCenter(float cx, float cy) {
        float diffX = Math.abs((centerX - cx));
        float diffY = Math.abs((centerY - cy));

        centered = (diffX <= 20) && (diffY <= 20) && marks == 8;
    }
}
