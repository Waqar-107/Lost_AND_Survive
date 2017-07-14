package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by utshaw on 7/5/17.
 */

public class MountainFragment extends Fragment{

    public MountainFragment()
    {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stuck_in_mountain,container,false);
    }
}
