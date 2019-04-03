package com.edgar.imagebrowser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter {

    private ArrayList<String> folderNameList;

    public FolderListAdapter(ArrayList<String> folderNameList) {
        this.folderNameList = folderNameList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_list_style,
                viewGroup, false);
        return new NormalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NormalViewHolder vh = (NormalViewHolder) viewHolder;
        vh.tvFolderPath.setText(folderNameList.get(i));
    }

    @Override
    public int getItemCount() {
        return (folderNameList == null ? 0 : folderNameList.size());
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFolderPath;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFolderPath = itemView.findViewById(R.id.tv_folderPathName);
        }
    }

}
