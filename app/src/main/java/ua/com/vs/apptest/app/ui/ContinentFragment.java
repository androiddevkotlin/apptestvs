package ua.com.vs.apptest.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.com.vs.apptest.app.R;
import ua.com.vs.apptest.app.Utils.MemoryUtil;
import ua.com.vs.apptest.app.adapter.RVAdapter;
import ua.com.vs.apptest.app.api.XMLRepo;
import ua.com.vs.apptest.app.interfaces.ObserverRepository;
import ua.com.vs.apptest.app.interfaces.OnRegionFragment;
import ua.com.vs.apptest.app.interfaces.RepositoryItem;
import ua.com.vs.apptest.app.models.Region;

public class ContinentFragment extends Fragment implements ObserverRepository, RVAdapter.OnChangeAction {
    @BindView(R.id.rv_word_regions)
    protected RecyclerView mRecyclerRegions;
    @BindView(R.id.progressBar)
    protected ProgressBar mProgressBar;
    @BindView(R.id.tv_memory_size)
    protected TextView mFieldMemorySize;
    RVAdapter mRvAdapter;
    RepositoryItem mRepositoryItem;
    private OnRegionFragment mListener;


    public ContinentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_continent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Download maps");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMemoryField();
        mRepositoryItem = new XMLRepo(getContext());
        mRvAdapter = new RVAdapter(this);
        mRecyclerRegions.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerRegions.setAdapter(mRvAdapter);
        if (mRepositoryItem != null) {
            mRepositoryItem.registerObserver(this);
            mRepositoryItem.loadRegionList(null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegionFragment) {
            mListener = (OnRegionFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRepositoryItem != null) {
            mRepositoryItem.registerObserver(this);
        }
    }

    @Override
    public void onStop() {
        mRepositoryItem.removeObserver();
        super.onStop();
    }

    private void initMemoryField() {
        long totalMemory = Long.parseLong(MemoryUtil.getTotalInternalMemorySize());
        long freeMemory = Long.parseLong(MemoryUtil.getAvailableInternalMemorySize());

        String str = mFieldMemorySize.getText() + "<b> " + MemoryUtil.formatSize(freeMemory) + "</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mFieldMemorySize.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            mFieldMemorySize.setText(Html.fromHtml(str));
        }
        mProgressBar.setMax((int) (totalMemory / 1000));
        mProgressBar.setProgress((int) (freeMemory / 1000));
    }

    @Override
    public void update(List<Region> list) {
        mRvAdapter.setNewData(list);
    }

    @Override
    public void onChangeRegion(Region region) {
        if (!region.isHasMap()) {
            mListener.applyRegionFragment(region.getName());
        } else {
            showSnackBar("download map: " + region.getName());
        }
    }

    private void showSnackBar(String s) {
        Snackbar.make(mRecyclerRegions, s, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }
}
