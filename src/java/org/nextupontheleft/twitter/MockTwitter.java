package org.nextupontheleft.twitter;

import twitter4j.StatusUpdate;

public class MockTwitter {

    private String lastUpdate;
    private long lastDestroyed;

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
