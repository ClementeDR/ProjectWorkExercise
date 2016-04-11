package com.example.clemente.caneva;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class List extends Activity {

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        textView = (TextView) findViewById(R.id.textViewList);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            textView.setText(bundle.getString(MainActivity.OBJECT));
        }

        Button back = (Button) findViewById(R.id.backList);
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
}
