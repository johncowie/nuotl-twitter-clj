package org.nextupontheleft.twitter;

import clojure.lang.IFn;
import clojure.lang.Keyword;
import clojure.lang.PersistentHashMap;
import clojure.lang.PersistentVector;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class ClojureStatusListener implements UserStreamListener {

    private long applicationId;
    private IFn statusFunction;
    private IFn deleteFunction;
    private IFn exceptionFunction;

    public ClojureStatusListener(long applicationId, IFn statusFunction, IFn deleteFunction, IFn exceptionFunction) {
        this.applicationId = applicationId;
        this.statusFunction = statusFunction;
        this.deleteFunction = deleteFunction;
        this.exceptionFunction = exceptionFunction;
    }

    @Override
    public void onStatus(Status status) {
        List<String> hashtagList = new ArrayList<String>();
        List<PersistentHashMap> urlMaps = new ArrayList<PersistentHashMap>();
        for(HashtagEntity e : status.getHashtagEntities()) {
            hashtagList.add(e.getText());
        }
        for(URLEntity u : status.getURLEntities()) {
            urlMaps.add(PersistentHashMap.create(
                                     Keyword.intern("url"), u.getURL().toString(),
                                     Keyword.intern("display-url"), u.getDisplayURL(),
                                     Keyword.intern("expanded-url"), u.getExpandedURL().toString()));
        }
        PersistentVector tags = PersistentVector.create(hashtagList);
        PersistentVector urls = PersistentVector.create(urlMaps);
        PersistentHashMap user = PersistentHashMap.create(Keyword.intern("_id"),
                                                          status.getUser().getId(),
                                                          Keyword.intern("name"),
                                                          status.getUser().getScreenName(),
                                                          Keyword.intern("display-name"),
                                                          status.getUser().getName());
        PersistentHashMap map = PersistentHashMap.create(
                                                         Keyword.intern("_id"), status.getId(),
                                                         Keyword.intern("text"), status.getText(),
                                                         Keyword.intern("tweeter"), user,
                                                         Keyword.intern("tags"), tags,
                                                         Keyword.intern("urls"), urls,
                                                         Keyword.intern("in-response-to"), status.getInReplyToStatusId(),
                                                         Keyword.intern("application-id"), applicationId
                                                         );
        this.statusFunction.invoke(map);
    }
    @Override
    public void onException(Exception ex) {
        this.exceptionFunction.invoke(ex.getMessage());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        this.deleteFunction.invoke(statusDeletionNotice.getStatusId(),
                                   statusDeletionNotice.getUserId());
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        // this.deleteFunction.invoke(directMessageId, userId);
    }

    @Override
    public void onFriendList(long[] friendIds) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFollow(User source, User followedUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
