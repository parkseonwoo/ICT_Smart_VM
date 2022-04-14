package net.embsys.homeiot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment1Home extends Fragment {
    View view;
    MainActivity mainActivity;

    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Button payment;

    ViewPager viewPager;
    ViewPagerAdapter pageradapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    // ArrayList에 person 객체(이름과 번호) 넣기
    List<Items> person = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment1home, container, false);
        mainActivity = (MainActivity)getActivity();

        RecyclerView itemListView = view.findViewById(R.id.itemListView);

        payment = view.findViewById(R.id.payment);


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Payment.class);
                startActivity(intent);
            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        pageradapter = new ViewPagerAdapter(mainActivity);
        viewPager.setAdapter(pageradapter);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 9) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        linearLayoutManager = new LinearLayoutManager(mainActivity);
        //지정된 레이아웃매니저를 RecyclerView에 Set
        itemListView.setLayoutManager(new GridLayoutManager(mainActivity, 4));

        //recyclerview 항목들 사이에 구분선 추가
        //수평, 수직의 스크롤 리스트 / getOrientation을 이용하여 스크롤 방향 설정
        itemListView.addItemDecoration(
                new DividerItemDecoration(mainActivity,linearLayoutManager.getOrientation()));

        //person.add(new Items("NEO","2000", R.drawable.coke));

        //recvDataProcess(mainActivity.clientThread.getData());

        // Adapter생성
        recyclerViewAdapter = new RecyclerViewAdapter(mainActivity,person);
        itemListView.setAdapter(recyclerViewAdapter);

        return view;

    }

    public void recvDataProcess(String recvData) {
        String[] splitLists = recvData.toString().split("\\[|]|%");
        Log.d("hihihihi",":" + splitLists[0] + ", " + splitLists[2]);
        //[KSH_AND]LIGHT@ON    splitLists[1]:BSJ_DB, splitLists[2]:START 또는  UPDATE, splitLists[3]:%
        if(splitLists[2].equals("START")) {
            for(int i = 3; i < splitLists.length; i++) {
                String[] product = splitLists[i].toString().split("\\@");
                Log.d("recvDataProcess00",product[0]+ " " + product[1]);

                person.add(new Items(product[0], product[1], product[2], product[3]));
                recyclerViewAdapter.setFriendList(person);
            }
        }
        else if(splitLists[2].equals("UPDATE")) {
            for(int i = 3; i < splitLists.length; i++) {
                String[] product = splitLists[i].toString().split("\\@");
                Log.d("recvDataProcess01",product[0]+ " " + product[1]);

                person.add(new Items(product[0], product[1], product[2], product[3]));
                recyclerViewAdapter.setFriendList(person);
            }
        }
    }
}
