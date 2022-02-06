package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.View;

import com.freddon.android.snackkit.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by fred on 2016/11/5.
 */

/**
 * Created by fred on 2016/11/5.
 */

public class RecyclerLayersLayout extends LayersLayout {


    public SmartRefreshLayout refreshLayout;

    public RecyclerView rvDataView;

    public RecyclerLayersLayout(Context context) {
        this(context, null);
    }

    public RecyclerLayersLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerLayersLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        refreshLayout = (SmartRefreshLayout) View.inflate(context, R.layout.recyler_layers_layout_item, null);
        setCustomerView(refreshLayout);
        rvDataView = refreshLayout.findViewById(R.id.rv_data_view);
        rvDataView.setLayoutManager(new LinearLayoutManager(context));
//        rvDataView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000,
                        pullRefreshAndLoadMoreEvent != null &&
                                !pullRefreshAndLoadMoreEvent.onRefresh(refreshlayout));
            }
        });
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (pullRefreshAndLoadMoreEvent != null)
                pullRefreshAndLoadMoreEvent.onLoadMore(refreshlayout);
            refreshlayout.finishLoadMore(1000,
                    true
                    , pullRefreshAndLoadMoreEvent != null &&
                            !pullRefreshAndLoadMoreEvent.hasMoreData());
        });
    }

    public void disableFreshAndLoad(boolean isForbidden) {
        if (refreshLayout != null) {
            refreshLayout.setEnableLoadMore(!isForbidden);
            refreshLayout.setEnableRefresh(!isForbidden);
        }
    }


    @Override
    protected void initChild() {

    }

    public void setPullRefreshAndLoadMoreEvent(PullRefreshAndLoadMoreEvent pullRefreshAndLoadMoreEvent) {
        this.pullRefreshAndLoadMoreEvent = pullRefreshAndLoadMoreEvent;
    }

    PullRefreshAndLoadMoreEvent pullRefreshAndLoadMoreEvent;

    public interface PullRefreshAndLoadMoreEvent {

        boolean onRefresh(RefreshLayout refreshlayout);

        boolean onLoadMore(RefreshLayout refreshlayout);

        boolean hasMoreData();
    }
}
