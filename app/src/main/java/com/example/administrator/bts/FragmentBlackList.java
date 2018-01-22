package com.example.administrator.bts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import model.ModelCarBlackList;
import model.ModelCarNearby;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBlackList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBlackList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBlackList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<ModelCarBlackList> carBlackList = new ArrayList<ModelCarBlackList>();
    private String response;
    MyListAdapter adapter;
    ListView lv;


    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            //如果返现msg.what=SHOW_RESPONSE，则进行制定操作，如想进行其他操作，则在子线程里将SHOW_RESPONSE改变
            switch (msg.what) {
                case 0:
                    String response = (String) msg.obj;
                    FragmentBlackList.this.updateView(response);
                default:
                    break;
            }
        }
    };



    public FragmentBlackList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBlackList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBlackList newInstance(String param1, String param2) {
        FragmentBlackList fragment = new FragmentBlackList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentBlackList getInstance() {

        return  newInstance("blacklist", "2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_black_list, container, false);
        lv = (ListView) v.findViewById(R.id.listview);

        adapter = new MyListAdapter(getActivity(), R.layout.layout_5col_items);
        lv.setAdapter(adapter);

        TextView tvCarNum = v.findViewById(R.id.title_carnumber);
        TextView tvCarOwner = v.findViewById(R.id.title_carowner);
        TextView tvCarStatus = v.findViewById(R.id.title_carstatus);
        TextView tvCarLostDate = v.findViewById(R.id.title_lost_Date);

        int width = lv.getWidth();
        tvCarNum.setWidth(width/4);
        tvCarOwner.setWidth(width/4);
        tvCarStatus.setWidth(width/4);
        tvCarLostDate.setWidth(width/4);
//        tvSearch.setWidth(width/5);

        ImageButton btnRefresh = v.findViewById(R.id.btn_refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection("http://47.96.38.194/bts/api/api_queryAllBlackListCarDetial.do");
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity(), "OnItemClickListener_" + id,
//                            Toast.LENGTH_SHORT).show();
                if( null != response ) {
                    mListener.onFragmentInteraction(Uri.parse(response + "parmdivider" + "blacklist" + "parmdivider" + id));
                }
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void updateView(String response) {
        if( null != carBlackList ) {
            carBlackList.clear();
        }
        try {
            this.response = response;
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("data");
            String plate_number; //车牌号
            String full_name; //姓名
            String add_time; //报案时间
            int status;//状态
            for( int i = 0; i < array.length(); i++) {
                JSONObject fullInfo  = array.getJSONObject(i);
                JSONObject owner = fullInfo.getJSONObject("carOwnerVO");
                plate_number = owner.getString("plate_number");
                full_name = owner.getString("full_name");
                status = fullInfo.getInt("status");
                add_time = fullInfo.getString("addtime_desc");

                carBlackList.add(new ModelCarBlackList(plate_number,full_name,status + "",add_time));
            }
            lv.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    // ---------------------------------------------------------------------------
    // adpter
    public class MyListAdapter extends ArrayAdapter<Object> {

        public MyListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return carBlackList.size();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if( null == convertView ){
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.layout_4col_items, null);
            }
            TextView tvCarNum = (TextView)convertView.findViewById(R.id.tv_col0);
            TextView tvCarOwner = (TextView)convertView.findViewById(R.id.tv_col1);
            TextView tvCarStatus = (TextView)convertView.findViewById(R.id.tv_col2);
            TextView tvLostDate = (TextView)convertView.findViewById(R.id.tv_col3);
//            ImageButton btn = convertView.findViewById(R.id.btn_col4);

            int width = parent.getWidth();
            tvCarNum.setWidth(width/4);
            tvCarOwner.setWidth(width/4);
            tvCarStatus.setWidth(width/4);
            tvLostDate.setWidth(width/4);
//            btn.setMinimumWidth(0);

            ModelCarBlackList md = carBlackList.get(position);
            tvCarNum.setText(md.strCarNumber);
            tvCarOwner.setText(md.strCarOwner);
            tvCarStatus.setText(md.strCarStatus);
            tvLostDate.setText(md.strAddTime);

            return convertView;
        }
    }


    private void sendRequestWithHttpURLConnection(final String strUrl ) {
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
                    message.what = 0;
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
//-------------------------------------------------------------------------
}
