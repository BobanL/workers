package com.example.boban.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class RecyclerAdapter_Image extends RecyclerView.Adapter<RecyclerAdapter_Image.ViewHolder>{
    private ArrayList<Bitmap> imageSrc = new ArrayList<>();
    private LayoutInflater mInflater;
    // data is passed into the constructor

    public RecyclerAdapter_Image(Context context, ArrayList<Bitmap> imageSrc) {
        this.mInflater = LayoutInflater.from(context);
        this.imageSrc = imageSrc;
    }
    // inflates the row layout from xml when needed

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_Image.ViewHolder viewHolder, int i) {
        viewHolder.myView.setImageBitmap(imageSrc.get(i));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return imageSrc.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView myView;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = (ImageView) itemView.findViewById(R.id.jobImageRecycler);
        }
    }
}
