package org.nextupontheleft.twitter;

import twitter4j.*;

import java.util.Date;

public class TestStatus implements Status {

    private final long id;
    private final String text;
    private final long inReplyToStatusId;
    private final User user;
    private final HashtagEntity[] hashtagEntities;
    private final URLEntity[] urlEntities;

    public TestStatus(long id,
                      String text,
                      long inReplyToStatusId,
                      User user,
                      URLEntity urlEntity,
                      HashtagEntity hashtagEntity) {

        this.id = id;
        this.text = text;
        this.inReplyToStatusId = inReplyToStatusId;
        this.user = user;
        this.urlEntities = new URLEntity[]{urlEntity};
        this.hashtagEntities = new HashtagEntity[]{hashtagEntity};
    }


    @Override
    public Date getCreatedAt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTruncated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    @Override
    public long getInReplyToUserId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInReplyToScreenName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GeoLocation getGeoLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Place getPlace() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFavorited() {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public boolean isRetweet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status getRetweetedStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getContributors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getRetweetCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRetweetedByMe() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(Status status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URLEntity[] getURLEntities() {
        return urlEntities;
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return hashtagEntities;
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAccessLevel() {
        throw new UnsupportedOperationException();
    }

    public boolean isPossiblySensitive() {
        throw new UnsupportedOperationException();
    }

    public long getCurrentUserRetweetId() {
        throw new UnsupportedOperationException();
    }

    public URLEntity[] getDescriptionURLEntities() {
        throw new UnsupportedOperationException();
    }

}
