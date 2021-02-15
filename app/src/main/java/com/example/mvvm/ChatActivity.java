package com.example.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvm.adapter.DiscussionListAdapter;
import com.example.mvvm.adapter.PostListAdapter;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.PostModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.DiscussionListViewModel;
import com.example.mvvm.viewmodel.PostListViewModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements PostListAdapter.ItemListener {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    EditText postEditText;
    private int discussion_id;
    private int user_id;
    private List<PostModel> postModelList;
    private PostListAdapter adapter;
    private PostListViewModel viewModel;
    CountDownTimer countDownTimer;
    Toolbar toolbar;
    TextView noDiscussionProfileTextView;
    ImageView discussionProfilePicture;
    TextView discussionTitle;

    public void sendPost(View view) {
        String text = postEditText.getText().toString();
        String jsonString = "{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", text);
            jsonObject.put("discussion_id", discussion_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        apiService.createPost(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Something went wrong , error message : " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getContext(),"Successfully created.",Toast.LENGTH_LONG).show();
                    viewModel.makeApiCall(discussion_id);
                    postEditText.setText(null);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Context getContext() {
        return this;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        postEditText=(EditText)findViewById(R.id.postEditText);
        discussion_id= getIntent().getIntExtra("EXTRA_DISCUSSION_ID",0);
        user_id= getIntent().getIntExtra("USER_ID",-1);

        toolbar=(Toolbar)findViewById(R.id.chatToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noDiscussionProfileTextView=(TextView)findViewById(R.id.noChatDiscussionProfileTextView);
        discussionProfilePicture=(ImageView)findViewById(R.id.chat_discussion_profile_picture);
        discussionTitle=(TextView)findViewById(R.id.chatDiscussionTitleTextView);
        setDiscussionInformation(getIntent().getStringExtra("EXTRA_DISCUSSION_URL"),getIntent().getStringExtra("EXTRA_DISCUSSION_TITLE"));
        RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.post_list_view);
        final TextView noPostFound = (TextView) findViewById(R.id.noPostFoundTextView);
        noPostFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        postRecyclerView.setLayoutManager(layoutManager);
        adapter = new PostListAdapter(this, postModelList, this,user_id);
        postRecyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(PostListViewModel.class);
        viewModel.getPostListObserver().observe(this, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(List<PostModel> postModels) {
                if (postModels != null) {
                    postModelList = postModels;
                    adapter.setPostList(postModels);
                    noPostFound.setVisibility(View.INVISIBLE);
                    postRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noPostFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall(discussion_id);
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                viewModel.makeApiCall(discussion_id);
                countDownTimer.start();
            }
        };
        countDownTimer.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostClick(PostModel postModel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.choose_action);
        if (postModel.getUser_id()==user_id){
            builder.setItems(R.array.personal_post_options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            deletePost(postModel);
                            break;

                        case 1:

                            break;

                        case 2:
                            if (postModel.getText()!=null && !postModel.getText().equals("")){
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("copied text from discussion app", postModel.getText());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(getApplicationContext(),"Copied to clipboard",Toast.LENGTH_SHORT).show();

                            }
                            else Toast.makeText(getApplicationContext(),"No text to copy",Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            });
        }
        else {
            builder.setItems(R.array.post_options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            if (postModel.getText()!=null && !postModel.getText().equals("")){
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("copied text from discussion app", postModel.getText());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(getApplicationContext(),"Copied to clipboard",Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(getApplicationContext(),"No text to copy",Toast.LENGTH_SHORT).show();

                            break;

                        case 1:

                            break;
                        case 2:

                            break;

                        case 3:

                            break;

                    }
                }
            });
        }

        Dialog dialog= builder.create();
        dialog.show();

    }

    @Override
    public void onPostHold(PostModel postModel) {
    onPostClick(postModel);
    }

    @Override
    public void onUserProfileClick(int user_id) {
        Intent intent=new Intent(this,ShowUserActivity.class);
        intent.putExtra("EXTRA_USER_ID",user_id);
        startActivity(intent);
    }
    public void onDiscussionProfileClick(View view){

    }

    private void deletePost(PostModel postModel){

        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle("Delete Post");
        builder.setMessage("Are you sure you want to delete this post ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
                apiService.deletePost(postModel.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (!response.isSuccessful())
                            {
                                Toast.makeText(getContext(),"Delete Unsuccessful , error message : "+response.errorBody().string(),Toast.LENGTH_LONG).show();
                                return;
                            }
                            String json=response.body().string();
                            JsonElement jsonElement = new JsonParser().parse(json);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (!jsonObject.has("message")){
                                return;
                            }
                            viewModel.makeApiCall(discussion_id);
                        }catch (Exception e){
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Toast.makeText(getContext(),"Delete Unsuccessful , network error",Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Delete Post");
        alert.show();


    }

    private void setDiscussionInformation(String url,String title ){
        discussionTitle.setText(title);

        Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(discussionProfilePicture);
        Log.d("no_profile_check","before if");
        if (discussionProfilePicture.getDrawable()==null){
            String[] temp= title.split(" ");
            noDiscussionProfileTextView.setVisibility(View.VISIBLE);
            discussionProfilePicture.setVisibility(View.INVISIBLE);
            if (temp.length==1)
            {
                StringBuilder tempBuilder=new StringBuilder();
                tempBuilder.append(temp[0].toUpperCase().charAt(0));
                noDiscussionProfileTextView.setText(tempBuilder.toString());
            } else if (temp.length<=1)
            {
                noDiscussionProfileTextView.setText("");
            }
            else {
                StringBuilder tempBuilder=new StringBuilder();
                tempBuilder.append(temp[0].toUpperCase().charAt(0));
                tempBuilder.append(' ');
                tempBuilder.append(temp[1].toUpperCase().charAt(0));
                noDiscussionProfileTextView.setText(tempBuilder.toString());

            }
            Drawable myIcon = getContext().getResources().getDrawable( R.drawable.profile_default);
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
            noDiscussionProfileTextView.setBackground(myIcon);
        }
        else {
            noDiscussionProfileTextView.setVisibility(View.INVISIBLE);
            discussionProfilePicture.setVisibility(View.VISIBLE);
        }
    }
}