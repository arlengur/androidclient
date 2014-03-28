package com.onstar.gobn.android.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Gtbt is a root class for element of XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class Gtbt {
    /** Gtbt TimeStamp. */
    private String timeStamp_;
    /** Gtbt sites. */
    private List<Site> sites_;

    /** Default Constructor. */
    public Gtbt() {
        sites_ = new ArrayList<Site>();
    }

    /**
     * Gets the timeStamp.
     * @return timeStamp
     */
    public String getTimeStamp() {
        return timeStamp_;
    }

    /**
     * Sets the timeStamp.
     * @param timeStamp - date of creating XML
     */
    public void setTimeStamp(final String timeStamp) {
        timeStamp_ = timeStamp;
    }

    /**
     * Gets the sites.
     * @return sites
     */
    public List<Site> getSites() {
        return sites_;
    }

    /**
     * Sets the sites.
     * @param sites - sites of the gtbt
     */
    public void setSites(final List<Site> sites) {
        sites_ = sites;
    }
}
