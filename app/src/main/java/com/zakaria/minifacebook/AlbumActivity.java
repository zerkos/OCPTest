package com.zakaria.minifacebook;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.zakaria.minifacebook.adapter.PicturesAdapter;
import com.zakaria.minifacebook.model.FacebookAlbums;
import com.zakaria.minifacebook.model.FacebookPictures;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<FacebookPictures> facebookPicturesList = new ArrayList<>();
    public static String ALBUM_OBJ = "ALBUM_OBJ";
    private Toolbar toolbar;
    private RecyclerView ui_recyclerView_picture;
    private PicturesAdapter adapter;
    private FloatingActionButton fab;
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

        FacebookAlbums facebookAlbums = getIntent().getExtras().getParcelable(ALBUM_OBJ);
        GetFacebookImages(facebookAlbums.getId()+"");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Not working
                savePictures(adapter.getFacebookPicturesCheckedList());
            }
        });


    }
    public void GetFacebookImages(final String albumId) {

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

    void savePictures(List<FacebookPictures> list)
    {
        Bitmap bitmap = null;
        for(int i=0 ; i<list.size(); i++)
        {
            Log.e("size",list.size()+" : "+list.get(i).getSource());
            File f=new File(list.get(i).getSource());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(list.get(i).getSource(), options);

            //bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File file = wrapper.getDir("ImagesFB",MODE_PRIVATE);
            file = new File(file, "fb_"+i);
            try{
                OutputStream stream = null;
                stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();
            }
            catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
            }
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
