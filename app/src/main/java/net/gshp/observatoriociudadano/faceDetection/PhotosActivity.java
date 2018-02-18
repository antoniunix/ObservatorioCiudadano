package net.gshp.observatoriociudadano.faceDetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.gshp.observatoriociudadano.Home;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
import net.gshp.observatoriociudadano.dao.DaoPhoto;
import net.gshp.observatoriociudadano.dto.DtoBundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos);
        getSupportActionBar().hide();

        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
        TextView timestamp = findViewById(R.id.date);
        timestamp.setText(df.format(System.currentTimeMillis()).toUpperCase());

        if (getIntent().getIntExtra(getString(R.string.user_roll),
                getResources().getInteger(R.integer.rollSupervisor)) == getResources().getInteger(R.integer.rollSupervisor))
            rol = getResources().getInteger(R.integer.rollSupervisor);
        else
            rol = getResources().getInteger(R.integer.rollRepresentanteCasilla);

        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        Log.w(TAG, "rol: " + rol);

        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        prepareAlbums();
        adapter = new PhotosAdapter(pictureList);

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

    @Override
    protected void onResume() {
        prepareAlbums();
        adapter = new PhotosAdapter(pictureList);
        super.onResume();
    }

    private void saveAndCheck() {
        int minPhotos = pictureList.size();
        int takenPhotos = 0;

        for (Photo p : pictureList) {
            if (!p.getPicture().isEmpty()) {
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
                R.drawable.cara2,
                R.drawable.cara3,
                R.drawable.cara4,
                R.drawable.cara5,
                R.drawable.cara7,
                R.drawable.cara8,
                R.drawable.cara9};

        Photo a = new Photo("Enojado", covers[0], "");
        pictureList.add(a);

        a = new Photo("Viendo a lado", covers[1], "");
        pictureList.add(a);

        a = new Photo("Serio de Frente", covers[2], "");
        pictureList.add(a);

        a = new Photo("Sonriendo enseñando dientes", covers[3], "");
        pictureList.add(a);

        a = new Photo("Ojos cerrados", covers[4], "");
        pictureList.add(a);

        a = new Photo("Sonriendo", covers[5], "");
        pictureList.add(a);

        a = new Photo("Frunciendo ceño", covers[6], "");
        pictureList.add(a);

        if (rol == getResources().getInteger(R.integer.rollSupervisor)) {
            userName = preferences.getString(getString(R.string.app_share_preference_user_account), "");
            Log.w(TAG, "userName: " + userName);
            List<DtoPhoto> pictures = new DaoPhoto().selectAll(userName);

            for (DtoPhoto picture : pictures) {
                pictureList.get(picture.getFace_id()).setPicture(picture.getPath());
            }
        } else {
            userName = new DaoEARespuesta().selectUserName(1, 2, dtoBundle.getIdReportLocal());
            Log.w(TAG, "userName: " + userName);
            List<DtoPhoto> pictures = new DaoPhoto().selectAll(userName);

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
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.thumbnail.setImageBitmap(myBitmap);
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
                    Log.w(TAG, "photo id: " + data.getStringExtra(getString(R.string.PHOTO_PATH)));
                    Photo photo = pictureList.get(photoPosition);
                    photo.setPicture(data.getStringExtra(getString(R.string.PHOTO_PATH)));

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

    /*public class PhotoSender extends AsyncTask<Void, Void, Response> {
        private static final String TAG = "PhotoSender";

        private String json;
        private long pictureId;
        private FileData data;
        private DaoSession daoSession;
        private String path;
        private DtoPhoto image;
        private Photo photo;

        PhotoSender(long pictureId) {
            this.pictureId = pictureId;
            daoSession = DaoService.getInstance().getSession();
        }

        @Override
        protected void onPreExecute() {
            Log.w(TAG, "image id: " + pictureId);
            image = daoSession.getPictureDao().load(pictureId);

            if (image != null) {
                photo = pictureList.get(photoPosition);
                photo.setPicture(image.getPath());
                adapter.notifyItemChanged(photoPosition);
                //adapter.notifyDataSetChanged();

                File file = new File(image.getPath());

                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                data = new FileData(file.getName(), bytes);

                json = new Gson().toJson(image);
            }

            path = daoSession.getSendListDao().queryBuilder().where(SendListDao.Properties.Path_name.eq("send_photo"))
                    .unique().getUrl() + preferences.getString(Constants.PROJECT_NAME, "")
                    + "/" + preferences.getString(Constants.COP_PLATE, "") + "/";
            //path = "http://172.16.41.77:80/gosharp/recognition/init-load/" ;
        }

        @Override
        protected Response doInBackground(Void... params) {
            if (image != null) {
                Request request = new Request
                        .RequestBuilder(Request.Type.POST, path)
                        .addJson(json)
                        .addFileData(data)
                        .build();

                Log.w(TAG, json);

                return NetworkService.getInstance().send(request);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            if (image != null && response != null) {
                Log.w(TAG, response.getStatusCode() + " O_o " + response.getResponse());

                JsonParser parser = new JsonParser();
                JsonObject data = parser.parse(response.getResponse()).getAsJsonObject();

                if (data.get("status").getAsString().equals("OK") && response.getStatusCode() == 201) {
                    image.setSent(true);
                    daoSession.getPictureDao().update(image);
                    photo.setSent(true);
                    photo.setPicture(image.getPath());

                    adapter.notifyItemChanged(photoPosition);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }*/

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
