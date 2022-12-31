package com.example.instagramcontentsaverapp.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.instagramcontentsaverapp.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePicFragment extends Fragment {
    String URL="NULL";
    EditText profilePicLink;
    String profilePicUrl="1";
    Button getProfilePic,downloadProfilePic;
    ImageView profilePicContainer;
    private Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_pic_fragment, null);
        init(view);

        getProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL=profilePicLink.getText().toString().trim();
                if(profilePicLink.equals("NULL")){
                    Toast.makeText(getContext(), "Please Enter URL:", Toast.LENGTH_SHORT).show();
                }
                else{
                    String result2= StringUtils.substringBefore(URL , "?");
                    URL= result2 + "/?__a=1&__d=dis";
                    processData();
                }
            }
        });

        downloadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!profilePicUrl.equals("1")){
                    DownloadManager.Request downloadRequest=new DownloadManager.Request(uri);
                    downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                    downloadRequest.setTitle("Download");
                    downloadRequest.setDescription("........");
                    downloadRequest.allowScanningByMediaScanner();
                    downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS ,"" + System.currentTimeMillis()+".mp4");
                    DownloadManager manager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(downloadRequest);
                    Toast.makeText(getContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "No Profile Pic To Download", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void init(View view){
        profilePicLink=view.findViewById(R.id.etProfilePicLink);
        getProfilePic=view.findViewById(R.id.btnGetProfilePic);
        downloadProfilePic=view.findViewById(R.id.profilePicBtnDownload);
        profilePicContainer=view.findViewById(R.id.ivProfilePic);
    }

    private void processData() {

        JsonObjectRequest profilePicObjectRequest=new JsonObjectRequest(Request.Method.GET, URL,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject   graphQL = response.getJSONObject("graphql");
                    Log.d("ResMAIN", response.toString());
                    profilePicUrl=graphQL.getJSONObject("user").getString("profile_pic_url_hd");
                    uri=Uri.parse(profilePicUrl);
                    Glide.with(getContext())
                            .load(uri)
                            .into(profilePicContainer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Network Issue", Toast.LENGTH_SHORT).show();
                Log.e("ERRORinMainObjR", error.toString() );
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(profilePicObjectRequest);

    }
}
