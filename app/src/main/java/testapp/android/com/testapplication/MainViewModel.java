package testapp.android.com.testapplication;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import testapp.android.com.testapplication.model.News;
import testapp.android.com.testapplication.model.Rows;
import testapp.android.com.testapplication.remote.RemoteService;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<String> mObservableError = new MutableLiveData<>();

    private MutableLiveData<List<News>> mObservableNews = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        RemoteService.getInstance().getNews(new Callback<Rows>() {
            @Override
            public void onResponse(Call<Rows> call, Response<Rows> response) {
                mObservableNews.setValue(response.body().getRows());
                mObservableError = null;
            }

            @Override
            public void onFailure(Call<Rows> call, Throwable t) {
                mObservableError.setValue(t.getMessage());
            }
        });
    }

    public MutableLiveData<List<News>> getNewsData() {
        return mObservableNews;
    }

    public MutableLiveData<String> getError(){
        return mObservableError;
    }

    public void refreshData(){
        RemoteService.getInstance().getNews(new Callback<Rows>() {
            @Override
            public void onResponse(Call<Rows> call, Response<Rows> response) {
                mObservableNews.setValue(response.body().getRows());
            }

            @Override
            public void onFailure(Call<Rows> call, Throwable t) {
                mObservableError.setValue(t.getMessage());
            }
        });
    }
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainViewModel(mApplication);
        }
    }


}