/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulinkagent;

import jade.util.leap.Serializable;
/**
 *
 * @author xcszbdnl
 */
public class MessageInfo implements Serializable{
    private double longti;
    private double lati;
    private int status;
    public MessageInfo(double longti, double lati, int status) {
        this.longti = longti;
        this.lati = lati;
        this.status = status;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getInformation() {
        String info = "longti:" + longti + ", lati:" + lati + ", status:" + status; 
        return info;
    }
    
    private static double rad(double d) {    
        return d * Math.PI / 180.0;    
    }

    public void walk(double longti_delta, double lati_delta) {
        setLongti(longti + longti_delta);
        setLati(lati + lati_delta);
    }
    
    public double calDist(MessageInfo info) {
        double EARTH_RADIUS = 6378.137; 
        double radLat1 = rad(info.getLati());    
        double radLat2 = rad(lati);    
        double a = radLat1 - radLat2;    
        double b = rad(info.getLongti()) - rad(longti);    
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)    
                + Math.cos(radLat1) * Math.cos(radLat2)    
                * Math.pow(Math.sin(b / 2), 2)));    
        s = s * EARTH_RADIUS;    
        s = Math.round(s * 10000d) / 10000d;    
        s = s*1000;    
        return s;    
    }
}
