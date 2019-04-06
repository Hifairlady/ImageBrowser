package com.edgar.imagebrowser.DetailReader;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgar.imagebrowser.MyUtils;
import com.edgar.imagebrowser.R;

import java.io.File;
import java.util.ArrayList;


public class ReaderFragment extends Fragment {

    private static final String URL_STRING = "URL_STRING";
    private static final String TAG = "============" + ReaderFragment.class.getName();

    private String urlString;
    private RecyclerView rvReader;
    private ReaderListAdapter adapter;

    public ReaderFragment() {
        // Required empty public constructor
    }


    public static ReaderFragment newInstance(String urlString) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle args = new Bundle();
        args.putString(URL_STRING, urlString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlString = getArguments().getString(URL_STRING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);
        rvReader = rootView.findViewById(R.id.rv_reader);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvReader.setLayoutManager(layoutManager);
        adapter = new ReaderListAdapter(getActivity());
        rvReader.setAdapter(adapter);
        rvReader.setItemAnimator(null);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (urlString == null) return;
        File curDir = new File(urlString);
        if (!curDir.isDirectory()) {
            Snackbar.make(rvReader, "Empty Folder!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> imagePaths = MyUtils.filterImageFilenames(curDir, urlString);
        adapter.addAllPaths(imagePaths);
    }
}
