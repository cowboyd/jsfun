((function() {

	var Context = Packages.org.mozilla.javascript.Context;
	var Query = Packages.twitter4j.Query
	//var out = java.lang.System.out


	function string(s) {
		return "" + s
	}

	function merge(object, properties) {
		for (var name in properties) {
			object[name] = properties[name]
		}
	}

	function List(transform) {
		return function(list) {
			var mapped = []
			for (var i = list.iterator(); i.hasNext();) {
				var item = i.next();
				mapped.push(transform(item))
			}
			return mapped
		}
	}

	function User(user) {
		return {
			id: user.id,
			name: string(user.name),
			screenName: string(user.screenName),
			description: string(user.description),
			followersCount: user.followersCount,
			location: string(user.location)			
		}
	}

	function UserWithStatus(user) {
		return merge(User(user), {

		})
	}

	function Status(status) {
		return {
			id: status.id,
			inReplyToStatusId: status.inReplyToStatusId,
			source: string(status.source),
			text: string(status.text),
			user: User(status.user)
		}
	}

	function QueryResult(result) {
		return {
			completedIn: result.completedIn,
			maxId: result.maxId,
			page: result.page,
			query: string(result.query),
			warning: string(result.warning),
			tweets: List(Tweet)(result.tweets)
		}
	}

	function Tweet(t) {
		return {
			id: t.id,
			fromUser: string(t.fromUser),
			fromUserId: t.fromUserId,
			toUser: string(t.toUser),
			toUserId: t.toUserId,
			isoLanguageCode: string(t.isoLanguageCode),
			text: string(t.text),
			source: string(t.source)
		}
	}

	function apicall(name, arguments, transform) {
		var api = Context.getCurrentContext().getThreadLocal("TWITTER_API")
		var result = api[name].apply(api, arguments);
		if (transform) {
			return transform(result)
		} else {
			return new Object().undefined
		}
	}

//	function puts(str) {
//		out.println('' + str)
//	}

	var PublicInterface = {
		login: function(username, password) {
			apicall('login', [username, password])
		},
		friends: function() {
			return apicall('getFriends', arguments, List(User))
		},
		followers: function() {
			return apicall('getFollowers', arguments, List(User))
		},
		timeline: function() {
			return apicall('getFriendsTimeline', arguments, List(Status))
		},
		search: function(query) {
			query = query || {}
			if (typeof query == 'string') {
				query = {
					query: query
				}
			}
			var q = new Query()
			var fields = ['lang', 'page', 'query', 'rpp', 'sinceId']
			for (var i = 0; i < fields.length; i++) {
				var field = fields[i]
				if (typeof query[field] != 'undefined') {
					q[field] = query[field]
				}
			}
			if (query.geocode) {
				var els = query.geocode.toString().split(/,/)
				if (!els[1]) {
					els[1] = els[2] = null
				} else if (!els[2]) {
					els[2] = null
				}
				q.setGeoCode(els[0], els[1], els[2])
			}
			return apicall('search', [q], QueryResult)
		},
		help: function(fun) {
			if (fun == PublicInterface.login) {
				return {
					"login(String username, String password)": {
						description: "Sets the authentication credentials which will be used for subsequent requests.",
						parameters: {
							username: 'the twitter username',
							password: 'the password for the account with username'
						}
					},
					returns: "void"
				}
			}
			if (fun == PublicInterface.friends) {
				return {
					"friends()": {
						description: "Returns the friends of the currently authenticated user.",
						parameters: {}
					},
					"friends(int page)": {
						description: "Returns the specified user's friends, each with current status inline.",
						parameters: {
							page: "the page number to return"
						}
					},
					"friends(String id)": {
						description: "Returns the user's friends, each with current status inline.",
						parameters: {
							id: "the ID or screen name of the user for whom to request a list of friends"
						}
					},
					"friends(String id, int page)": {
						description: "Returns the user's friends, each with current status inline.",
						parameters: {
							id: "the ID or screen name of the user for whom to request a list of friends",
							page: "the page number to return"
						}
					},
					returns: "list of users"
				}
			}
			if (fun == PublicInterface.followers) {
				return {
					"followers()": {
						description: "Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter"
					},
					"followers(int page)": {
						description: "Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter",
						parameters: {
							page: "Retrieves the next 100 followers."
						}
					},
					"followers(String id)": {
						description: "Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter",
						parameters: {
							id: "The ID or screen name of the user for whom to request a list of followers."
						}
					},
					"followers(String id, int page)": {
						description: "Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter",
						parameters: {
							id: "The ID or screen name of the user for whom to request a list of followers.",
							page: "Retrieves the next 100 followers."
						}
					},
					returns: "list of users"
				}
			}
			if (fun == PublicInterface.timeline) {
				return {
					"timeline()": {
						description: "Returns up to 20 statuses posted in the last 24 hours from the authenticating user and that user's friends.",
						parameters: {}
					},
					"timeline(int page)": {
						description: "Returns up to 20 most statuses posted from the authenticating user.",
						parameters: {
							id: "page number to return"
						}
					},
					"timeline(int sinceId, int page)": {
						description: "Returns up to 20 most recent statuses greater than sincId from the authenticating user",
						parameters: {
							sinceId: "Returns only statuses with an ID greater than (that is, more recent than) the specified ID",
							page: "page number to return"
						}
					},
					"timeline(String id)": {
						description: "Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.",
						parameters: {
							id: "the ID or screen name of the user's timeline"
						}
					},
					"timeline(String id, int page)": {
						description: "Returns up to 20 from the specified userid.",
						parameters: {
							id: "the ID or screen name of the user's timeline",
							page: "page number to return"
						}
					},
					"timeline(int sinceId, String id, int page)": {
						description: "Returns a page of 20 statuses greater than sinceId from the given user",
						parameters: {
							sinceId: "Returns only statuses with an ID greater than (that is, more recent than) the specified ID",
							id: "the ID or screen name of the user's timeline",
							page: "page number to return"
						}
					},
					returns: "list of users"
				}
			}
			if (fun == PublicInterface.search) {
				return {
					"search(String query)": {
						description: "perform a string search for tweets",
						parameters: {
							query: "the search terms"
						}
					},
					"search(options)": {
						description: "perform a search with specific options",
						options: {
							query: "the search terms",
							page: "page number to fetch",
							rpp: "number of tweets to return per page (max 100)",
							sinceId: "do not return tweets with id less than sinceId (more recent)"
						}
					},
					returns: {
						completedIn: "time for query completion",
						maxId: "id of the most recent tweet",
						page: "the page number of this result set",
						warning: "a warning about the search",
						tweets: "a list of tweets"
					}
				}
			}
			if (fun == PublicInterface.help) {
				return {
					"help()": "get a list of all the commands",
					"help(fn)": "get help about a particular function"
				}
			}
			return {
				login: "authenticate as a user. not required for searching",
				friends: "fetch friends, or friends of friends",
				followers: "list of followers, or followers of friends",
				timeline: "get the tweets from the authenticated user or authenticated user's friends",
				search: "search twitter statuses for keywords, language, geocode and much more",
				help: "get more detailed help about a function"
			}

		}
	}

	return PublicInterface
})())
