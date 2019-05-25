package com.themightyducks.photos;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Queries the arrylist of images for tags
 * @author Jetmir Berisha
 *
 */
public class Searcher {
    private String tagValues;
    private ArrayList<CustomImage> images;

    /**
     * Create a new query to search the images
     *
     * @param album
     *            The album to search on. This field can be left null and the
     *            searching will be done on all of the active albums.
     * @param tagValues
     *            String of the format: "tag1=value1||tag2=value2&&tag3=value3..."
     *            where && and || are interchangeable.
     */
    public Searcher(Album album, String tagValues) {
        this.tagValues = tagValues;
        Album albo = null;
        if (album != null)
            images = album.getImages();
        else {
            images = new ArrayList<CustomImage>();
            for (String name : AlbumView.imageData.keySet()) {
                albo = AlbumView.imageData.get(name);
                images.addAll(albo.getImages());
            }
        }
    }

    /**
     * Perform the search query defined in the constructor
     *
     * @return The ArrayList of images that match the result.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<CustomImage> search() {
        return tagsSearch();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CustomImage> tagsSearch() {
        ArrayList<CustomImage> result = images;
        String tagVals[] = tagValues.split("&&|\\|\\|");
        if (tagVals[0].equals("="))
            return result;
        String str = tagValues.substring(0);
        String vals[];
        for (int i = 0; i < tagVals.length; i++) {
            vals = tagVals[i].split("=");
            if (i == 0)
                result = intersect(result, singleTagSearch(vals[0], (vals.length == 2)? vals[1] : ""));
            else if (vals.length == 1) {
                if (str.indexOf("&&") > str.indexOf("||")) {
                    str = str.substring(str.indexOf("&&") + 2);
                    result = intersect(result, singleTagSearch(vals[0], ""));
                }
                else {
                    str = str.substring(str.indexOf("||") + 2);
                    result = union(result, singleTagSearch(vals[0], ""));
                }
            }
            else if (vals.length == 2) {
                if (str.indexOf("&&") > str.indexOf("||")) {
                    str = str.substring(str.indexOf("&&") + 2);
                    result = intersect(result, singleTagSearch(vals[0], vals[1]));
                }
                else {
                    str = str.substring(str.indexOf("||") + 2);
                    result = union(result, singleTagSearch(vals[0], vals[1]));
                }
            }
        }
        return result;
    }

    private ArrayList<CustomImage> singleTagSearch(String tag, String value) {
        ArrayList<CustomImage> result = new ArrayList<CustomImage>();
        if (value.isEmpty()) {
            for (CustomImage image : images)
                if (image.getTags().containsKey(tag))
                    result.add(image);
        }
        else
            for (CustomImage image : images)
                if (image.getTags().containsKey(tag))
                    for (String i : image.getTags().get(tag))
                        if (i.startsWith(value)){
                            result.add(image);
                            break;
                        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CustomImage> union(ArrayList<CustomImage>... sect) {
        Set<CustomImage> set = new HashSet<>();
        for (int i = 0; i < sect.length; i++)
            set.addAll(sect[i]);
        return new ArrayList<CustomImage>(set);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CustomImage> intersect(ArrayList<CustomImage>... sect) {
        ArrayList<CustomImage> first = sect[0];
        for (int i = 1; i < sect.length; i++) {
            first = intersectTwo(first, sect[i]);
        }
        return first;
    }

    private ArrayList<CustomImage> intersectTwo(ArrayList<CustomImage> A, ArrayList<CustomImage> B){
        ArrayList<CustomImage> result = new ArrayList<>();
        for(CustomImage img : A)
            if (B.contains(img))
                result.add(img);
        return result;
    }

}
