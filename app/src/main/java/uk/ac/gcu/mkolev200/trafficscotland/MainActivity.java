package uk.ac.gcu.mkolev200.trafficscotland;

import android.app.ActivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.*;

import uk.ac.gcu.mkolev200.trafficscotland.data.Feed;
import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;
import uk.ac.gcu.mkolev200.trafficscotland.databinding.FeedItemBinding;

public class MainActivity extends AppCompatActivity implements FeedListFragment.OnFragmentInteractionListener{

    UIContainerMainActivity ui;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ui = new UIContainerMainActivity(this);

        Feed feed_Incidents = new Feed(getString(R.string.RSS_incidents));
        Feed feed_Roadworks = new Feed(getString(R.string.RSS_roadworks));
        Feed feed_Planned = new Feed(getString(R.string.RSS_planned));


//        ui.recView_Incidents.setLayoutManager(new LinearLayoutManager(this));
//        ui.recView_Roadworks.setLayoutManager(new LinearLayoutManager(this));
//        ui.recView_Planned.setLayoutManager(new LinearLayoutManager(this));
//
//        ui.recView_Incidents.setAdapter(feed_Incidents.getAdapter());
//        ui.recView_Roadworks.setAdapter(feed_Roadworks.getAdapter());
//        ui.recView_Planned.setAdapter(feed_Planned.getAdapter());


//        //add refresh listener
//        ui.swipe_Incidents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                feed_Incidents.update();
//            }
//        });
//
//
//        ui.lay_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                ui.viewPager.setCurrentItem(tab.getPosition());
//                Toast.makeText(getBaseContext(), "SelectedTab: " + tab.getPosition(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
