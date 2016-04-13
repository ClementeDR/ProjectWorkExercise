package com.example.clemente.caneva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
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
import java.util.Collections;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class List extends Activity {

    HttpResponse response;
    HttpEntity entity;
    String result;
    private JSONObject jo;
    private JSONArray ja;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        listView.setClickable(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button back = (Button) findViewById(R.id.backList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(MainActivity.URL);

        ArrayList<NameValuePair> lista = new ArrayList<>();
        lista.add(new BasicNameValuePair("action", MainActivity.ACTION));
        lista.add(new BasicNameValuePair("blog", MainActivity.BLOG));
        lista.add(new BasicNameValuePair("old", "false"));

        //lista.add(new BasicNameValuePair("limit", "10"));
        /*lista.add(new BasicNameValuePair("offset", "5"));
        */

        try{
            post.setEntity(new UrlEncodedFormEntity(lista));

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        InputStream inputStream = null;


        try{
            response = client.execute(post);
            entity = response.getEntity();
            inputStream = entity.getContent();

            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
            StringBuilder sb = new StringBuilder();

            String linea = "";
            while ((linea = bf.readLine()) != null){
                sb.append(linea + "\n");
            }

            result = sb.toString();


        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
            }catch(Exception e){

            }
        }


        try {
            jo = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> Adattatore;
        ArrayList<String> listL = new ArrayList<>();

        try{
            if(jo.getBoolean("success")){
                ja = jo.getJSONArray("data");

                if(ja.length()>0){
                    for(int i = 0; i<ja.length();i++){
                        String all = "";
                        try {
                            JSONObject temp = ja.getJSONObject(i);
                            all += temp.getString("blogname").toString()+"\n"
                                    + temp.getString("post_excerpt").toString()+"\n"
                                    + temp.getString("evcal_start_date").toString()+"\n";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listL.add(all);
                    }
                }else{
                    listL.add("niente");
                }
            }else{
                listL.add("Errori di connessione");
            }
            Collections.reverse(listL);
            Adattatore = new ArrayAdapter<String>(this, R.layout.mylist, listL);
            listView.setAdapter(Adattatore);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(List.this, Event.class);
                Bundle bundle = new Bundle();
                //bundle.putString(ELEMENTO, listView.getItemAtPosition(position).toString());
                try {
                    bundle.putString(MainActivity.ELEMENTO, ja.getJSONObject(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
