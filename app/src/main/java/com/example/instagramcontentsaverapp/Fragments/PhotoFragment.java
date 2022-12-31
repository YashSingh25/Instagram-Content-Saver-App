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
import android.widget.Toast;

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

public class PhotoFragment extends Fragment {
    String URL="NULL";
    EditText photoLink;
    String photoUrl="1";
    Button getPhoto,downloadPhoto;
    ImageView photoContainer;
    private Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment , null);

        init(view);

        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL=photoLink.getText().toString().trim();
                if(photoLink.equals("NULL")){
                    Toast.makeText(getContext(), "Please Enter URL:", Toast.LENGTH_SHORT).show();
                }
                else{
                    String result2= StringUtils.substringBefore(URL , "/?");
                    URL= result2 + "/?__a=1&__d=dis";
                    processData();
                }
            }
        });

        downloadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!photoUrl.equals("1")){
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
                    Toast.makeText(getContext(), "No Photo To Download", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public void init(View view){
        photoLink=view.findViewById(R.id.etPhotoLink);
        getPhoto=view.findViewById(R.id.btnGetPhoto);
        downloadPhoto=view.findViewById(R.id.photoBtnDownload);
        photoContainer=view.findViewById(R.id.ivPhoto);
    }

    private void processData() {

        JsonObjectRequest photoObjectRequest=new JsonObjectRequest(Request.Method.GET, URL,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject   graphQL = response.getJSONObject("graphql");
                    Log.d("ResMAIN", response.toString());
                    photoUrl=graphQL.getJSONObject("shortcode_media").getString("display_url");
                    uri=Uri.parse(photoUrl);
                    Glide.with(getContext()).load(uri).into(photoContainer);

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
        requestQueue.add(photoObjectRequest);

    }
}
