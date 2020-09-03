package com.example.garbagespotter;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLocationListener implements LocationListener {

    private File recordingsDir;
    private File currentRecFile;
    private String currentPhotoName;
    private ProgressDialog progressDialog;
    private LocationManager manager;
    private static boolean firstLocationRegistered;


    private BufferedWriter bufferedWriter;

    public CustomLocationListener(String currentPhotoName, File recordingsDir, ProgressDialog progressDialog, LocationManager manager) {

        this.recordingsDir = recordingsDir;
        this.currentPhotoName = currentPhotoName;
        this.progressDialog = progressDialog;
        this.manager = manager;
        firstLocationRegistered = false;

    }

    @Override
    public void onLocationChanged(Location location)  {


        if(!firstLocationRegistered) {

            try {
                currentRecFile = createRecFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(currentRecFile));
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                GPXWriter gpxWriter = new GPXWriter(bufferedWriter);
                gpxWriter.writeHeader();
                gpxWriter.writeWayPoint(latitude, longitude);
                gpxWriter.writeExtensionData(currentPhotoName);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            firstLocationRegistered = true;
            progressDialog.dismiss();
            manager.removeUpdates(this);

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private File createRecFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String currentRecFileName = "REC_" + timeStamp + "_";
        File recFile = File.createTempFile(currentRecFileName,".gpx",recordingsDir);
        return recFile;
    }
}
