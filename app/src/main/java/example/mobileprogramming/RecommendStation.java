package example.mobileprogramming;

import java.io.Serializable;

/**
 * Created by user on 2017-12-01.
 */

public class RecommendStation implements Serializable {
    private String midstation;
    private StationPoint recommend1;
    private StationPoint recommend2;
    private StationPoint recommend3;
    private int AverageTime1;
    private int AverageTime2;
    private int AverageTime3;

    public int getAverageTime1() {
        return AverageTime1;
    }

    public void setAverageTime1(int averageTime1) {
        AverageTime1 = averageTime1;
    }

    public int getAverageTime2() {
        return AverageTime2;
    }

    public void setAverageTime2(int averageTime2) {
        AverageTime2 = averageTime2;
    }

    public int getAverageTime3() {
        return AverageTime3;
    }

    public void setAverageTime3(int averageTime3) {
        AverageTime3 = averageTime3;
    }

    public String getMidstation() {
        return midstation;
    }

    public void setMidstation(String midstation) {
        this.midstation = midstation;
    }

    public StationPoint getRecommend1() {
        return recommend1;
    }

    public void setRecommend1(StationPoint recommend1) {
        this.recommend1 = recommend1;
    }

    public StationPoint getRecommend2() {
        return recommend2;
    }

    public void setRecommend2(StationPoint recommend2) {
        this.recommend2 = recommend2;
    }

    public StationPoint getRecommend3() {
        return recommend3;
    }

    public void setRecommend3(StationPoint recommend3) {
        this.recommend3 = recommend3;
    }
}
