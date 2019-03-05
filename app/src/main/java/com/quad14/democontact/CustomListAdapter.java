package com.quad14.democontact;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<CustomDataListModel> {

    ArrayList<CustomDataListModel> customDataListModels;
    Context context;
    int resource;
    CustomItemClickListener listener;
    SQliteHelperClass myhelper;
    ArrayList<Integer> AdColor;
    ArrayList<Integer> Adfont;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull List<CustomDataListModel> objects,CustomItemClickListener listener) {
        super(context, resource,objects);
        this.customDataListModels = customDataListModels;
        this.context = context;
        this.resource = resource;
        this.listener = listener;
        myhelper=new SQliteHelperClass(context.getApplicationContext());
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CustomDataListModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.customcontact, null, true);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, position);

                }
            });
        }

        final CustomDataListModel customDataListModel=getItem(position);

        TextView Name=(TextView) convertView.findViewById(R.id.nametxtl);
        TextView Number=(TextView) convertView.findViewById(R.id.nutxtl);

        Name.setText(customDataListModel.getName());
        Number.setText(customDataListModel.getNumber());

//##############################################################################
        Cursor cursor=myhelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(context.getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();

            return convertView;
        }
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {

            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getInt(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");

            Name.setTextColor(cursor.getInt(3));
            Name.setTextSize(cursor.getInt(4));

//            Number.setTextColor(cursor.getInt(3));
//            Number.setTextSize(cursor.getInt(4));

        }

        return convertView;
    }

    public void DataRead(){

        Cursor cursor=myhelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(context.getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();

            return;

        }
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {

            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getInt(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");
        }

//            ViewDataDialog("DataRecords",String.valueOf(stringBuffer));

    }
}
