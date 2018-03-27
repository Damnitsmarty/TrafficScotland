package uk.ac.gcu.mkolev200.trafficscotland.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FeedItem {
    public enum ItemType{
        INCIDENT,
        ROADWORK,
        PLANNED
    }

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
        return  title == item.title &&
                description == item.description &&
                postDate == item.postDate;
    }

    public void extractRoadID(){
        String idRegex = "[MA]\\d{1,3}";
        Pattern regex = Pattern.compile(idRegex);
        Matcher matches = regex.matcher(this.title);

        if(matches.find())
            this.roadID = matches.group(0);
    }

    public void extractWorkInfo(){
        String workDatesRegex = "Start Date: (.*?)&lt;br /&gt;.*?End Date: (.*?)&lt;br /&gt;";
        Pattern regex = Pattern.compile(workDatesRegex);
        Matcher matches = regex.matcher(this.description);

        if(matches.find()){
            this.startDate = parseDateString(matches.group(1),"E, dd MMM yyyy - HH:mm");
            this.endDate = parseDateString(matches.group(2),"E, dd MMM yyyy - HH:mm");
            //we do not need this information in the description; remove it
            description.replaceAll(workDatesRegex,"");
        }
    }

    public static Date parseDateString(String dateString, String pattern) {
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
        IncidentItem item = new IncidentItem();
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
        //if(item.type != ItemType.INCIDENT)
            item.extractWorkInfo();

        return item;
    }
}
