package com.quad14.democontact;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ConfigContact extends AppCompatActivity implements textFrag.OnDataPass {

    TextView NameConfig,NumberConfig;
    private Toolbar confiToolBar;
    FloatingTextButton Colorpick,FontSizePick;
    boolean colorFlag=false;
    String selectedcolor;
    TempSqliteDatabaseHelper tempSqliteDatabaseHelperConf;
    String GId;
    String UseableId;
    Bundle Idbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_contact);

        init();
        setToolbar();

        if(getIntent()!= null){
            String GName = getIntent().getExtras().getString("CName");
            String GNumber = getIntent().getExtras().getString("CNumber");
            GId=getIntent().getExtras().getString("CId");

            UseableId=GId.toString();
            Idbundle.putString("Fid",GId);
            NameConfig.setText(GName);
            NumberConfig.setText(GNumber);
            Log.e("Gid",String.valueOf(GId));
        }else{
            NameConfig.setText("none");
            NumberConfig.setText("none");

        }

        Colorpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Color_dialog();
            }
        });

        FontSizePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textFrag otetxfrag=new textFrag();
                otetxfrag.setArguments(Idbundle);
                FragmentTransaction ft = ConfigContact.this.getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.frameid, otetxfrag);
                ft.commit();
            }
        });

    }

//######--method bt which getting data from fragment using interface--##########
    @Override
    public void onDataPass(Integer data) {
        Log.d("LOG","hello " + data);
        NameConfig.setTextSize(data);

    }

//################--init method for initialize commponent--###################################
    public void init(){
        NameConfig=(TextView)findViewById(R.id.nametxtCon);
        NumberConfig=(TextView)findViewById(R.id.nutxtCon);
        confiToolBar=(Toolbar)findViewById(R.id.confitoolbar);
        FontSizePick=(FloatingTextButton)findViewById(R.id.sizepick);
        Colorpick=(FloatingTextButton) findViewById(R.id.colorpick);
        tempSqliteDatabaseHelperConf=new TempSqliteDatabaseHelper(getApplicationContext());
        Idbundle=new Bundle();
    }
//###########--Toolbar method--##############
    public void setToolbar(){
        confiToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
        confiToolBar.setTitle("configure");
        confiToolBar.setTitleTextColor(Color.WHITE);
        confiToolBar.setSubtitle("You can change font size and color here");
        confiToolBar.setSubtitleTextColor(Color.WHITE);

        confiToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(),"navigation",Toast.LENGTH_LONG).show();
            }
        });
        setSupportActionBar(confiToolBar);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

//############--color dialog method--########
    public void Color_dialog(){
        ColorPickerDialogBuilder
                .with(ConfigContact.this)
                .setTitle("Choose color")
                .initialColor(R.color.initColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.e("onColorSelected: 0x" , Integer.toHexString(selectedColor));
                        selectedcolor=Integer.toHexString(selectedColor);

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        colorFlag=true;
                        NameConfig.setTextColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                        Update_fontColor(GId,"#"+Integer.toHexString(selectedColor));
                        Log.e("selected",String.valueOf(selectedColor));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    //################--DataBase stuff--#######################

    public void Update_fontColor(String id,String FontColor){
        tempSqliteDatabaseHelperConf.updateRecordoldColor(id,FontColor);
        boolean isUpdate = tempSqliteDatabaseHelperConf.updateRecordoldColor(id,FontColor);
        if(isUpdate == true) {

            Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();

        }else
            Toast.makeText(getApplicationContext(),"Data not Updated",Toast.LENGTH_LONG).show();

    }


}
