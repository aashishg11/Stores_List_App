package com.aashishgodambe.android.brstores;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aashishgodambe.android.brstores.Adapter.AdapterStoreList;
import com.aashishgodambe.android.brstores.Interface.StoreService;
import com.aashishgodambe.android.brstores.Model.Stores;
import com.aashishgodambe.android.brstores.Model.StoresList;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LIST_STORES = "LIST_STORES";
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    AdapterStoreList mAdapterStoreList;
    public ArrayList<Stores> mStoresList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the android version is 21+ for activity transition animations.
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementExitTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.shared_element_transition));

        }
        setContentView(R.layout.activity_main);

        // Configure the toolbar
        configToolbar();

        // Get reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Configure Swipe Refresh Layout
        configSwipeRefresh();


        // Load the arraylist of stores from saved instance incase of orientation changes.
        if (savedInstanceState != null) {
            mStoresList = savedInstanceState.getParcelableArrayList(LIST_STORES);
            mAdapterStoreList = new AdapterStoreList(mStoresList);
            mRecyclerView.setAdapter(mAdapterStoreList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else
        // Check connectivity and load data from web url.
        {
            // Checks the network connectivity
            if (!isNetConnected()) {

                Toast.makeText(this, "Please connect to the network", Toast.LENGTH_LONG).show();

                // A dummy variable to display error. If this is not done, the app will crash if user tries to refresh
                // using swiperefresh as the SwipeRefreshLayout will have no child.
                Stores dummyStore = new Stores();
                dummyStore.setAddress("Error");
                dummyStore.setCity("Error");
                dummyStore.setStoreLogoURL("");
                mStoresList.add(dummyStore);

                // Set the adapter and layout manager
                mAdapterStoreList = new AdapterStoreList(mStoresList);
                mRecyclerView.setAdapter(mAdapterStoreList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } else {

                // Fetch data using Retrofit2
                fetchStoresData();
            }

        }

        // Handle the recycler view item clicks.
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //Configure activityOptionsCompat for activity transitions.
                view.setTransitionName("selected_transition");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this
                        , view
                        , view.getTransitionName());

                // Build intent and pass data to ActivityStoreDetails
                Intent intent = new Intent(getApplicationContext(), ActivityStoreDetails.class);
                intent.putExtra("ADDR", mStoresList.get(position).getAddress());
                intent.putExtra("CITY", mStoresList.get(position).getCity());
                intent.putExtra("STATE", mStoresList.get(position).getState());
                intent.putExtra("ZIP", mStoresList.get(position).getZipcode());
                intent.putExtra("NAME", mStoresList.get(position).getName());
                intent.putExtra("ID", mStoresList.get(position).getStoreID());
                intent.putExtra("LOGO", mStoresList.get(position).getStoreLogoURL());
                intent.putExtra("LAT", mStoresList.get(position).getLatitude());
                intent.putExtra("LON", mStoresList.get(position).getLongitude());
                intent.putExtra("PHONE", mStoresList.get(position).getPhone());
                startActivity(intent, optionsCompat.toBundle());

            }
        }));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(LIST_STORES, mStoresList);
    }

    private void configSwipeRefresh() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (isNetConnected()) {
            fetchStoresData();
        } else {
            Toast.makeText(this, "Please connect and then refresh", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public Boolean isNetConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void fetchStoresData() {

        Retrofit retrofit = new Retrofit
                .Builder()
                // Cannot reveal the actual URL
                .baseUrl("http://abc.xyz.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StoreService storeService = retrofit.create(StoreService.class);

        Call<StoresList> call = storeService.loadStores();
        call.enqueue(new Callback<StoresList>() {
            @Override
            public void onResponse(Response<StoresList> response, Retrofit retrofit) {

                List<Stores> storesList = response.body().getStores();
                Log.v(getLocalClassName(), "" + storesList.size());
                // Toast.makeText(getApplicationContext(), storesList.get(0).toString(), Toast.LENGTH_LONG).show();

                /* In case of a swipe refresh clear the list if data is already present or it
                will duplicate the data*/

                if (mStoresList.size() > 0) {
                    mStoresList.clear();
                }

                mStoresList = (ArrayList<Stores>) storesList;

                Log.v(getLocalClassName(), "Before Sending " + mStoresList.size());

                // Set the adapter and layout manager
                mAdapterStoreList = new AdapterStoreList(mStoresList);
                mRecyclerView.setAdapter(mAdapterStoreList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                // Close the refreshing circle once data is available
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            }

            @Override
            public void onFailure(Throwable t) {

                // Display toast if there is an error fetching data
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.v(getLocalClassName(), t.getMessage());
            }
        });
    }


    public void configToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // Listener to catch the gesture events and fire appropriate child of recyclerview.
    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            Log.d("Main Activity", "RecyclerTouchListener constructor invoked");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d("Main Activity", "onSingleTapUp invoked");

                    View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onClick(child, mRecyclerView.getChildAdapterPosition(child));
                        Log.d("Main Activity", "onSingleTapUp invoked" + mRecyclerView.getChildAdapterPosition(child));
                    }

                    return true;
                }
            });

        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            Log.d("Main Activity", "onInterceptTouchEvent invoked" + gestureDetector.onTouchEvent(e) + "" + e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("Main Activity", "onTouchEvent invoked" + e);

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        void onClick(View view, int position);
    }


}
