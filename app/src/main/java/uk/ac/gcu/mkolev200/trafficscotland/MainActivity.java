package uk.ac.gcu.mkolev200.trafficscotland;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.*;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;
import uk.ac.gcu.mkolev200.trafficscotland.data.IncidentItem;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //assign views
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        //add refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }












    public List<FeedItem> parseFeedRegex(String feed){

        //result
        List<FeedItem> items = new ArrayList<>();

        String itemRegex = "<item>.*?</item>";
        Pattern regex = Pattern.compile(itemRegex);

        Matcher matches = regex.matcher(feed);
        while(matches.find()){
            items.add(
                    IncidentItem.parse(matches.group(0))
            );
        }



        return items;
    }


    private class FetchFeedTask extends AsyncTask<Void, Void, List<FeedItem>> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }


        @Override
        protected List<FeedItem> doInBackground(Void... voids) {
            try {

                URL url = new URL(getResources().getString(R.string.RSS_incidents));
                URLConnection con = url.openConnection();
                InputStream inputStream = con.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));

                String source = "";
                String line = "";
                while((line = bReader.readLine()) != null){
                    source+= line;
                }
                //close the reader
                bReader.close();

                List<FeedItem> list = parseFeedRegex(source);
                return list;
            } catch (IOException e) {
                Log.e("IOException", "Error trying to get page", e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<FeedItem> res){
            swipeRefreshLayout.setRefreshing(false);
            if(res != null){

            }
        }
    }
}
