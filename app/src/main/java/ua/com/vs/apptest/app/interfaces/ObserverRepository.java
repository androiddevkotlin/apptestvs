package ua.com.vs.apptest.app.interfaces;

import java.util.List;

import ua.com.vs.apptest.app.models.Region;

public interface ObserverRepository {
    void update(List<Region> list);
}
