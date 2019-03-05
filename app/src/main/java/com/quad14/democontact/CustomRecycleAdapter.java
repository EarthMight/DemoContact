package com.quad14.democontact;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyContactHolder> implements ItemTouchHelperAdapter{
    Context mcontext;
    List<CustomDataListModel> customDataListModels;
    CustomItemClickListener listener;
    private final OnDragStartListener mDragStartListener;
    SQliteHelperClass myhelper;



    public CustomRecycleAdapter(OnDragStartListener dragStartListener,Context mcontext, List<CustomDataListModel> customDataListModels, CustomItemClickListener listener) {
        this.mcontext = mcontext;
        this.customDataListModels = customDataListModels;
        this.listener = listener;
        mDragStartListener = dragStartListener;
        myhelper=new SQliteHelperClass(mcontext.getApplicationContext());
        notifyDataSetChanged();

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

        //##############################################################################
        Cursor cursor=myhelper.getAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(mcontext.getApplicationContext(),"No Contact Selected",Toast.LENGTH_SHORT).show();

            return ;
        }
        StringBuffer stringBuffer= new StringBuffer();
        while (cursor.moveToNext()) {

            stringBuffer.append("Id :"+ cursor.getString(0)+"\n");
            stringBuffer.append("Name :"+ cursor.getString(1)+"\n");
            stringBuffer.append("Number :"+ cursor.getString(2)+"\n");
            stringBuffer.append("color :"+ cursor.getInt(3)+"\n");
            stringBuffer.append("Font Size :"+ cursor.getInt(4)+"\n");

            myContactHolder.Name.setTextColor(cursor.getInt(3));
            myContactHolder.Number.setTextSize(cursor.getInt(4));

//            Number.setTextColor(cursor.getInt(3));
//            Number.setTextSize(cursor.getInt(4));

        }
    }

    @Override
    public int getItemCount() {
        return customDataListModels.size();
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

            Log.e("postion", "from pos : "+String.valueOf(fromPosition)+" : to Position :"+String.valueOf(toPosition));

        notifyItemMoved(fromPosition, toPosition);
        return true;    }

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
}
