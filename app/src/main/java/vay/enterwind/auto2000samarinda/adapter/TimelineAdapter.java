package vay.enterwind.auto2000samarinda.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Timeline;
import vay.enterwind.auto2000samarinda.models.Type;
import vay.enterwind.auto2000samarinda.utils.DateTimeUtils;
import vay.enterwind.auto2000samarinda.utils.VectorDrawableUtils;

/**
 * Created by novay on 08/03/18.
 */

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<Timeline> itemList;
    private Context context;

    private OnLoadMoreListener onLoadMoreListener;

    private boolean isMoreLoading = true;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public TimelineAdapter(OnLoadMoreListener onLoadMoreListener, Context context) {
        this.onLoadMoreListener = onLoadMoreListener;
        itemList = new ArrayList<>();
        this.context = context;
    }

    public TimelineAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (viewType == VIEW_ITEM) {
            return new TimelineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timeline, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }
    }

    public void showLoading() {
        if (isMoreLoading && itemList != null && onLoadMoreListener != null) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    itemList.add(null);
                    notifyItemInserted(itemList.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (itemList != null && itemList.size() > 0) {
            itemList.remove(itemList.size() - 1);
            notifyItemRemoved(itemList.size());
        }
    }

    public void addAll(List<Timeline> lst){
        itemList.clear();
        itemList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<Timeline> lst){
        int sizeInit = itemList.size();
        itemList.addAll(lst);
        notifyItemRangeChanged(sizeInit, itemList.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TimelineViewHolder) {
            final Timeline singleItem = (Timeline) itemList.get(position);

            if(singleItem.getStatus() == Type.NONE) {
                ((TimelineViewHolder) holder).mTimelineView.setMarker(VectorDrawableUtils.getDrawable(this.context, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
            } else if(singleItem.getStatus() == Type.FULL) {
                ((TimelineViewHolder) holder).mTimelineView.setMarker(VectorDrawableUtils.getDrawable(this.context, R.drawable.ic_marker_active, R.color.colorPrimary));
            } else {
                ((TimelineViewHolder) holder).mTimelineView.setMarker(ContextCompat.getDrawable(this.context, R.drawable.ic_marker), ContextCompat.getColor(this.context, R.color.colorPrimary));
            }

            ((TimelineViewHolder) holder).txtPesan.setText(singleItem.getMessage());
            ((TimelineViewHolder) holder).txtTanggal.setText(singleItem.getDate());


        }
    }

    @Override
    public int getItemCount() {
        // return itemList.size();
        return (itemList!=null ? itemList.size() : 0);
    }

    static class TimelineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtTanggal) TextView txtTanggal;
        @BindView(R.id.txtPesan) TextView txtPesan;
        @BindView(R.id.time_marker) TimelineView mTimelineView;

        TimelineViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pBar) ProgressBar pBar;
        ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
