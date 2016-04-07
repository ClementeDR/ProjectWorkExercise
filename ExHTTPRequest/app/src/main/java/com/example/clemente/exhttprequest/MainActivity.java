package com.example.clemente.exhttprequest;


import android.app.Activity;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends Activity {

    EditText editText;
    private  static String url = "http://192.168.42.103/prova1/add-ingredient-v0.php";
    private static final String TAGSUCCESS = "success";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        editText = (EditText) findViewById(R.id.editText);
        Button add = (Button) findViewById(R.id.add);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection(editText.getText().toString());

            }
        });



    }


    public void connection(String text){

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> myProduct = new ArrayList<>();


        myProduct.add(new BasicNameValuePair("item", text));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(myProduct));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //making POST request.
        try {
            HttpResponse httpResponse = client.execute(httpPost);

            Log.d("Http Post Response:", httpResponse.toString());
        } catch (NetworkOnMainThreadException e){
            e.printStackTrace();

        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }



    }

