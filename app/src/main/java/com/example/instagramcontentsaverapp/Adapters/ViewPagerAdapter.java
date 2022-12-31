package com.example.instagramcontentsaverapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.instagramcontentsaverapp.Fragments.IgtvFragment;
import com.example.instagramcontentsaverapp.Fragments.PhotoFragment;
import com.example.instagramcontentsaverapp.Fragments.ProfilePicFragment;
import com.example.instagramcontentsaverapp.Fragments.ReelsFragment;
import com.example.instagramcontentsaverapp.Fragments.VideoFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
   int tabcount;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new PhotoFragment();
            case 1:return new VideoFragment();
            case 2:return new ReelsFragment();
            case 3:return new IgtvFragment();
            case 4:return new ProfilePicFragment();
            default:return null;

        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
