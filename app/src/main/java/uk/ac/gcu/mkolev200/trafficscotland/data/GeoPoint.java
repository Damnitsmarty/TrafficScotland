package uk.ac.gcu.mkolev200.trafficscotland.data;

public class GeoPoint{
    //TODO: More precise floats!
    public float x;
    public float y;

    public GeoPoint(float x, float y)
    {
        this.x = x;
        this.y = y;
        
    }
    public static GeoPoint fromStrings(String x, String y){
        float fx = Float.parseFloat(x);
        float fy = Float.parseFloat(y);
        return new GeoPoint(fx,fy);
    }
}
