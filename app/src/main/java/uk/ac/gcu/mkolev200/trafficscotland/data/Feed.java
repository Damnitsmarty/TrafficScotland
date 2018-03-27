package uk.ac.gcu.mkolev200.trafficscotland.data;

import android.os.AsyncTask;

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

import uk.ac.gcu.mkolev200.trafficscotland.FeedListAdapter;
import uk.ac.gcu.mkolev200.trafficscotland.R;

/**
 * Created by Martin Kolev (S1435614)
 */

public class Feed {

    private List<FeedItem> m_items;
    private FeedListAdapter m_adapter;
    private String m_url;

    public List<FeedItem> getItems() {
        return m_items;
    }

    public FeedListAdapter getAdapter() {
        return m_adapter;
    }


    public Feed(String url) {
        this.m_url = url;
        m_items = new ArrayList<>();
        m_adapter = new FeedListAdapter(m_items);
    }

    public void update() {
        new FetchFeedTask().execute();
    }

    //RSS LIST PARSER
    private List<FeedItem> parseFeedRegex(FeedItem.ItemType type, String source) {

        //result
        List<FeedItem> items = new ArrayList<>();

        String itemRegex = "<item>.*?</item>";
        Pattern regex = Pattern.compile(itemRegex);

        Matcher matches = regex.matcher(source);
        while (matches.find()) {
            items.add(
                    FeedItem.parse(type, matches.group(0))
            );
        }
        return items;
    }


    private class FetchFeedTask extends AsyncTask<Void, Void, List<FeedItem>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<FeedItem> res) {
            m_items = res;
        }

        @Override
        protected List<FeedItem> doInBackground(Void... voids) {
            try {
                URL url = new URL(m_url);
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
