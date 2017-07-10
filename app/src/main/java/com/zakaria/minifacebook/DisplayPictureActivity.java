package com.zakaria.minifacebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DisplayPictureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static String PICTURE = "picture";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_picture);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView ui_imageView = (ImageView) findViewById(R.id.image_view_picture_display);

        String facebookPictures = (String) getIntent().getExtras().get(PICTURE);
//        Log.e("pic",facebookPictures.getSource());
        Glide
                .with(this)
                .load(facebookPictures)
                .centerCrop()
                .into(ui_imageView);

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
