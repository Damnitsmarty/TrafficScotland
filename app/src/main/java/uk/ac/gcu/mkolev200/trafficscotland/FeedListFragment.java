package uk.ac.gcu.mkolev200.trafficscotland;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedListFragment extends Fragment {

    //the swipeRefreshLayout parent that triggers updates
    SwipeRefreshLayout m_swipeRefreshLayout;

    //Recycler list view and its custom feed list adapter
    RecyclerView m_recyclerView;
    FeedListAdapter m_adapter;

    //the current async task is needed
    //in order to check if there is an RSS fetch in progress
    FetchFeedTask m_task;

    //the type of data this fragment fetches
    FeedItem.ItemType feedType;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_FEED_TYPE = "arg_feedType";

    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public FeedListFragment() {}

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param arg_feedType The type of feed items this fragment will fetch.
//     * @return A new instance of fragment FeedListFragment.
//     */
//    public static FeedListFragment newInstance(String arg_feedType) {
//        FeedListFragment fragment = new FeedListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_FEED_TYPE, arg_feedType);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get feedType from arguments
//        if (getArguments() != null)
//            this.feedType = FeedItem.ItemType.valueOf(getArguments().getString(ARG_FEED_TYPE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_list, container, false);

        //bind UI variables
        m_swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        m_recyclerView = v.findViewById(R.id.recyclerView);
        //init m_recyclerView's layout manager
        m_recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        m_adapter = new FeedListAdapter(new ArrayList<FeedItem>());

        m_recyclerView.setAdapter(m_adapter);

        //set onRefresh listener
        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try_FetchFeed();
            }
        });
        return v;
    }

    /**
     * Gets the string resource containing the URL to the RSS Feed page appropriate
     * for this fragment's type.
     * @return The URL to the feed containing this fragment's item type.
     */
    public String get_FeedURL(){
        Resources r = getResources();
        if(this.feedType == null)
            this.feedType = FeedItem.ItemType.INCIDENT;
        switch (this.feedType){
            case INCIDENT:
            default:
                return r.getString(R.string.RSS_incidents);
            case PLANNED:
                return r.getString(R.string.RSS_roadworks);
            case ROADWORK:
                return r.getString(R.string.RSS_roadworks);

        }
    }
    public boolean try_FetchFeed(){

        //Create a new m_task if null
        if (m_task == null) {
            m_task = new FetchFeedTask();
        }

        AsyncTask.Status s = m_task.getStatus();

        if (s == AsyncTask.Status.RUNNING){
            return false;
        } else if (s == AsyncTask.Status.FINISHED) {
            m_task = new FetchFeedTask();
        }
        m_task.execute((Void) null);
        return true;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * The AsyncTask that fetches the feed and transforms it into a list of FeedItems
     */
    private class FetchFeedTask extends AsyncTask<Void, Void, List<FeedItem>> {

        @Override
        protected void onPreExecute() {
            m_swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(List<FeedItem> res) {
            m_swipeRefreshLayout.setRefreshing(false);

            m_adapter.swapList(res);
        }

        protected List<FeedItem> parseFeedRegex(FeedItem.ItemType type, String source) {
            //result
            List<FeedItem> items = new ArrayList<>();

            String itemRegex = "<item>.*?</item>";
            Pattern regex = Pattern.compile(itemRegex);

            Matcher matches = regex.matcher(source);
            while(matches.find()){
                items.add(
                        FeedItem.parse(type, matches.group(0))
                );
            }
            return items;
        }

        @Override
        protected List<FeedItem> doInBackground(Void... voids) {
            try {
                URL url = new URL(get_FeedURL());
                //download the HTML/XML source
                URLConnection con = url.openConnection();
                con.setConnectTimeout(500);
                con.setReadTimeout(500);
                InputStream inputStream = con.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));

                //dump the source into a string
                String line, source = "";
                while ((line = bReader.readLine()) != null) {
                    source += line;
                }
                //close the reader
                bReader.close();

                List<FeedItem> list = parseFeedRegex(FeedItem.ItemType.INCIDENT, source);

                return list;

            } catch (IOException ioe) {

            }
            return null;
        }
    }
}
