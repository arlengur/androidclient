package com.onstar.gobn.android.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Cluster is a class for element Cluster of XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class Cluster implements Cloneable {
    /** Cluster type. */
    private String type_;
    /** Cluster servers. */
    private List<Server> servers_;

    /** Default Constructor. */
    public Cluster() {
        servers_ = new ArrayList<Server>();
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
     * @param type - cluster type
     */
    public void setType(final String type) {
        this.type_ = type;
    }

    /**
     * Gets for the Servers.
     * @return servers
     */
    public List<Server> getServers() {
        return servers_;
    }

    /**
     * Sets for the Servers.
     * @param servers - cluster servers
     */
    public void setServers(final List<Server> servers) {
        this.servers_ = servers;
    }

    @Override
    public Cluster clone() throws CloneNotSupportedException {
        final Cluster copy = (Cluster) super.clone();
        copy.type_ = type_;
        final List<Server> tempServers = new ArrayList<Server>();
        for (Server server : servers_) {
            tempServers.add(server);
        }
        copy.servers_ = tempServers;

        return copy;
    }

    /**
     * Clones and resets the cluster.
     * @return cluster clone
     * @throws CloneNotSupportedException if something wrong
     */
    public Cluster cloneAndReset() throws CloneNotSupportedException {
        final Cluster copy = this.clone();
        servers_ = new ArrayList<Server>();
        return copy;
    }

    @Override
    public String toString() {
        final StringBuilder builderCluster = new StringBuilder("Cluster Type=");
        builderCluster.append(type_)
                      .append("\n{");
        for (Server server : servers_) {
            builderCluster.append("\n\t");
            builderCluster.append(server.toString());
        }
        builderCluster.append("\n}");
        return builderCluster.toString();
    }
}
