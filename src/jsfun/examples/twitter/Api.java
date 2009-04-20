package jsfun.examples.twitter;

import twitter4j.*;

import java.util.List;
import java.util.Date;


public interface Api {

	void login(String username, String password);

	List<Status> getFriendsTimeline() throws TwitterException;

	List<Status> getFriendsTimeline(int page) throws TwitterException;

	List<Status> getFriendsTimeline(long sinceId, int page) throws TwitterException;

	List<Status> getFriendsTimeline(String id) throws TwitterException;

	List<Status> getFriendsTimeline(String id, int page) throws TwitterException;

	List<Status> getFriendsTimeline(long sinceId, String id, int page) throws TwitterException;

	List<Status> getFriendsTimeline(long sinceId) throws TwitterException;

	List<Status> getFriendsTimeline(String id, long sinceId) throws TwitterException;

	List<Status> getUserTimeline(String id, int count, long sinceId) throws TwitterException;

	List<Status> getUserTimeline(String id, Date since) throws TwitterException;

	List<Status> getUserTimeline(String id, int count) throws TwitterException;

	List<Status> getUserTimeline(int count, Date since) throws TwitterException;

	List<Status> getUserTimeline(String id) throws TwitterException;

	List<Status> getUserTimeline() throws TwitterException;

	List<Status> getReplies() throws TwitterException;

	List<Status> getReplies(long sinceId) throws TwitterException;

	List<Status> getReplies(int page) throws TwitterException;

	List<Status> getReplies(long sinceId, int page) throws TwitterException;

	List<User> getFriends() throws TwitterException;

	List<User> getFriends(int page) throws TwitterException;

	List<User> getFriends(String id) throws TwitterException;

	List<User> getFriends(String id, int page) throws TwitterException;

	List<User> getFollowers() throws TwitterException;

	List<User> getFollowers(int page) throws TwitterException;

	List<User> getFollowers(String id) throws TwitterException;

	List<User> getFollowers(String id, int page) throws TwitterException;

	QueryResult search(Query query) throws TwitterException;
}
