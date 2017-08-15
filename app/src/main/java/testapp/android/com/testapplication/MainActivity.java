package testapp.android.com.testapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import testapp.android.com.testapplication.databinding.ActivityMainBinding;
import testapp.android.com.testapplication.model.News;

public class MainActivity extends AppCompatLifecycleActivity {

    private ActivityMainBinding mBinding;
    private ListRecyclerViewAdapter listRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        listRecyclerViewAdapter = new ListRecyclerViewAdapter(this, this);
        mBinding.listView.setAdapter(listRecyclerViewAdapter);

        MainViewModel.Factory factory = new MainViewModel.Factory(getApplication());
        final MainViewModel viewModel =
                ViewModelProviders.of(this, factory).get(MainViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(MainViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getNewsData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                if (newsList != null) {
                    mBinding.setIsLoading(false);
                    listRecyclerViewAdapter.setNewsList(newsList);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    public void onListInteraction(News item){
        if(TextUtils.isEmpty(item.getTitle())){
            setTitle(getResources().getString(R.string.list_no_title));
        }else{
            setTitle(item.getTitle());
        }
    }
}
