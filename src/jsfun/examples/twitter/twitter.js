((function() {

	var Context = Packages.org.mozilla.javascript.Context;
	var Query = Packages.twitter4j.Query
	//var out = java.lang.System.out


	function string(s) {
		return "" + s
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

	return {
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
				} else if (!el[2]) {
					els[2] = null
				}
				q.setGeocode(els[0], els[1], els[2])
			}
			return apicall('search', [q], QueryResult)
		}
	}
})())
