package org.nextupontheleft.twitter;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

import java.net.URL;
import java.util.Date;

public class TestUser implements User {

    private final long id;
    private final String name;
    private final String screenName;

    public TestUser(long id, String name, String screenName) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getScreenName() {
        return this.screenName;
    }

    @Override
    public String getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isContributorsEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileImageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBiggerProfileImageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMiniProfileImageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOriginalProfileImageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getProfileImageUrlHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileImageURLHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBiggerProfileImageURLHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMiniProfileImageURLHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOriginalProfileImageURLHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProtected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFollowersCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status getStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileBackgroundColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileTextColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileLinkColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileSidebarFillColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileSidebarBorderColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProfileUseBackgroundImage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShowAllInlineMedia() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFriendsCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreatedAt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFavouritesCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getUtcOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTimeZone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileBackgroundImageUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfileBackgroundImageUrlHttps() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProfileBackgroundTiled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLang() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatusesCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGeoEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isVerified() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTranslator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getListedCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFollowRequestSent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(User user) {
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

    public URLEntity getURLEntity() {
        throw new UnsupportedOperationException();
    }

    public URLEntity[] getDescriptionURLEntities() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerMobileRetinaURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerMobileURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerIPadRetinaURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerIPadURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerRetinaURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBannerURL() {
        throw new UnsupportedOperationException();
    }

    public String getProfileBackgroundImageURL() {
        throw new UnsupportedOperationException();
    }
}
