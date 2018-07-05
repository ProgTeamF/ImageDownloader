package com.progteamf.test.imagedownloader.controller;

import com.progteamf.test.imagedownloader.model.Image;

import java.util.Comparator;


/**
 * Created by N.Babiy on 7/5/2018.
 */

/**
 * Comparator for sorting of list of images by status.
 * If the statuses of both variables are equal then it sorts by date.
 */
class SortingImageByStatusComparator implements Comparator<Image> {

    @Override
    public int compare(Image o1, Image o2) {
        int result = o1.getStatus().getId()-o2.getStatus().getId();
        if (result!=0){
            return result;
        } else {
            if(o1.getTime().after(o2.getTime())){
                return -1;
            } else {
                return 1;
            }
        }
    }

}
