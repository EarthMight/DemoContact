package com.quad14.democontact;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyContactHolder> implements ItemTouchHelperAdapter,ISimpleItemTouchCallback{
    Context mcontext;
    List<CustomDataListModel> customDataListModels;
    CustomItemClickListener listener;
    private final OnDragStartListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public final static String PREFERENCE_FILE = "preference_file";
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public static List<CustomDataListModel> chCustom;
    public static List<CustomDataListModel> reCustom;
    public static boolean removeflag=false;
    TempSqliteDatabaseHelper tempSqliteDatabaseHelper;

    public CustomRecycleAdapter(OnDragStartListener dragStartListener,OnCustomerListChangedListener listChangedListener,Context mcontext, List<CustomDataListModel> customDataListModels, CustomItemClickListener listener) {
        this.mcontext = mcontext;
        this.customDataListModels = customDataListModels;
        this.listener = listener;
        mDragStartListener = dragStartListener;
        mListChangedListener = listChangedListener;
        notifyDataSetChanged();
        chCustom=new ArrayList<>();
        reCustom=new ArrayList<>();
        tempSqliteDatabaseHelper=new TempSqliteDatabaseHelper(mcontext);
        mSharedPreferences = this.mcontext.getApplicationContext()
                .getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }

    @NonNull
    @Override
    public MyContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mlinflator = LayoutInflater.from(mcontext);
        view=mlinflator.inflate(R.layout.customcontact,viewGroup,false);
        final  MyContactHolder myContactHolder=new MyContactHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                listener.onItemClick(v, myContactHolder.getPosition());
            }
        });
        return myContactHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactHolder myContactHolder, int i) {

        myContactHolder.Name.setText(customDataListModels.get(i).getName());
        myContactHolder.Number.setText(customDataListModels.get(i).getNumber());
        Log.e("poistionAdapter:", String.valueOf(myContactHolder.getPosition()));
    //#######################################################################################
        Cursor cursor = tempSqliteDatabaseHelper.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(mcontext.getApplicationContext(), "No Contact Selected", Toast.LENGTH_SHORT).show();

            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {

            stringBuffer.append("Id :" + cursor.getString(0) + "\n");
            stringBuffer.append("Name :" + cursor.getString(1) + "\n");
            stringBuffer.append("Number :" + cursor.getString(2) + "\n");
            stringBuffer.append("color :" + cursor.getString(3) + "\n");
            stringBuffer.append("Font Size :" + cursor.getInt(4) + "\n");

            Integer SFontsize=cursor.getInt(4);
            String SColor=cursor.getString(3);

            myContactHolder.Name.setTextColor(Color.parseColor(cursor.getString(3)));
            myContactHolder.Name.setTextSize(SFontsize);

            Log.d("Font_Size", String.valueOf(SFontsize));
            Log.d("Font_color", String.valueOf(SColor));

        }

    }


    @Override
    public int getItemCount() {
        return customDataListModels.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(customDataListModels, i, i + 1);

            }
//            Log.e("UpTODown", String.valueOf(toPosition));
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(customDataListModels, i, i - 1);

            }
//            Log.e("DownToUp", String.valueOf(toPosition));
        }

        mEditor.putString(LIST_OF_SORTED_DATA_ID,customDataListModels.toString() ).commit();
        mEditor.commit();

            Log.e("postion", "from pos : "+String.valueOf(fromPosition)+" : to Position :"+String.valueOf(toPosition));

        mListChangedListener.onNoteListChanged(customDataListModels);

        notifyItemMoved(fromPosition, toPosition);

        Log.e("fromtoTO", String.valueOf(customDataListModels.toString()));

        chCustom=customDataListModels;

        return true;
    }

    @Override
    public void removeItem(int position) {
        customDataListModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, customDataListModels.size());

    }

    @Override
    public void editItem(int position) {
        //ToDo
    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    public static class MyContactHolder extends RecyclerView.ViewHolder{

        TextView Name;
        TextView Number;

        public MyContactHolder(@NonNull View itemView) {
            super(itemView);
            Name=(TextView)itemView.findViewById(R.id.nametxtl);
            Number=(TextView)itemView.findViewById(R.id.nutxtl);
        }
    }

    public void remove(int pos){
        removeflag=true;
        Log.e("remove","smothing is deleted");
        customDataListModels.remove(pos);
        notifyItemRemoved(pos);
        notifyItemChanged(pos, customDataListModels.size());
        Log.e("removeItem", String.valueOf(customDataListModels.toString()));
        reCustom=customDataListModels;
    }
}
