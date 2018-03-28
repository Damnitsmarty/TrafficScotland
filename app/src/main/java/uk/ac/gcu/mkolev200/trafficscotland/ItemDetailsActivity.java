package uk.ac.gcu.mkolev200.trafficscotland;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;

public class ItemDetailsActivity extends AppCompatActivity
    implements OnMapReadyCallback{

    public FeedItem m_item;

    public MapFragment mapFragment;
    public TextView detailsText;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle extras = getIntent().getExtras();
        m_item = FeedItem.parse(FeedItem.ItemType.valueOf(extras.getString(MainActivity.EXTRA_FEED_ITEM_TYPE)), extras.getString(MainActivity.EXTRA_FEED_ITEM));


        detailsText =   (TextView)  findViewById(R.id.itemDetailsText);
        toolbar =       (Toolbar)   findViewById(R.id.itemDetailsToolbar);

        toolbar.setTitle(m_item.title);
        toolbar.setBackgroundResource(m_item.getColorCoding());

        String endSpan = m_item.getEndDateTimeSpan();
        if(endSpan.length() > 0)
            toolbar.setSubtitle("Ends " + endSpan);
        detailsText.setText(m_item.description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(m_item.point.x, m_item.point.y);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
