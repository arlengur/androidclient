package com.onstar.gobn.android.parser;

/**
 * ServerParserFactory is a factory for parsing XML.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public abstract class ServerParserFactory {
    /** ParserType is a enum types of parsers. */
    enum ParserType {
        /** Parser type. */
        ANDROID_SAX;
    }

    /**
     * Acquires an instance of the parser.
     * @return server parser.
     */
    public static ServerParser getParser() {
        return getParser(ParserType.ANDROID_SAX);
    }

    /**
     * Acquires an instance of the parser by parser type.
     * @param type - parser type.
     * @return server parser.
     */
    public static ServerParser getParser(final ParserType type) {
        if (type == ParserType.ANDROID_SAX) {
            return new ServerStatusSaxParser();
        }
        return null;
    }
}