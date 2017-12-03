package example.mobileprogramming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONException;

/**
 * Created by user on 2017-12-03.
 */

public class SearchActivity extends Activity {
    private ODsayService odsayService;
    private TextView text1, text2, text3, text4;

    //출발역들 정보저장 역이름, x, y좌표
    private StationPoint start1;
    private StationPoint start2;
    private RecommendStation recommendStation;
    private String xPoint, yPoint;
    private int countFlag = 0;
    private double[][] totalTime;
    private Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();

        //이제 그 출발역이랑 추천역 잇자나 값을 다 받아왔다??그럼 이제 여기서 부터 각자 평균소요시간을 구해줘야해
        text1.setText("!");
        text2.setText("!");
        text3.setText("!");
        text4.setText("!");


        findAverageTime();

        findAverageTime2();

        findAverageTime3();
    }

    private void init() {
        //API 설정 초기화
        odsayService = ODsayService.init(this, getString(R.string.odsay_key));
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        totalTime = new double[3][2];
        getIntent = getIntent();

        start1 = (StationPoint) getIntent.getSerializableExtra("start1");
        start2 = (StationPoint) getIntent.getSerializableExtra("start2");
        recommendStation = (RecommendStation) getIntent.getSerializableExtra("recommend");
    }

    /********************************************recommend1*********************************************/
    private OnResultCallbackListener findAverageTotalTimeListener = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[0][1] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    recommendStation.setAverageTime1((int)((totalTime[0][0] + totalTime[0][1]) / 2));
//                    text1.setText("recommend1 : " + recommendStation.getRecommend1().getStationName() + "  (x, y)" + recommendStation.getRecommend1().getX() + ", " + recommendStation.getRecommend1().getY() + "\n" + "averageTime = " + recommendStation.getAverageTime1());

//                    findAverageTime2();
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    private OnResultCallbackListener findTotalTimeListener = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[0][0] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    odsayService.requestSearchPubTransPath(start2.getX(),start2.getY(), recommendStation.getRecommend1().getX(), recommendStation.getRecommend1().getY(),"0","","0", findAverageTotalTimeListener);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };
    private OnResultCallbackListener searchStationPointListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
//            JSONObject jsonObject = new JSONObject();
            if (api == API.SEARCH_STATION) {
                try {
//                    jsonObject = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0);
                    recommendStation.getRecommend1().setX(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("x")));
                    recommendStation.getRecommend1().setY(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("y")));
                    odsayService.requestSearchPubTransPath(start1.getX(),start1.getY(), recommendStation.getRecommend1().getX(), recommendStation.getRecommend1().getY(),"0","","0", findTotalTimeListener);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    /********************************************recommend2*********************************************/
    private OnResultCallbackListener findAverageTotalTimeListener2 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[1][1] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    recommendStation.setAverageTime2((int)((totalTime[1][0] + totalTime[1][1]) / 2));
//                    text2.setText("recommend2 : " + recommendStation.getRecommend2().getStationName() + "  (x, y)" + recommendStation.getRecommend2().getX() + ", " + recommendStation.getRecommend2().getY() + "\n" + "averageTime = " + recommendStation.getAverageTime2());

//                    findAverageTime3();
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    private OnResultCallbackListener findTotalTimeListener2 = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[1][0] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    odsayService.requestSearchPubTransPath(start2.getX(),start2.getY(), recommendStation.getRecommend2().getX(), recommendStation.getRecommend2().getY(),"0","","0", findAverageTotalTimeListener2);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };
    private OnResultCallbackListener searchStationPointListener2 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
//            JSONObject jsonObject = new JSONObject();
            if (api == API.SEARCH_STATION) {
                try {
//                    jsonObject = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0);
                    recommendStation.getRecommend2().setX(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("x")));
                    recommendStation.getRecommend2().setY(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("y")));
                    odsayService.requestSearchPubTransPath(start1.getX(),start1.getY(), recommendStation.getRecommend2().getX(), recommendStation.getRecommend2().getY(),"0","","0", findTotalTimeListener2);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    /********************************************recommend3*********************************************/
    private OnResultCallbackListener findAverageTotalTimeListener3 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[2][1] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    recommendStation.setAverageTime3((int)((totalTime[2][0] + totalTime[2][1]) / 2));

                    text1.setText("recommend1 : " + recommendStation.getRecommend1().getStationName() + "  (x, y)" + recommendStation.getRecommend1().getX() + ", " + recommendStation.getRecommend1().getY() + "\n" + "averageTime = " + recommendStation.getAverageTime1());
                    text2.setText("recommend2 : " + recommendStation.getRecommend2().getStationName() + "  (x, y)" + recommendStation.getRecommend2().getX() + ", " + recommendStation.getRecommend2().getY() + "\n" + "averageTime = " + recommendStation.getAverageTime2());
                    text3.setText("recommend3 : " + recommendStation.getRecommend3().getStationName() + "  (x, y)" + recommendStation.getRecommend3().getX() + ", " + recommendStation.getRecommend3().getY() + "\n" + "averageTime = " + recommendStation.getAverageTime3());
                    //intent!!!!!!!!!!!!!!1- 실제로는 선택한것 넣어야 함 recommend저거
                    Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                    intent.putExtra("start",start1);
                    intent.putExtra("end",recommendStation.getRecommend2());
                    startActivity(intent);

                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    private OnResultCallbackListener findTotalTimeListener3 = new OnResultCallbackListener() {
        @Override
        //api 호출 성공
        public void onSuccess(ODsayData oDsayData, API api) {
            if (api == API.SEARCH_PUB_TRANS_PATH) {
                try {
                    totalTime[2][0] = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(0).getJSONObject("info").getInt("totalTime");
                    odsayService.requestSearchPubTransPath(start2.getX(),start2.getY(), recommendStation.getRecommend3().getX(), recommendStation.getRecommend3().getY(),"0","","0", findAverageTotalTimeListener3);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };
    private OnResultCallbackListener searchStationPointListener3 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
//            JSONObject jsonObject = new JSONObject();
            if (api == API.SEARCH_STATION) {
                try {
//                    jsonObject = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0);
                    recommendStation.getRecommend3().setX(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("x")));
                    recommendStation.getRecommend3().setY(Double.toString(oDsayData.getJson().getJSONObject("result").getJSONArray("station").getJSONObject(0).getDouble("y")));
                    odsayService.requestSearchPubTransPath(start1.getX(),start1.getY(), recommendStation.getRecommend3().getX(), recommendStation.getRecommend3().getY(),"0","","0", findTotalTimeListener3);
                }catch (JSONException e) {
                    e.printStackTrace();
                }}
        }
        @Override
        public void onError(int i, String errorMessage, API api) {
        }
    };

    /********************************************************평균소요시간계산 함수*****************************************************************/
    public void findAverageTime(){
        //api 호출
        odsayService.requestSearchStation(recommendStation.getRecommend1().getStationName(), "1000", "2", "","","", searchStationPointListener);
    }
    public void findAverageTime2(){
        //api 호출
        odsayService.requestSearchStation(recommendStation.getRecommend2().getStationName(), "1000", "2", "","","", searchStationPointListener2);
    }
    public void findAverageTime3(){
        //api 호출
        odsayService.requestSearchStation(recommendStation.getRecommend3().getStationName(), "1000", "2", "","","", searchStationPointListener3);
    }
}
