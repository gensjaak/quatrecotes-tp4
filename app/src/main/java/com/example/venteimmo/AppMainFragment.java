package com.example.venteimmo;

import android.support.v4.app.Fragment;
import android.view.View;

public class AppMainFragment extends Fragment {
    protected View viewRoot;

    public AppMainFragment() {
    }

    public View getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(View viewRoot) {
        this.viewRoot = viewRoot;
    }
}
