package com.example.mvvm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PERMISSION_FOR_ATTACTCHMENT = 2;
    private static final int PICKFILE_RESULT_CODE = 1;
    private int user_id;
    private String profilePicture;
    private String profilePreview;
    ImageView profilePictureImageView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profilePictureImageView=(ImageView)findViewById(R.id.user_profile_picture_EditProfile);
        user_id=getIntent().getIntExtra("USER_ID",0);
        profilePicture=getIntent().getStringExtra("USER_PROFILE_ADDRESS");
        profilePreview=getIntent().getStringExtra("USER_PROFILE_PREVIEW_ADDRESS");
        Glide.with(getContext()).load(profilePreview).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(profilePictureImageView);

    }

    public void editProfilePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.choose_action);
            builder.setItems(R.array.edit_profile_picture_options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:

                            break;

                        case 1:
                            deleteProfilePicture();
                            break;

                        case 2:
                            replaceProfilePicture();
                            break;
                    }
                }
            });
        Dialog dialog= builder.create();
        dialog.show();

    }

    private void replaceProfilePicture() {
        pickFile();
    }

    private void deleteProfilePicture() {
    }


    private void sendFile(File file){
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody setUserProfile =RequestBody.create(MediaType.parse("text/plain"), "true");

        RequestBody setDiscussionProfile =RequestBody.create(MediaType.parse("text/plain"), "false");

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        APIService apiService = RetrofitInstance.getFileRetrofitClient().create(APIService.class);
        apiService.uploadFile(body,filename,setUserProfile,setDiscussionProfile).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(getContext(),"Upload unsuccessful"+response.errorBody().string(),Toast.LENGTH_LONG).show();
                        return;
                    }
                    String json=response.body().string();
                    JsonElement jsonElement = new JsonParser().parse(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!jsonObject.has("message")){
                        return;
                    }
                    Toast.makeText(getContext(),"Upload successful",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Log.d("upload_file",t.getMessage());
                Toast.makeText(getContext(),"Upload Unsuccessful , network error",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private Context getContext() {
    return this;
    }

    public void pickFile() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            ActivityCompat
                    .requestPermissions(
                            EditProfileActivity.this,
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            REQUEST_READ_PERMISSION_FOR_ATTACTCHMENT);
        }
        else {
            Intent chooseFile = new Intent(Intent.ACTION_PICK);
            chooseFile.setType("image/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
        }

    }
    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedFile = data.getData();
            String path=getRealPathFromURI(selectedFile);
            Log.d("file_upload",path);
            File file =new File(path);
            Log.d("file_upload data",file.length()+"");
            sendFile(file);
        }

    }
}