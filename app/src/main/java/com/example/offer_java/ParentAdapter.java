package com.example.offer_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {
    private final List<UserData> userDataList;
    private final Context context;

    public ParentAdapter(List<UserData> userDataList, Context context) {
        this.userDataList = userDataList;
        this.context = context;
    }
    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {
        UserData userData = userDataList.get(position);
        // Set up the child RecyclerView
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ImageAdapter imageAdapter = new ImageAdapter(context, (ArrayList<String>) userData.imageUrls); // Assuming ImageAdapter exists
        holder.childRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView childRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        }
    }
}
