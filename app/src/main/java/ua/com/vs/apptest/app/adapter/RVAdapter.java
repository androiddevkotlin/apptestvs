package ua.com.vs.apptest.app.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.com.vs.apptest.app.R;
import ua.com.vs.apptest.app.models.Region;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private final OnChangeAction mListener;

    public interface OnChangeAction {
        void onChangeRegion(Region region);
    }

    private List<Region> mList = null;

    public RVAdapter(OnChangeAction listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_card, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null || mList.size() == 0) return;
        Region region = mList.get(position);
        holder.mName.setText(region.getName());
        if (region.isContinent()) {
            holder.mContinent.setImageResource(R.drawable.ic_world_globe_dark);
        } else {
            holder.mContinent.setImageResource(R.drawable.ic_map);

        }
        if (!region.isHasMap()) {
            holder.mImport.setVisibility(View.GONE);
        } else {
            holder.mImport.setImageResource(R.drawable.ic_action_import);
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public void setNewData(List<Region> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_region)
        TextView mName;
        @BindView(R.id.iv_ic_map)
        ImageView mContinent;
        @BindView(R.id.iv_ic_import)
        ImageView mImport;
        private View mRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRootView = itemView;
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onChangeRegion(mList.get(getAdapterPosition()));
                    }
                }
            });
        }

    }
}
