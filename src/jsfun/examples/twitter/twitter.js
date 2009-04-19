


(function() {
	var twitter = new Packages.twitter4j.Twitter()
	var Query = Packages.twitter4j.Query

	var FIELDS = {
		Status: ['createdAt', 'id', 'inReplyToStatusId', 'inReplyToUserId', 'source', 'text', 'user'],
		User: ['description', 'followersCount', 'id', 'location', 'name', 'profileImageURL', 'screenName'],
		QueryResult: ['page', 'query', 'total', 'tweets'],
		Tweet: ['id', 'createdAt', 'fromUser', 'toUser', 'source', 'text']		
	}

	function unwrap(object) {
		var fields = object.getClass ? FIELDS[object.getClass().getSimpleName()] : null
		if (fields) {
			var value = {}
			for (var field in fields) {
				value[field] = unwrap(object[field])
			}
			return value
		} else if (object) {
			return "" + object.toString()
		} else {
			return ""
		}
	}

	function method(impl) {
		var fun = function() {
			return unwrap(impl.apply(this, arguments))
		}
		fun.toSource = fun.toString = function() {return "function() { [Native Code] }"}
	}

	login = method(function(username, password) {
		twitter.userId = username
		twitter.password = password
	})

	friends = method(function() {
		return twitter.getFriends.apply(twitter, arguments)
	})

	followers = method(function() {
		return twitter.getFollowers.apply(twitter, arguments)
	})

	timeline = method(function() {
		return twitter.getFriendsTimeline.apply(twitter, arguments)
	})

	search = function(query) {
		query = query || {}
		if (typeof query == 'string') {
			query = {
				query: query
			}
		}
		var q = new Query()
		for (var field in ['lang', 'page', 'query', 'rpp', 'sinceId']) {
			if (query[field]) {
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
			q.setGeocode(els[0],els[1], els[2])
		}
		return twitter.search(q)
	}
})()