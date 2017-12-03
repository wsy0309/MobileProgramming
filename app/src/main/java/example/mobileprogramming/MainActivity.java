package example.mobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//addactivity
public class MainActivity extends AppCompatActivity {
    private ODsayService odsayService;
    private TextView X;
    private TextView Y;

    //출발역들 정보저장 역이름, x, y좌표
    private StationPoint start1;
    private StationPoint start2;

    //example - input으로 받은 값
    private String station = "오리";
    private String station2 = "서현";

    //꼭 필요!!!!
    private int checkFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize
        init();
        findRecommend();
    }


    private void init() {
        //API 설정 초기화
        odsayService = ODsayService.init(this, getString(R.string.odsay_key));
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        X = (TextView) findViewById(R.id.x);
        Y = (TextView) findViewById(R.id.y);

        start1 = new StationPoint();
        start2 = new StationPoint();
    }

    private OnResultCallbackListener findMidStationListener = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            JSONObject jsonObject = new JSONObject();
            JSONArray subRouteArray = new JSONArray();
            RecommendStation recommendStation = new RecommendStation();
            int stationCount = 0, midStationCnt = 0, subCount = 0;
            int[] sectionCount = new int[30];

            //호출한 api가 맞을 경우
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                jsonObject = oDsayData.getJson();

                try {
                    subRouteArray = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONArray("subPath");
                    subCount = subRouteArray.length();

                    //subroute의 개수만큼 arraylist생성하여 값 할당
                    for(int i = 0; i < subCount; i++) {
                        if(subRouteArray.getJSONObject(i).getInt("trafficType") == 1) {
                            try {
                                sectionCount[i] = Integer.parseInt(subRouteArray.getJSONObject(i).getString("stationCount"));
                            }catch(NumberFormatException nfe){
                                nfe.printStackTrace();
                            }
                            stationCount += sectionCount[i];
                        }else{
                            sectionCount[i] = 0;
                        }
                    }
                    midStationCnt = stationCount / 2;
                    for(int i = 0; i < subCount; i++){
                        if(subRouteArray.getJSONObject(i).getInt("trafficType") == 1) {
                            if (sectionCount[i] > midStationCnt) {
                                recommendStation.setMidstation(subRouteArray.getJSONObject(i).getJSONObject("passStopList").getJSONArray("stations").getJSONObject(midStationCnt).getString("stationName"));
                                i = subCount;
                            }else{
                                midStationCnt = midStationCnt - sectionCount[i] -1;
                            }
                        }
                    }
                    Y.setText("midStation : " + recommendStation.getMidstation());
                    //recommendStation의 midstation에 중간역 저장되어 있음
                    //여기서 추천역 골라서 recommedstation 마져 채우고 intent 사용해서 search activity로 넘겨주면 됨 ㅇㅇ
                    //넘겨야 되는게 start1, start2, recommendstation이렇게!!
                    StationPoint recommend1= new StationPoint();
                    StationPoint recommend2= new StationPoint();
                    StationPoint recommend3= new StationPoint();
                    recommend1.setStationName("정자");
                    recommend2.setStationName("미금");
                    recommend3.setStationName("수내");

                    recommendStation.setRecommend1(recommend1);
                    recommendStation.setRecommend2(recommend2);
                    recommendStation.setRecommend3(recommend3);

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("start1", start1);
                    intent.putExtra("start2", start2);
                    intent.putExtra("recommend", recommendStation);

                    startActivity(intent);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
            //  stationPoint.setX("API : " + api.name() + "\n" + errorMessage);
            //  stationPoint.setY("API : " + api.name() + "\n" + errorMessage);
        }
    };

    private OnResultCallbackListener searchStationPointListener = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            //호출한 api가 맞을 경우
            if (api == API.SEARCH_STATION) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0);
                    //start1 할당
                    start1.setStationName(station);
                    start1.setX(Double.toString(jsonObject.getDouble("x")));
                    start1.setY(Double.toString(jsonObject.getDouble("y")));
                    //start2좌표 채워야딩
                    odsayService.requestSearchStation(station2, "1000", "2", "", "", "", searchStationPointListener2);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    private OnResultCallbackListener searchStationPointListener2 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            //호출한 api가 맞을 경우
            if (api == API.SEARCH_STATION) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0);
                    //start2 할당
                    start2.setStationName(station2);
                    start2.setX(Double.toString(jsonObject.getDouble("x")));
                    start2.setY(Double.toString(jsonObject.getDouble("y")));

                    odsayService.requestSearchPubTransPath(start1.getX(),start1.getY(), start2.getX(), start2.getY(),"0","","1", findMidStationListener);
                    X.setText("start1 (x,y = " + start1.getX() + ", " + start1.getY() + ") start2 (x, y = " + start2.getX() + ", " + start2.getY() + ")");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    public void findRecommend(){
        //api 호출 - 첫번째 출발역
        odsayService.requestSearchStation(station, "1000", "2", "","","", searchStationPointListener);
    }

    //클래스로 따로 만들어빼려고 햇는데 그럼 인텐트로 값을 못보내더라고 그래서 그냥 이렇게 해야될듯해해
}
