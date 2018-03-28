package uk.ac.gcu.mkolev200.trafficscotland.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.gcu.mkolev200.trafficscotland.R;


public class FeedItem{

    public enum ItemType{
        INCIDENT,
        PLANNED,
        ROADWORK
    }

    //we need the source string so we can compile the object at any time from a parcel
    public String source = "";


    public ItemType type;
    public String title = "NO_TITLE";
    public String description = "NO_DESC";
    public Date postDate = new Date();
    public GeoPoint point;

    public String roadID = "";

    //Roadwork information extracted from description
    public Date startDate = new Date();
    public Date endDate = new Date();


    public boolean equals(FeedItem item){
        return  title.equals(item.title) &&
                description.equals(item.description) &&
                postDate == item.postDate;
    }
    public String getPostDateTimeSpan(){
        if(type == ItemType.PLANNED)
            return "";
        return android.text.format.DateUtils.getRelativeTimeSpanString(postDate.getTime()).toString();
    }

    public String getEndDateTimeSpan(){
        if(type == ItemType.INCIDENT)
            return "";
        return android.text.format.DateUtils.getRelativeTimeSpanString(endDate.getTime()).toString();
    }
    public int getColorCoding() {
        if(type == ItemType.INCIDENT)
            return R.color.timespan_l;
        long diffDays = (endDate.getTime()- Calendar.getInstance().getTime().getTime())/1000/60/60/24;
        if(diffDays < 7)
            return R.color.timespan_s;
        if(diffDays < 14)
            return R.color.timespan_m;
        return R.color.timespan_l;

    }


    public String getFeedTitle(){
        switch (type){
            default:
            case INCIDENT:
                return "Current Incidents";
            case PLANNED:
                return "Planned Roadworks";
            case ROADWORK:
                return "Current Roadworks";
        }
    }


    protected void extractRoadID(){
        String idRegex = "[MA]\\d{1,3}";
        Pattern regex = Pattern.compile(idRegex);
        Matcher matches = regex.matcher(this.title);

        if(matches.find())
            this.roadID = matches.group(0);
    }

    protected void extractWorkInfo(){
        String workDatesRegex = "Start Date: (.*?)[\\n\\s\\t]*?End Date: (.*?)\\n";
        Pattern regex = Pattern.compile(workDatesRegex);
        Matcher matches = regex.matcher(this.description);

        if(matches.find()){
            this.startDate = parseDateString(matches.group(1),"E, dd MMM yyyy - HH:mm");
            this.endDate = parseDateString(matches.group(2),"E, dd MMM yyyy - HH:mm");
            //we do not need this information in the description; remove it
            description.replaceAll(workDatesRegex,"");
        }
    }

    protected static Date parseDateString(String dateString, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.UK);
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException pe) {
            return null;
        }
    }


    public static FeedItem parse(ItemType type, String str){

        //--LOCAL DEFINITIONS--//
        String titleRegex = "<title>(.*?)<\\/title>";
        String descRegex = "<description>(.*?)<\\/description>";
        String geoPointRegex = "<georss:point>([\\d.-]*?)\\s([\\d.-]*?)<\\/georss:point>";
        String dateRegex = "<pubDate>(.*?)<\\/pubDate>";

        Pattern regex = Pattern.compile(titleRegex);
        Matcher matches = regex.matcher(str);



        //create resulting item
        FeedItem item = new FeedItem();
        //set the source
        item.source = str;
        //Item type set
        item.type = type;


        //--REGEX SEARCHES--//

        //title search
        if(matches.find())
            item.title = matches.group(1);

        //description search;
        matches.usePattern(Pattern.compile(descRegex));
        if(matches.find())
            item.description = matches.group(1).replaceAll("&lt;br\\s\\/&gt;","\n"); //replace the HTML escapes with new lines

        //post date search
        matches.usePattern(Pattern.compile(dateRegex));
        if(matches.find())
            item.postDate = parseDateString(matches.group(1),"E, dd MMM yyyy HH:mm:ss");


        //geoPoint search
        matches.usePattern(Pattern.compile(geoPointRegex));
        if(matches.find())
            item.point = GeoPoint.fromStrings(matches.group(1),matches.group(2));


        //--EXTRACTIONS--//
        item.extractRoadID();
        item.extractWorkInfo();

        return item;
    }
}
