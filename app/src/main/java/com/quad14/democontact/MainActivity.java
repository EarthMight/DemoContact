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
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import static com.quad14.democontact.CustomRecycleAdapter.chCustom;
import static com.quad14.democontact.CustomRecycleAdapter.reCustom;
import static com.quad14.democontact.CustomRecycleAdapter.removeflag;

public class MainActivity extends AppCompatActivity implements CustomRecycleAdapter.OnDragStartListener,OnCustomerListChangedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mainToolBar;
    private ImageView AddImage,OpenData,ViewArray,dod;
    private TextView NameT,ContactNoT;
    TempSqliteDatabaseHelper tempSqliteDatabaseHelper;
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
    List<Integer> LastIndexGetter;

    Boolean swapChecker=false;

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
                    TDatatestRead();
                    Log.e("Tdata","Tdata");
            }
        });
        TDataRead();

       customRecycleAdapter=new CustomRecycleAdapter(this,this,getApplicationContext(), customDataListModelList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                int pos=position;
                String name=customDataListModelList.get(position).getName();
                String number=customDataListModelList.get(position).getNumber();
                String Id = customDataListModelList.get(position).getId();

                Intent ContactData=new Intent(MainActivity.this,ConfigContact.class);
                ContactData.putExtra("CName",name);
                ContactData.putExtra("CNumber",number);
                ContactData.putExtra("CId",Id);
                startActivity(ContactData);

               Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(customRecycleAdapter);


        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(customRecycleAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        SwipeToDismiss swipeToDismiss = new SwipeToDismiss(getApplicationContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        swipeToDismiss.setLeftBackgroundColor(R.color.Red);
        swipeToDismiss.setRightBackgroundColor(R.color.Green);
        swipeToDismiss.setLeftImg(R.drawable.ic_delete_black_24dp);
        swipeToDismiss.setRightImg(R.drawable.ic_delete_black_24dp);
        swipeToDismiss.setSwipetoDismissCallBack(getCallback(customRecycleAdapter));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDismiss);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }
//#########################--Initalization--#########################
    public void init(){

        mainToolBar=(Toolbar)findViewById(R.id.toolbar);
        AddImage=(ImageView)findViewById(R.id.addimag);
        OpenData=(ImageView)findViewById(R.id.opendata);
//      listView=(ListView)findViewById(R.id.listcontact);
        recyclerView=(RecyclerView)findViewById(R.id.listRecycle);
        customDataListModelList=new ArrayList<>();
        DataNameArrayList=new ArrayList<>();

        LastIndexGetter=new ArrayList<>();
        tempSqliteDatabaseHelper=new TempSqliteDatabaseHelper(this);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (REQUEST_CODE_ADDRESS_BOOK):
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Response: " + data.toString());
                    uriContact = data.getData();

                    tempinsert(getDisplayName(),getPhNumber(),"#2d2d2d",35,1);
                    TDataRead();
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

//#####################-Methods of getting Various data--#################

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

    @Override
    public void onNoteListChanged(List<CustomDataListModel> customers) {
        swapChecker=true;
        for (CustomDataListModel customer : customers) {
//            listOfSortedCustomerId.add(customer.getId());

        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();



        }
//#############--Data base methods / stuff--############################

    //********************--insert method--***********************
    public void tempinsert(String name,String num,String color,int size,int index){
        boolean inSamedata=tempSqliteDatabaseHelper.IsItemExist(name,num);
        if(inSamedata==false){
            boolean indata = tempSqliteDatabaseHelper.insertData(new ContactModel(name,num,color,size,index));
            if (indata == true) {
                Log.e("DataSave","DataSave");
            }
            else{
                Log.e("In_Data_not_True","In Data not True");
            }
        }else if(inSamedata==true){
            Log.e("Record_Exist","Record Exist");
        }
    }

    //********************--read data method--***********************
    public void TDataRead(){

        customDataListModelList.clear();
        Cursor cursor=tempSqliteDatabaseHelper.getAllData();
        if(cursor.getCount() ==0){
            Log.e("No_temp","No temp Contact Selected");
            return;
        }
        int i=0;
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {

            DataNameArrayList.add(cursor.getString(1));
            customDataListModelList.add(new CustomDataListModel(( cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5)));
            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getString(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");
            stringBuffer.append("Index:"+ cursor.getInt(5)+"\n");

            Log.e("CustomDataRecord", String.valueOf(customDataListModelList.get(i).Name));
            i++;
        }

    }

    //********************--read last record method--***********************
    public void TDataReadLastRecord(){
        Cursor cursor=tempSqliteDatabaseHelper.getLastData();

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
    }

    //********************--read for testing purpose --***********************
    public void TDatatestRead(){

        Cursor cursor=tempSqliteDatabaseHelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(getApplicationContext(),"No temp Contact Selected",Toast.LENGTH_SHORT).show();

            return;

        }
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {
            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getString(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");
            stringBuffer.append("Index:"+ cursor.getInt(5)+"\n");
        }

        ViewDataDialog("DataRecords",String.valueOf(stringBuffer));
    }


    public void chekerofDragDrop(){

        if(swapChecker==true ) {
            tempSqliteDatabaseHelper.deleteall();
            for (int i = 0; i < chCustom.size(); i++) {
                Log.e("chname", String.valueOf(chCustom.get(i).getName()));
                tempinsert(chCustom.get(i).getName(), chCustom.get(i).getNumber(), chCustom.get(i).getColor(), chCustom.get(i).getFSize(), 1);
            }
        }
    }

    public void checkerofRemove(){
        if(removeflag==true){
            tempSqliteDatabaseHelper.deleteall();
            for (int i = 0; i < reCustom.size(); i++) {
                Log.e("chname", String.valueOf(reCustom.get(i).getName()));
                tempinsert(reCustom.get(i).getName(), reCustom.get(i).getNumber(), reCustom.get(i).getColor(), reCustom.get(i).getFSize(), 1);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(swapChecker==true ) {
            chekerofDragDrop();
        }else if(removeflag==true){
            checkerofRemove();
        }
        else{
            Log.e("no_drag_or_delete","no drag or delete");
        }
        removeflag=false;
    }

    private SwipeToDismiss.SwipetoDismissCallBack getCallback(final CustomRecycleAdapter adapter){
        return new SwipeToDismiss.SwipetoDismissCallBack() {
            @Override
            public void onSwipedLeft(RecyclerView.ViewHolder viewHolder) {
                Log.e("viewHOlder", String.valueOf(viewHolder.getAdapterPosition()));
                adapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public void onSwipedRight(RecyclerView.ViewHolder viewHolder) {
                Log.e("viewHOlder", String.valueOf(viewHolder.getAdapterPosition()));
                adapter.remove(viewHolder.getAdapterPosition());
            }
        };
    }
}


