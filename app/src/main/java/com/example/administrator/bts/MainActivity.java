package com.example.administrator.bts;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.uart.SerialUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements FragmentNearby.OnFragmentInteractionListener ,
                                                                FragmentBlackList.OnFragmentInteractionListener,
        FragmentCarDetail.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SerialUtils serialUtils = SerialUtils.getInstance();

    private TabLayoutFragmentAdapter fragmentAdapter;
//  private ArrayList<ModelCarBlackList> carBlackList = new ArrayList<ModelCarBlackList>();

    FragmentNearby fragmentNearby;
    FragmentBlackList fragmentBlackList;
    FragmentCarDetail fragmentCarDetail;
    private  final static String BLACK_LIST_URL = "http://47.96.38.194/bts/api/api_queryAllBlackListCarDetial.do";
    private  final static String CAR_DETAIL = "http://47.96.38.194/bts/api/api_getCarDetailByTerminalNo.do?carOwnerVO.TermID=";

    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {

            String response = (String) msg.obj;
            //如果返现msg.what=SHOW_RESPONSE，则进行制定操作，如想进行其他操作，则在子线程里将SHOW_RESPONSE改变
            switch (msg.what) {
                case 1:
                    if (null != fragmentBlackList) {
                        fragmentBlackList.updateView(response);
                    }
                    break;
                case 2:
                    sendRequestWithHttpURLConnection(CAR_DETAIL+response,3);
                    break;
                case 3:
                    if(null != fragmentCarDetail){
                        fragmentCarDetail.updateView(response);
                    }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        serialUtils.open();
        serialUtils.setHandler(handler);

        this.viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        this.tabLayout = (TabLayout) this.findViewById(R.id.tabLayout);

        this.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialUtils.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initData() {
        String[] tabTitles = {"附近", "黑名单", "车辆详情"};
        fragmentNearby = FragmentNearby.getInstance();
        fragmentBlackList = FragmentBlackList.getInstance();
        fragmentCarDetail = FragmentCarDetail.getInstance();

        Fragment[] fragments = {
                fragmentNearby,
                fragmentBlackList,
                fragmentCarDetail,
        };
        this.fragmentAdapter = new TabLayoutFragmentAdapter(this.getSupportFragmentManager(), fragments, tabTitles);
        this.viewPager.setAdapter(this.fragmentAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager);

        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                switch ( position ){
                    case 1:
                        sendRequestWithHttpURLConnection(BLACK_LIST_URL,position);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        viewPager.setCurrentItem(2);
        fragmentCarDetail.updateView(uri.toString());
//        Toast.makeText(this, uri.toString(),
//                            Toast.LENGTH_SHORT).show();

    }



    private void sendRequestWithHttpURLConnection(final String strUrl,final int position) {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(strUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line = null;
                    while ((line = bufr.readLine()) != null) {
                        response.append(line);
                    }

                    Message message = new Message();
                    message.what = position;
                    //将服务器返回的数据存放到Message中
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}


