package com.example.clemente.caneva;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Event extends Activity {
   // private TextView textView;
    private TextView textView2;
    //private ImageView imageView;
    private String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
            //textView = (TextView) findViewById(R.id.TextEvent);
        textView2 = (TextView) findViewById(R.id.info);
        textView2.setMovementMethod(new ScrollingMovementMethod());
        Bundle bundle = getIntent().getExtras();
        String element = "";
        if (bundle != null) {
            element = bundle.getString(MainActivity.ELEMENTO);
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(element);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            textView2.setText(jsonObject.getString("blogname") + "\n"
                    + jsonObject.getString("blogname_slug") + "\n"
                    + jsonObject.getString("post_title") + "\n"
            );
            textView2.setText(textView2.getText() + "\n" + Html.fromHtml(jsonObject.getString("post_content")));
            textView2.setText(textView2.getText() + "\n"
                    +"Link: " + jsonObject.getString("permalink") + "\n"
                    + jsonObject.getString("evcal_start_date") + "\n"
            );
            textView2.setVerticalScrollBarEnabled(true);
           //impostare immag si imageView
            url = jsonObject.getString("post_thumbnail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this).load(url).into(imageView);



        Button back = (Button) findViewById(R.id.backEvent);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
