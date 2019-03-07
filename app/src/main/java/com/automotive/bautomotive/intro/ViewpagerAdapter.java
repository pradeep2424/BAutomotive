package com.automotive.bautomotive.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    public ViewpagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                Viewpager1 tab1 = new Viewpager1();
                return tab1;
            case 1:
                Viewpager2 tab2 = new Viewpager2();
                return  tab2;
            case 2:
                Viewpager3 tab3 = new Viewpager3();
                return tab3;
            case 3:
                Viewpager4 tab4 = new Viewpager4();
                return tab4;

                default:
                    return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
