package com.example.offer_java.ImageLoadingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    //new
    OnItemClickListener clickListener; // Interface for handling parent item clicks

    public interface OnItemClickListener {
        void onItemClick(int position, ParentModelClass parentItem);
    }

    public ParentAdapter(ArrayList<ParentModelClass> childMClassList, Context context,OnItemClickListener clickListener) {
        this.parentMClassList = childMClassList;
        this.context = context;
        //new
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.parent_lv_layout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {

        //new
        ParentModelClass parentItem = parentMClassList.get(position);
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

        //new
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(position, parentItem));

    }

    @Override
    public int getItemCount() {
        return parentMClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RecyclerView rv_child;
        TextView title;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rv_child=itemView.findViewById(R.id.child_recycler_view);
            title=itemView.findViewById(R.id.banner_text);

        }
    }


}
