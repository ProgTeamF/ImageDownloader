package com.progteamf.test.imagedownloader.controllers;

import com.progteamf.test.imagedownloader.model.Image;

import java.util.Comparator;


/**
 * Created by N.Babiy on 7/5/2018.
 */

/**
 * Comparator for sorting of list of images by date.
 */
class SortingImageByDateComparator implements Comparator<Image> {

    @Override
    public int compare(Image o1, Image o2) {
        if (o1.getTime().after(o2.getTime())) {
            return -1;
        } else {
            return 1;
        }
    }

}
