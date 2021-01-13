package com.example.mvvm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm.R;
import com.example.mvvm.model.DiscussionModel;

import java.util.List;

public class DiscussionListAdapter extends RecyclerView.Adapter<DiscussionListAdapter.MyViewHolder> {
    private Context context;
    private ItemClickListener itemClickListener;
    public void setDiscussionList(List<DiscussionModel> discussionList) {
        this.discussionList = discussionList;
        notifyDataSetChanged();
    }

    private List<DiscussionModel> discussionList;

    public DiscussionListAdapter(Context context, List<DiscussionModel> discussionList,ItemClickListener itemClickListener) {
        this.context = context;
        this.discussionList = discussionList;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public DiscussionListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.discussion_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionListAdapter.MyViewHolder holder, int position) {
    holder.discussionTitle.setText(this.discussionList.get(position).getTitle().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onDiscussionClick(discussionList.get(position));
            }
        });
        Log.d("image_URL",this.discussionList.get(position).getUrl());
    Glide.with(context).load(this.discussionList.get(position).getUrl()).into(holder.discussionProfilePicture);
    }

    @Override
    public int getItemCount() {
        if (this.discussionList != null)
            return this.discussionList.size();
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView discussionTitle;
        ImageView discussionProfilePicture;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            discussionTitle=(TextView)itemView.findViewById(R.id.discussion_title_text);
            discussionProfilePicture=(ImageView)itemView.findViewById(R.id.discussion_profile_picture);
        }
    }
    public interface ItemClickListener{
        public void onDiscussionClick(DiscussionModel discussionModel);
    }
}
