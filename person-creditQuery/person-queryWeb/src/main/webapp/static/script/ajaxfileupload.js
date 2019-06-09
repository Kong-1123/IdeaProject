(function(jQuery){
if(jQuery.browser) return;
jQuery.browser = {};
jQuery.browser.mozilla = false;
jQuery.browser.webkit = false;
jQuery.browser.opera = false;
jQuery.browser.msie = false;
var nAgt = navigator.userAgent;
jQuery.browser.name = navigator.appName;
jQuery.browser.fullVersion = ''+parseFloat(navigator.appVersion);
jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);
var nameOffset,verOffset,ix;
// In Opera, the true version is after "Opera" or after "Version"
if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
jQuery.browser.opera = true;
jQuery.browser.name = "Opera";
jQuery.browser.fullVersion = nAgt.substring(verOffset+6);
if ((verOffset=nAgt.indexOf("Version"))!=-1)
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
}
// In MSIE, the true version is after "MSIE" in userAgent
else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
jQuery.browser.msie = true;
jQuery.browser.name = "Microsoft Internet Explorer";
jQuery.browser.fullVersion = nAgt.substring(verOffset+5);
}
// In Chrome, the true version is after "Chrome"
else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
jQuery.browser.webkit = true;
jQuery.browser.name = "Chrome";
jQuery.browser.fullVersion = nAgt.substring(verOffset+7);
}
// In Safari, the true version is after "Safari" or after "Version"
else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
jQuery.browser.webkit = true;
jQuery.browser.name = "Safari";
jQuery.browser.fullVersion = nAgt.substring(verOffset+7);
if ((verOffset=nAgt.indexOf("Version"))!=-1)
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
}
// In Firefox, the true version is after "Firefox"
else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
jQuery.browser.mozilla = true;
jQuery.browser.name = "Firefox";
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
}
// In most other browsers, "name/version" is at the end of userAgent
else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) <
(verOffset=nAgt.lastIndexOf('/')) )
{
jQuery.browser.name = nAgt.substring(nameOffset,verOffset);
jQuery.browser.fullVersion = nAgt.substring(verOffset+1);
if (jQuery.browser.name.toLowerCase()==jQuery.browser.name.toUpperCase()) {
jQuery.browser.name = navigator.appName;
}
}
// trim the fullVersion string at semicolon/space if present
if ((ix=jQuery.browser.fullVersion.indexOf(";"))!=-1)
jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);
if ((ix=jQuery.browser.fullVersion.indexOf(" "))!=-1)
jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);
jQuery.browser.majorVersion = parseInt(''+jQuery.browser.fullVersion,10);
if (isNaN(jQuery.browser.majorVersion)) {
jQuery.browser.fullVersion = ''+parseFloat(navigator.appVersion);
jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);
}
jQuery.browser.version = jQuery.browser.majorVersion;
})(jQuery);

jQuery.extend({
    createUploadIframe: function(id, uri)
    {
            //create frame
            var frameId = 'jUploadFrame' + id;
            var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
            if(window.ActiveXObject) {
               if(jQuery.browser.version=="9.0" || jQuery.browser.version=="10.0"){
                    var io = document.createElement('iframe');
                    io.id = frameId;
                    io.name = frameId;
                }else if(jQuery.browser.version=="6.0" || jQuery.browser.version=="7.0" || jQuery.browser.version=="8.0"){
                    var io = document.createElement('<iframe id="' + frameId + '" name="' + frameId + '" />');
                    if(typeof uri== 'boolean'){
                        io.src = 'javascript:false';
                    }
                    else if(typeof uri== 'string'){
                        io.src = uri;
                    }
                }
            }
            iframeHtml += ' />';
            jQuery(iframeHtml).appendTo(document.body);
 
            return jQuery('#' + frameId).get(0);            
    },

    createUploadForm: function(id, fileElementId,data) {
        //create form
        var formId = 'jUploadForm' + id;
        var fileId = 'jUploadFile' + id;
        var form = jQuery('<form action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
        var oldElement = jQuery('#' + fileElementId);
        var newElement = jQuery(oldElement).clone();
        jQuery(oldElement).attr('id', fileId);
        jQuery(oldElement).before(newElement);
        jQuery(oldElement).appendTo(form);
     
        // 增加参数的支持
        if (data) {
            for (var i in data) {
                $('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
            }
        }
     
        //set attributes
        jQuery(form).css('position', 'absolute');
        jQuery(form).css('top', '-1200px');
        jQuery(form).css('left', '-1200px');
        jQuery(form).appendTo('body');
        return form;
    },
 
    ajaxFileUpload: function(s) {
        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout        
        s = jQuery.extend({}, jQuery.ajaxSettings, s);
        var id = new Date().getTime();
        var form = jQuery.createUploadForm(id, s.fileElementId, s.data);
        var io = jQuery.createUploadIframe(id, s.secureuri);
        var frameId = 'jUploadFrame' + id;
        var formId = 'jUploadForm' + id;        
        // Watch for a new set of requests
        if ( s.global && ! jQuery.active++ )
        {
            jQuery.event.trigger( "ajaxStart" );
        }            
        var requestDone = false;
        // Create the request object
        var xml = {};
        if ( s.global )
            jQuery.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
        {           
            var io = document.getElementById(frameId);
            try 
            {               
                if(io.contentWindow)
                {
                     xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
                     xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
                     
                }else if(io.contentDocument)
                {
                     xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
                    xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
                }                       
            }catch(e)
            {
                jQuery.handleError(s, xml, null, e);
            }
            if ( xml || isTimeout == "timeout") 
            {               
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" )
                    {
                        // process the data (runs the xml through httpData regardless of callback)
                        var data = jQuery.uploadHttpData( xml, s.dataType );    
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success )
                            s.success( data, status );
    
                        // Fire the global callback
                        if( s.global )
                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
                    } else
                        jQuery.handleError(s, xml, status);
                } catch(e) 
                {
                    status = "error";
                    jQuery.handleError(s, xml, status, e);
                }
 
                // The request was completed
                if( s.global )
                    jQuery.event.trigger( "ajaxComplete", [xml, s] );
 
                // Handle the global AJAX counter
                if ( s.global && ! --jQuery.active )
                    jQuery.event.trigger( "ajaxStop" );
 
                // Process result
                if ( s.complete )
                    s.complete(xml, status);
 
                jQuery(io).unbind();
 
                setTimeout(function()
                                    {   try 
                                        {
                                            jQuery(io).remove();
                                            jQuery(form).remove();  
                                            
                                        } catch(e) 
                                        {
                                            jQuery.handleError(s, xml, null, e);
                                        }                                   
 
                                    }, 100);
 
                xml = null;
 
            }
        };
        // Timeout checker
        if ( s.timeout > 0 ) 
        {
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        try 
        {
 
            var form = jQuery('#' + formId);
            jQuery(form).attr('action', s.url);
            jQuery(form).attr('method', 'POST');
            jQuery(form).attr('target', frameId);
            if(form.encoding)
            {
                jQuery(form).attr('encoding', 'multipart/form-data');               
            }
            else
            {   
                jQuery(form).attr('enctype', 'multipart/form-data');            
            }           
            jQuery(form).submit();
 
        } catch(e) 
        {           
            jQuery.handleError(s, xml, null, e);
        }
        
        jQuery('#' + frameId).load(uploadCallback   );
        return {abort: function () {}}; 
 
    },
 
    // handleError 方法在jquery1.4.2之后移除了，此处重写改方法
    handleError: function( s, xhr, status, e ) {
        // If a local callback was specified, fire it
        if ( s.error ) {
            s.error.call( s.context || s, xhr, status, e );
        }
 
        // Fire the global callback
        if ( s.global ) {
            (s.context ? jQuery(s.context) : jQuery.event).trigger( "ajaxError", [xhr, s, e] );
        }
    },
 
    uploadHttpData: function( r, type ) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" ){
            eval('data = " "+data+" " ');
        }
        // evaluate scripts within html
        if ( type == "html" )
            jQuery("<div>").html(data).evalScripts();
 
        return data;
    }
});