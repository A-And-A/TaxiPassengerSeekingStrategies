/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxidriverproject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author archit08jain
 */
public class Utility {
    
    public static JSONParser parser= new JSONParser();
   
    public static JSONObject requestJSON(MapNode m, int type)
    {
        String s = getURL(m, type); 
        //System.out.println(s);
        String dJSON = "";
        try
        {
            URL url = new URL(s);
            HttpURLConnection hps = (HttpURLConnection)url.openConnection();
          
            InputStream in = hps.getInputStream();
            
            //Reading Data from Link(s)
            int ch;
            while((ch=in.read())!=-1)
            {
                //System.out.print((char)ch);
                dJSON = dJSON + (char)ch;
            }
            
            //Closing Connection
            hps.disconnect();
        
        }
        catch(Exception e)
        {}
        
        try
        {
            return (JSONObject)parser.parse(dJSON);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
     private static String getURL(MapNode m,int type)
    {
        switch(type)
        {
            case 0:
                return "https://maps.googleapis.com/maps/api/distancematrix/json?"
                + "origins="
                + m.sLat + "," + m.sLon
                + "&destinations="
                + m.dLat + "," + m.dLon
                + "&key=AIzaSyAQeBAutaj4zNav-fgtQnzCwBNpYtMag4A";
            case 1:
                return "https://maps.googleapis.com/maps/api/directions/json?origin="
                        + m.sLat + "," + m.sLon
                        +"&destination="
                        + m.dLat + "," + m.dLon
                        + "&key=AIzaSyCuxBrNhuIArFDI7OwiEx9-sUy2wc1o-ag";
            default:
                return null;
        }
    }
}
