package uk.ac.gcu.mkolev200.trafficscotland;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;

/**
 * Created by Martin Kolev (S1435614)
 */

public class FeedsPagerAdapter extends FragmentPagerAdapter{

    private static List<Fragment> m_frags;

    public FeedsPagerAdapter(FragmentManager fm) {
        super(fm);
        m_frags = new ArrayList<>();
        m_frags.add(FeedListFragment.newInstance(FeedItem.ItemType.INCIDENT));
        m_frags.add(FeedListFragment.newInstance(FeedItem.ItemType.PLANNED));
        m_frags.add(FeedListFragment.newInstance(FeedItem.ItemType.ROADWORK));
    }

    @Override
    public Fragment getItem(int position) {
        return m_frags.get(position);
    }

    @Override
    public int getCount() {
        return m_frags.size();
    }
    @Override
    public CharSequence getPageTitle(int position){
        String[] titles = {"Incidents","Planned Roadworks","Current Roadworks"};
        return titles[position];
    }
}
