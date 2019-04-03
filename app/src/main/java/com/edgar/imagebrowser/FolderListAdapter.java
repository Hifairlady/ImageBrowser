package com.edgar.imagebrowser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FolderListAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
