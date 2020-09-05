package com.example.garbagespotter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button enableGPSBtn;
    private Button recordBtn;
    private Button uploadBtn;

    private TextView gpsStatusTextBox;
    private LocationManager manager;
    private File photosDir;
    private File recordings;
    private String currentPhotoPath;
    private String currentImageFileName;

    private ProgressDialog progressDialog;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private CustomLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        photosDir  = new File (getExternalCacheDir(),"photos");
        recordings = new File (getExternalCacheDir(),"recordings");

       //Creating directories for storing photos and additional information

        photosDir.mkdir();
        recordings.mkdir();

        enableGPSBtn = (Button) findViewById(R.id.enableGPSBtn);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);

        gpsStatusTextBox = (TextView) findViewById(R.id.gpsStatusTextBox);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        checkAndSetGpsStatusTextBox();


        enableGPSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }

                    // Continue only if the File was successfully created

                    if (photoFile != null)
                    {
                        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),"com.example.android.fileprovider",photoFile);
                        currentImageFileName = photoUri.getLastPathSegment();

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                File [] currentGpxFiles = recordings.listFiles();
                File [] currentImages = photosDir.listFiles();

                if(currentGpxFiles.length == 0){
                    Toast toast = Toast.makeText(getApplicationContext(),"Recordings directory is empty.",Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Intent serviceIntent = new Intent (getApplicationContext(), UploadingService.class);
                    serviceIntent.putExtra("gpxFiles",currentGpxFiles);
                    serviceIntent.putExtra("images",currentImages);
                    ContextCompat.startForegroundService(getApplicationContext(),serviceIntent);
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndSetGpsStatusTextBox();
    }

   private void checkAndSetGpsStatusTextBox ()
    {
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gpsStatusTextBox.setText("On");
            recordBtn.setEnabled(true);
        }
        else {
            gpsStatusTextBox.setText("Off");
            recordBtn.setEnabled(false);
        }
    }

     private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         currentImageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                currentImageFileName,  /* prefix */
                ".jpg",         /* suffix */
                photosDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //Loading image before getting the location
            //Code for getting Location and relate to file name stored in currentPhotoPath. We use the gpx format.

            progressDialog = ProgressDialog.show(this, "Identifying location", "Location info retrieving...");

            locationListener = new CustomLocationListener(currentImageFileName ,recordings,progressDialog, manager);

            try {


                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }catch (SecurityException e)
            {
                e.printStackTrace();
            }

        }
    }




}
