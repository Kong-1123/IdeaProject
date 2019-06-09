function LoadMyFile(arguments) //函数可以批量引入多个js、css
{
    for (var i = 0; i < arguments.length; i++) {
        var file = arguments[i];
        if (file.match(/.*.js$/))
            document.write('<script type="text/javascript" src="' + file + '"></script>');
        else if (file.match(/.*.css$/))
            document.write('<link rel="stylesheet" href="' + file + '" type="text/css" />');
    }
}
function yinru(argument) //函数可以单独引入一个js或者css
{
    var file = argument;
    if (file.match(/.*.js$/)) //以任意开头但是以.js结尾正则表达式
    {
        document.write('<script type="text/javascript" src="' + file + '"></script>');
    } else if (file.match(/.*.css$/)) {
        document.write('<link rel="stylesheet" href="' + file + '" type="text/css" />');
    }
}
function loadJs(src, callback) {
    var script = document.createElement('script');
    script.type="text/javascript";
    script.language = 'javascript';
    script.src= src;
    var done = false;
    script.onload =script.onreadystatechange = function() {
            if (!done &&(!script.readyState ||  script.readyState == 'loaded' || script.readyState == 'complete')) {
                    done = true;
            }
            script.onload = script.onreadystatechange = null;
            if (callback) {
                    callback.call(script);
            }
    }
    document.getElementsByTagName('head')[0].appendChild(script);
}

function loadCss(url, callback) {
        var link = document.createElement('link');
        link.type="text/css";
        link.rel = "stylesheet";
        link.href = url;
        link.media = 'screen';
        document.getElementsByTagName('head')[0].appendChild(link);
        if (callback) {
                callback.call(link);
        }
}