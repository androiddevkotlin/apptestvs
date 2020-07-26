package testapp.downloadmaps;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;

public class ParserXml {

    private Context context;

    public ParserXml(Context context){
        this.context = context;
    }




    public ArrayList<String> parserXmlForCountries() {
        ArrayList<String> listNamesCountries = new ArrayList<>();

        try {

            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.regions);

            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                switch (xmlPullParser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT: {
                        if (BuildConfig.DEBUG) {

                        }
                        break;
                    }

                    case XmlPullParser.START_TAG: {
                        if (BuildConfig.DEBUG) {

                        }

                        if (xmlPullParser.getName().equals("region")) {
                            if (xmlPullParser.getAttributeValue(null, "lang") != null) {
                                listNamesCountries.add(xmlPullParser.getAttributeValue(null, "name"));
                            }

                        }
                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        if (BuildConfig.DEBUG) {

                        }
                        break;
                    }
                    default:
                        break;

                }
                xmlPullParser.next();

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return sortAndUpperCaseList(listNamesCountries);
    }

    public ArrayList<String> parserXmlForCountriesWithRegions() {
        ArrayList<String> listNamesCountriesWithRegions = new ArrayList<>();

        try {

            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.regions);

            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                switch (xmlPullParser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT: {

                        break;
                    }

                    case XmlPullParser.START_TAG: {


                        if (xmlPullParser.getName().equals("region")) {
                            if (xmlPullParser.getDepth() == 3 && xmlPullParser.getAttributeValue(null, "join_map_files") != null) {
                                listNamesCountriesWithRegions.add(xmlPullParser.getAttributeValue(null, "name"));
                            }
                            if (xmlPullParser.getDepth() == 3 && xmlPullParser.getAttributeValue(null, "map") != null) {
                                listNamesCountriesWithRegions.add(xmlPullParser.getAttributeValue(null, "name"));
                            }
                        }
                        break;
                    }

                    case XmlPullParser.END_TAG: {

                        break;
                    }
                    default:
                        break;

                }
                xmlPullParser.next();

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        sortAndUpperCaseList(listNamesCountriesWithRegions);
        return listNamesCountriesWithRegions;
    }

    public ArrayList<String> parserXmlForRegionsCounrty(String nameCountry) {
        ArrayList<String> listNamesRegionsCounrty = new ArrayList<>();
        nameCountry = Character.toLowerCase(nameCountry.charAt(0)) + nameCountry.substring(1);

        try {

            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.regions);

            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                switch (xmlPullParser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT: {

                        break;
                    }

                    case XmlPullParser.START_TAG: {

                        if (xmlPullParser.getName().equals("region")) {
                        if (xmlPullParser.getAttributeValue(null, "name").equals(nameCountry)) {
                            xmlPullParser.nextTag();
                            while(xmlPullParser.getDepth() > 3){
                                if (xmlPullParser.getDepth() > 4) {
                                    xmlPullParser.nextTag();
                                    continue;
                                }
                                listNamesRegionsCounrty.add(xmlPullParser.getAttributeValue(null, "name"));
                                xmlPullParser.nextTag();
                            }


                        }}
                        break;
                    }

                    case XmlPullParser.END_TAG: {

                        break;
                    }
                    default:
                        break;

                }
                xmlPullParser.next();

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        listNamesRegionsCounrty.removeAll(Collections.singleton(null));

        sortAndUpperCaseList(listNamesRegionsCounrty);
        return listNamesRegionsCounrty;
    }

    public ArrayList<String> parserXmlForForDownload() {
        ArrayList<String> listNamesRegionsForDownload = new ArrayList<>();

        try {

            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.regions);

            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                switch (xmlPullParser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT: {

                        break;
                    }

                    case XmlPullParser.START_TAG: {

                        if (xmlPullParser.getName().equals("region")) {

                                while(xmlPullParser.getDepth() > 3){
                                    if (xmlPullParser.getDepth() > 4) {
                                        xmlPullParser.nextTag();
                                        continue;
                                    }
                                    listNamesRegionsForDownload.add(xmlPullParser.getAttributeValue(null, "name"));
                                    xmlPullParser.nextTag();
                                }


                            }
                        break;
                    }

                    case XmlPullParser.END_TAG: {

                        break;
                    }
                    default:
                        break;

                }
                xmlPullParser.next();

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        listNamesRegionsForDownload.removeAll(Collections.singleton(null));

        sortAndUpperCaseList(listNamesRegionsForDownload);

        return listNamesRegionsForDownload;
    }


    private ArrayList<String> sortAndUpperCaseList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, Character.toUpperCase(list.get(i).charAt(0)) + list.get(i).substring(1).toLowerCase());
        }

        Collections.sort(list);
        return list;
    }

}
