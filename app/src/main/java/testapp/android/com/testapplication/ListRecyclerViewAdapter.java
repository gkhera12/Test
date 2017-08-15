package testapp.android.com.testapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import testapp.android.com.testapplication.model.News;

/**
 * {@link RecyclerView.Adapter} that can display a {@link News} and makes a call to the
 * specified {@link MainActivity}.
 */
public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private List<News> mNewsList;

    @Nullable
    private MainActivity mListener;

    private Context context;

    public ListRecyclerViewAdapter(Context context, @Nullable MainActivity listener) {
        mListener = listener;
        this.context = context;
    }

    /**
    * setNewsList is called by View everytime the data is changed by the ViewModel.
     * It compares the new data with old data and changes the data only if necessary.
    * */
    public void setNewsList(final List<News> newsList) {
        if (mNewsList == null) {
            mNewsList = newsList;
            notifyItemRangeInserted(0, newsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mNewsList.size();
                }

                @Override
                public int getNewListSize() {
                    return newsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mNewsList.get(oldItemPosition).getTitle() ==
                            newsList.get(newItemPosition).getTitle();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    News news = newsList.get(newItemPosition);
                    News old = newsList.get(oldItemPosition);
                    return news.getTitle() == old.getTitle()
                            && Objects.equals(news.getTitle(), old.getTitle());
                }
            });
            mNewsList = newsList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mNewsList.get(position);
        holder.titleView.setText(mNewsList.get(position).getTitle());
        holder.descriptionView.setText(mNewsList.get(position).getDescription());
        String imageUrl = mNewsList.get(position).getImageHref();
        if(!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(context).load(imageUrl).into(holder.imageView);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListInteraction(holder.mItem);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final TextView descriptionView;
        public final ImageView imageView;

        public News mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = (TextView) view.findViewById(R.id.item_title);
            descriptionView = (TextView)view.findViewById(R.id.item_description);
            imageView = (ImageView)view.findViewById(R.id.item_image);
        }

    }
}
