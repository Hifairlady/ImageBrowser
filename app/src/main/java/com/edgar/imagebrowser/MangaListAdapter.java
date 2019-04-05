package com.edgar.imagebrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MangaListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<MangaItem> mangaItems = new ArrayList<>();

    public MangaListAdapter(Context mContext, ArrayList<MangaItem> mangaItems) {
        this.mContext = mContext;
        this.mangaItems = mangaItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_manga_list_style,
                viewGroup, false);
        return new NormalViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) viewHolder;
        normalViewHolder.tvTitle.setText(mangaItems.get(i).getTitleString());
        String coverName = mangaItems.get(i).getCoverName();
        if (coverName != null) {
            String coverPath = mangaItems.get(i).getUrlString() + "/" + coverName;
            File coverFile = new File(coverPath);
            GlideApp.with(mContext)
                    .load(coverFile)
                    .placeholder(R.drawable.loading_bg)
                    .error(R.drawable.error_bg)
                    .into(normalViewHolder.ivCoverImage);
        } else {
            normalViewHolder.ivCoverImage.setImageResource(R.drawable.empty_bg);
        }
    }

    @Override
    public int getItemCount() {
        return (mangaItems == null ? 0 : mangaItems.size());
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCoverImage;
        private TextView tvTitle;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCoverImage = itemView.findViewById(R.id.iv_cover_image);
            tvTitle = itemView.findViewById(R.id.tv_manga_title);
        }
    }

}
