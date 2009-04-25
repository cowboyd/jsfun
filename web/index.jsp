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
    <textarea id="script" rows="15">
//uncomment one of the following to get started
//help()
//help(search) //can be used with any function
    </textarea>
    <div id="buttons">
        <img class="invisible" id="throb" src="pub/throb.gif" alt="throbber"/>
        <button id="run">Run Script</button>
    </div>
</div>    
<div id="stdout">
    <pre class="brush: js"></pre>
</div>
<script type="text/javascript">
    $(function() {
        var stdout = $('#stdout')
        var run = $('#run')
        var script = $('#script')
        var throb = $('#throb')
        var height = window.innerHeight || document.documentElement.clientHeight
        stdout.css('height', Math.max(height, $('html').attr('scrollHeight')))

        function append(text) {
            stdout.html("<pre>" + text + "</pre>\n");
        }

        run.click(function() {
            throb.removeClass('invisible')
            $.ajax({
                url: "api",
                type: "POST",
                data: script.val(),
                dataType: 'text',
                processData: false,
                success: function(data) {
                    append(data)
                },
                error: function(xhr, status) {
                   append(xhr.responseText)
                },
                complete: function() {
                    throb.toggleClass('invisible', true)
                }
            })
        })
    })
</script>
</body>

</html>