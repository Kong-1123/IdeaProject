// ie兼容console
window._console = window.console;//将原始console对象缓存
window.console = (function (orgConsole) {
    return {//构造的新console对象
        log: getConsoleFn("log"),
        debug: getConsoleFn("debug"),
        info: getConsoleFn("info"),
        warn: getConsoleFn("warn"),
        exception: getConsoleFn("exception"),
        assert: getConsoleFn("assert"),
        dir: getConsoleFn("dir"),
        dirxml: getConsoleFn("dirxml"),
        trace: getConsoleFn("trace"),
        group: getConsoleFn("group"),
        groupCollapsed: getConsoleFn("groupCollapsed"),
        groupEnd: getConsoleFn("groupEnd"),
        profile: getConsoleFn("profile"),
        profileEnd: getConsoleFn("profileEnd"),
        count: getConsoleFn("count"),
        clear: getConsoleFn("clear"),
        time: getConsoleFn("time"),
        timeEnd: getConsoleFn("timeEnd"),
        timeStamp: getConsoleFn("timeStamp"),
        table: getConsoleFn("table"),
        error: getConsoleFn("error"),
        memory: getConsoleFn("memory"),
        markTimeline: getConsoleFn("markTimeline"),
        timeline: getConsoleFn("timeline"),
        timelineEnd: getConsoleFn("timelineEnd")
    };
    function getConsoleFn(name) {
        return function actionConsole() {
            if (typeof (orgConsole) !== "object") return;
            if (typeof (orgConsole[name]) !== "function") return;//判断原始console对象中是否含有此方法，若没有则直接返回
            return orgConsole[name].apply(orgConsole, Array.prototype.slice.call(arguments));//调用原始函数
        };
    }
}(window._console));

// IE8兼容性解决forEach()
if ( !Array.prototype.forEach ) {  
      Array.prototype.forEach = function forEach( callback, thisArg ) {  
        var T, k;  
        if ( this == null ) {  
          throw new TypeError( "this is null or not defined" );  
        }  
        var O = Object(this);  
        var len = O.length >>> 0;   
        if ( typeof callback !== "function" ) {  
          throw new TypeError( callback + " is not a function" );  
        }  
        if ( arguments.length > 1 ) {  
          T = thisArg;  
        }  
        k = 0;  
        while( k < len ) {  
          var kValue;  
          if ( k in O ) {  
            kValue = O[ k ];  
            callback.call( T, kValue, k, O );  
          }  
          k++;  
        }  
      };  
} 