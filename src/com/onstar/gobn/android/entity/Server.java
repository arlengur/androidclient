package com.onstar.gobn.android.entity;

/**
 * Server is a class for element Server of XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class Server implements Cloneable {
    /** Server address. */
    private String address_;
    /** Server name. */
    private String name_;
    /** Server port. */
    private String port_;
    /** Server status. */
    private String status_;

    /**
     * Gets for the address.
     * @return address
     */
    public String getAddress() {
        return address_;
    }

    /**
     * Sets for the address.
     * @param address - server address
     */
    public void setAddress(final String address) {
        address_ = address;
    }

    /**
     * Gets for the name.
     * @return name
     */
    public String getName() {
        return name_;
    }

    /**
     * Sets for the name.
     * @param name - server name
     */
    public void setName(final String name) {
        name_ = name;
    }

    /**
     * Gets for the port.
     * @return port
     */
    public String getPort() {
        return port_;
    }

    /**
     * Sets for the port.
     * @param port - server port
     */
    public void setPort(final String port) {
        port_ = port;
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
     * @param status - server status
     */
    public void setStatus(final String status) {
        this.status_ = status;
    }

    @Override
    public Server clone() throws CloneNotSupportedException {
        final Server copy = (Server) super.clone();
        copy.address_ = address_;
        copy.name_ = name_;
        copy.port_ = port_;
        copy.status_ = status_;
        return copy;
    }

    @Override
    public String toString() {
        final StringBuilder builderServer = new StringBuilder("\tServer Status=");
        builderServer.append(status_)
                     .append("\n\t\t Address=")
                     .append(address_)
                     .append("\n\t\t Name=")
                     .append(name_)
                     .append("\n\t\t Port=")
                     .append(port_);
        return builderServer.toString();
    }
}
