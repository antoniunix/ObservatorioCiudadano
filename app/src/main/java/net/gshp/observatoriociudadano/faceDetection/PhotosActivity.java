package net.gshp.observatoriociudadano.faceDetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
import net.gshp.observatoriociudadano.dao.DaoPhoto;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoEARespuesta;
import net.gshp.observatoriociudadano.dto.DtoPhoto;
import net.gshp.observatoriociudadano.faceDetection.adapters.PhotoItem;
import net.gshp.observatoriociudadano.faceDetection.models.Photo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {

    private static final String TAG = "PhotosActivity";
    private static final int PICTURE_REQUEST_CODE = 2001;
    private static final int PHOTO_REQUEST_CODE = 2000;

    private PhotosAdapter adapter;
    private List<Photo> pictureList = new ArrayList<>();
    private int photoPosition;

    private SharedPreferences preferences;
    private int rol;
    private DtoBundle dtoBundle;
    private String userName;
    private String hash;
    private int placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos);
        getSupportActionBar().hide();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        TextView timestamp = findViewById(R.id.date);
        timestamp.setText(df.format(System.currentTimeMillis()).toUpperCase());

        if (getIntent().getIntExtra(getString(R.string.user_roll),
                getResources().getInteger(R.integer.rollSupervisor)) == getResources().getInteger(R.integer.rollSupervisor))
            rol = getResources().getInteger(R.integer.rollSupervisor);
        else
            rol = getResources().getInteger(R.integer.rollRepresentanteCasilla);

        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));

        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        prepareAlbums();
        adapter = new PhotosAdapter(pictureList);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndCheck();
            }
        });
    }

    private void initGrid() {
    }

    @Override
    protected void onResume() {
        //prepareAlbums();
        //adapter = new PhotosAdapter(pictureList);
        super.onResume();
    }

    private void saveAndCheck() {
        int minPhotos = pictureList.size();
        int takenPhotos = 0;

        for (Photo p : pictureList) {
            if (p.getPicture() != null && !p.getPicture().isEmpty()) {
                takenPhotos++;
            }
        }

        int missingPhotos = minPhotos - takenPhotos;

        if (takenPhotos < minPhotos) {
            if (missingPhotos > 1)
                Toast.makeText(this, "Faltan " + missingPhotos + " fotos", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Falta " + missingPhotos + " foto", Toast.LENGTH_SHORT).show();
        } else {
            //Intent intent = new Intent(this, Home.class);
            //startActivity(intent);
            finish();
        }
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        int[] covers = new int[]{
                R.drawable.c1,
                R.drawable.c2,
                R.drawable.c3,
                R.drawable.c4,
                R.drawable.c5};

        Photo a = new Photo("FRUNCIENDO CEÑO", covers[0], "");
        pictureList.add(a);

        a = new Photo("TRISTE", covers[1], "");
        pictureList.add(a);

        a = new Photo("SORPRENDIDO", covers[2], "");
        pictureList.add(a);

        a = new Photo("ENOJADO", covers[3], "");
        pictureList.add(a);

        a = new Photo("SONRIENDO", covers[4], "");
        pictureList.add(a);

        if (rol == getResources().getInteger(R.integer.rollSupervisor)) {
            DtoEARespuesta respuesta = new DaoEARespuesta().selectUserName(1, 1, dtoBundle.getIdReportLocal());
            userName = respuesta.getRespuesta();
            hash = respuesta.getHash();
            placeId = respuesta.getPdv();

            List<DtoPhoto> pictures = new DaoPhoto().selectAll(userName.replaceAll("\\s+", ""));

            for (DtoPhoto picture : pictures) {
                pictureList.get(picture.getFace_id()).setPicture(picture.getPath());
            }
        } else {
            DtoEARespuesta respuesta = new DaoEARespuesta().selectUserName(8, 2, dtoBundle.getIdReportLocal());
            userName = respuesta.getRespuesta();
            hash = respuesta.getHash();
            placeId = respuesta.getPdv();

            List<DtoPhoto> pictures = new DaoPhoto().selectAll(userName.replaceAll("\\s+", ""));
            Log.w(TAG, userName);

            for (DtoPhoto picture : pictures) {
                pictureList.get(picture.getFace_id()).setPicture(picture.getPath());
            }
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void openCamera(String name, int index) {
        Intent intent = new Intent(this, FaceDetectionActivity.class);
        intent.putExtra(getString(R.string.PHOTO_TYPE), name);
        intent.putExtra(getString(R.string.PICTURE_POSITION), index);
        intent.putExtra(getString(R.string.is_reco), false);
        intent.putExtra(getString(R.string.user_roll), rol);
        intent.putExtra("userName", userName);
        intent.putExtra("hash", hash);
        intent.putExtra("placeId", placeId);
        intent.putExtra(getString(R.string.app_bundle_name), dtoBundle);
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    private class PhotosAdapter extends RecyclerView.Adapter<PhotoItem> {

        private List<Photo> photoList;

        PhotosAdapter(List<Photo> photoList) {
            this.photoList = photoList;
        }

        @Override
        public PhotoItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_card, parent, false);

            return new PhotoItem(itemView);
        }

        @Override
        public void onBindViewHolder(final PhotoItem holder, int position) {
            final Photo photo = photoList.get(position);
            holder.title.setText(photo.getName());
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoPosition = holder.getAdapterPosition();
                    openCamera(photo.getName(), photoPosition);
                }
            });

            if (photo.getPicture() != null && !photo.getPicture().isEmpty()) {
                File imgFile = new File(photo.getPicture());

                if (imgFile.exists()) {
                    //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //holder.thumbnail.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 80, 120));
                    Picasso.with(getApplicationContext()).load(new File(imgFile.getAbsolutePath())).into(holder.thumbnail);
                }
            } else {
                holder.thumbnail.setImageResource(photo.getThumbnail());
            }
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                if (data.hasExtra(getString(R.string.PHOTO_PATH))) {
                    //new PhotoSender(data.getLongExtra(Constants.PHOTO_ID, 0)).execute();
                    Photo photo = pictureList.get(photoPosition);
                    photo.setPicture(data.getStringExtra(getString(R.string.PHOTO_PATH)));
                    photo.setRotation(data.getIntExtra("rotation", 0));

                    adapter.notifyItemChanged(photoPosition);
                    adapter.notifyDataSetChanged();
                }
            }

            if (requestCode == PHOTO_REQUEST_CODE) {
                if (data.hasExtra(getString(R.string.PHOTO_PATH)) && !data.getStringExtra(getString(R.string.PHOTO_PATH)).isEmpty()) {
                    //DaoService.getInstance().getSession().getPictureDao().deleteAll();

                    finish();
                    //startActivity(new Intent(this, MainActivity.class));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("POSITION", photoPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("POSITION")) {
                photoPosition = savedInstanceState.getInt("POSITION");
            }
        }
    }
}
