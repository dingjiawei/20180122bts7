package com.example.administrator.bts;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

import model.ModelCarNearby;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNearby.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNearby#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNearby extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<ModelCarNearby> carNearbyArrayList = new ArrayList<ModelCarNearby>();

    String response;

    public FragmentNearby() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNearby.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNearby newInstance(String param1, String param2) {
        FragmentNearby fragment = new FragmentNearby();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentNearby getInstance() {

        return  newInstance("nearby", "2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        carNearbyArrayList.add(new ModelCarNearby("粤B607sw","sam","nomal",0));
        carNearbyArrayList.add(new ModelCarNearby("粤Ben098","kenny","nomal",1));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","hh","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rgafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rffg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","deafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","twhst","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","jraffjjsx","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rgafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rffg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rrrrrr","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","twffgshst","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","jraerfeffjjsx","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rgasfg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rffdfsfg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","deesfgafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","twfehst","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","jraffjjsx","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rgiiiafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","rffioog","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","deqqqafg","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","twhwwwst","nomal",5));
        carNearbyArrayList.add(new ModelCarNearby("粤Bxxxxx","jrarffjjsx","nomal",5));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_nearby, container, false);
        ListView lv = (ListView) v.findViewById(R.id.nearby_listview);

        MyListAdapter adapter = new MyListAdapter(getActivity(), R.layout.layout_5col_items);
        lv.setAdapter(adapter);

        TextView tvCarNum = v.findViewById(R.id.title_carnumber);
        TextView tvCarOwner = v.findViewById(R.id.title_carowner);
        TextView tvCarStatus = v.findViewById(R.id.title_carstatus);
        TextView tvCarScanedTime = v.findViewById(R.id.title_timescaned);

        int width = lv.getWidth();
        tvCarNum.setWidth(width/4);
        tvCarOwner.setWidth(width/4);
        tvCarStatus.setWidth(width/4);
        tvCarScanedTime.setWidth(width/4);

        ImageButton btnRefresh = v.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "btn click",
//                        Toast.LENGTH_SHORT).show();
                // 启动串口扫描 , 扫描周围的设备
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity(), "OnItemClickListener_" + id,
//                            Toast.LENGTH_SHORT).show();
                if( null != response ) {
                    mListener.onFragmentInteraction(Uri.parse(response + "parmdivider" + "nearbylist" + "parmdivider" + id));
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

    // ---------------------------------------------------------------------------
    // adpter
    public class MyListAdapter extends ArrayAdapter<Object>{

        public MyListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return carNearbyArrayList.size();
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
            TextView tvCarScan = (TextView)convertView.findViewById(R.id.tv_col3);

            int width = parent.getWidth();
            tvCarNum.setWidth(width/4);
            tvCarOwner.setWidth(width/4);
            tvCarStatus.setWidth(width/4);
            tvCarScan.setWidth(width/4);

            ModelCarNearby md = carNearbyArrayList.get(position);
            tvCarNum.setText(md.strCarNumber);
            tvCarOwner.setText(md.strCarOwner);
            tvCarStatus.setText(md.strCarStatus);
            tvCarScan.setText(md.iCarScanedTime+"");

            return convertView;
        }
    }




}


