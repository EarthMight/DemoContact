package com.quad14.democontact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.angrybyte.numberpicker.listener.OnValueChangeListener;
import me.angrybyte.numberpicker.view.ActualNumberPicker;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;


public class textFrag extends Fragment implements OnValueChangeListener {

    View view;
    private ActualNumberPicker mPicker;
    FloatingTextButton Selectok;
    Integer GotSize;
    OnDataPass dataPasser;
    String Data;
    TempSqliteDatabaseHelper tempSqliteDatabaseHelperfragment;

    public textFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_text, container, false);
        mPicker = (ActualNumberPicker)view.findViewById(R.id.actual_picker);
        Selectok=(FloatingTextButton)view.findViewById(R.id.selectok);
        tempSqliteDatabaseHelperfragment=new TempSqliteDatabaseHelper(getActivity());
        Bundle bundle = this.getArguments();
        Data = bundle.getString("Fid");

        Selectok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GotFont",String.valueOf(GotSize));
                try{
                    passData(GotSize);

                }catch (Exception e){
                    Log.e("passDataException","passDataException");
                    GotSize=50;
                    passData(GotSize);
                }
                Log.e("Data", String.valueOf(Data));

                Update_fontSize(Data,GotSize);

            }
        });
        mPicker.setListener(this);
        return view;

    }

    @Override
    public void onValueChanged(int oldValue, int newValue) {

        float percent = (float) newValue / (float) (mPicker.getMaxValue() - mPicker.getMinValue());
        Log.d("percent", "Currently the picker is at " + percent + " percent.");
        Log.d("newValue", "Currently the picker is at " + newValue + " percent.");
        GotSize=newValue;

    }

    public void passData(Integer data) {
        dataPasser.onDataPass(data);
    }


    public interface OnDataPass {
        public void onDataPass(Integer data);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    //################--DataBase stuff--#######################

    public void Update_fontSize(String id,Integer FontSize){
        tempSqliteDatabaseHelperfragment.updateRecordFontSize(id,FontSize);

        boolean isUpdate = tempSqliteDatabaseHelperfragment.updateRecordoldFontSize(id,FontSize);
        if(isUpdate == true) {

            Toast.makeText(getActivity(), "Data Update", Toast.LENGTH_LONG).show();

        }else
            Toast.makeText(getActivity(),"Data not Updated",Toast.LENGTH_LONG).show();

    }

}
