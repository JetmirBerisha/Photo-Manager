package com.themightyducks.photos;

import android.net.Uri;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jetmir Berisha
 */
public class CustomImage implements Serializable {
    private String path;
    private String juriBoyka;
    private HashMap<String, ArrayList<String>> tags;

    /**
     * Metadata for an image
     * @param path  the physical path of this image
     * @param uri URI.toString()
     */
    public CustomImage(String path, String uri) {
        this.path = path;
        juriBoyka = uri;
        tags = new HashMap<>();
        tags.put("location", new ArrayList<>());
        tags.put("person", new ArrayList<>());
    }

    /**
     * get the uri of the current image
     * @return the uri
     */
    public Uri getUri() {
        return Uri.parse(juriBoyka);
    }

    /**
     *
     * @param key
     *            The tag whose value we want to obtain
     * @return The given value of the selected tag. Returns null if the tag wasn't
     *         found
     */
    public ArrayList<String> getTag(String key) {
        return tags.get(key);
    }

    /**
     * Set a new tag or add to an existing tag
     * @param key The key of the new tag
     * @param value the value of the new tag
     */
    public void setTag(String key, String value) {
        if (key == null || value == null || key.isEmpty())
            return;
        ArrayList<String> val;
        if (tags.containsKey(key)) {
            val = tags.get(key);
            if (!val.contains(value))
                val.add(value);
        }
        else {
            val = new ArrayList<String>();
            val.add(value);
        }
        tags.put(key, val);
    }

    /**
     * Get one tag's value as a string
     * @param key the key of the desired tag
     * @return a comma separated string of values
     */
    public String getTagAsString(String key) {
        String ret = "";
        ArrayList<String> vals = tags.get(key);
        for (int i = 0; i < vals.size(); i++) {
            ret += vals.get(i) + ',';
            if (i == vals.size()-1)
                ret = ret.substring(0, ret.length()-1);
        }
        return ret;
    }

    /**
     * Formats and gets the tags.
     * @return The tags in the following format:  tag1=value1,value2,value3 | tag2=value4,value5,value6
     */
    public String getAllTagsAsString() {
        StringBuilder toRet = new StringBuilder();
        for (String i : tags.keySet()) {
            if (tags.get(i).size() == 0)
                continue;
            toRet.append( i + " = " + getTagAsString(i) + " | ");
        }
        if (toRet.length() > 2)
            toRet.delete(toRet.length() - 2, toRet.length());
        return toRet.toString();
    }

    /**
     * get the raw tags map
     * @return a hashmap of the tags in the format <tag, ArrayList<values>>
     */
    public HashMap<String, ArrayList<String>> getTags() {
        return tags;
    }


    /**
     * update the tags of this image
     * @param newTags the new tags from where we pull the data
     */
    public void updateTags(HashMap<String, ArrayList<String>> newTags) {
        if (newTags == null)
            return;
        ArrayList<String> hold;
        for (String tg : newTags.keySet()) {
            hold = new ArrayList<String>(newTags.get(tg));
            tags.put(tg, hold);
        }
    }

    /**
     * get the path in normal form
     * @return the path in normal form a.k.a. not in Uri format
     */
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomImage))
            return false;
        CustomImage o = (CustomImage) obj;
        ArrayList<String> location = new ArrayList<>(o.getTags().get("location"));
        ArrayList<String> person = new ArrayList<>(o.getTags().get("person"));
        if (path.equals(o.getPath()))
            if (Uri.parse(juriBoyka).equals(o.getUri()))
                if (location.size() == tags.get("location").size() && person.size() == tags.get("person").size()) {
                    location.removeAll(tags.get("location"));
                    person.removeAll(tags.get("person"));
                    return location.size() == 0 && person.size() == 0;
                }
        return false;
    }

    @Override
    public String toString() {
        return path;
    }
}
