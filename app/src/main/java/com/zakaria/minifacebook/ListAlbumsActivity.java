package com.zakaria.minifacebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.zakaria.minifacebook.adapter.AlbumsAdapter;
import com.zakaria.minifacebook.model.FacebookAlbums;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.facebook.AccessToken.getCurrentAccessToken;

public class ListAlbumsActivity extends AppCompatActivity {

    private RecyclerView ui_recyclerView_albums;
    private AlbumsAdapter adapter;
    private ArrayList<FacebookAlbums> allFBAlbum = new ArrayList<>();
    private Toolbar toolbar;
    private CallbackManager callbackManager;
    private ProgressBar ui_progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_albums);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Albums");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ui_progressBar = (ProgressBar) findViewById(R.id.progressBar_list_album);

        String user_id = (String) getIntent().getExtras().get(LoginFragment.USER_ID);
        checkPermissionFacebook(user_id);
        callAPiGraph(user_id);


    }

    void checkPermissionFacebook(String user_id)
    {
        new GraphRequest(
                getCurrentAccessToken(),
                "/"+user_id+"/permissions",
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
                                        if(jsonObjectAlbum.optString("permission").equals("user_photos")
                                                && jsonObjectAlbum.optString("status").equals("declined")) {
                                            LoginManager.getInstance().logInWithReadPermissions(ListAlbumsActivity.this, Arrays.asList("user_photos"));
                                        }
                                    }
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
    }
    void callAPiGraph(final String idUser)
    {
        ui_progressBar.setVisibility(View.VISIBLE);
        new GraphRequest(
                getCurrentAccessToken(),  //your fb AccessToken
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
                               /*     if(jsonArrayData.length()==0)
                                        checkPermissionFacebook(idUser);
*/
                                    for (int i = 0; i < jsonArrayData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject jsonObjectAlbum = jsonArrayData.getJSONObject(i); //convert perticular album into JSONObject
                                        allFBAlbum.add(new FacebookAlbums(jsonObjectAlbum.optString("created_time"),
                                                jsonObjectAlbum.optString("name"),jsonObjectAlbum.optString("id")));
                                    }
                                    ui_progressBar.setVisibility(View.GONE);
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
