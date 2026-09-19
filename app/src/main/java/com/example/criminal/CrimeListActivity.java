package com.example.criminal;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
