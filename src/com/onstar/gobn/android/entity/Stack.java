package com.onstar.gobn.android.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Stack is a class for element Stack of XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class Stack implements Cloneable {
    /** Stack status. */
    private String status_;
    /** Stack id. */
    private String id_;
    /** Stack description. */
    private String description_;
    /** Stack clusters. */
    private List<Cluster> clusters_;
    /** Stack type. */
    private String type_;

    /** Default Constructor. */
    public Stack() {
        clusters_ = new ArrayList<Cluster>();
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
     * @param id - stack id
     */
    public void setId(final String id) {
        this.id_ = id;
    }

    /**
     * Gets for the clusters.
     * @return clusters
     */
    public List<Cluster> getClusters() {
        return clusters_;
    }

    /**
     * Sets for the clusters.
     * @param clusters - stack clusters
     */
    public void setClusters(final List<Cluster> clusters) {
        this.clusters_ = clusters;
    }

    /**
     * Gets for the status.
     * @return status
     */
    public String getStatus() {
        return status_;
    }

    /**
     * Sets for the status.
     * @param status - stack status
     */
    public void setStatus(final String status) {
        this.status_ = status;
    }

    /**
     * Gets for the description.
     * @return description
     */
    public String getDescription() {
        return description_;
    }

    /**
     * Sets for the description.
     * @param description - stack description
     */
    public void setDescription(final String description) {
        this.description_ = description;
    }

    /**
     * Gets for the type.
     * @return type
     */
    public String getType() {
        return type_;
    }

    /**
     * Sets for the type.
     * @param type - stack type
     */
    public void setType(final String type) {
        this.type_ = type;
    }

    @Override
    public Stack clone() throws CloneNotSupportedException {
        final Stack copy = (Stack) super.clone();
        copy.id_ = id_;
        copy.status_ = status_;
        copy.description_ = description_;
        copy.type_ = type_;

        final List<Cluster> tempClusters = new ArrayList<Cluster>();
        for (Cluster cluster : clusters_) {
            tempClusters.add(cluster);
        }
        copy.clusters_ = tempClusters;

        return copy;
    }

    /**
     * Clones and resets the cluster.
     * @return cluster clone
     * @throws CloneNotSupportedException if something wrong
     */
    public Stack cloneAndReset() throws CloneNotSupportedException {
        final Stack copy = this.clone();
        clusters_ = new ArrayList<Cluster>();
        return copy;
    }

    @Override
    public String toString() {
        final StringBuilder builderSlucter = new StringBuilder("Stack ID=");
        builderSlucter.append(id_)
                      .append(" Status=")
                      .append(status_)
                      .append("\n[\n\t description=")
                      .append(description_);
        for (Cluster cluster : clusters_) {
            builderSlucter.append("\n\t");
            builderSlucter.append(cluster.toString());
        }
        builderSlucter.append("\n\t Type=")
                      .append(type_)
                      .append("\n]");
        return builderSlucter.toString();
    }
}
