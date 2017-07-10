package com.zakaria.minifacebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.zakaria.minifacebook.adapter.AlbumsAdapter;
import com.zakaria.minifacebook.model.FacebookAlbums;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListAlbumsActivity extends AppCompatActivity {

    private RecyclerView ui_recyclerView_albums;
    private AlbumsAdapter adapter;
    private ArrayList<FacebookAlbums> allFBAlbum = new ArrayList<>();
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_albums);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Albums");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        String user_id = (String) getIntent().getExtras().get(LoginFragment.USER_ID);
        callAPiGraph(user_id);

    }

    List<FacebookAlbums> callAPiGraph(String idUser)
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + idUser + "/albums", //user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObjectResponse = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (jsonObjectResponse.has("data")) {
                                    JSONArray jsonArrayData = jsonObjectResponse.optJSONArray("data"); //find JSONArray from JSONObject

                                    for (int i = 0; i < jsonArrayData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject jsonObjectAlbum = jsonArrayData.getJSONObject(i); //convert perticular album into JSONObject
                                        allFBAlbum.add(new FacebookAlbums(jsonObjectAlbum.optString("created_time"),
                                                jsonObjectAlbum.optString("name"),jsonObjectAlbum.optString("id")));
                                    }
                                    loadRecycleView();
                                }
                            } else {
                                Log.e("errorGraph", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
        return allFBAlbum;
    }

    void loadRecycleView()
    {
        ui_recyclerView_albums = (RecyclerView) findViewById(R.id.recycler_view_albums);

        adapter = new AlbumsAdapter(this, allFBAlbum);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        ui_recyclerView_albums.setLayoutManager(mLayoutManager);
        ui_recyclerView_albums.setItemAnimator(new DefaultItemAnimator());
        ui_recyclerView_albums.setAdapter(adapter);
    }
}
