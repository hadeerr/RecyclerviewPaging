package com.example.recyclerviewtest;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.recyclerviewtest.Adapter.RecyclerviewAdapter;
import com.example.recyclerviewtest.Model.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    LinearLayoutManager manager = new LinearLayoutManager(this);
    RecyclerviewAdapter mAdapter;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(manager);
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new RecyclerviewAdapter(new ArrayList<Item>());
        recyclerView.setAdapter(mAdapter);
        preparedListItem();
        recyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void preparedListItem() {
        final ArrayList<Item> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    itemCount++;
                    Log.d("", "run: " + itemCount);
                    Item postItem = new Item();
                    postItem.setTitle("this is message number  " + itemCount);
                    items.add(postItem);

                }
                if (currentPage != PAGE_START) mAdapter.removeLoading();
                mAdapter.addAll(items);
                if (currentPage < totalPage) mAdapter.addLoading();
                else isLastPage = true;
                isLoading = false;

            }
        }, 2000);
    }



    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        preparedListItem();


    }
}
