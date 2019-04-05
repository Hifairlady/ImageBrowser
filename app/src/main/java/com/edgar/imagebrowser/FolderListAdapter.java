package com.edgar.imagebrowser;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter {

    private ArrayList<String> folderNameList;
    private MyOnItemClickListener mOnItemClickListener;

    public FolderListAdapter(ArrayList<String> folderNameList) {
        this.folderNameList = folderNameList;
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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
        final NormalViewHolder vh = (NormalViewHolder) viewHolder;
        vh.tvFolderPath.setText(folderNameList.get(i));
        vh.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, vh.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (folderNameList == null ? 0 : folderNameList.size());
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFolderPath;
        private ConstraintLayout itemLayout;

        private NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFolderPath = itemView.findViewById(R.id.tv_folderPathName);
            itemLayout = itemView.findViewById(R.id.item_container_layout);
        }
    }

}
