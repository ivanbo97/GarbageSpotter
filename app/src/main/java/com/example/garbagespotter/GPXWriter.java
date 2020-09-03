package com.example.garbagespotter;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;

public class GPXWriter {

    private static final String fileHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><gpx version= \"1.1\" creator=\"Undefined\">";

    private BufferedWriter gpxFileStream;

    public GPXWriter (BufferedWriter gpxFileStream)
    {
        this.gpxFileStream = gpxFileStream;

    }

    public void writeHeader () throws IOException {
        gpxFileStream.write(fileHeader);
        Log.d("INFO","HEADER WRITING");
    }

    public void writeWayPoint (Double latitude, Double longitude) throws IOException{
        gpxFileStream.write("<wpt lat=\"" + latitude.toString() + "\"" +
                             " lon=\""+ longitude.toString() + "\">");
    }

    public void writeExtensionData(String photoFileName) throws IOException
    {
        gpxFileStream.write("<extensions><img>" + photoFileName + "</img></extensions></wpt></gpx>");
    }
}
