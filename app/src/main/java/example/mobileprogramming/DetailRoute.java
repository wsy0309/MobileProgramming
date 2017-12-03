package example.mobileprogramming;

import java.io.Serializable;

/**
 * Created by user on 2017-12-01.
 */


public class DetailRoute implements Serializable{
    private int    trafficType; // 1:지하철, 2:버스, 3:도보
    private String startName;       //시작 정류장
    private String endName;         //도착 정류장
    private String stationCount;   //총 이동 정류장수
    private String sectionTime;    //구간 이동 시간
    private String subwayID;    //버스번호, 지하철 호선
    private String busID;
    private double distance;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getStationCount() {
        return stationCount;
    }

    public void setStationCount(String stationCount) {
        this.stationCount = stationCount;
    }

    public String getSectionTime() {
        return sectionTime;
    }

    public void setSectionTime(String sectionTime) {
        this.sectionTime = sectionTime;
    }

    public String getSubwayID() {
        return subwayID;
    }

    public void setSubwayID(String subwayID) {
        this.subwayID = subwayID;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

}
