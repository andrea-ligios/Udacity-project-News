package com.example.android.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.news.QueryUtils.LOG_TAG;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    // Declare base url components as constants

    // Base url
    private static final String URL = "https://content.guardianapis.com/search?q=";

    // Query topic, put separate from base url so it can be easily changed if needed.
    private static final String URL_TOPIC = "london";

    // Format is in JSON
    private static final String URL_FORMAT = "&format=json";

    // Content component which shows thumbnails and contributor
    private static final String URL_CONTENT = "&show-fields=thumbnail&show-tags=contributor";

    // API key which is needed to access content from the API
    private static final String URL_KEY = "test";

    // Constant value for the news loader ID
    private static int NEWS_LOADER_ID = 1;

    // Adapter for the list of News Items
    private NewsAdapter Adapter;

    // TextView that is displayed when the list is empty
    private TextView EmptyStateTextView;

    // Progress bar that is displayed to show that the info is loading
    private ProgressBar LoadingProgressSpinner;

    private RecyclerView RecyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: London News Activity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Set the refresh listener to refresh the layout when the user swipes the screen
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView = (RecyclerView) findViewById(R.id.list_view);
        RecyclerView.setHasFixedSize(true);

        android.support.v7.widget.RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);

        EmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        LoadingProgressSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty list of news items as input
        Adapter = new NewsAdapter(this, new ArrayList<News>(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                String url = news.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        RecyclerView.setAdapter(Adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            /*
             * Initialise the loader. Pass in the int ID constant defined above and pass in null for
             * the bundle. Pass in this activity for the LoaderCallbacks parameter.
             */
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {

            // Otherwise, display error. Hide loading indicator so error message will be visible.
            LoadingProgressSpinner.setVisibility(View.GONE);

            // Hide RecyclerView
            RecyclerView.setVisibility(View.GONE);

            // Update empty state with no connection error message.
            EmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Create a new loader for the given URL
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_TAG, "TEST: onCreateLoader() called...");

        // Read the user's latest preferences for the news order
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String order = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_default));

        RecyclerView.setVisibility(View.GONE);

        /*
         * Read from SharedPreferences and check for the value associated with the key:
         * getString(R.string.settings_order_by_key). When building the URI and appending query
         * parameters, instead of hardcoding the "order-by" parameter, we will use the user's
         * preference.
         */

        String query = URL + URL_TOPIC + URL_FORMAT + URL_CONTENT;
        Uri baseUri = Uri.parse(query);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by", order);
        uriBuilder.appendQueryParameter("api-key", URL_KEY);

        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        Log.i(LOG_TAG, "TEST: onLoadFinished() called...");

        LoadingProgressSpinner.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);

        // Clear the adapter of previous news data
        Adapter.clear();

        /*
         * If there is a valid list of {@link News}s, then add them to the adapter's data set. This
         * will trigger the list to update. If there aren't any news items to display, then set
         * empty state text to display "no news found"
         */
        if (news != null && !news.isEmpty()) {
            RecyclerView.setVisibility(View.VISIBLE);
            Adapter.addAll(news);
        } else
            EmptyStateTextView.setText(R.string.no_articles_found);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        Log.i(LOG_TAG, "TEST: onLoaderReset() called...");

        // Loader reset, so we can clear out our existing data.
        Adapter.clear();
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }

}
