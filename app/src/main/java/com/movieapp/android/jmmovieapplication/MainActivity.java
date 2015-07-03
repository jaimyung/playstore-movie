package com.movieapp.android.jmmovieapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.movieapp.android.jmmovieapplication.fragment.ImageFragment;
import com.movieapp.android.jmmovieapplication.fragment.SearchNameFragment;


public class MainActivity extends Activity implements SearchNameFragment.Communicator{

    SearchNameFragment searchNameFragment;
    ImageFragment imageFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getFragmentManager();
        searchNameFragment = (SearchNameFragment)fm.findFragmentById(R.id.fragment);
        searchNameFragment.setCommunicator(this);

    }

    @Override
    public void response(String index) {
        imageFragment = (ImageFragment)fm.findFragmentById(R.id.fragment2);
        imageFragment.changeData(index);
    }
}
