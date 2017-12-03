package example.mobileprogramming;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2017-12-01.
 */

public class Route implements Serializable {
    private String startStation;
    private String endStation;
    private int totalTime;      //총 소요시간
    private int payment;        //요금
    private ArrayList<DetailRoute> detailRoute = new ArrayList<DetailRoute>();


    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }


    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public ArrayList<DetailRoute> getDetailRoute() {
        return detailRoute;
    }

    public void setDetailRoute(ArrayList<DetailRoute> detailRoute) {
        this.detailRoute = detailRoute;
    }
}