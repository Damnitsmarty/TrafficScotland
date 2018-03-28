package uk.ac.gcu.mkolev200.trafficscotland;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;

public class MainActivity extends AppCompatActivity implements FeedListFragment.OnFragmentItemInteractionListener {
    public static final String EXTRA_FEED_ITEM = "extra:feed_item";
    public static final String EXTRA_FEED_ITEM_TYPE = "extra:feed_item_type";

    //Icons for the error message layout
    private class UIIcon{
        public static final int NOICON = -1;
        public static final int CLOUD = R.drawable.ic_cloud_off_black_24dp;
    }
    enum Error{
        NOCONNECTION,
        NOITEMS,
    }


    private ViewPager m_Pager;
    private PagerAdapter m_PagerAdapter;
    private TabLayout m_TabLayout;

    private View m_layout_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_layout_msg = findViewById(R.id.layout_msg);


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

    @Override
    public void onRefreshStart(){
        hideErrorMessage();
    }
    @Override
    public void onRefreshEnd(FeedListAdapter adapter) {
        if(adapter.getItemCount() == 0)
            showErrorMessage(Error.NOITEMS);
    }



    public void showErrorMessage(Error err){
        switch (err){
            case NOITEMS:
                showErrorMessage(R.string.ERR_feedNoItems,UIIcon.NOICON);
                break;
            case NOCONNECTION:
                showErrorMessage(R.string.ERR_noConnection, UIIcon.CLOUD);
                break;
            default:
                showErrorMessage("An unknown error has occurred", UIIcon.NOICON);
                break;
        }
    }
    public void showErrorMessage(int msgID, int icon){
        showErrorMessage(getString(msgID),icon);
    }
    public void showErrorMessage(String msg, int icon){
        //set parameters
        ((TextView) m_layout_msg.findViewById(R.id.lbl_errMsg)).setText(msg);

        //set icon
        ImageView iconView = (ImageView) m_layout_msg.findViewById(R.id.icon_errMsg);



        //assume a valid icon is given and show the icon view
        iconView.setVisibility(View.VISIBLE);
        switch (icon){
            case UIIcon.CLOUD:
                iconView.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                break;
            case UIIcon.NOICON:
            default:
                //hide if no valid icon is given
                iconView.setVisibility(View.GONE);
                break;
        }


        //animate appearance
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.layout_root));
        m_layout_msg.setVisibility(View.VISIBLE);


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideErrorMessage();
//            }
//        },3000);
    }
    public void hideErrorMessage(){
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.layout_root));
        m_layout_msg.setVisibility(View.GONE);
    }
}
