package com.onstar.gobn.android.parser;

import java.io.InputStream;

import com.onstar.gobn.android.entity.Gtbt;

/**
 * ServerParser is a interface for parsing XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public interface ServerParser {
    /**
     * Parses incoming XML.
     * @param is - InputStream
     * @return XML parsing result
     * @throws ParserException in case of parse exception
     */
    Gtbt parse(InputStream is) throws ParserException;
}
