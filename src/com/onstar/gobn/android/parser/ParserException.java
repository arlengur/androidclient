package com.onstar.gobn.android.parser;

/**
 * ParserException is a exception class.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */
public class ParserException extends Exception {

    /** SerialVersionUID variable. */
    private static final long serialVersionUID = -3346447682574686068L;

    /**
     * Constructor for the ParserException.
     * @param throwable - exception
     */
    public ParserException(final Throwable throwable) {
        super(throwable);
    }
}
