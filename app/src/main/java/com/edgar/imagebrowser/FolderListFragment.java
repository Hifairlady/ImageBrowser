package com.edgar.imagebrowser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class FolderListFragment extends Fragment {
    private static final String REQUEST_PATH = "REQUEST_PATH";
    private static final String TAG = "=============" + FolderListFragment.class.getName();
    private String requestPath;
    private RecyclerView rvFolderList;
    private ArrayList<String> folderPathList = new ArrayList<>();
    private TextView tvCurAbsPath;

    public FolderListFragment() {
        // Required empty public constructor
    }

    public static FolderListFragment newInstance(String requestPath) {
        FolderListFragment fragment = new FolderListFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_PATH, requestPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestPath = getArguments().getString(REQUEST_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);

        tvCurAbsPath = view.findViewById(R.id.tv_curAbsPath);
        Log.d(TAG, "onCreateView: " + requestPath);

        folderPathList.clear();
        if (requestPath == null) return view;
        tvCurAbsPath.setText(requestPath);
        File curDir = new File(requestPath);
        if (!curDir.exists() || !curDir.isDirectory()) {
            Objects.requireNonNull(getActivity()).finish();
            return view;
        }

        File[] childFolders = curDir.listFiles();
        for (File folder : childFolders) {
            if (folder.isDirectory() && !folder.getName().startsWith(".")) {
                folderPathList.add(folder.getName());
            }
        }
        Collections.sort(folderPathList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        rvFolderList = view.findViewById(R.id.rv_folder_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvFolderList.setLayoutManager(layoutManager);
        FolderListAdapter adapter = new FolderListAdapter(folderPathList);
        rvFolderList.setAdapter(adapter);

        return view;
    }
}
