package testapp.android.com.testapplication;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import testapp.android.com.testapplication.databinding.ActivityMainBinding;
import testapp.android.com.testapplication.model.News;

public class MainActivity extends LifecycleActivity{

    private ActivityMainBinding mBinding;
    private ListRecyclerViewAdapter listRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        listRecyclerViewAdapter = new ListRecyclerViewAdapter(this, this);
        mBinding.listView.setAdapter(listRecyclerViewAdapter);

        List<News> news = new ArrayList<>();
        News news1 = new News();
        news1.setTitle("My Title");
        news1.setDescription("My Description");
        news1.setImageHref("https://dummyimage.com/popunder");
        news.add(news1);
        listRecyclerViewAdapter.setNewsList(news);
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
        if(item.getTitle() == null){
            setTitle(getResources().getString(R.string.list_no_title));
        }else{
            setTitle(item.getTitle());
        }
    }
}
