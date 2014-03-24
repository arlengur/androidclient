package com.onstar.gobn.android.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.onstar.gobn.android.entity.Cluster;
import com.onstar.gobn.android.entity.Server;
import com.onstar.gobn.android.entity.Site;
import com.onstar.gobn.android.entity.Stack;


import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

/**
 * AndroidSaxParser is a class implements an interface parsing XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class ServerStatusSaxParser implements ServerParser {

    /** Variable to store GTBT XML tag. */
    public static final String GTBT = "GTBT";
    /** Variable to store Site XML tag. */
    public static final String SITE = "Site";
    /** Variable to store Stack XML tag. */
    public static final String STACK = "Stack";
    /** Variable to store Description XML tag. */
    public static final String DESCRIPTION = "Description";
    /** Variable to store Server_Cluster XML tag. */
    public static final String SERVER_CLISTER = "Server_Cluster";
    /** Variable to store Server XML tag. */
    public static final String SERVER = "Server";
    /** Variable to store Address XML tag. */
    public static final String ADDRESS = "Address";
    /** Variable to store Name XML tag. */
    public static final String NAME = "Name";
    /** Variable to store Port XML tag. */
    public static final String PORT = "Port";
    /** Variable to store Type XML tag. */
    public static final String TYPE = "Type";

    @Override
    public List<Site> parse(final InputStream is) throws ParserException {
        final RootElement root = new RootElement(GTBT);
        final List<Site> gtbt = new ArrayList<Site>();
        final Site currentSite = new Site();
        final Stack currentStack = new Stack();
        final Cluster currentCluster = new Cluster();
        final Server currentServer = new Server();
        final Element site = root.getChild(SITE);
        final Element stack = site.getChild(STACK);
        final Element cluster = stack.getChild(SERVER_CLISTER);
        final Element server = cluster.getChild(SERVER);

        site.setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                try {
                    gtbt.add(currentSite.cloneAndReset());
                } catch (CloneNotSupportedException e) {
                    //NOPMD
                }
            }
        });

        site.setStartElementListener(new StartElementListener() {
            @Override
            public void start(final Attributes attributes) {
                currentSite.setId(attributes.getValue(0));
            }
        });

        site.getChild(STACK).setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                try {
                    currentSite.getStacks().add(currentStack.cloneAndReset());
                } catch (CloneNotSupportedException e) {
                    //NOPMD
                }
            }
        });

        stack.setStartElementListener(new StartElementListener() {
            @Override
            public void start(final Attributes attributes) {
                currentStack.setId(attributes.getValue(0));
                currentStack.setStatus(attributes.getValue(1));
            }
        });

        stack.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(final String body) {
                currentStack.setDescription(body);
            }
        });

        stack.getChild(SERVER_CLISTER).setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                try {
                    currentStack.getClusters().add(currentCluster.cloneAndReset());
                } catch (CloneNotSupportedException e) {
                    //NOPMD
                }
            }
        });

        stack.getChild(TYPE).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(final String body) {
                currentStack.setType(body);
            }
        });

        cluster.setStartElementListener(new StartElementListener() {
            @Override
            public void start(final Attributes attributes) {
                currentCluster.setType(attributes.getValue(0));
            }
        });

        cluster.getChild(SERVER).setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                try {
                    currentCluster.getServers().add(currentServer.clone());
                } catch (CloneNotSupportedException e) {
                    //NOPMD
                }
            }
        });

        server.setStartElementListener(new StartElementListener() {
            @Override
            public void start(final Attributes attributes) {
                currentServer.setStatus(attributes.getValue(0));
            }
        });

        server.getChild(ADDRESS).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(final String body) {
                currentServer.setAddress(body);
            }
        });

        server.getChild(NAME).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(final String body) {
                currentServer.setName(body);
            }
        });

        server.getChild(PORT).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(final String body) {
                currentServer.setPort(body);
            }
        });

        try {
            Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (IOException e) {
            throw new ParserException(e);
        } catch (SAXException e) {
            throw new ParserException(e);
        }
        return gtbt;
    }
}
