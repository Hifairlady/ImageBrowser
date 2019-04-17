package com.edgar.imagebrowser.DetailReader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edgar.imagebrowser.GlideApp;
import com.edgar.imagebrowser.MyTransformation;
import com.edgar.imagebrowser.R;

import java.io.File;
import java.util.ArrayList;

public class ReaderListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "============" + ReaderListAdapter.class.getName();
    private ArrayList<String> imagePaths = new ArrayList<>();
    private Context mContext;
    private int width;

    public ReaderListAdapter(Context mContext, int width) {
        this.mContext = mContext;
        this.width = width;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_reader_style, viewGroup, false);
        return new NormalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NormalViewHolder vh = (NormalViewHolder) viewHolder;
        GlideApp.with(mContext)
                .load(new File(imagePaths.get(i)))
                .placeholder(R.drawable.loading_bg)
                .error(R.drawable.error_bg)
                .transform(new MyTransformation(width))
                .into(vh.ivImage);
    }

    @Override
    public int getItemCount() {
        return (imagePaths == null ? 0 : imagePaths.size());
    }

    public void addAllPaths(ArrayList<String> imagePaths) {
        this.imagePaths.clear();
        this.imagePaths.addAll(imagePaths);
        notifyDataSetChanged();
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_reader_item);
        }
    }

}
