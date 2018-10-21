package com.example.roopanc.wiki;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roopanc.wiki.adapters.ListAdapter;
import com.example.roopanc.wiki.adapters.RecyclerAdapter;
import com.example.roopanc.wiki.data.DataItem;
import com.example.roopanc.wiki.data.JsonData;
import com.example.roopanc.wiki.data.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, SearchView.OnQueryTextListener, FragmentManager.OnBackStackChangedListener, MyCallbacks, SearchView.OnCloseListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int REQUEST_CODE = 101;
    private static final String TAG = "recents";
    ImageView voiceSearch, searchBack, deleteImg, backList;
    TextView searchTv;
    LinearLayout welcomeLayout, moreRecents, searchLayout, listToolbar, recentsHead;
    RelativeLayout searchRLayout;
    CardView recentCards;
    ListView listView;
    LinkedHashMap<String, DataItem> recentItemMap = new LinkedHashMap<>();
    ArrayList<DataItem> recentDataItems;
    ArrayList<DataItem> dataItems = new ArrayList<>();
    ListAdapter listAdapter;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ProgressBar progressBar;
    View searchViewLayout;
    SearchView searchView;
    String searchQuery;
    Retrofit retrofit;
    Call<JsonData> call;
    OkHttpClient okHttpClient;
    Retrofit.Builder builder;
    RetrofitAPI retrofitAPI;
    SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "pref_name";
    SharedPreferences.Editor editor;
    Cache cache;
    int cacheSize = 10 * 1024 * 1024;
    boolean listViewExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        showLogo();
        initializeViews();
        recents();
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        cache = new Cache(getCacheDir(), cacheSize);

        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!isConnected())
                        {
                            Log.d(TAG, "intercept: No internet");
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Log.d(TAG, "intercept: internet");
                        //request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 10).build();
                        okhttp3.Response response = chain.proceed(chain.request());
                        int maxAge = 60; // read from cache
                        return response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .build();
                    }
                })
                /*.addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!isConnected())
                        {
                            Log.d(TAG, "intercept: No internet");
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                            return chain.proceed(request);
                        }
                        else {
                            Log.d(TAG, "intercept: internet");
                            //request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 10).build();
                            okhttp3.Response response = chain.proceed(chain.request());
                            int maxAge = 60; // read from cache
                            return response.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();
                        }
                    }
                })*/
                .build();

        builder = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    private void recents() {

        //retrieve from sharedpreferences
        recentDataItems = new ArrayList<>();
        //recentDataItems.clear();
        recentDataItems = getDataFromSP();

        /*recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));
        recentDataItems.add(new DataItem("0001", "Sachin", "Indian Cricketer", "ac"));*/

        if (recentDataItems != null && recentDataItems.size() > 0)
        {
            welcomeLayout.setVisibility(View.GONE);
            recentCards.setVisibility(View.VISIBLE);
            if (recentDataItems.size() > 5)
            {
                moreRecents.setVisibility(View.VISIBLE);
            }
            else {
                moreRecents.setVisibility(View.GONE);
            }

            Log.d(TAG, "getDataFromSP: "+recentDataItems.size());
            listAdapter = new ListAdapter(recentDataItems, getApplicationContext(), true); // pass true to get only 5 items
            listView.setAdapter(listAdapter);
        }
        else {
            welcomeLayout.setVisibility(View.VISIBLE);
            recentCards.setVisibility(View.GONE);
        }

    }

    private ArrayList<DataItem> getDataFromSP() {

        Gson gson = new Gson();
        String json = sharedPreferences.getString(TAG, null);
        Log.d(TAG, "getDataFromSP: "+json);
        Type type = new TypeToken<LinkedHashMap<String, DataItem>>() {}.getType();
        recentItemMap = gson.fromJson(json, type);
        if (recentItemMap != null){
            ArrayList<DataItem> arrayList = new ArrayList<>(recentItemMap.values());
            if (arrayList != null && !arrayList.isEmpty())
            {
                return arrayList;
            }
            else {
                return null;
            }
        }
        return null;
    }

    private void initializeViews() {
        voiceSearch = findViewById(R.id.voicesearch);
        voiceSearch.setOnClickListener(this);
        searchTv = findViewById(R.id.searchtv);
        listView = findViewById(R.id.listviewrc);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        welcomeLayout = findViewById(R.id.welcomelyt);
        moreRecents =findViewById(R.id.morerecents);
        moreRecents.setOnClickListener(this);
        recentCards = findViewById(R.id.recentscard);
        searchLayout = findViewById(R.id.searchlayout);
        searchLayout.setOnClickListener(this);
        searchRLayout = findViewById(R.id.relative);
        searchViewLayout = findViewById(R.id.searchbarview);
        progressBar = findViewById(R.id.progressbar);
        searchBack = findViewById(R.id.back);
        searchBack.setOnClickListener(this);
        backList = findViewById(R.id.backlist);
        backList.setOnClickListener(this);
        recentsHead = findViewById(R.id.recentshead);
        listToolbar = findViewById(R.id.listtoolbar);
        searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextFocusChangeListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        deleteImg = findViewById(R.id.deleteall);
        deleteImg.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, dataItems);
        recyclerView.setAdapter(recyclerAdapter);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    private void showLogo() {
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SplashScreen()).addToBackStack("logo").commit();
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null){
            int bscount = fragmentManager.getBackStackEntryCount();
            if (bscount == 0)
            {
                getSupportActionBar().show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.voicesearch:
                voiceSearch();
                break;
            case R.id.searchlayout:
                getSupportActionBar().hide();
                searchViewLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.back:
                onClose();
                searchViewLayout.setVisibility(View.GONE);
                //invalidateSearchView();
                getSupportActionBar().show();
                break;
            case R.id.deleteall:
                deleteConfirmation(null);
                break;
            case R.id.morerecents:
                expandRecents();
                break;
            case R.id.backlist:
                collapseRecents();
        }
    }

    private void collapseRecents() {
        listViewExpanded = false;
        getSupportActionBar().show();
        searchRLayout.setVisibility(View.VISIBLE);
        listToolbar.setVisibility(View.GONE);
        recentsHead.setVisibility(View.VISIBLE);
        moreRecents.setVisibility(View.VISIBLE);
        recents();
    }

    private void expandRecents() {
        listViewExpanded = true;
        getSupportActionBar().hide();
        searchRLayout.setVisibility(View.GONE);
        listToolbar.setVisibility(View.VISIBLE);
        recentsHead.setVisibility(View.GONE);
        moreRecents.setVisibility(View.GONE);
        listAdapter = new ListAdapter(recentDataItems, getApplicationContext(), false); // pass true to get only 5 items, else all items
        listView.setAdapter(listAdapter);
    }


    private void deleteConfirmation(final String title_key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromRecents(title_key);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (title_key != null)
        {
            builder.setMessage("Are you sure to delete "+title_key+" from Recents ?");
        }
        else {
            builder.setMessage("Are you sure to delete all items from Recents ?");
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteFromRecents(String title_key) {

        if (title_key == null)
        {
            recentItemMap.clear();
        }
        else {
            recentItemMap.remove(title_key);
        }

        applySharedPreferences(recentItemMap);
        recents();
    }

    private void voiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Try saying something...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList< String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty())
            {
                String query = matches.get(0);
                getSupportActionBar().hide();
                searchViewLayout.setVisibility(View.VISIBLE);
                searchView.setQuery(query, true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (searchViewLayout.getVisibility() == View.VISIBLE)
        {
            searchViewLayout.setVisibility(View.GONE);
            getSupportActionBar().show();
        }
        else if (listViewExpanded)
        {
            collapseRecents();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
        else {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(View.VISIBLE);
        searchQuery = query;
        getQueryResult(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        progressBar.setVisibility(View.VISIBLE);
        searchQuery = newText;
        getQueryResult(searchQuery);
        return false;
    }

    private void getQueryResult(String searchQuery) {

        if (searchQuery != null && searchQuery.length() > 0)
        {
            // performing network call
            searchQuery = searchQuery.replaceAll(" ", "+");

            Map<String, String> query = new HashMap();
            query.put("action", "query");
            query.put("format", "json");
            query.put("prop", "pageimages|pageterms");
            query.put("generator", "prefixsearch");
            query.put("redirects", String.valueOf(1));
            query.put("formatversion", String.valueOf(2));
            query.put("piprop", "thumbnail");
            query.put("pithumbsize", String.valueOf(75));
            query.put("pilimit", String.valueOf(10));
            query.put("wbptterms", "description");
            query.put("gpssearch", searchQuery);
            query.put("gpslimit", String.valueOf(10));

            call = retrofitAPI.getSearchSuggestions(query);

            call.enqueue(new Callback<JsonData>() {
                @Override
                public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                    JsonData jsonData = response.body();
                    if (jsonData != null && jsonData.getBatchcomplete()){
                        List<Page> pageList = jsonData.getQuery().getPages();
                        int pagecount = pageList.size();
                        if (pagecount > 0)
                        {
                            dataItems.clear();
                            int position = 0;
                            //dataItems = new ArrayList<>(pagecount);
                            while (pagecount > 0){
                                String[] results = new String[4];

                                try {
                                    results[0] = pageList.get(position).getPageid().toString();
                                }
                                catch (NullPointerException e)
                                {
                                    results[0] = null;
                                }

                                try {
                                    results[1] = pageList.get(position).getTitle();
                                }
                                catch (NullPointerException e)
                                {
                                    results[1] = null;
                                }

                                try {
                                    results[2] = pageList.get(position).getTerms().getDescription().get(0);
                                }
                                catch (NullPointerException e)
                                {
                                    results[2] = null;
                                }

                                try {
                                    results[3] = pageList.get(position).getThumbnail().getSource();
                                }
                                catch (NullPointerException e)
                                {
                                    results[3] = null;
                                }

                                DataItem dataItem = new DataItem(results[0], results[1], results[2], results[3]);
                                dataItems.add(dataItem);
                                position++;
                                pagecount--;
                            }
                            recyclerAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonData> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        else {
            dataItems.clear();
            recyclerAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void adapterCallback(DataItem dataItem, boolean saveToRecent) {
        if (dataItem != null)
        {
            callWebFragment(dataItem);
            if (saveToRecent)
            {
                //save data into recents
                saveRecents(dataItem);
                invalidateSearchView();
                searchViewLayout.setVisibility(View.GONE);
            }
            else {
                getSupportActionBar().hide();
                if (listViewExpanded)
                {
                    collapseRecents();
                }
            }

        }
    }

    private void callWebFragment(DataItem dataItem) {
        WebPage webPage = new WebPage();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", dataItem);
        webPage.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, webPage).addToBackStack("webview").commit();
    }

    private void saveRecents(DataItem dataItem) {

        Log.d(TAG, "saveRecents: "+recentItemMap.keySet().size());
        //save in SP
        //recentItemMap.remove(dataItem.getTitle());
        recentItemMap.put(dataItem.getTitle(), dataItem);
        LinkedHashMap<String, DataItem> reversedMap = new LinkedHashMap<>();
        List<String> keyset = new ArrayList<>(recentItemMap.keySet());
        Collections.reverse(keyset);
        for (String key : keyset)
        {
            reversedMap.put(key, recentItemMap.get(key));
        }
        applySharedPreferences(reversedMap);
        recentItemMap = reversedMap;
        recents();

        /*recentDataItems = getDataFromSP();
        if (recentDataItems != null)
        {
            recentDataItems.add(dataItem);
        }
        else {
            recentDataItems = new ArrayList<>();
            recentDataItems.add(dataItem);
        }
        listAdapter.notifyDataSetChanged();*/
    }

    private void applySharedPreferences(LinkedHashMap<String, DataItem> recentItemMap) {
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentItemMap, LinkedHashMap.class);
        editor.putString(TAG, json);
        editor.commit();
    }

    @Override
    public boolean onClose() {
        invalidateSearchView();
        return false;
    }

    private void invalidateSearchView() {
        if (call != null)
        {
            call.cancel();
            dataItems.clear();
        }
        searchView.setQuery("", false);
        recyclerAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataItem dataItem = (DataItem) listView.getAdapter().getItem(position);
        if (dataItem != null){
            adapterCallback(dataItem, false);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DataItem dataItem = (DataItem) listView.getAdapter().getItem(position);
        if (dataItem != null)
        {
            String title_key = dataItem.getTitle();
            deleteConfirmation(title_key);
        }
        return true;
    }
}
