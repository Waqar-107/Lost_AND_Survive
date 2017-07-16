package com.wordpress.farhantanvirutshaw.lostsurvive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by waqar on 7/15/2017.
 */

public class AbroadFragment extends Fragment
{
    public AbroadFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.stuck_in_abroad,container,false);
    }
}
