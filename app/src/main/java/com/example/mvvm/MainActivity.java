package com.example.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvvm.adapter.DiscussionListAdapter;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.UserModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.DiscussionListViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DiscussionListAdapter.ItemListener ,NavigationView.OnNavigationItemSelectedListener {

    private List<DiscussionModel> discussionModelList;
    private DiscussionListAdapter adapter;
    private DiscussionListViewModel viewModel;
    EditText titleEditText;
    CountDownTimer countDownTimer;
    Toolbar toolbar;
    String selectedPage="trendingDiscussions";
    UserModel user;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
drawerLayout.closeDrawer(GravityCompat.START);
        }else
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        initializeUser();
        toolbar=(Toolbar)findViewById(R.id.mainToolbar);
        toolbar.setTitle("Discussions");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView discussionRecyclerView = (RecyclerView) findViewById(R.id.discussionlistview);
        final TextView noDiscussionFound = (TextView) findViewById(R.id.noDiscussionFoundTextView);
        noDiscussionFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        discussionRecyclerView.setLayoutManager(layoutManager);
        adapter = new DiscussionListAdapter(this, discussionModelList, this);
        discussionRecyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(DiscussionListViewModel.class);
        viewModel.getDiscussionListObserver().observe(this, new Observer<List<DiscussionModel>>() {
            @Override
            public void onChanged(List<DiscussionModel> discussionModels) {
                if (discussionModels != null) {
                    discussionModelList = discussionModels;
                    adapter.setDiscussionList(discussionModels);
                    noDiscussionFound.setVisibility(View.INVISIBLE);
                } else {
                    noDiscussionFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall(selectedPage);
        countDownTimer = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
    viewModel.makeApiCall(selectedPage);
    countDownTimer.start();
            }
        };
        countDownTimer.start();
    }

    private void initializeUser() {
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<UserModel> call=apiService.getPersonalInformation();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("laravel_response",response.body().toString());
                    user=response.body();

                    ImageView user_profile_picture=(ImageView)findViewById(R.id.user_profile_image_view);
                    TextView name=(TextView)findViewById(R.id.navigation_name_text_view);
                    TextView email=(TextView)findViewById(R.id.navigation_email_text_view);
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    if (user!=null && user.getProfilePreview()!=null)
                        Glide.with(getContext()).load(user.getProfilePreview()).circleCrop().into(user_profile_picture);
                }
                else {
                    logOut();
                }
                }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("laravel_response","request failed");
                logOut();
            }
        });
    }

    private Context getContext() {
        return this;
    }
    private DiscussionListAdapter.ItemListener getItemListener(){
        return this;
    }

    @Override
    public void onDiscussionClick(DiscussionModel discussionModel) {
        Intent intent =new Intent(this,ChatActivity.class);
        intent.putExtra("EXTRA_DISCUSSION_ID", discussionModel.getDiscussion_id());
        intent.putExtra("USER_ID", user.getId());
        startActivity(intent);
//                Toast.makeText(this,"Clicked Discussion : "+discussionModel.getDiscussion_id(),Toast.LENGTH_LONG).show();
//        viewModel.makeApiCall();
    }

    @Override
    public void onDiscussionHold(DiscussionModel discussionModel) {
        if(selectedPage.equals("personalDiscussions")){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("delete");
            builder.setMessage("Are you sure you want to delete "+discussionModel.getTitle()+" ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    deleteDiscussion(discussionModel.getDiscussion_id());
                    viewModel.makeApiCall(selectedPage);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //  Action for 'NO' Button
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(),"you chose not to delete "+discussionModel.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("AlertDialogExample");
            alert.show();
        }
        else
        Toast.makeText(this,"Clicked Discussion : "+discussionModel.getDiscussion_id(),Toast.LENGTH_LONG).show();

    }


    public void changePage(View view) {
        ImageView temp=(ImageView)view;

        String tag=temp.getTag().toString();
        if (selectedPage.equals(tag))
            return;
        Log.d("clicked tag is :",tag);
        Drawable background = getResources().getDrawable( R.drawable.bg_edittext);
        findViewById(R.id.followedDiscussionsImageView).setBackground(background);
        findViewById(R.id.categorizedDiscussionsImageView).setBackground(background);
        findViewById(R.id.trendingDiscussionsImageView).setBackground(background);
        findViewById(R.id.personalDiscussionsImageView).setBackground(background);
        background = getResources().getDrawable( R.drawable.login_signup_button);
        temp.setBackground(background);
        selectedPage=tag;
        viewModel.makeApiCall(selectedPage);
//        Intent intent=new Intent(this,CreateDiscussionActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.makeApiCall(selectedPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_followed,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.item1_id)
        {
            Toast.makeText(getContext(),"item 1 is selected",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.item2_id)
        {
            Toast.makeText(getContext(),"item 2 is selected",Toast.LENGTH_SHORT).show();

        }
        else if (id==R.id.item3_id)
        {
            Toast.makeText(getContext(),"item 3 is selected",Toast.LENGTH_SHORT).show();

        }
        else if (id==R.id.item4_id)
        {
            Toast.makeText(getContext(),"item 4 is selected",Toast.LENGTH_SHORT).show();

        } else if (id==R.id.search_action_main_id)
        {
            Toast.makeText(getContext(),"search is selected",Toast.LENGTH_SHORT).show();

        }
        else if (id==R.id.create_discussion_action_main_id)
        {
            Toast.makeText(getContext(),"create discussion is selected",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,CreateDiscussionActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void deleteDiscussion(int discussion_id){

        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        apiService.deleteDiscussion(discussion_id).enqueue(new Callback<ResponseBody>() {
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
                }catch (Exception e){
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                return;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toast.makeText(this,"whT",Toast.LENGTH_SHORT).show();
        int id=menuItem.getItemId();
        switch (id){
            case R.id.create_discussion_button:
                Intent intent =new Intent(this,CreateDiscussionActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_button :
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure you want to log out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
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
                alert.setTitle("Log out");
                alert.show();

                break;
            case R.id.edit_profile :

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("access_token").commit();
        Intent intent1 =new Intent(getContext(),AuthenticationActivity.class);
        startActivity(intent1);
        finish();
    }
}



