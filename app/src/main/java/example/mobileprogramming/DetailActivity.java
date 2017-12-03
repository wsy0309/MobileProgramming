package example.mobileprogramming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2017-12-03.
 */

public class DetailActivity extends Activity {
    private ODsayService odsayService;
    private TextView text1, text2, text3, text4;
    private Intent getIntent;
    private StationPoint start, end;
    private Route route;
    private ArrayList<DetailRoute> detailRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        findRoute();
    }


    private void init() {
        //API 설정 초기화
        odsayService = ODsayService.init(this, getString(R.string.odsay_key));
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        text1 = (TextView) findViewById(R.id.text);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);

        route = new Route();
        detailRoute = new ArrayList<DetailRoute>();

        getIntent = getIntent();

        start = (StationPoint) getIntent.getSerializableExtra("start");
        end = (StationPoint) getIntent.getSerializableExtra("end");

        route.setStartStation(start.getStationName());
        route.setEndStation(end.getStationName());
    }

    private OnResultCallbackListener findRouteListener = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONArray subRouteArray = new JSONArray();
            JSONObject infoObject = new JSONObject();
            //호출한 api가 맞을 경우
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    subRouteArray = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONArray("subPath");
                    infoObject = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info");
                    int error = 0;
                    int subCount = subRouteArray.length();
                    DetailRoute tempRoute = new DetailRoute();

                    //subroute의 개수만큼 arraylist생성하여 값 할당
                    for(int i = 0; i < subCount; i++){
                        tempRoute = new DetailRoute();
                        if(subRouteArray.getJSONObject(i).getInt("trafficType") == 3){//도보
                            if(subRouteArray.getJSONObject(i).getInt("distance") <= 1){//distance = 0 무시
                                if(subRouteArray.getJSONObject(i).getInt("distance") == 1){
                                    error++;
                                }
                                //ignore
                                continue;
                            }
                            else{//도보면서 distance = 0 이 아닐경우
                                tempRoute.setTrafficType(subRouteArray.getJSONObject(i).getInt("trafficType"));
                                tempRoute.setSectionTime(subRouteArray.getJSONObject(i).getString("sectionTime"));
                                tempRoute.setDistance(subRouteArray.getJSONObject(i).getDouble("distance"));
                                //이전 subroute의 도착지에서 다음 subroute 출발지까지 걷는거임!
                                detailRoute.add(tempRoute);
                            }
                        }else{//지하철 & 버스 공통
                            tempRoute.setTrafficType(subRouteArray.getJSONObject(i).getInt("trafficType"));
                            tempRoute.setStartName(subRouteArray.getJSONObject(i).getString("startName"));
                            tempRoute.setEndName(subRouteArray.getJSONObject(i).getString("endName"));
                            tempRoute.setSectionTime(subRouteArray.getJSONObject(i).getString("sectionTime"));
                            tempRoute.setStationCount(subRouteArray.getJSONObject(i).getString("stationCount"));

                            if(tempRoute.getTrafficType() == 1){//지하철
                                tempRoute.setSubwayID(subRouteArray.getJSONObject(i).getJSONArray("lane").getJSONObject(0).getString("name"));
                            }else if(tempRoute.getTrafficType() == 2){//버스
                                tempRoute.setBusID(subRouteArray.getJSONObject(i).getJSONArray("lane").getJSONObject(0).getString("busNo"));
                            }
                            detailRoute.add(tempRoute);
                        }

                    }
                    route.setDetailRoute(detailRoute);
                    route.setTotalTime(infoObject.getInt("totalTime") - error);
                    route.setPayment(infoObject.getInt("payment"));

                    //출력
                    text1.setText("start : " + route.getStartStation() + "  " + "end : " + route.getEndStation());
                    text2.setText("totalTime : " + route.getTotalTime() + "  " + "payment : " + route.getPayment());
                    text3.setText("route" + "\n" + "traffictype : " + route.getDetailRoute().get(0).getTrafficType() + "  " + "sectionTime : " + route.getDetailRoute().get(0).getSectionTime());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
            text1.setText("API : " + api.name() + "\n" + errorMessage);
            // y = "API : " + api.name() + "\n" + errorMessage;
        }
    };


    public void findRoute(){
        //api 호출
        odsayService.requestSearchPubTransPath(start.getX(),start.getY(), end.getX(), end.getY(),"0","","0", findRouteListener);
    }
}
