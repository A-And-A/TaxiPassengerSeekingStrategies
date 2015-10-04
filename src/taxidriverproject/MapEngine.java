package taxidriverproject;import java.net.*;
import java.io.*;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
class MapNode
{
    double sLat,sLon,dLat,dLon;
    MapNode(double sLat,double sLon,double dLat,double dLon)
    {
        this.sLat = sLat;
        this.sLon = sLon;
        this.dLat = dLat;
        this.dLon = dLon;
    }
    MapNode(DataPoint d1,DataPoint d2)
    {
        this.sLat = d1.lat;
        this.sLon = d1.lon;
        this.dLat = d2.lat;
        this.dLon = d2.lon;
    }

    @Override
    public String toString() {
        return "MapNode{" + "sLat=" + sLat + ", sLon=" + sLon + ", dLat=" + dLat + ", dLon=" + dLon + '}';
    }   
}

public class MapEngine {
    
    private JSONObject Obj;
    private int type;
    private double time,dist;
    private String srcAddress, destAddress,polyLine;
    private MapNode node;
    
    MapEngine(MapNode m)
    {
        //Type 0: Distance matrix api
         node = m;
         time = -1;
         dist = -1;
         srcAddress = destAddress = null;
        
    }

    private void setTimeAndDistance()
    {
        Obj =  Utility.requestJSON(node, 0);
        JSONArray destination_address = (JSONArray)Obj.get("destination_addresses");
        JSONArray origin_address = (JSONArray)Obj.get("origin_addresses");
        
        JSONArray rows = (JSONArray)Obj.get("rows");
        srcAddress = (String) origin_address.get(0);
        destAddress = (String) destination_address.get(0);
        
        JSONObject elements = (JSONObject)rows.get(0);
        JSONArray innerElements  = (JSONArray)elements.get("elements");
        JSONObject aux = (JSONObject)innerElements.get(0);
 
        JSONObject duration = (JSONObject)aux.get("duration");
        JSONObject distance = (JSONObject)aux.get("distance");
      
        time = Double.parseDouble(duration.get("value").toString())/60;
        dist= Double.parseDouble(distance.get("value").toString());
    }
    
    private void setPolyLine()
    {
        Obj =  Utility.requestJSON(node, 1);
        //System.out.println(Obj);
        JSONArray routes = (JSONArray)Obj.get("routes");
        JSONObject temp = (JSONObject)routes.get(0);
        JSONObject pl = (JSONObject)temp.get("overview_polyline");
        polyLine = pl.get("points").toString();
    }
    
    //InterFacing Functions
    
    public double getTime()
    {
        if(time==-1)
            setTimeAndDistance();
        return time;
    }
    
    public double getDistance()
    {
        if(dist==-1)
            setTimeAndDistance();
        return dist;
    }
    public String getSource()
    {
        if(srcAddress==null)
            setTimeAndDistance();
        return srcAddress;
    }
    public String getDestination()
    {
        if(destAddress==null)
            setTimeAndDistance();
        return destAddress;
    }
    public String getPolyLine()
    {
        if(polyLine==null)
            setPolyLine();
        return polyLine;
    }
    public void makeMap()
    {
        Utility.makeMap(node,2,getPolyLine());
        ShowImage.displayMap();
    }
  
}
