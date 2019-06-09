  
 //<![CDATA[

//*****************************************************************************
// Do not remove this notice.
//
// Copyright 2000-2004 by Mike Hall.
// See http://www.brainjar.com for terms of use.
//*****************************************************************************

//----------------------------------------------------------------------------
// Code to determine the browser and version.
//----------------------------------------------------------------------------

function Browser() {

  var ua, s, i;

  this.isIE    = false;  // Internet Explorer
  this.isOP    = false;  // Opera
  this.isNS    = false;  // Netscape
  this.version = null;

  ua = navigator.userAgent;

  s = "Opera";
  if ((i = ua.indexOf(s)) >= 0) {
    this.isOP = true;
    this.version = parseFloat(ua.substr(i + s.length));
    return;
  }

  s = "Netscape6/";
  if ((i = ua.indexOf(s)) >= 0) {
    this.isNS = true;
    this.version = parseFloat(ua.substr(i + s.length));
    return;
  }

  // Treat any other "Gecko" browser as Netscape 6.1.

  s = "Gecko";
  if ((i = ua.indexOf(s)) >= 0) {
    this.isNS = true;
    this.version = 6.1;
    return;
  }

  s = "MSIE";
  if ((i = ua.indexOf(s))) {
    this.isIE = true;
    this.version = parseFloat(ua.substr(i + s.length));
    return;
  }
}

var browser = new Browser();

//----------------------------------------------------------------------------
// Code for handling the menu bar and active button.
//----------------------------------------------------------------------------

var activeButton = null;

// Capture mouse clicks on the page so any active button can be
// deactivated.

if (browser.isIE)
  document.onmousedown = pageMousedown;
else
  document.addEventListener("mousedown", pageMousedown, true);

function pageMousedown(event) {

  var el;

  // If there is no active button, exit.

  if (activeButton == null)
    return;

  // Find the element that was clicked on.

  if (browser.isIE)
    el = window.event.srcElement;
  else
    el = (event.target.tagName ? event.target : event.target.parentNode);

  // If the active button was clicked on, exit.

  if (el == activeButton)
    return;

  // If the element is not part of a menu, reset and clear the active
  // button.

  if (getContainerWith(el, "DIV", "menu") == null) {
    resetButton(activeButton);
    activeButton = null;
  }
}

function buttonClick(event, menuId) {

  var button;

  // Get the target button element.

  if (browser.isIE)
    button = window.event.srcElement;
  else
    button = event.currentTarget;

  // Blur focus from the link to remove that annoying outline.

  button.blur();

  // Associate the named menu to this button if not already done.
  // Additionally, initialize menu display.

  if (button.menu == null) {
    button.menu = document.getElementById(menuId);
    if (button.menu.isInitialized == null)
      menuInit(button.menu);
  }

  // Reset the currently active button, if any.

  if (activeButton != null)
    resetButton(activeButton);

  // Activate this button, unless it was the currently active one.

  if (button != activeButton) {
    depressButton(button);
    activeButton = button;
  }
  else
    activeButton = null;

  return false;
}

function buttonMouseover(event, menuId) {

  var button;

  // Find the target button element.

  if (browser.isIE)
    button = window.event.srcElement;
  else
    button = event.currentTarget;

  // If any other button menu is active, make this one active instead.

  if (activeButton != null && activeButton != button)
    buttonClick(event, menuId);
}

function depressButton(button) {

  var x, y;

  // Update the button's style class to make it look like it's
  // depressed.

  button.className += " menuButtonActive";

  // Position the associated drop down menu under the button and
  // show it.

  x = getPageOffsetLeft(button);
  y = getPageOffsetTop(button) + button.offsetHeight;

  // For IE, adjust position.

  if (browser.isIE) {
    x += button.offsetParent.clientLeft;
    y += button.offsetParent.clientTop;
  }

  button.menu.style.left = x + "px";
  button.menu.style.top  = y + "px";
  button.menu.style.visibility = "visible";

  // For IE; size, position and show the menu's IFRAME as well.

  if (button.menu.iframeEl != null)
  {
    button.menu.iframeEl.style.left = button.menu.style.left;
    button.menu.iframeEl.style.top  = button.menu.style.top;
    button.menu.iframeEl.style.width  = button.menu.offsetWidth + "px";
    button.menu.iframeEl.style.height = button.menu.offsetHeight + "px";
    button.menu.iframeEl.style.display = "";
  }
}

function resetButton(button) {

  // Restore the button's style class.

  removeClassName(button, "menuButtonActive");

  // Hide the button's menu, first closing any sub menus.

  if (button.menu != null) {
    closeSubMenu(button.menu);
    button.menu.style.visibility = "hidden";

    // For IE, hide menu's IFRAME as well.

    if (button.menu.iframeEl != null)
      button.menu.iframeEl.style.display = "none";
  }
}

//----------------------------------------------------------------------------
// Code to handle the menus and sub menus.
//----------------------------------------------------------------------------

function menuMouseover(event) {

  var menu;

  // Find the target menu element.

  if (browser.isIE)
    menu = getContainerWith(window.event.srcElement, "DIV", "menu");
  else
    menu = event.currentTarget;

  // Close any active sub menu.

  if (menu.activeItem != null)
    closeSubMenu(menu);
}

function menuItemMouseover(event, menuId) {

  var item, menu, x, y;

  // Find the target item element and its parent menu element.

  if (browser.isIE)
    item = getContainerWith(window.event.srcElement, "A", "menuItem");
  else
    item = event.currentTarget;
  menu = getContainerWith(item, "DIV", "menu");

  // Close any active sub menu and mark this one as active.

  if (menu.activeItem != null)
    closeSubMenu(menu);
  menu.activeItem = item;

  // Highlight the item element.

  item.className += " menuItemHighlight";

  // Initialize the sub menu, if not already done.

  if (item.subMenu == null) {
    item.subMenu = document.getElementById(menuId);
    if (item.subMenu.isInitialized == null)
      menuInit(item.subMenu);
  }

  // Get position for submenu based on the menu item.

  x = getPageOffsetLeft(item) + item.offsetWidth;
  y = getPageOffsetTop(item);

  // Adjust position to fit in view.

  var maxX, maxY;

  if (browser.isIE) {
    maxX = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft) +
      (document.documentElement.clientWidth != 0 ? document.documentElement.clientWidth : document.body.clientWidth);
    maxY = Math.max(document.documentElement.scrollTop, document.body.scrollTop) +
      (document.documentElement.clientHeight != 0 ? document.documentElement.clientHeight : document.body.clientHeight);
  }
  if (browser.isOP) {
    maxX = document.documentElement.scrollLeft + window.innerWidth;
    maxY = document.documentElement.scrollTop  + window.innerHeight;
  }
  if (browser.isNS) {
    maxX = window.scrollX + window.innerWidth;
    maxY = window.scrollY + window.innerHeight;
  }
  maxX -= item.subMenu.offsetWidth;
  maxY -= item.subMenu.offsetHeight;

  if (x > maxX)
    x = Math.max(0, x - item.offsetWidth - item.subMenu.offsetWidth
      + (menu.offsetWidth - item.offsetWidth));
  y = Math.max(0, Math.min(y, maxY));

  // Position and show it.

  item.subMenu.style.left       = x + "px";
  item.subMenu.style.top        = y + "px";
  item.subMenu.style.visibility = "visible";

  // For IE; size, position and show the menu's IFRAME as well.

  if (item.subMenu.iframeEl != null)
  {
    item.subMenu.iframeEl.style.left    = item.subMenu.style.left;
    item.subMenu.iframeEl.style.top     = item.subMenu.style.top;
    item.subMenu.iframeEl.style.width   = item.subMenu.offsetWidth + "px";
    item.subMenu.iframeEl.style.height  = item.subMenu.offsetHeight + "px";
    item.subMenu.iframeEl.style.display = "";
  }

  // Stop the event from bubbling.

  if (browser.isIE)
    window.event.cancelBubble = true;
  else
    event.stopPropagation();
}

function closeSubMenu(menu) {

  if (menu == null || menu.activeItem == null)
    return;

  // Recursively close any sub menus.

  if (menu.activeItem.subMenu != null) {
    closeSubMenu(menu.activeItem.subMenu);


    // Hide the sub menu.
    menu.activeItem.subMenu.style.visibility = "hidden";

    // For IE, hide the sub menu's IFRAME as well.

    if (menu.activeItem.subMenu.iframeEl != null)
      menu.activeItem.subMenu.iframeEl.style.display = "none";

    menu.activeItem.subMenu = null;
  }

  // Deactivate the active menu item.

  removeClassName(menu.activeItem, "menuItemHighlight");
  menu.activeItem = null;
}

//----------------------------------------------------------------------------
// Code to initialize menus.
//----------------------------------------------------------------------------

function menuInit(menu) {

  var itemList, spanList;
  var textEl, arrowEl;
  var itemWidth;
  var w, dw;
  var i, j;

  // For IE, replace arrow characters.

  if (browser.isIE) {
    menu.style.lineHeight = "2.5ex";
    spanList = menu.getElementsByTagName("SPAN");
    for (i = 0; i < spanList.length; i++)
      if (hasClassName(spanList[i], "menuItemArrow")) {
        spanList[i].style.fontFamily = "Webdings";
        spanList[i].firstChild.nodeValue = "4";
      }
  }

  // Find the width of a menu item.

  itemList = menu.getElementsByTagName("A");
  if (itemList.length > 0)
    itemWidth = itemList[0].offsetWidth;
  else
    return;

  // For items with arrows, add padding to item text to make the
  // arrows flush right.

  for (i = 0; i < itemList.length; i++) {
    spanList = itemList[i].getElementsByTagName("SPAN");
    textEl  = null;
    arrowEl = null;
    for (j = 0; j < spanList.length; j++) {
      if (hasClassName(spanList[j], "menuItemText"))
        textEl = spanList[j];
      if (hasClassName(spanList[j], "menuItemArrow")) {
        arrowEl = spanList[j];
      }
    }
    if (textEl != null && arrowEl != null) {
      textEl.style.paddingRight = (itemWidth 
        - (textEl.offsetWidth + arrowEl.offsetWidth)) + "px";

      // For Opera, remove the negative right margin to fix a display bug.

      if (browser.isOP)
        arrowEl.style.marginRight = "0px";
    }
  }

  // Fix IE hover problem by setting an explicit width on first item of
  // the menu.

  if (browser.isIE) {
    w = itemList[0].offsetWidth;
    itemList[0].style.width = w + "px";
    dw = itemList[0].offsetWidth - w;
    w -= dw;
    itemList[0].style.width = w + "px";
  }

  // Fix the IE display problem (SELECT elements and other windowed controls
  // overlaying the menu) by adding an IFRAME under the menu.

  if (browser.isIE) {
    var iframeEl = document.createElement("IFRAME");
    iframeEl.frameBorder = 0;
    iframeEl.src = "javascript:;";
    iframeEl.style.display = "none";
    iframeEl.style.position = "absolute";
    iframeEl.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";
    menu.iframeEl = menu.parentNode.insertBefore(iframeEl, menu);
  }

  // Mark menu as initialized.

  menu.isInitialized = true;
}

//----------------------------------------------------------------------------
// General utility functions.
//----------------------------------------------------------------------------

function getContainerWith(node, tagName, className) {

  // Starting with the given node, find the nearest containing element
  // with the specified tag name and style class.

  while (node != null) {
    if (node.tagName != null && node.tagName == tagName &&
        hasClassName(node, className))
      return node;
    node = node.parentNode;
  }

  return node;
}

function hasClassName(el, name) {

  var i, list;

  // Return true if the given element currently has the given class
  // name.

  list = el.className.split(" ");
  for (i = 0; i < list.length; i++)
    if (list[i] == name)
      return true;

  return false;
}

function removeClassName(el, name) {

  var i, curList, newList;

  if (el.className == null)
    return;

  // Remove the given class name from the element's className property.

  newList = new Array();
  curList = el.className.split(" ");
  for (i = 0; i < curList.length; i++)
    if (curList[i] != name)
      newList.push(curList[i]);
  el.className = newList.join(" ");
}

function getPageOffsetLeft(el) {

  var x;

  // Return the x coordinate of an element relative to the page.

  x = el.offsetLeft;
  if (el.offsetParent != null)
    x += getPageOffsetLeft(el.offsetParent);

  return x;
}

function getPageOffsetTop(el) {

  var y;

  // Return the x coordinate of an element relative to the page.

  y = el.offsetTop;
  if (el.offsetParent != null)
    y += getPageOffsetTop(el.offsetParent);

  return y;
}




    function searchRec(){
    	
    	form1.exportflag.value="none";
	form1.action="feesheetlist.action";
	document.form1.submit();				
	}
	
	function newRec(){
		window.location.href="feeSheetnew.jsp";
	}
	function exportExcel(){
		form1.exportflag.value="excel";
		form1.submit();
	}
	
	function turnOver(pageno){	
		document.form1.curPage.value = pageno;
		document.form1.submit();				
	}
	function updateit(frm,id){
			frm.action="feesheetdisplay.action";
			frm.submit();
	}
	function deleteit(frm,id){
		if(confirm('È·¶¨ÒªÉ¾³ýÂð?')){
			frm.action="feesheetdelete.action";
			frm.submit();
		}
	}
//??????
  function showmenu(strID){

		var i;
		var img_closefolder=new Image();
		var img_openfolder=new Image();	
		var arrow;
		var lay;
		//??????????
      for(i=0;i<Menu.length;i++){
			lay = eval('lay' + i);
			arrow = eval('arrow' + i);
			
			if (lay.style.display=="block" && lay!=eval("lay"+strID)){
				lay.style.display = "none";
				}
			
			}
		//var parentMenu=eval('parentMenu'+strID);
		
		var layId=eval('lay'+strID);
		if (layId.style.display=="none"){
			layId.style.display = "block";
			
		}else{
			layId.style.display = "none";
			
		}
		//???????
	for(i=0;i<Menu.length;i++){
		lay = eval('lay' + i);
		arrow = eval('arrow' + i);
		if(lay.style.display=="block"){
		arrow.src="images/left_3.gif";
		 }
		else{				
		arrow.src="images/left_5.gif";
		  }
	}
			   
	}

//LOAD??,??????
function showmenu1(strID){

		var i;
		var img_closefolder=new Image();
		var img_openfolder=new Image();	
		var arrow;
		
      for(i=0;i<Menu.length;i++){
			var lay;
			lay = eval('lay' + i);
			arrow = eval('arrow' + strID);
			arrow.src="images/left_3.gif";
			if (lay.style.display=="block" && lay!=eval("lay"+strID)){
				lay.style.display = "none";
				}
			}
		//var parentMenu=eval('parentMenu'+strID);
		
		var layId=eval('lay'+strID);
		if (layId.style.display=="none"){
			layId.style.display = "block";
			
		}else{
			layId.style.display = "none";
			
		}
	}


	function showMenuTable(target){
	  
	   for (var i=0;i<Menu.length;i++){

		//document.write(Menu[i][0]);

		document.write("<table bgcolor=\"#D3E4F7\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\">");
		document.write("  <tr    id=\"parentMenu"+i+"\" onMouseOver=\"this.style.backgroundColor='"+z_bscolor+"'\" onMouseOut=\"this.style.backgroundColor='"+bgcolorDark+"'\"> ");
		document.write("		<td width=\"100%\">");
		
		//Parent Menu 
		document.write('<div   onClick="showmenu(\''+i+'\');" style="cursor:hand">');
              
		document.write(" <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"  width=\"100%\">");
		document.write("	<tr><td width=\"20\"><img src=\"images/left_1.gif\" width=\"20\" height=\"23\"></td><td  background=\"images/left_2.gif\"  width=\"100%\"><div align=\"center\" style=\"width:95%\" >"+Menu[i][0]+"</div></td><td width=\"23\"><img id=\"arrow"+i+"\" src=\"images/left_5.gif\" width=\"23\" height=\"23\"></td></tr>");
		document.write(" </table>");
		
		document.write('</div>');
		

		//document.write("</td>");
		//document.write("	  </tr>");
		//document.write("	  <tr id=\"lay"+i+"\"  STYLE=\"display:none;\"> ");
		//document.write("		<td>");
		 //???table
		document.write("<table id=\"lay"+i+"\" STYLE=\"display:none;\" width=\"100%\"  border=\"1\" cellpadding=\"0\" cellspacing=\"1\" bordercolor=\"#5A8CC1\" bgcolor=\"#FFFFFF\"><tr><td bordercolor=\"#FFFFFF\">");
		//?
		
		document.write('<div id="um1" >') ;

	 
		document.write("		<table width=\"95%\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"list\" ");

		//Children Menu
		for (var j=1;j<Menu[i].length-2;j++){
					
			document.write("<tr vlign='middle'>");
			document.write("<td align='left'  onMouseOver=\"this.style.backgroundColor='#D3E4F7'\" onMouseOut=\"this.style.backgroundColor='"+bgcolorDark+"'\">");
			
			document.write("&nbsp;&nbsp;<img  width=\"4\"  src=\"images/left_4.gif\"  height=\"6\" hspace=\"2\"><a href=\""+Menu[i][j+1]+"\"  target=\""+target+"\"  style=\"width:100%;text-decoration:none\" >&nbsp;&nbsp;<font size=\"2\" color=\"#000000\" >"+Menu[i][j]+"</font></a>");
			//alert("<img  width=\"4\"  src=\"images/left_4.gif\"  height=\"8\" hspace=\"2\"><a href=\""+Menu[i][j+1]+"\"  target=\""+target+"\"  style=\"width:100%\">"+Menu[i][j]+"</a>")
			j++;
			
			document.write("		</td></tr>");
			
		}
		
		document.write("		</table>");
		document.write(" </div>");
		//???table
		document.write("		</td></tr></table>");
		//?
		
		document.write("		</td>");
		document.write("	  </tr>");
		document.write("	    <tr><td>&nbsp;</td></tr><tr><td></td></tr>");
		document.write(" </table>");
		}
	}
	

