package com.example.garbagespotter;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadRunnable implements Runnable {

    private Service serviceRef;
    private Context mainActivity;
    private File [] recordings;
    private File [] images;

    public FileUploadRunnable( File [] recordings, File [] images, Service serviceRef, Context mainActivity) {

        this.serviceRef = serviceRef;
        this.mainActivity =  mainActivity;
        this.recordings = recordings;
        this.images = images;
    }

    @Override
    public void run() {

        String content_type ="application/octet-stream";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES);

        OkHttpClient client = builder.build();

        for (File file : recordings) {


            RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file);

            RequestBody request_body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type",content_type)
                    .addFormDataPart("uploaded_file",file.getName(), file_body)
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.0.104/garbage_proj/save_recordings.php")
                    .post(request_body)
                    .build();
            try {
                Log.d("Response","!!!");
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    Handler h = new Handler(mainActivity.getMainLooper());

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity,"The file has been successfully uploaded!!!",Toast.LENGTH_LONG).show();

                        }
                    });

                    file.delete();
                }
                else{
                    response.close();
                    throw new IOException("Error : "+response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        //Uploading images

        for (File image: images) {

            content_type = "image_jpeg";
            RequestBody file_body = RequestBody.create(MediaType.parse(content_type),image);
            RequestBody request_body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type",content_type)
                    .addFormDataPart("uploaded_file",image.getName(), file_body)
                    .build();


            Request request = new Request.Builder()
                    .url("http://192.168.0.104/garbage_proj/save_images.php")
                    .post(request_body)
                    .build();


            try {
                Log.d("Response","!!!");
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    Handler h = new Handler(mainActivity.getMainLooper());

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity,"The image has been successfully uploaded!!!",Toast.LENGTH_LONG).show();
                        }
                    });
                    image.delete();
                }
                else{
                    response.close();
                    throw new IOException("Error : "+response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        serviceRef.stopSelf();
    }
}
