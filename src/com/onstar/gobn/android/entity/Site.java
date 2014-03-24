package com.onstar.gobn.android.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Site is a class for element Site of XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class Site implements Cloneable {
    /** Site id. */
    private String id_;
    /** Site stacks. */
    private List<Stack> stacks_;

    /** Default Constructor. */
    public Site() {
        stacks_ = new ArrayList<Stack>();
    }

    /**
     * Gets for the id.
     * @return id
     */
    public String getId() {
        return id_;
    }

    /**
     * Sets for the id.
     * @param id - site id
     */
    public void setId(final String id) {
        this.id_ = id;
    }

    /**
     * Gets for the stacks.
     * @return stacks
     */
    public List<Stack> getStacks() {
        return stacks_;
    }

    /**
     * Sets for the stacks.
     * @param stacks - site stacks
     */
    public void setStacks(final List<Stack> stacks) {
        this.stacks_ = stacks;
    }

    @Override
    public Site clone() throws CloneNotSupportedException {
        final Site copy = (Site) super.clone();
        copy.id_ = id_;
        final List<Stack> tempStacks = new ArrayList<Stack>();
        for (Stack stack : stacks_) {
            tempStacks.add(stack);
        }
        copy.stacks_ = tempStacks;
        return copy;
    }

    /**
     * Clones and resets the site.
     * @return site clone
     * @throws CloneNotSupportedException if something wrong
     */
    public Site cloneAndReset() throws CloneNotSupportedException {
        final Site copy = this.clone();
        stacks_ = new ArrayList<Stack>();

        return copy;
    }

    @Override
    public String toString() {
        final StringBuilder builderSite = new StringBuilder("Site ID=").append(id_);
        for (Stack stack : stacks_) {
            builderSite.append("\n\t");
            builderSite.append(stack.toString());
        }
        return builderSite.toString();
    }
}
