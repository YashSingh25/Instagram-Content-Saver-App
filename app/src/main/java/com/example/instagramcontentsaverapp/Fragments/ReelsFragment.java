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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instagramcontentsaverapp.R;
import com.google.gson.JsonArray;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReelsFragment extends Fragment {
    String URL="NULL";
    EditText reelLink;
    String reelUrl="1";
    Button getReel,downloadReel;
    VideoView reelContainer;
    private MediaController reelMediaController;
    private Uri uri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.reel_fragment , null);
        init(view);
        reelMediaController=new MediaController(getContext());
        reelMediaController.setAnchorView(reelContainer);
        getReel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL=reelLink.getText().toString().trim();
                if(reelLink.equals("NULL")){
                    Toast.makeText(getContext(), "Please Enter URL:", Toast.LENGTH_SHORT).show();
                }
                else{
                    String result2=StringUtils.substringBefore(URL , "/?");
                    URL= result2 + "/?__a=1&__d=dis";
                    processData();
                }
            }
        });

        downloadReel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!reelUrl.equals("1")){
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
                   Toast.makeText(getContext(), "No Video To Download", Toast.LENGTH_SHORT).show();
               }
            }
        });

        return view;
    }

    public void init(View view){
        reelLink=view.findViewById(R.id.etReelsLink);
        getReel=view.findViewById(R.id.btnGetReels);
        downloadReel=view.findViewById(R.id.reelsBtnDownload);
        reelContainer=view.findViewById(R.id.vvReel);
    }

    private void processData() {

        JsonObjectRequest reelsObjectRequest=new JsonObjectRequest(Request.Method.GET, URL,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject   graphQL = response.getJSONObject("graphql");
                    Log.d("ResMAIN", response.toString());
                    reelUrl=graphQL.getJSONObject("shortcode_media").getString("video_url");
                    uri=Uri.parse(reelUrl);
                    reelContainer.setMediaController(reelMediaController);
                    reelContainer.setVideoURI(uri);
                    reelContainer.requestFocus();
                    reelContainer.start();
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
        requestQueue.add(reelsObjectRequest);

            }

    }


