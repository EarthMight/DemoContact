package com.quad14.democontact;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConfigContact extends AppCompatActivity {

    TextView NameConfig,NumberConfig;
    private Toolbar confiToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_contact);

        init();
        setToolbar();

        if(getIntent()!= null){
            String GName = getIntent().getExtras().getString("CName");
            String GNumber = getIntent().getExtras().getString("CNumber");

            NameConfig.setText(GName);
            NumberConfig.setText(GNumber);
        }else{
            NameConfig.setText("none");
            NumberConfig.setText("none");

        }

    }

    public void init(){

        NameConfig=(TextView)findViewById(R.id.nametxtCon);
        NumberConfig=(TextView)findViewById(R.id.nutxtCon);
        confiToolBar=(Toolbar)findViewById(R.id.confitoolbar);

    }


    public void setToolbar(){
        confiToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
        confiToolBar.setTitle("configure");
        confiToolBar.setTitleTextColor(Color.WHITE);
        confiToolBar.setSubtitle("You can change font size and color here");
        confiToolBar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(confiToolBar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }



}
