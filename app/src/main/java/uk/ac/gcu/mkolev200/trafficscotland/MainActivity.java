package uk.ac.gcu.mkolev200.trafficscotland;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import uk.ac.gcu.mkolev200.trafficscotland.data.Feed;
import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;

public class MainActivity extends AppCompatActivity implements FeedListFragment.OnFragmentItemInteractionListener {
    public static final String EXTRA_FEED_ITEM = "extra:feed_item";
    public static final String EXTRA_FEED_ITEM_TYPE = "extra:feed_item_type";
    UIContainerMainActivity ui;

    private ViewPager m_Pager;
    private PagerAdapter m_PagerAdapter;
    private TabLayout m_TabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Pager = findViewById(R.id.viewPager);
        m_TabLayout = findViewById(R.id.layout_tabs);
        m_PagerAdapter = new FeedsPagerAdapter(getSupportFragmentManager());

        m_Pager.setAdapter(m_PagerAdapter);
        m_TabLayout.setupWithViewPager(m_Pager);
        m_Pager.setOffscreenPageLimit(m_PagerAdapter.getCount());

    }


    @Override
    public void onItemClick(FeedItem item) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra(EXTRA_FEED_ITEM_TYPE, item.type.name());
        intent.putExtra(EXTRA_FEED_ITEM, item.source);

        startActivity(intent);
    }
}
