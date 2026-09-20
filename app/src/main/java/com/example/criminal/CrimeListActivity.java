package com.example.criminal;

import androidx.fragment.app.Fragment;

// Мы создаем фрагмент элемента списка в активности CrimeListActivity,
// которая отображается посредством метода базового класса SingleFragmentActivity,
// и в которой отображается фрагмент CrimeListFragment (список преступлений)
public class CrimeListActivity extends SingleFragmentActivity {

    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
