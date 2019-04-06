package com.edgar.imagebrowser.SelectPath;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edgar.imagebrowser.MyUtils;
import com.edgar.imagebrowser.R;

import java.io.File;
import java.util.ArrayList;

public class FolderListFragment extends Fragment {
    private static final String REQUEST_PATH = "REQUEST_PATH";
    private static final String FIRST_POSITION = "FIRST_POSITION";
    private static final String TAG = "=============" + FolderListFragment.class.getName();
    private String requestPath;
    private int firstPosition = 0;
    private RecyclerView rvFolderList;
    private ArrayList<String> folderPathList = new ArrayList<>();
    private TextView tvCurAbsPath;
    private FolderListAdapter adapter;
    private CallbackValue mCallbackValue;

    public FolderListFragment() {
        // Required empty public constructor
    }

    public static FolderListFragment newInstance(String requestPath, int firstPosition) {
        FolderListFragment fragment = new FolderListFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_PATH, requestPath);
        args.putInt(FIRST_POSITION, firstPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestPath = getArguments().getString(REQUEST_PATH);
            firstPosition = getArguments().getInt(FIRST_POSITION, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);

        tvCurAbsPath = view.findViewById(R.id.tv_curAbsPath);
        tvCurAbsPath.setText("");

        rvFolderList = view.findViewById(R.id.rv_folder_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvFolderList.setLayoutManager(layoutManager);
        adapter = new FolderListAdapter(folderPathList);
        rvFolderList.setAdapter(adapter);

        adapter.setOnItemClickListener(new FolderListAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String nextPath;
                if (folderPathList.get(position).equals("...") && position == 0) {
                    File file = new File(requestPath);
                    nextPath = file.getParentFile().getAbsolutePath();
                } else {
                    nextPath = requestPath + "/" + folderPathList.get(position);
                }
                int firstPosition = ((LinearLayoutManager) rvFolderList.getLayoutManager()).findFirstVisibleItemPosition();
                mCallbackValue.onValueCallback(nextPath, requestPath, firstPosition);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (requestPath == null) return;
        tvCurAbsPath.setText(requestPath);
        new AddPathTask().execute(requestPath);
    }

    public void setCallbackValue(CallbackValue callbackValue) {
        this.mCallbackValue = callbackValue;
    }

    public interface CallbackValue {
        void onValueCallback(String nextPath, String curPath, int position);
    }

    private class AddPathTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            folderPathList.clear();
            String filePath = strings[0];
            File curDir = new File(filePath);
            if (!curDir.exists() || !curDir.isDirectory()) return null;

            File[] childFolders = curDir.listFiles();
            if (!requestPath.equals(SelectPathActivity.ROOT_PATH)) {
                folderPathList.add(0, "...");
            }

            for (File folder : childFolders) {
                if (folder.isDirectory() && !folder.getName().startsWith(".")) {
                    folderPathList.add(folder.getName());
                }
            }
            MyUtils.sortStringList(folderPathList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            rvFolderList.scrollToPosition(firstPosition);
        }
    }

}
