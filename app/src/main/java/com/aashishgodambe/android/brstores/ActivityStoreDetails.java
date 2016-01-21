package com.aashishgodambe.android.brstores;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ActivityStoreDetails extends ActionBarActivity {

    private String address,city,name,latitude,zipcode,storeLogoURL,phone,longitude,storeID,state;
    private Toolbar toolbar;
    private TextView textView_id,textView_name,textView_city,textView_latitude,textView_zipcode;
    private TextView textView_phone,textView_longitude,textView_address,textView_state;
    private ImageView imageView_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the android version is 21+ for activity transition animations.
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.shared_element_transition));

        }
        setContentView(R.layout.activity_store_details);
        Log.d(getLocalClassName(),"Activity created");

        // Configure the toolbar
        configToolbar();

        // Configure views
        configView();

        // Get store data from bundle
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            address = extras.getString("ADDR","");
            city = extras.getString("CITY","");
            name = extras.getString("NAME","");
            latitude =extras.getString("LAT","");
            zipcode = extras.getString("ZIP","");
            longitude = extras.getString("LON","");
            phone = extras.getString("PHONE","");
            state = extras.getString("STATE","");
            storeID = extras.getString("ID","");
            storeLogoURL = extras.getString("LOGO","");
        }

        // Set the views.
        setViews();

    }

    private void setViews() {

        if(!storeLogoURL.equals("") ) {
            Picasso.with(this).load(storeLogoURL).into(imageView_logo);
        }

        textView_name.setText(name);
        textView_phone.setText("Contact : "+phone);
        textView_address.setText(address+",");
        textView_city.setText(city+", ");
        textView_state.setText(state+",");
        textView_zipcode.setText(zipcode);
        textView_id.setText("Store ID : "+storeID);
        textView_latitude.setText("  Latitude: "+(latitude+(char) 0x00B0));
        textView_longitude.setText("  Longitude: "+longitude+(char) 0x00B0);
    }

    private void configView() {
        textView_id = (TextView) findViewById(R.id.textview_detail_storeID);
        textView_name = (TextView)findViewById(R.id.textview_detail_name);
        textView_phone = (TextView)findViewById(R.id.textview_detail_phone);
        textView_address = (TextView)findViewById(R.id.textview_detail_address) ;
        textView_city = (TextView)findViewById(R.id.textview_detail_city);
        textView_state = (TextView)findViewById(R.id.textview_detail_state);
        textView_zipcode = (TextView)findViewById(R.id.textview_detail_zipcode);
        textView_latitude = (TextView)findViewById(R.id.textview_detail_latitude);
        textView_longitude = (TextView)findViewById(R.id.textview_detail_longitude);
        imageView_logo = (ImageView) findViewById(R.id.imageView_detail);


    }

    public void configToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
