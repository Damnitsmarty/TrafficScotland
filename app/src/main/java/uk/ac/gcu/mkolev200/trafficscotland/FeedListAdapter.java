package uk.ac.gcu.mkolev200.trafficscotland;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gcu.mkolev200.trafficscotland.data.FeedItem;
import uk.ac.gcu.mkolev200.trafficscotland.databinding.FeedItemBinding;

/** Created by Martin Kolev (S1435614) */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder> {

    private List<FeedItem> feedItems;
    private Context ctx;
    private FeedListFragment.OnFragmentItemInteractionListener m_listener;

    public FeedListAdapter(List<FeedItem> items){
        this.feedItems = items;
    }
    @Override
    public FeedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);

        FeedListViewHolder vh = new FeedListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(FeedListViewHolder holder, int position) {
        final FeedItem item = feedItems.get(position);
        holder.getBinding().setVariable(uk.ac.gcu.mkolev200.trafficscotland.BR.feedItem, item);
        holder.getBinding().executePendingBindings();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_listener != null)
                    m_listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(feedItems == null) feedItems = new ArrayList<>();
        return feedItems.size();
    }

    public void attachListener(FeedListFragment.OnFragmentItemInteractionListener listener){
        m_listener = listener;
    }

    public boolean swapList(List<FeedItem> newList){
        if (newList == null) return false;

        if(feedItems.size() > 0)
            feedItems.clear();
        feedItems.addAll(newList);
        notifyDataSetChanged();
        return true;
    }

    public static class FeedListViewHolder extends RecyclerView.ViewHolder {

        private FeedItemBinding binding;

        public FeedListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public FeedItemBinding getBinding(){
            return binding;
        }
    }
}
