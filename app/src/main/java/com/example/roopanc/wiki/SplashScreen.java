package com.example.roopanc.wiki;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashScreen extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splashscreen, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // removing logo fragment after 1.5 secs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getFragmentManager() != null)
                {
                    getFragmentManager().popBackStack();
                }
            }
        }, 1500);
    }

}
