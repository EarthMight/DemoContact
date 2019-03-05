package com.quad14.democontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import java.io.File;

public class SQliteHelperClass extends SQLiteOpenHelper {
    public static final String Dabase_name= "UserRecord.db";
    public static final String Table_name= "RTable";
    public static final String col_0= "Id";
    public static final String col_1= "NAME";
    public static final String col_2= "NUMBER";
    public static final String col_3= "COLOR";
    public static final String col_4= "FSIZE";



    public SQliteHelperClass(Context context) {
        super(context, Dabase_name, null, 1);
    }

//    public SQliteHelperClass(Context context) {
//        super(context, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                + File.separator+"Q CONTACT"
//                + File.separator +  Dabase_name, null, 1);
//
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Table_name +"(Id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, NUMBER TEXT, COLOR INTEGER, FSIZE INTEGER) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Table_name);
        onCreate(db);
    }

    public boolean insertData(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(col_0,contactModel.getId());
        contentValues.put(col_1,contactModel.getName());
        contentValues.put(col_2,contactModel.getNumber());
        contentValues.put(col_3,contactModel.getColor());
        contentValues.put(col_4,contactModel.getFSize());
//        long result =db.insert(Table_name,null,contentValues);

        long result = db.insertWithOnConflict(Table_name, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }



    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+Table_name,null);

//        if(res.getCount()!=0) {
//            res.moveToFirst();
//            Log.w("getAllData:total:-", String.valueOf(res.getCount()));
//            Log.w("getAllData:col1:-", String.valueOf(res.getString(0)));
//            Log.w("getAllData:col2:-", String.valueOf(res.getString(1)));
//            Log.w("getAllData:col3:-", String.valueOf(res.getString(2)));
//        }

        return res;
    }

    public Cursor getLastData() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * \n" +
                "    FROM    RTable \n" +
                "    WHERE   Id = (SELECT MAX(Id)  FROM RTable);",null);

       /* Log.w("getLastData:total:-", String.valueOf(res.getCount()));
        Log.w("getAllData:col1:-", String.valueOf(res.getString(Integer.parseInt(col_0))));
        Log.w("getAllData:col2:-", String.valueOf(res.getString(Integer.parseInt(col_1))));
        Log.w("getAllData:col3:-", String.valueOf(res.getString(Integer.parseInt(col_2))));*/

        return res;
    }


    public boolean IsItemExist(String name,String mobile) {
        try
        {
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT "+col_1+" FROM "+Table_name+" WHERE "+col_1+"=?",new String[]{name});
            Cursor cursor1=db.rawQuery("SELECT "+col_2+" FROM "+Table_name+" WHERE "+col_2+"=?",new String[]{mobile});
            if (cursor.moveToFirst() && cursor1.moveToFirst())
            {
                db.close();
                Log.d("Record  Already Exists", "Table is:"+Table_name+" ColumnName:"+col_1);
                return true;//record Exists

            }
            Log.d("New Record  ", "Table is:"+Table_name+" ColumnName:"+col_1+" Column Value:"+col_1);
            db.close();
        }
        catch(Exception errorException)
        {
            Log.d("Exception occured", "Exception occured "+errorException);
            // db.close();
        }

        return false;

    }

}

