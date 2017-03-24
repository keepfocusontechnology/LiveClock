package com.mr_qual1ty.clock.timerclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.mr_qual1ty.clock.timerclock.fragment.TimerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().
                add(R.id.fl_content, TimerFragment.newInstance()).disallowAddToBackStack().
                commit();
    }
}
