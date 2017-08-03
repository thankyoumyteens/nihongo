package com.example.admin.nihongo.util;

import android.support.v4.app.Fragment;

/**
 * Created by Admin on 2017/8/3.
 */

public interface FragmentEvents {
    void changeFragment(Fragment fragment);
    void executeQueryString(String queryString, Object[] params);
}
