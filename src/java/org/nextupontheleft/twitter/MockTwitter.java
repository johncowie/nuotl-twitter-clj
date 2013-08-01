package org.nextupontheleft.twitter;

import twitter4j.StatusUpdate;

public class MockTwitter {

    private String lastUpdate;
    private long lastDestroyed;
    private final long id;
    private final String screenName;


    public MockTwitter(long id, String screenName) {
        this.id = id;
        this.screenName = screenName;
    }

    public long getId() {
        return this.id;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public void updateStatus(StatusUpdate status) {
        this.lastUpdate = status.getStatus();
    }

    public String getLastUpdate() {
        if(this.lastUpdate != null) {
            return this.lastUpdate;
        } else {
            throw new RuntimeException("Last update not found.");
        }
    }

    public void destroyStatus(long id) {
        this.lastDestroyed = id;
    }

    public long lastDestroyed() {
        return this.lastDestroyed;
    }

}
