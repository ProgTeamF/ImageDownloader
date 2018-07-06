package com.progteamf.test.imagedownloader.controller;

import com.progteamf.test.imagedownloader.exceptions.SortException;
import com.progteamf.test.imagedownloader.model.Image;
import com.progteamf.test.imagedownloader.model.SortType;

import java.util.Collections;
import java.util.List;

/**
 * Created by N.Babiy on 7/5/2018.
 */

/**
 * This class implements sorting controller.
 */

public class SortingImageController {


    /**
     * This method controls process of sorting of list of Images. If one of the parameters is null it throws exception.
     *
     * @param nonSortedImageList it is not sorted list.
     * @param sortType it is type of sorting.
     * @return
     * @throws SortException
     */
    public static List<Image> sort(List<Image> nonSortedImageList, SortType sortType) throws SortException{
        if (sortType!=null && nonSortedImageList!=null) {
            switch (sortType) {
                case SORT_BY_DATE:
                    Collections.sort(nonSortedImageList, new SortingImageByDateComparator());
                    break;
                case SORT_BY_STATUS:
                    Collections.sort(nonSortedImageList, new SortingImageByStatusComparator());
                    break;
            }
        } else {
            if (sortType==null) throw new SortException("sortType is null");
            else if (nonSortedImageList==null) throw new SortException("nonSortedImageList is null");
        }
        return nonSortedImageList;
    }

}
