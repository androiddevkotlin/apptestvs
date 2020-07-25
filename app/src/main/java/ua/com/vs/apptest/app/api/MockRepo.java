package ua.com.vs.apptest.app.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.com.vs.apptest.app.interfaces.ObserverRepository;
import ua.com.vs.apptest.app.interfaces.RepositoryItem;
import ua.com.vs.apptest.app.models.Region;

public class MockRepo implements RepositoryItem {

    private ObserverRepository mObserverRepository;
    private List<Region> mList = new ArrayList<>();

    @Override
    public void loadRegionList(String nodes) {
        mList.add(new Region("Africa", true, true));
        mList.add(new Region("Ukraine", false, false));
        mList.add(new Region("Germany", false, true));
        mList.add(new Region("Europe", true, true));
        mList.add(new Region("Asia", true, true));
        Collections.sort(mList, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        notifyObserver();
    }

    @Override
    public void registerObserver(ObserverRepository o) {
        mObserverRepository = o;

    }

    @Override
    public void removeObserver() {
        mObserverRepository = null;

    }

    @Override
    public void notifyObserver() {
        mObserverRepository.update(mList);

    }
}
