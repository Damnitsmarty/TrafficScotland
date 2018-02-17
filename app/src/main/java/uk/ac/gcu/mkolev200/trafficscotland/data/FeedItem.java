package uk.ac.gcu.mkolev200.trafficscotland.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.ac.gcu.mkolev200.trafficscotland.R;

public class FeedItem {
    public String title;
    public String description;
    public Date postDate;


    public FeedItem(){

        title = "NO_TITLE";
        description = "NO_DESC";
        postDate = new Date();
    }
    public FeedItem(String title, String description, Date postDate){
        this.title = title;
        this.description = description;
        this.postDate = postDate;
    }
    public FeedItem(String title, String description, String dateString) {
        // post date format Thu, 14 Sep 2017 00:00:00 GMT
        this.title = title;
        this.description = description;
    }
    public boolean setDate(String dateString){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.UK);
            Date date = formatter.parse(dateString);
            this.postDate = date;
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

}
