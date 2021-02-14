package com.example.mvvm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvm.R;
import com.example.mvvm.model.DiscussionModel;

import java.util.List;
import java.util.Random;

public class DiscussionListAdapter extends RecyclerView.Adapter<DiscussionListAdapter.MyViewHolder> {
    private Context context;
    private ItemListener itemListener;
    public void setDiscussionList(List<DiscussionModel> discussionList) {
        this.discussionList = discussionList;
        notifyDataSetChanged();
    }

    private List<DiscussionModel> discussionList;

    public DiscussionListAdapter(Context context, List<DiscussionModel> discussionList, ItemListener itemListener) {
        this.context = context;
        this.discussionList = discussionList;
        this.itemListener = itemListener;
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
                itemListener.onDiscussionClick(discussionList.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemListener.onDiscussionHold(discussionList.get(position));
                return true;
            }
        });
        Log.d("image_URL",this.discussionList.get(position).getUrl());
    Glide.with(context).load(this.discussionList.get(position).getUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(holder.discussionProfilePicture);
    Log.d("no_profile_check","before if");
    if (holder.discussionProfilePicture.getDrawable()==null){
        Log.d("no_profile_check","after if");
        Log.d("no_profile_check",position+"");
        String title= this.discussionList.get(position).getTitle().toString();
        if (position==2)
            Log.d("no_profile_check","trouble");
        String[] temp= title.split(" ");
        holder.discussionNoProfileTextView.setVisibility(View.VISIBLE);
        holder.discussionProfilePicture.setVisibility(View.INVISIBLE);
        if (temp.length==1)
        {
        StringBuilder tempBuilder=new StringBuilder();
        tempBuilder.append(temp[0].toUpperCase().charAt(0));
        holder.discussionNoProfileTextView.setText(tempBuilder.toString());
        } else if (temp.length<=1)
        {
            holder.discussionNoProfileTextView.setText("");
        }
        else {
            StringBuilder tempBuilder=new StringBuilder();
            tempBuilder.append(temp[0].toUpperCase().charAt(0));
            tempBuilder.append(' ');
            tempBuilder.append(temp[1].toUpperCase().charAt(0));
            holder.discussionNoProfileTextView.setText(tempBuilder.toString());

        }
        Drawable myIcon = context.getResources().getDrawable( R.drawable.profile_default);
        char toGenerateColor=0;
        for (int i = 0; i <title.length() ; i++) {
            if (Character.MAX_VALUE-title.charAt(i)>toGenerateColor)
            toGenerateColor= (char) (toGenerateColor+title.charAt(i));
            else break;
        }
        int red=toGenerateColor%128+64;
        int blue=(toGenerateColor*2)%128+64;
        int green=(toGenerateColor*3)%128+64;
        ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.rgb(red,blue,green));
        myIcon.setColorFilter(filter);
        holder.discussionNoProfileTextView.setBackground(myIcon);
    }
    else {
        holder.discussionNoProfileTextView.setVisibility(View.INVISIBLE);
        holder.discussionProfilePicture.setVisibility(View.VISIBLE);
    }
    }

    @Override
    public int getItemCount() {
        if (this.discussionList != null)
            return this.discussionList.size();
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView discussionTitle;
        TextView discussionLastMessage;
        TextView discussion_number_of_unread_messages;
        TextView discussion_last_message_time;
        TextView discussionNoProfileTextView;
        ImageView discussionProfilePicture;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            discussionTitle=(TextView)itemView.findViewById(R.id.discussion_title_text);
            discussionProfilePicture=(ImageView)itemView.findViewById(R.id.discussion_profile_picture);
            discussionLastMessage=(TextView)itemView.findViewById(R.id.discussion_last_message);
            discussionNoProfileTextView=(TextView)itemView.findViewById(R.id.noDiscussionProfileTextView);
            discussion_number_of_unread_messages=(TextView)itemView.findViewById(R.id.discussion_number_of_unread_messages);
            discussion_last_message_time=(TextView)itemView.findViewById(R.id.discussion_last_message_time);
        }
    }
    public interface ItemListener {
        public void onDiscussionClick(DiscussionModel discussionModel);
        public void onDiscussionHold(DiscussionModel discussionModel);
    }
}
