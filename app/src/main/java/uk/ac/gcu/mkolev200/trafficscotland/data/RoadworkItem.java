package uk.ac.gcu.mkolev200.trafficscotland.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoadworkItem extends FeedItem {

    public String delayInfo;
    public Date startDate;
    public Date endDate;
    public GeoPoint point;

    public RoadworkItem(){
        super();
        this.delayInfo = "NO_DELAY_INFO";
        this.startDate = new Date();
        this.endDate = new Date();
        this.point = new GeoPoint(0,0);
    }
//
//    public RoadworkItem(String title, String desc, Date postDate, GeoPoint point){
//        super(title,desc,postDate);
//        this.point = point;
//        extractInfo();
//    }
//    public RoadworkItem(String title, String desc, String postDate, GeoPoint point){
//        super(title,desc,postDate);
//        this.point = point;
//        extractInfo();
//    }


    public void extractInfo(){
        String descRegex = "<description>Start Date:(.*?)&lt;br \\/&gt;.*?End Date:(.*?)&lt;br \\/&gt;.*?Delay Information:(.*?)</description>";
        Pattern regex = Pattern.compile(descRegex);
        Matcher matches = regex.matcher(this.description);

        if(matches.find()){
            try {
                //Date Format: Monday, 25 September 2017 - 00:00
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss", Locale.UK);
                this.startDate = formatter.parse(matches.group(1));
                this.endDate = formatter.parse(matches.group(2));

            } catch (ParseException pe) {}
            this.delayInfo = matches.group(3);

        }

    }
    public static RoadworkItem parse(String str){
        String titleRegex = "<title>(.*?)<\\/title>";
        String descRegex = "<description>(.*?)<\\/description>";
        String geoPointRegex = "<georss:point>([\\d.-]*?)\\s([\\d.-]*?)<\\/georss:point>";
        String dateRegex = "<pubDate>(.*?)<\\/pubDate>";

        Pattern regex = Pattern.compile(titleRegex);
        Matcher matches = regex.matcher(str);

        RoadworkItem item = new RoadworkItem();


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

        item.extractInfo();

        return item;
    }
}
