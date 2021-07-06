package com.ruehyeon.sanmo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ruehyeon.sanmo.DailycheckupFragment;
import com.ruehyeon.sanmo.DiaryFragment;
import com.ruehyeon.sanmo.HomeFragment;
import com.ruehyeon.sanmo.InfoFragment;
import com.ruehyeon.sanmo.SettingFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
        super(fm, NumOfTabs);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new DailycheckupFragment();
            case 1: return new DiaryFragment();
            case 2: return new HomeFragment();
            case 3: return new InfoFragment();
            case 4: return new SettingFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
