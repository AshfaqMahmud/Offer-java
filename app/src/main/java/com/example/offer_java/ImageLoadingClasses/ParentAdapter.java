package com.example.offer_java.ImageLoadingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offer_java.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    ArrayList<ParentModelClass> parentMClassList;
    Context context;

    public ParentAdapter(ArrayList<ParentModelClass> childMClassList, Context context) {
        this.parentMClassList = childMClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.parent_lv_layout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {

        holder.title.setText(parentMClassList.get(position).title);

        ChildAdapter childAdapter;
        childAdapter = new ChildAdapter(parentMClassList.get(position).childModelClassArrayList,context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.rv_child.setLayoutManager(layoutManager);
        holder.rv_child.setAdapter(childAdapter);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(holder.rv_child);
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (layoutManager.findLastCompletelyVisibleItemPosition()<(childAdapter.getItemCount()-1)){
                    layoutManager.smoothScrollToPosition(holder.rv_child, new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
                } else {
                    layoutManager.smoothScrollToPosition(holder.rv_child, new RecyclerView.State(),0);
                }
            }
        },0,2000);
        childAdapter.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return parentMClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RecyclerView rv_child;
        TextView title;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rv_child=itemView.findViewById(R.id.child_recycler_view);
            title=itemView.findViewById(R.id.banner_text);

        }
    }
}
