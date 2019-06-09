var lastTab = null;
function tabToggle(tabID){
	if ((lastTab != tabID) && (lastTab != null)){
		lastTab.style.zIndex = "1";
		lastTab.style.top = "3";
		lastTab.style.backgroundImage='url(../../images/UI_graytab.gif)';
		lastTab.style.fontWeight='normal';
		lastTab.style.paddingTop='2px';
    	}
	if (tabID.style.zIndex == "3")
		{
      	tabID.style.zIndex = "3";
	  	tabID.style.top = "0";
		tabID.style.backgroundImage='url(../../images/UI_bluetab.gif)';
		tabID.style.fontWeight='bold';
		tabID.style.paddingTop='4px';
		}
	else 
		{
		tabID.style.zIndex = "3"; 
		tabID.style.top = "0";
		tabID.style.backgroundImage='url(../../images/UI_bluetab.gif)';
		tabID.style.fontWeight='bold';
		tabID.style.paddingTop='4px';
   		lastTab = tabID;
    	}
}

var lastDisplay = null;
function displayToggle(displayID){

	if ((lastDisplay != displayID) && (lastDisplay != null)){
	  lastDisplay.style.display="none";
    }
	if (displayID.style.display=="block"){
      displayID.style.display="block";
	}
	else {displayID.style.display="block"; 
   	  lastDisplay = displayID;
    }
}