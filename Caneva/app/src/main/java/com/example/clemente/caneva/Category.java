package com.example.clemente.caneva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;





public class Category extends Activity {
    private TextView Event;
    private String name;
    private  static String url = "http://incaneva.it/wp-admin/admin-ajax.php";
    private HttpEntity entity_event;
    private InputStream iStream;
    private String result;
    private JSONObject jObject;
    private JSONArray jsonArray;
    private ListView listEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final Button NatureButton=(Button) findViewById(R.id.Nature);
        final Button HistoryButton=(Button) findViewById(R.id.History);
        final Button SportButton=(Button) findViewById(R.id.Sport);
        final Button PassionButton=(Button) findViewById(R.id.Passion);
        final Button GastronomyButton=(Button) findViewById(R.id.Gastronomy);
        listEvento= (ListView) findViewById(R.id.listEvent);
        listEvento.setClickable(true);


        Event=(TextView) findViewById(R.id.namEvent);
        NatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= NatureButton.getText().toString();
                Connection(name);
                updateGUI();
            }
        });
        HistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= HistoryButton.getText().toString();
                Connection(name);
                updateGUI();
            }
        });
        SportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= SportButton.getText().toString();
                Connection(name);
                updateGUI();
            }
        });
        PassionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= PassionButton.getText().toString();
                Connection(name);
                updateGUI();
            }
        });
        GastronomyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= GastronomyButton.getText().toString();
                Connection(name);
                updateGUI();
            }
        });



    }
    private void updateGUI(){
        Event.setText(""+name);
    }

    private void Connection (String category) {
        HttpClient client = new DefaultHttpClient();
        HttpPost Post_connection = new HttpPost(url);
        ArrayList<NameValuePair> eventRequest = new ArrayList<>();

//Passing all the paramters to get them
        eventRequest.add(new BasicNameValuePair("action",MainActivity.ACTION ));
        eventRequest.add(new BasicNameValuePair("blog", MainActivity.BLOG));
        eventRequest.add(new BasicNameValuePair("filter", category));
        eventRequest.add(new BasicNameValuePair("old", "false"));


        try {
            Post_connection.setEntity(new UrlEncodedFormEntity(eventRequest));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse httpResponse = client.execute(Post_connection);
            entity_event = httpResponse.getEntity();
            iStream = entity_event.getContent();
            BufferedReader BFR = new BufferedReader(new InputStreamReader(iStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String Line = "";
            while ((Line = BFR.readLine()) != null) {
                sb.append(Line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
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
            Collections.reverse(listp);
            adapter = new ArrayAdapter<>(this, R.layout.row_event, listp);
            listEvento.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        listEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(Category.this, com.example.clemente.caneva.Event.class);
                Bundle bundle = new Bundle();
                //bundle.putString(ELEMENTO, listView.getItemAtPosition(position).toString());
                try {
                    bundle.putString("ELEMENTO", jsonArray.getJSONObject(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listEvento.isVerticalScrollBarEnabled();
        listEvento.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, final int totalItems) {

            }
        });




    }
}