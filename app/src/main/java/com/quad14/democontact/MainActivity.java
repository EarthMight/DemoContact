package com.quad14.democontact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements CustomRecycleAdapter.OnDragStartListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mainToolBar;
    private ImageView AddImage,OpenData,ViewArray;
    private TextView NameT,ContactNoT;
    SQliteHelperClass myhelper;
    static final int REQUEST_CODE_ADDRESS_BOOK=1;
    private Uri uriContact;
    private String contactID;
    String display,family,given;
    String first_name ="";
    String last_name = "";
    RecyclerView recyclerView;
    ArrayList<CustomDataListModel> customDataListModelList;
    CustomRecycleAdapter customRecycleAdapter;
    Thread ListUpdateThread;
    ArrayList<String> DataNameArrayList;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setToolbar();

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent , REQUEST_CODE_ADDRESS_BOOK);


            }
        });

        OpenData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatatestRead();

//                DataReadLastRecord();
            }
        });

        DataRead();

       customRecycleAdapter=new CustomRecycleAdapter(this,getApplicationContext(), customDataListModelList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                int pos=position;
                String name=customDataListModelList.get(position).getName();
                String number=customDataListModelList.get(position).getNumber();

//                Intent ContactData=new Intent(MainActivity.this,ConfigContact.class);
//                ContactData.putExtra("CName",name);
//                ContactData.putExtra("CNumber",number);
//                startActivity(ContactData);

               Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(customRecycleAdapter);


        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(customRecycleAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        ViewArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateDataRead();

            }
        });


    }
//#########################--Initalization--#########################
    public void init(){

        mainToolBar=(Toolbar)findViewById(R.id.toolbar);
        AddImage=(ImageView)findViewById(R.id.addimag);
        myhelper=new SQliteHelperClass(MainActivity.this);
        OpenData=(ImageView)findViewById(R.id.opendata);
//        listView=(ListView)findViewById(R.id.listcontact);
        recyclerView=(RecyclerView)findViewById(R.id.listRecycle);
        customDataListModelList=new ArrayList<>();
        DataNameArrayList=new ArrayList<>();
        ViewArray=(ImageView)findViewById(R.id.viewArray);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (REQUEST_CODE_ADDRESS_BOOK):
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Response: " + data.toString());
                    uriContact = data.getData();
                    DataAdd(getDisplayName(),getPhNumber(),R.color.colorPrimary,25);
                    DataRead();
                    customRecycleAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
//######################--ToolBar Method--##############################
    public void setToolbar(){
        mainToolBar.setTitle("Q Contact");
        mainToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mainToolBar);
    }

//########################--Cool Dialog--################################
    public void ViewDataDialog(String title,String info ){
        new TTFancyGifDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(info)
                .setPositiveBtnText("Ok")
                .setGifResource(R.drawable.contact)      //pass your gif, png or jpg
                .setPositiveBtnBackground("#2E7D32")
                .isCancellable(true)
                .build();

    }

//#####################-Methods of getting Various data--##################

    public String getDisplayName(){
        Cursor c =  getContentResolver().query(uriContact, null, null, null, null);
        c.moveToFirst();
        String name=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        c.close();
//        ViewDataDialog("DisplayName",name);
        return name;
    }


    public String getPhNumber(){
        Cursor c =  getContentResolver().query(uriContact, null, null, null, null);
        c.moveToFirst();
        String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        c.close();
//        ViewDataDialog("PhoneNO",phoneNumber);
        return phoneNumber;
    }


//    private void retrieveContactPhoto() {
//
//        Bitmap photo = null;
//
//        try {
//            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
//                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
//
//            if (inputStream != null) {
//                photo = BitmapFactory.decodeStream(inputStream);
//                ContactImage.setImageBitmap(photo);
//            }
//
//            assert inputStream != null;
//            inputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.contact);

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

//#####################--DataBase Stuff--#################################
    public void DataAdd(String name,String num,int color,int size){
        boolean inSamedata=myhelper.IsItemExist(name,num);

        if(inSamedata==false){

            boolean indata = myhelper.insertData(new ContactModel(name,num,color,size));


            if (indata == true) {

                Toast.makeText(getApplicationContext(),"DataSave",Toast.LENGTH_SHORT).show();

            }
            else{

                Toast.makeText(getApplicationContext(),"In Data not True",Toast.LENGTH_SHORT).show();

            }
        }else if(inSamedata==true){


            Toast.makeText(getApplicationContext(),"Record Exist",Toast.LENGTH_SHORT).show();


        }

    }

    public void DataRead(){

        customDataListModelList.clear();
        Cursor cursor=myhelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();

            return;

        }

        int i=0;
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {

            DataNameArrayList.add(cursor.getString(1));
            customDataListModelList.add(new CustomDataListModel(cursor.getString(1),cursor.getString(2)));
            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getInt(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");
            Log.e("CustomDataRecord", String.valueOf(customDataListModelList.get(i).Name));
            i++;
        }

//            ViewDataDialog("DataRecords",String.valueOf(stringBuffer));

    }


    public void DataReadLastRecord(){
        Cursor cursor=myhelper.getLastData();

        if(cursor.getCount() ==0){
            Toast.makeText(getApplicationContext(),"There is nothing inside Database",Toast.LENGTH_SHORT).show();

            return;

        }
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {


            customDataListModelList.add(new CustomDataListModel(cursor.getString(1),cursor.getString(2)));
            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");

        }

//        ViewDataDialog("LastRecord",String.valueOf(stringBuffer));
    }


    public void DatatestRead(){

        Cursor cursor=myhelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();

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

//        try {
//            Toast.makeText(getApplicationContext(),DataNameArrayList.get(1),Toast.LENGTH_SHORT).show();
//
//        }catch (Exception e){
//            Toast.makeText(getApplicationContext(),"exeption",Toast.LENGTH_SHORT).show();
//
//        }

            ViewDataDialog("DataRecords",String.valueOf(stringBuffer));

    }

    public void ValidateDataRead(){

//        Cursor cursor=myhelper.getAllData();
//        if(cursor.getCount() ==0){
//            Toast.makeText(getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();
//
//            return;
//
//        }
//
//        while (cursor.moveToNext()) {
//            DataNameArrayList.add(cursor.getString(1));
//        }
//
//
//
//        for(String str : DataNameArrayList) {
//            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
//        }
//
//        int i=0;
//
//           if(DataNameArrayList.get(i).contains(getDisplayName())){
//               Toast.makeText(getApplicationContext(),"same name",Toast.LENGTH_SHORT).show();
//            }
//
//
////        ViewDataDialog("Array",String.valueOf(stringBuffer));

        int i=0;

        try{
            if(DataNameArrayList.get(i).contains(getDisplayName())){
                Toast.makeText(getApplicationContext(),"same name",Toast.LENGTH_SHORT).show();
            }}catch (Exception e){

        }

    }

    //#########################--thread method--######################################

    public void threadMethod(){

        ListUpdateThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!ListUpdateThread.isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                                customListAdapter.notifyDataSetInvalidated();
//                                customListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        ListUpdateThread.start();
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}


