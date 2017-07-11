package com.zakaria.minifacebook;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.zakaria.minifacebook.adapter.PicturesAdapter;
import com.zakaria.minifacebook.model.FacebookAlbums;
import com.zakaria.minifacebook.model.FacebookPictures;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<FacebookPictures> facebookPicturesList = new ArrayList<>();
    public static String ALBUM_OBJ = "ALBUM_OBJ";
    private Toolbar toolbar;
    private RecyclerView ui_recyclerView_picture;
    private PicturesAdapter adapter;
    private FloatingActionButton fab;
    public static int WRITE_EXTERNAL_STORAGE = 123;
    private FacebookAlbums facebookAlbums;
    private ProgressBar ui_progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Pictures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ui_progressBar = (ProgressBar) findViewById(R.id.progressBar_album);

        facebookAlbums = getIntent().getExtras().getParcelable(ALBUM_OBJ);
        GetFacebookImages(facebookAlbums.getId()+"");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Not working
                addImageToGallery(adapter.getFacebookPicturesCheckedList());
            }
        });
        checkPermission();

    }
    public void GetFacebookImages(final String albumId) {
        ui_progressBar.setVisibility(View.VISIBLE);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                    /* handle the result */
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObject = response.getJSONObject();
                                if (jsonObject.has("data")) {
                                    JSONArray jsonArrayData = jsonObject.optJSONArray("data");
                                    List<?>lstFBImages = new ArrayList<>();
                                    for (int i = 0; i < jsonArrayData.length(); i++)
                                    {//Get no. of images
                                        JSONObject jsonObjectAlbum = jsonArrayData.getJSONObject(i);
                                        JSONArray jsonArrayImage=jsonObjectAlbum.getJSONArray("images");
                                        if(jsonArrayImage.length()>0)
                                        {
                                            facebookPicturesList.add(
                                                    new FacebookPictures(jsonArrayImage.getJSONObject(0).getInt("height"),
                                                            jsonArrayImage.getJSONObject(0).getInt("width"),
                                                    jsonArrayImage.getJSONObject(0).getString("source")));
                                        }
                                    }
                                    ui_progressBar.setVisibility(GONE);
                                    loadRecycleView();
                                }
                        } else {
                            Log.e("errorGraph", response.getError().toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    }
        ).executeAsync();

    }
    void loadRecycleView()
    {
        ui_recyclerView_picture = (RecyclerView) findViewById(R.id.recycler_view_picture);

        adapter = new PicturesAdapter(this, facebookPicturesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        ui_recyclerView_picture.setLayoutManager(mLayoutManager);
        ui_recyclerView_picture.setItemAnimator(new DefaultItemAnimator());
        ui_recyclerView_picture.setAdapter(adapter);
    }

    public void addImageToGallery(List<FacebookPictures> list) {
        for(int i=0 ; i<list.size(); i++) {

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, list.get(i).getSource());

            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Toast.makeText(AlbumActivity.this,"Images sauvgarder",Toast.LENGTH_SHORT).show();
            GetFacebookImages(facebookAlbums.getId()+"");
        }
    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(AlbumActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    AlbumActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    addImageToGallery(adapter.getFacebookPicturesCheckedList());
                }
                break;

            default:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
