package uk.ac.gcu.mkolev200.trafficscotland.data;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncidentItem extends FeedItem {

    public GeoPoint point;

    public IncidentItem(){
        super();
        this.point = new GeoPoint(0,0);
    }

    public IncidentItem(String title, String desc, Date postDate, GeoPoint point){
        super();
        this.point = point;
    }
    public IncidentItem(String title, String desc, String postDate, GeoPoint point){
        super();
        this.point = point;
    }


    public static IncidentItem parse(String str){
        String titleRegex = "<title>(.*?)<\\/title>";
        String descRegex = "<description>(.*?)<\\/description>";
        String geoPointRegex = "<georss:point>([\\d.-]*?)\\s([\\d.-]*?)<\\/georss:point>";
        String dateRegex = "<pubDate>(.*?)<\\/pubDate>";

        Pattern regex = Pattern.compile(titleRegex);
        Matcher matches = regex.matcher(str);

        IncidentItem item = new IncidentItem();


        //title search
        if(matches.find())
            item.title = matches.group(1);

        //description search;
        matches.usePattern(Pattern.compile(descRegex));
        if(matches.find())
            item.description = matches.group(1);

        //date search
        matches.usePattern(Pattern.compile(dateRegex));
        if(matches.find())
//            item.setDate(matches.group(1));


        //geoPoint search
        matches.usePattern(Pattern.compile(geoPointRegex));
        if(matches.find())
            item.point = GeoPoint.fromStrings(matches.group(1),matches.group(2));



        return item;
    }
}
