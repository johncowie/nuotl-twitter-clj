package org.nextupontheleft.twitter;

import twitter4j.URLEntity;

public class TestUrlEntity implements URLEntity {

    private final String url;
    private final String expandedUrl;
    private final String displayUrl;

    public TestUrlEntity(String url, String expandedUrl, String displayUrl) {
        this.url = url;
        this.expandedUrl = expandedUrl;
        this.displayUrl = displayUrl;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getExpandedURL() {
        return expandedUrl;
    }

    @Override
    public String getDisplayURL() {
        return displayUrl;
    }

    @Override
    public int getStart() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEnd() {
        throw new UnsupportedOperationException();
    }
}
