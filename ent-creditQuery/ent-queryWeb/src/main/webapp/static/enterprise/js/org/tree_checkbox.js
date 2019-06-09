//***************************************************
//
//modified by zhangwl, 2005/8/3
//[1] add global variable _type
//
//***************************************************
var t;
var ID;
var DESC;
var _type;
var _orglevel;

function addFromXML(path, myTree, myID, myParent, url, target){

	var sID, sDesc, sParent;
	var parenObj = myTree.getElement(myID);

	var vXmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	var httpOb = new ActiveXObject("Microsoft.XMLHTTP");
	var pathstr= path + "?ID=" + myID + "&TYPE=" + _type + "&LEVEL=" + _orglevel +  "&DEPTH=" + parenObj._level ;
	httpOb.Open("POST",pathstr, false);
	httpOb.send("");
	vXmlDoc = httpOb.responseXML; // or responseText
	if (vXmlDoc != null){
		var myNodes = vXmlDoc.selectNodes("/nodes/node");
		for(var i=0;i<myNodes.length;i++){
	  		var myNode = myNodes.item(i);
  			sID = myNode.selectSingleNode("id").text;
   			sDesc = myNode.selectSingleNode("desc").text;
   			addTreeElementX(path, myTree, parenObj, sDesc, sID, url, target);
  		}
  		if (myNodes.length!=0) {
			parenObj.render(true, true);
		}
	}
}

function checkboxChanged(treeElement) {
 // alert('改变 ' + treeElement.caption + ' 新值 ' + treeElement.isChecked);
}



function radioClick(path, sID, url, target) {
	var nodeObj = t.getElement(sID);
 	var sParentDesc;
	var sDesc;

	t.elementToggleOpenClose(nodeObj.id);

	if (nodeObj._children.length == 0) {
		addFromXML(path, t, sID, "", url, target);
		if (nodeObj._children.length != 0) {
			nodeObj.setCaption(nodeObj.caption + " (" + nodeObj._children.length +")");
		}
	}

	var ind = nodeObj.caption.indexOf("(");
	sDesc = nodeObj.caption;

	if ( ind > 0) {
		sDesc = sDesc.substring(0, ind);
	}
	setParentValue(sID, sDesc);
	document.all(sID).checked=true;
}

function myActionA(path, sID, url, target) {
 
  //对金融机构代码输入框赋值
  setOrgCode(sID);		

	var nodeObj = t.getElement(sID);
	t.elementToggleOpenClose(nodeObj.id);
	if (nodeObj._children.length == 0) {
		addFromXML(path, t, sID, "", url, target);
		if (nodeObj._children.length != 0) {
			//zhangwl modify 2005/8/22
			//nodeObj.setCaption(nodeObj.caption + " (" + nodeObj._children.length +")");
		//	nodeObj.setCaption(nodeObj.caption);
		//alert(sID);
		//alert(parent.document.all(target).src);
		//alert(url);
		var url1 ;
		if(url != '')url1 = url + "?ID=" + sID;
		//parent.document.all(target).src = url1;
		}
		
	}
	nodeObj.radioButtonSelected = !nodeObj.radioButtonSelected;
}

function setOrgCode(sID) {
	window.returnValue=sID;
  var arguments = window.dialogArguments;

  if(arguments != null) {
    arguments.value = sID;
  }
}

function addTreeElementX(path, myTree, parentObj, sCaption, sID, sUrl, sTarget) {
	var elementData = new Array;
	var url = sUrl;
	elementData.caption = sCaption;
	elementData.id = sID;

	if(url != '')url = url + "?ID=" + sID;
	elementData.url     = url;
	elementData.target  = sTarget;
	elementData.radioButtonSelected = false;
	elementData.radioAction = "radioClick('" + path + "','" + sID + "','" + sUrl + "','" + sTarget + "')";
	elementData.onClick = "javascript:myActionA('" + path + "','" + sID + "','" + sUrl + "','" + sTarget + "');";
	parentObj.addChildByArrayX(elementData);
}

function init(rootId, rootCaption, type, level, path, divId, url, target, imagePath) {

  	var a = new Array;
	a[0] = new Array;
	a[0]['id']          = rootId;
	a[0]['caption']          = rootCaption;
	a[0]['onClick'] = "javascript:myActionA('"+ path + "','" + rootId + "','" + url + "','" + target + "');";
	a[0].radioButtonSelected = false;
	a[0]['url']              = url + "?ID=" + rootId ;
	a[0]['target']           = target;
	a[0]['isOpen']           = true;
	//a[0]['radioAction'] = "radioClick('" + path + "','" + rootId + "','" + url + "','" + target + "')";
  a[0]['onChangeCheckbox'] = checkboxChanged;
	//add by zwl
	//begin
	_type = type;
	_orglevel = level;
	//end
   	t = new Bs_Tree();
  	//t.imageDir = imagePath;
  	//if(imagePath.indexOf(";") != -1) {
		//t.imageDir = imagePath.substring(0, imagePath.indexOf(";"));
	//}
	
	//new add 
  // t.imageDir = '/InfoProject/images/winXp/';
t.imageDir = imagePath;
   t.checkboxSystemImgDir = imagePath+'winXp/';
  
   t.useCheckboxSystem = true;
   t.checkboxSystemWalkTree = 3;
   t.lookAhead = 1;
	//add end
	
	//t.useRadioButton = true;
	//t.radioButtonName= 'rbcountry';
	t.useAutoSequence = false;
	t.autoCollapse = false;
  t.imageHeight = 16;
  t.divStyle  = 'font-size:13px; color:#000063;';
  t.linkStyle = 'color:blue;';
 
 	var status = t.initByArray(a);
	if (!status) {
    	var treeHtml = t.getLastError();
    	document.getElementById(divId).innerHTML = treeHtml;
  	} else {
    	t.drawInto(divId);
  	}
  	//myActionA(path, rootId, url, target);
  	myActionA(path, rootId, url, target);
  	getQueryString();
 	  
}

function setParentValue (sID, sDesc) {
	parent.document.all(ID).value = sID;
	parent.document.all(DESC).value = sDesc;
}

function getQueryString()
{
	var d="";
	var param;
	var queryStr = window.location.search.substr(1);
	var i, splitArray;
	queryStr=unescape(queryStr);
	queryStr=queryStr.replace("+"," ").replace("+"," ");
	if (queryStr.length != 0) {
		splitArray = queryStr.split("&");
		for (i=0; i<splitArray.length; i++) {
			var ind = splitArray[i].indexOf("=");
			param = splitArray[i].substring(0,ind+1) + "'";
			param = param +  splitArray[i].substring(ind+1) + "';";
			eval(param);
		}
	}
}

//*************************************************************
//
//
//*************************************************************
