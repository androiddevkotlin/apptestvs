package ua.com.vs.apptest.app.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import ua.com.vs.apptest.app.interfaces.ObserverRepository;
import ua.com.vs.apptest.app.interfaces.RepositoryItem;
import ua.com.vs.apptest.app.models.Region;


public class XMLRepo implements RepositoryItem {

    private Context mContext;
    private ObserverRepository mObserverRepository;
    private List<Region> mList = new LinkedList<>();

    public XMLRepo(Context context) {
        mContext = context;
    }

    @Override
    public void loadRegionList(String nodes) {
        mList.clear();
        if (nodes == null || nodes.isEmpty()) {
            getListContinent();
        } else {
            getListChildRegion(nodes);
        }
        notifyObserver();
    }

    private void getListContinent() {
        XmlPullParser xpp;
        try {
            xpp = prepareXpp();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth() == 2) {
                            mList.add(getRegion(xpp));
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getListChildRegion(String nodes) {
        int saveDepth = 0;
        XmlPullParser xpp;

        try {
            xpp = prepareXpp();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equalsIgnoreCase("region")) {
                            if (saveDepth != 0 && xpp.getDepth() == saveDepth + 1) {
                                mList.add(getRegion(xpp));
                            } else {
                                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                    if (xpp.getAttributeName(i).equalsIgnoreCase("name") && xpp.getAttributeValue(i).equalsIgnoreCase(nodes)) {
                                        saveDepth = xpp.getDepth();
                                        break;
                                    }
                                }
                            }

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (xpp.getDepth() == saveDepth) {
                            return;
                        } else {
                            break;
                        }

                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    private Region getRegion(XmlPullParser xpp) {
        Region region = new Region();
        boolean mapDefined = false;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "name":
                    region.setName(firstUpperCase(xpp.getAttributeValue(i)));
                    break;
                case "map":
                    if (!mapDefined) {
                        region.setHasMap(xpp.getAttributeValue(i).equalsIgnoreCase("yes"));
                    }
                    break;
                case "type":
                    region.setHasMap(xpp.getAttributeValue(i).equalsIgnoreCase("map"));
                    mapDefined = true;
                    break;

            }


            region.setContinent(xpp.getDepth() == 2);

        }
        return region;
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
        Collections.sort(mList, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        mObserverRepository.update(mList);

    }


    private XmlPullParser prepareXpp() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        try {
            xpp.setInput(new InputStreamReader(mContext.getAssets().open("regions.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xpp;
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }


}
