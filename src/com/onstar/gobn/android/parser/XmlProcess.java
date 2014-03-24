package com.onstar.gobn.android.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.onstar.gobn.android.entity.Site;
import com.onstar.gobn.android.entity.Stack;

/**
 * XmlProcess is a class for parsing XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class XmlProcess extends AsyncTask<Void, Void, List<Stack>> {

    /** Server URL variable. */
    private static final String SERVER_URL = "SERVER_URL";
    /** An array of error messages. */
    private static final String[] ERRORS = { "ERROR_CONNECT", "ERROR_PARSE", "ERROR_OPEN" };
    /** Application properties. */
    private transient Properties properties_;
    /** Variable to store URL. */
    private transient StringBuilder url_;
    /** String for errors. */
    private transient String error_;

    /** Default constructor. */
    public XmlProcess(final AssetManager assetManager) {
        super();
        InputStream is = null;
        properties_ = new Properties();
        try {
            is = assetManager.open("application.properties");
            properties_.load(is);
            url_ = new StringBuilder(properties_.getProperty(SERVER_URL));
        } catch (IOException e) {
            error_ = properties_.getProperty(ERRORS[2]);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // NOPMD
                }
            }
        }
    }

    /**
     * Constructor with URL parameter.
     * @param id - URL parameter
     */
    public XmlProcess(final AssetManager assetManager, final String id) {
        this(assetManager);
        if (id != null && !id.isEmpty()) {
            url_.append("?id=").append(id);
        }
    }

    @Override
    protected List<Stack> doInBackground(final Void... params) {
        List<Site> sites = new ArrayList<Site>();
        final List<Stack> stacks = new ArrayList<Stack>();
        final ServerParser parser = ServerParserFactory.getParser();
        HttpURLConnection connect = null;
        InputStream is = null;

        try {
            final URL serverUrl = new URL(url_.toString());
            connect = (HttpURLConnection) serverUrl.openConnection();
            is = connect.getInputStream();
            sites = parser.parse(is);
            for (Site site : sites) {
                stacks.addAll(site.getStacks());
            }
        } catch (IOException e) {
            error_ = properties_.getProperty(ERRORS[0]);
        } catch (ParserException e) {
            error_ = properties_.getProperty(ERRORS[1]);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // NOPMD
                }
            }
            if (connect != null) {
                connect.disconnect();
            }
        }
        return stacks;
    }

    /**
     * Returns error message.
     * @return error message
     */
    public String getError() {
        return error_;
    }
}
