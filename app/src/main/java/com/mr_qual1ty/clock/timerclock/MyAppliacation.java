package com.mr_qual1ty.clock.timerclock;

import android.app.Application;
import android.util.Log;

/**
 * Created by mr_qual1ty on 2017/3/23.
 */

public class MyAppliacation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DensityUtils.setAppContext(this);
    }
}
