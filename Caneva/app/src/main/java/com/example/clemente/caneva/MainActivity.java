package com.example.clemente.caneva;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;



    public class MainActivity extends Activity {
        protected static final String URL = "http://incaneva.it/wp-admin/admin-ajax.php";
        public static final String OBJECT = "BUNDLE";
        public static final String OFFSET = "offset";
        public static final String ELEMENTO = "ELEMENTO";
        private HttpResponse httpResponse;
        private HttpEntity entity;
        private InputStream inputStream;
        private String result;
        private ListView listView;
        private JSONObject jObject;
        private JSONArray jsonArray;
        private int limit = 5;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button btn = (Button) findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, List.class);
                    startActivity(intent);
                }
            });


            listView = (ListView) findViewById(R.id.listView);
            listView.setClickable(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);
            ArrayList<NameValuePair> myKey = new ArrayList<>();
            myKey.add(new BasicNameValuePair("action", "incaneva_events"));
            myKey.add(new BasicNameValuePair("blog", "1,6,7,8"));
            myKey.add(new BasicNameValuePair("old", "false"));
            myKey.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            //myKey.add(new BasicNameValuePair("offset","5"));
            //myKey.add(new BasicNameValuePair("filter",""));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(myKey));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //making POST request.
            try {
                httpResponse = client.execute(httpPost);
                entity = httpResponse.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> adapter;
            final ArrayList<String> listp = new ArrayList<>();
            try {
                if (jObject.getBoolean("success")) {
                    //mostro nella lista le robe
                    jsonArray = jObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String element = "";
                            try {
                                JSONObject oneObject = jsonArray.getJSONObject(i);
                                element += oneObject.getString("blogname").toString() + " " + "\n"
                                        + oneObject.getString("blogname_slug").toString() + " " + "\n"
                                        + oneObject.getString("post_excerpt").toString() + " " + "\n"
                                        + oneObject.getString("evcal_start_date").toString() + " " + "\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listp.add(element);
                        }
                    } else {

                        listp.add("Non ci sono eventi");
                    }
                } else {
                    listp.add("Problemi di connessione");
                }
                adapter = new ArrayAdapter<>(this, R.layout.simplerow, listp);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(MainActivity.this, Event.class);
                    Bundle bundle = new Bundle();
                    //bundle.putString(ELEMENTO, listView.getItemAtPosition(position).toString());
                    try {
                        bundle.putString(ELEMENTO, jsonArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            listView.isVerticalScrollBarEnabled();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, final int totalItems) {

                }
            });

            Button goToList = (Button) findViewById(R.id.btnList);
            goToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, List.class);

                    startActivity(intent);
                }
            });

            Button searchForCategory = (Button) findViewById(R.id.btnCategory);
            searchForCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Category.class);

                    startActivity(intent);
                }
            });

        }

   /* public void connection(){
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        HttpResponse httpResponse;
        HttpEntity entity;
        InputStream inputStream = null;
        String result = "";

        java.util.List<NameValuePair> myKey = new ArrayList<>();
        myKey.add(new BasicNameValuePair("action", "incaneva_events"));
        myKey.add(new BasicNameValuePair("blog", "1,6,7,8"));
        myKey.add(new BasicNameValuePair("old", "false"));

        myKey.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        myKey.add(new BasicNameValuePair("offset",String.valueOf(limit)));
        //myKey.add(new BasicNameValuePair("filter",""));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(myKey));
            limit += 5;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //making POST request.
        try {
            httpResponse = client.execute(httpPost);
            entity = httpResponse.getEntity();
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter;
        final ArrayList <String> listp = new ArrayList<>();
        try {
            if(jObject.getBoolean("success")){
                //mostro nella lista le robe
                jsonArray = jObject.getJSONArray("data");
                if (jsonArray.length() > 0){
                    for (int i=0; i < jsonArray.length(); i++)
                    {
                        String element = "";
                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            element += oneObject.getString("blogname").toString() + " " + "\n"
                                    + oneObject.getString("blogname_slug").toString() + " " + "\n"
                                    + oneObject.getString("post_excerpt").toString() + " " + "\n"
                                    + oneObject.getString("evcal_start_date").toString() + " " + "\n";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listp.add(element);
                    }
                }else {

                    listp.add("Non ci sono eventi");
                }
            } else {
                listp.add("Problemi di connessione");
            }
            adapter = new ArrayAdapter<>(this,R.layout.simplerow, listp);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onRestart() {
            super.onRestart();
        }

        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

        }
    }
