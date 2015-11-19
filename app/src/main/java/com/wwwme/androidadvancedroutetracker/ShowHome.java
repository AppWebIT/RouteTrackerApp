package com.wwwme.androidadvancedroutetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WDEZLA on 19.11.2015.
 */
public class ShowHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View showHomeView = inflater.inflate(R.layout.show_home, null);
        return showHomeView;
    }


}
