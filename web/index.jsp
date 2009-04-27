<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Twitter API</title>
    <link rel="stylesheet" type="text/css" href="pub/style.css"/>

    <script type="text/javascript" src="pub/jquery-1.3.2.min.js"></script>

</head>
<body>
<div id="controls">
    <strong>username:</strong> <input id="username" type="text" size="15"/> <strong>password:</strong>
    <input id="password" type="password" size="15"/> (not stored. not necessary for search)
    <textarea id="script" rows="25" style="font-size: 12px;"></textarea>

    <div id="buttons">
        <img class="invisible" id="throb" src="pub/throb.gif" alt="throbber"/>
        <button id="run">Run Script</button>
        (SHIFT-ENTER)
        <a href="#ClientStats" class="example">ClientStats</a>
        <a href="#Throttle" class="example">Throttle</a>
        <a href="#A2Javascript" class="example">A2 Javascript</a>

<div id="ClientStats" class="hidden">/*
* Searches your friends and followers, and counts what people are using to post to twitter
* You must authenticate to run this script
*/

function countClients(users) {
  var clients = {}
  for (var i = 0; i < users.length; i++) {
    var source = users[i].statusSource
    var count = clients[source]
    if (!count) {
      count = 0
      clients[source] = count
    }
    clients[source] += 1;
  }
  return clients
}

({
  friends: countClients(friends()),
  followers: countClients(followers())
})</div>
<div id="Throttle" class="hidden">/*
* This is a would-be malicious script that loops infinitely.
*
* Rhino is configured to limit the number of instructions the client can execute to prevent
* denial of service attacks, limit load on the server,
* and protect against programmer error.
*/

while (true);
</div>
<div id="A2Javascript" class="hidden">/*
* Searches all tweets about javascript within a 15mile radius of Ann Arbor
*
* Could be used, for example to automatically notify people about AAJUG meetings
* automatically
*/

var a2java = search({
  query: "javascript",
  rpp: 100,
  geocode: "42.281875,-83.748479,15mi"
})

var result = {}
for (var i = 0; i < a2java.tweets.length; i++) {
  var tweet = a2java.tweets[i]
  result[tweet.fromUser] = tweet.text
}

result;
</div>        
    </div>
    <div style="overflow: auto;">
        <pre style="font-size: 9px; height: 500px;">
{
  HELP: {
      help(): "get a list of all the commands",
      help(fn): "get help about a particular function"
  },
  SEARCH: {
    search(String query): {
      description: "perform a string search for tweets",
      parameters: {
        query: "the search terms"
      }
    },
    search(options): {
      description: "perform a search with specific options",
      options: {
        query: "the search terms",
        lang: "return results only with this two letter ISO Language Code",
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
  },
  TIMELINE: {
    timeline(): {
      description: "Returns up to 20 statuses from the authenticating user and that user's friends.",
      parameters: {}
    },
    timeline(int page): {
      description: "Returns up to 20 most statuses posted from the authenticating user.",
      parameters: {
        id: "page number to return"
      }
    },
    timeline(int sinceId, int page): {
      description: "Returns up to 20 most recent statuses greater than sincId from the authenticating user",
      parameters: {
        sinceId: "Returns only statuses with an ID greater than (that is, more recent than) the specified ID",
        page: "page number to return"
      }
    },
    timeline(String id): {
      description: "Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.",
      parameters: {
        id: "the ID or screen name of the user's timeline"
      }
    },
    timeline(String id, int page): {
      description: "Returns up to 20 from the specified userid.",
      parameters: {
        id: "the ID or screen name of the user's timeline",
        page: "page number to return"
      }
    },
    timeline(int sinceId, String id, int page): {
      description: "Returns a page of 20 statuses greater than sinceId from the given user",
      parameters: {
        sinceId: "Returns only statuses with an ID greater than (that is, more recent than) the specified ID",
        id: "the ID or screen name of the user's timeline",
        page: "page number to return"
      }
    },
    returns: "list of users"
  },
  FRIENDS: {
    friends(): {
      description: "Returns the friends of the currently authenticated user.",
      parameters: {}
    },
    friends(int page): {
      description: "Returns the specified user's friends, each with current status inline.",
      parameters: {
        page: "the page number to return"
      }
    },
    friends(String id): {
      description: "Returns the user's friends, each with current status inline.",
      parameters: {
        id: "the ID or screen name of the user for whom to request a list of friends"
      }
    },
    friends(String id, int page): {
      description: "Returns the user's friends, each with current status inline.",
      parameters: {
        id: "the ID or screen name of the user for whom to request a list of friends",
        page: "the page number to return"
      }
    },
    returns: "list of users"
  },
  FOLLOWERS: {
    followers(): {
      description: "Returns the authenticating user's followers, each with current status inline."
    },
    followers(int page): {
      description: "Returns the authenticating user's followers, each with current status inline. ",
      parameters: {
        page: "Retrieves the next 100 followers."
      }
    },
    followers(String id): {
      description: "Returns the specified user's followers, each with current status inline.",
      parameters: {
        id: "The ID or screen name of the user for whom to request a list of followers."
      }
    },
    followers(String id, int page): {
      description: "Returns the specified user's followers, each with current status inline.",
      parameters: {
        id: "The ID or screen name of the user for whom to request a list of followers.",
        page: "Retrieves the next 100 followers."
      }
    },
    returns: "list of users"
  }
}
        </pre>
    </div>
</div>
<div id="stdout">
    <pre class="brush: js"></pre>
</div>
<script type="text/javascript">
    $(function() {
        var stdout = $('#stdout')
        var run = $('#run')
        var script = $('#script').focus()
        var throb = $('#throb')
        var username = $('#username')
        var password = $('#password')
        var height = window.innerHeight || document.documentElement.clientHeight
        stdout.css('height', Math.max(height, $('html').attr('scrollHeight')))

        function append(text) {
            stdout.html("<pre>" + text + "</pre>\n");
        }

        function exec() {
            throb.removeClass('invisible')
            $.ajax({
                url: "api",
                type: "POST",
                data: {
                    u: username.val(),
                    p: password.val(),
                    script: script.val()
                },
                dataType: 'text',
                //processData: false,
                success: function(data) {
                    stdout.removeClass('error').addClass('ok')
                    append(data)
                },
                error: function(xhr, status, errorThrown) {
                    if (xhr.status == 0) {
                        alert('unable to connect to server')
                    } else {
                        stdout.addClass('error').removeClass('ok')
                        append(xhr.responseText)
                    }
                },
                complete: function() {
                    throb.toggleClass('invisible', true)
                }
            })
        }

        run.click(exec)

        $(window).keypress(function(e) {
            if (e.which == 13 && e.shiftKey) {
                exec()
                return false
            }
        })

        $('.example').click(function() {
            script.val($($(this).attr('href')).text())
            script.focus()
            return false;
        })
    })
</script>
</body>

</html>