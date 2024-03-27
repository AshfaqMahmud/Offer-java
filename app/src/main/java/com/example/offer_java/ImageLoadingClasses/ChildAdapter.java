package com.example.offer_java.ImageLoadingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.offer_java.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    ArrayList<ChildModelClass> childMClassList;
    Context context;

    public ChildAdapter(ArrayList<ChildModelClass> childMClassList, Context context) {
        this.childMClassList = childMClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.child_rv_layout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, int position) {
        //holder.iv_child_image.setImageResource(childMClassList.get(position).image);
        //String imageUrl = childMClassList.get(position).toString();
        ChildModelClass childModel = childMClassList.get(position);
        //Glide.with(context).load(childMClassList).into(holder.iv_child_image);
        Picasso.get().load(childModel.getImage()).into(holder.iv_child_image);

    }

    @Override
    public int getItemCount() {
        return childMClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_child_image;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            iv_child_image=itemView.findViewById(R.id.iv_child_image);
        }
    }
}
