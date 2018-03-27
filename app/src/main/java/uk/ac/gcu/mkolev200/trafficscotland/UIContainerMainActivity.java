package uk.ac.gcu.mkolev200.trafficscotland;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Martin Kolev (S1435614)
 */

public class UIContainerMainActivity {
    private class UIIcon{
        public static final int NOICON = -1;
        public static final int CLOUD = R.drawable.ic_cloud_off_black_24dp;
    }
    enum Error{
        NOCONNECTION,
        NOITEMS,
    }

    private Activity m_activity;

    public ViewGroup root;
    public TabLayout lay_tabs;
    public ViewGroup lay_msg;
    public ViewPager viewPager;

    public SwipeRefreshLayout swipe_Incidents;
    public RecyclerView recView_Incidents;
    public SwipeRefreshLayout swipe_Roadworks;
    public RecyclerView recView_Roadworks;
    public SwipeRefreshLayout swipe_Planned;
    public RecyclerView recView_Planned;




    public UIContainerMainActivity(Context ctx){
        this.m_activity = (Activity) ctx;
        assignVars();
    }

    private void assignVars(){
        this.root = (ViewGroup) m_activity.findViewById(R.id.layout_root);

        this.lay_tabs = (TabLayout) m_activity.findViewById(R.id.layout_tabs);
        this.lay_msg = (ViewGroup) m_activity.findViewById(R.id.layout_msg);
//        this.viewPager = (ViewPager) m_activity.findViewById(R.id.viewPager);
//
//        this.swipe_Incidents = (SwipeRefreshLayout) m_activity.findViewById(R.id.reflay_Incidents);
//        this.swipe_Roadworks = (SwipeRefreshLayout) m_activity.findViewById(R.id.reflay_Roadworks);
//        this.swipe_Planned = (SwipeRefreshLayout) m_activity.findViewById(R.id.reflay_Planned);
//
//        this.recView_Incidents = (RecyclerView) m_activity.findViewById(R.id.recView_Incidents);
//        this.recView_Roadworks = (RecyclerView) m_activity.findViewById(R.id.recView_Roadworks);
//        this.recView_Planned = (RecyclerView) m_activity.findViewById(R.id.recView_Planned);
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
        showErrorMessage(m_activity.getString(msgID),icon);
    }
    public void showErrorMessage(String msg, int icon){
        //set parameters
        ((TextView) lay_msg.findViewById(R.id.lbl_errMsg)).setText(msg);

        //set icon
        ImageView iconView = (ImageView) lay_msg.findViewById(R.id.icon_errMsg);



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
        TransitionManager.beginDelayedTransition(root);
        lay_msg.setVisibility(View.VISIBLE);


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideErrorMessage();
//            }
//        },3000);
    }
    public void hideErrorMessage(){
        TransitionManager.beginDelayedTransition(root);
        lay_msg.setVisibility(View.GONE);
    }
}
