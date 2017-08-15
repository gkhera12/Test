package testapp.android.com.testapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import testapp.android.com.testapplication.databinding.ActivityMainBinding;
import testapp.android.com.testapplication.model.News;
import testapp.android.com.testapplication.remote.RemoteService;

public class MainActivity extends AppCompatLifecycleActivity {

    private ActivityMainBinding mBinding;
    private ListRecyclerViewAdapter listRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        listRecyclerViewAdapter = new ListRecyclerViewAdapter(getApplicationContext(), this);
        mBinding.listView.setAdapter(listRecyclerViewAdapter);

        RemoteService remoteService = RemoteService.getInstance();
        MainViewModel.Factory factory = new MainViewModel.Factory(getApplication(), remoteService);
        final MainViewModel viewModel =
                ViewModelProviders.of(this, factory).get(MainViewModel.class);

        subscribeUi(viewModel);

        mBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshData();
            }
        });
    }

    /**
     * Subscribe to live data from View Model to disconnect UI from data
     * */
    private void subscribeUi(MainViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getNewsData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                mBinding.swiperefresh.setRefreshing(false);
                if (newsList != null) {
                    mBinding.loadingWorkouts.setVisibility(View.GONE);
                    listRecyclerViewAdapter.setNewsList(newsList);
                } else {
                    mBinding.loadingWorkouts.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                mBinding.swiperefresh.setRefreshing(false);
                if(error != null){
                    Toast.makeText(getApplicationContext(), "Network Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This listener is used by Activity to listen to click events from the Adapter
     * and change the title based on the click events.
     * */
    public void onListInteraction(News item){
        if(TextUtils.isEmpty(item.getTitle())){
            setTitle(getResources().getString(R.string.list_no_title));
        }else{
            setTitle(item.getTitle());
        }
    }

}
