package com.mr_qual1ty.clock.timerclock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mr_qual1ty.clock.timerclock.BaseFragment;
import com.mr_qual1ty.clock.timerclock.R;
import com.mr_qual1ty.clock.timerclock.widget.CreateLiveViewGroup;
import com.mr_qual1ty.clock.timerclock.widget.TimerClock;

/**
 * Created by mr_qual1ty on 2017/3/22.
 */

public class TimerFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout root;
    Button bt1;
    Button bt2;
    TimerClock tc;

    public static TimerFragment newInstance() {
        Bundle args = new Bundle();
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = (RelativeLayout) inflater.inflate(R.layout.fragment_timer, null);
        bt1 = (Button) root.findViewById(R.id.button);
        bt1.setOnClickListener(this);
        bt2 = (Button) root.findViewById(R.id.button2);
        bt2.setOnClickListener(this);
        tc = (TimerClock) root.findViewById(R.id.clock_demo);
        return root;
    }

    boolean flag = false;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            if (flag)
                tc.switchBlackStatus();
            else
                tc.switchWhiteStatus();
            flag = !flag;
        } else if (v.getId() == R.id.button2) {

        }
    }
}
