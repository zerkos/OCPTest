package com.zakaria.minifacebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zakaria.minifacebook.AlbumActivity;
import com.zakaria.minifacebook.R;
import com.zakaria.minifacebook.model.FacebookAlbums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zakaria on 17-07-10.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolders>  {

    private Context context;
    private List<FacebookAlbums> facebookAlbumsList = new ArrayList<>();

    public AlbumsAdapter(Context context, List<FacebookAlbums> facebookAlbumsList) {
        this.context = context;
        this.facebookAlbumsList = facebookAlbumsList;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card,parent, false);
        return new MyViewHolders(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolders holder, final int position) {
        holder.ui_textView_name_albums.setText(facebookAlbumsList.get(position).getName());
    //    holder.ui_textView_name_albums.setTag(facebookAlbumsList.get(position));

        holder.ui_textView_name_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(AlbumActivity.ALBUM_OBJ,facebookAlbumsList.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return facebookAlbumsList.size();
    }

    public class MyViewHolders extends RecyclerView.ViewHolder{
        TextView ui_textView_name_albums;
        public MyViewHolders(View itemView) {
            super(itemView);
            ui_textView_name_albums = (TextView) itemView.findViewById(R.id.text_view_name_album);
        }
    }


}
