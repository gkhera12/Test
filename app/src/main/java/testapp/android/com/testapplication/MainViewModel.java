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
/**
 * MainViewModel supports MainActivity by providing data
 * It creates observables for the data which View can subscribe to listen for data.
 * mObservableNews is used for reporting NewsData from the webservice.
 * mObservableError is used for reporting error to the View.
 * **/

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<String> mObservableError = new MutableLiveData<>();

    private MutableLiveData<List<News>> mObservableNews = new MutableLiveData<>();

    private RemoteService remoteService;

    public MainViewModel(Application application, RemoteService remoteService) {
        super(application);
        this.remoteService = remoteService;
        remoteService.getNews(new Callback<Rows>() {
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

    public MutableLiveData<List<News>> getNewsData() {
        return mObservableNews;
    }

    public MutableLiveData<String> getError(){
        return mObservableError;
    }

    /**
     * Used by View to refresh the data from the webservice on swipe.
     * */
    public void refreshData(){
        remoteService.getNews(new Callback<Rows>() {
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

        private final RemoteService mRemoteService;
        public Factory(@NonNull Application application, RemoteService remoteService) {
            mApplication = application;
            mRemoteService = remoteService;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainViewModel(mApplication,mRemoteService);
        }
    }


}