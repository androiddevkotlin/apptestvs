package ua.com.vs.apptest.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.com.vs.apptest.app.R;
import ua.com.vs.apptest.app.adapter.RVAdapter;
import ua.com.vs.apptest.app.api.XMLRepo;
import ua.com.vs.apptest.app.interfaces.ObserverRepository;
import ua.com.vs.apptest.app.interfaces.OnRegionFragment;
import ua.com.vs.apptest.app.interfaces.RepositoryItem;
import ua.com.vs.apptest.app.models.Region;

public class RegionFragment extends Fragment implements ObserverRepository, RVAdapter.OnChangeAction {
    public static final String KEY_ARGUMENT = RegionFragment.class.getName();
    @BindView(R.id.rv_word_regions)
    protected RecyclerView mRecyclerRegions;

    private RVAdapter mRvAdapter;
    private RepositoryItem mRepositoryItem;
    private String mParentRegion;
    private OnRegionFragment mListener;

    public RegionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_region, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        mParentRegion = getArguments().getString(KEY_ARGUMENT);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(mParentRegion);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRepositoryItem = new XMLRepo(getContext());
        mRvAdapter = new RVAdapter(this);
        mRecyclerRegions.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerRegions.setAdapter(mRvAdapter);
        if (mRepositoryItem != null) {
            mRepositoryItem.registerObserver(this);
            mRepositoryItem.loadRegionList(mParentRegion);
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

    @Override
    public void update(List<Region> list) {
        mRvAdapter.setNewData(list);
    }

    @Override
    public void onChangeRegion(Region region) {
        if (!region.isHasMap()) {
            mListener.applyRegionFragment(region.getName());
        }else {
            showSnackBar("download map: " + region.getName());
        }
    }

    private void showSnackBar(String s) {
        Snackbar.make(mRecyclerRegions,s, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }
}
