package com.ruehyeon.sanmo.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.ruehyeon.sanmo.ChattingActivity;
import com.ruehyeon.sanmo.MainViewModel;
import com.ruehyeon.sanmo.R;
import com.ruehyeon.sanmo.models.RuleDatabase;

import retrofit2.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class Rules extends Fragment {

    LottieAnimationView animationView;
    LottieAnimationView pop_anim;
    RecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    MainViewModel model;
    private int currentPosition;
    int user_id;
    WebView webview;
    WebSettings settings;
    final public Handler handler1 = new Handler();
    SharedPreferences pref;
    Retrofit retrofit;
    String pass;

    private static final String ARG_SECTION_NUMBER = "section_number";



    public static Rules newInstance(int index) {
        Rules fragment = new Rules();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = root.findViewById(R.id.recycler1);

        recyclerViewAdapter = new RecyclerViewAdapter(getContext());
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                RuleDatabase db = RuleDatabase.getRuleDatabase(getContext(), "user");

                Log.d("온클릭", "클릭됨" );

                Intent intent = new Intent(getContext(), ChattingActivity.class);
                intent.putExtra("uuid", db.rulesDao().loadNotification(position).counterpart_UUID);
                startActivity(intent);
                //p = myImageList.get(position);
                //n = myNameList.get(position);
                //id = myUUIDList.get(position);
                // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
            }
        }) ;
        //recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        model = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(MainViewModel.class);
        model.getAllNotification().observe(getViewLifecycleOwner(), entities -> {
            recyclerViewAdapter.setEntities(entities);
            recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());
        });
        return root;
    }
}