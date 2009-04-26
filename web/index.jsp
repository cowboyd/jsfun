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
    <textarea id="script" rows="15"></textarea>

    <div id="buttons">
        <img class="invisible" id="throb" src="pub/throb.gif" alt="throbber"/>
        <button id="run">Run Script</button>
        (SHIFT-ENTER)
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
                error: function(xhr, status) {
                    stdout.addClass('error').removeClass('ok')
                    append(xhr.responseText)
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
    })
</script>
</body>

</html>