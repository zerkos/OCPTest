package com.zakaria.minifacebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zakaria.minifacebook.DisplayPictureActivity;
import com.zakaria.minifacebook.R;
import com.zakaria.minifacebook.model.FacebookPictures;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zakaria on 17-07-10.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.MyViewHolders>  {

    private Context context;
    private List<FacebookPictures> facebookPictureList = new ArrayList<>();

    public List<FacebookPictures> getFacebookPicturesCheckedList() {
        return facebookPicturesCheckedList;
    }

    private List<FacebookPictures> facebookPicturesCheckedList = new ArrayList<>();
    public PicturesAdapter(Context context, List<FacebookPictures> facebookPictureList) {
        this.context = context;
        this.facebookPictureList = facebookPictureList;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_card,parent, false);
        return new MyViewHolders(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolders holder, final int position) {

        Glide
                .with(context)
                .load(facebookPictureList.get(position).getSource())
                .centerCrop()
                .into(holder.ui_imageView_pictures);

        holder.ui_checkbox_select_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    facebookPicturesCheckedList.add(facebookPictureList.get(position));
                }else{
                    facebookPicturesCheckedList.remove(facebookPictureList.get(position));
                }

            }
        });

        holder.ui_imageView_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisplayPictureActivity.class);
                intent.putExtra(DisplayPictureActivity.PICTURE,facebookPictureList.get(position).getSource());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return facebookPictureList.size();
    }

    public class MyViewHolders extends RecyclerView.ViewHolder{
        ImageView ui_imageView_pictures;
        CheckBox ui_checkbox_select_picture;
        public MyViewHolders(View itemView) {
            super(itemView);
            ui_imageView_pictures = (ImageView) itemView.findViewById(R.id.image_view_picture);
            ui_checkbox_select_picture = (CheckBox) itemView.findViewById(R.id.checkbox_selectPicture);
        }
    }



}
