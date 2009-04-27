package jsfun.examples.twitter;

import twitter4j.*;
import twitter4j.Twitter;

import java.util.List;
import java.util.Date;


public class ApiImpl implements Api {

	private Twitter api = new Twitter();

	public void login(String username, String password) {
		api.setUserId(username);
		api.setPassword(password);
	}

	public ExtendedUser getUserDetail(String id) throws TwitterException {
		return api.getUserDetail(id);
	}

	public List<Status> getFriendsTimeline() throws TwitterException {
		return api.getFriendsTimeline();
	}

	public List<Status> getFriendsTimeline(int page) throws TwitterException {
		return api.getFriendsTimeline(page);
	}

	public List<Status> getFriendsTimeline(long sinceId, int page) throws TwitterException {
		return api.getFriendsTimeline(sinceId, page);
	}

	public List<Status> getFriendsTimeline(String id) throws TwitterException {
		return api.getFriendsTimeline(id);
	}

	public List<Status> getFriendsTimeline(String id, int page) throws TwitterException {
		return api.getFriendsTimeline(id, page);
	}

	public List<Status> getFriendsTimeline(long sinceId, String id, int page) throws TwitterException {
		return api.getFriendsTimeline(sinceId, id, page);
	}

	public List<Status> getFriendsTimeline(long sinceId) throws TwitterException {
		return api.getFriendsTimeline(sinceId);
	}

	public List<Status> getFriendsTimeline(String id, long sinceId) throws TwitterException {
		return api.getFriendsTimeline(id, sinceId);
	}

	public List<User> getFriends() throws TwitterException {
		return api.getFriends();
	}

	public List<User> getFriends(int page) throws TwitterException {
		return api.getFriends(page);
	}

	public List<User> getFriends(String id) throws TwitterException {
		return api.getFriends(id);
	}

	public List<User> getFriends(String id, int page) throws TwitterException {
		return api.getFriends(id, page);
	}

	public List<User> getFollowers() throws TwitterException {
		return api.getFollowers();
	}

	public List<User> getFollowers(int page) throws TwitterException {
		return api.getFollowers(page);
	}

	public List<User> getFollowers(String id) throws TwitterException {
		return api.getFollowers(id);
	}

	public List<User> getFollowers(String id, int page) throws TwitterException {
		return api.getFollowers(id, page);
	}

	public QueryResult search(Query query) throws TwitterException {
		return api.search(query);
	}
}
