package ua.com.vs.apptest.app.models;

public class Region {
    public Region() {
    }

    /**
     * @param name      - name region
     * @param continent - Is this continent? true - a continent, false - is not a continent
     * @param child     - Are there any children?    true- it's not a continent or a map for downloading,
     *                  false - if there are no children then there is a map to download
     */
    public Region(String name, boolean continent, boolean child) {
        setName(name);
        setContinent(continent);
        setHasMap(child);
    }

    private String name;
    private boolean continent;
    /**
     * If the object has a map then this field is true, else false
     */
    private boolean hasMap = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasMap() {
        return hasMap;
    }

    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    public boolean isContinent() {
        return continent;
    }

    public void setContinent(boolean continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Name: "+getName()+" Continent: "+ isContinent()+" Child: " + isHasMap();
    }
}
