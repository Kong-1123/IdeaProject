
//????????
var _SelectListener = new Array();
//???????
var _SelectedNode = null;
//????????
var _hasShown = false;

var _imagePath = "/images/tree/";
//????
//  key       ?????key
//  label     ????
//  icon      ???????
function addTreeRoot(key,label,attribute,icon) {
	var _root = _getRoot();
	if (_root!=null) {
		alert("???????");
	}
	_root = document.all("TVTable").rows[0].cells[0];
	_root.insertAdjacentHTML("beforeEnd","<SPAN id='_tv_key_"+key+"' _label='"+label+"' _attribute='"+attribute+"' _icon='"+icon+"' style='display:none'>\n</span>\n");
}

var _tempNodeArray = new Array();

//????? 
//  parentKey ????key
//  key       ?????key
//  label     ????
//  icon      ???????
//  repeatTag ??????,??????????
function addTreeNode(parentKey, key, label, attribute, icon, repeatTag) {
	//??????
	if(parentKey==key){
		return;
	}
	if (_getNode(key)!=null) {	
		//alert("???????????key="+key+"?????");
		//return;
	}
	if (parentKey=="") {
		addTreeRoot(key,label,icon);
	} else {
		var _parent = _getNode(parentKey);
		if (_parent==null) {
		  //????????????????????????
		  if(!repeatTag) {
  		  _tempNodeArray[_tempNodeArray.length++] = new Array(parentKey, key, label, attribute, icon);
  		}
			//alert("???????key="+parentKey+"?????");
			return;
		} else {
		  //if(!repeatTag) {
  		//  document.write(label);
  		//}
			_parent.insertAdjacentHTML("beforeEnd","<SPAN id='_tv_key_"+key+"' _label='"+label+"'_attribute='"+attribute+"' _icon='"+icon+"' style='display:none'>\n</span>\n");
		}
	}
	if (_hasShown==true) {
		var _node = _getNode(key);
		var _parent = _getNode(parentKey);
		var _previous = _getPrevious(_node);
		if (_previous!=null) {
			_repaintNode(_previous);
			
		} else if (_parent!=null) {
			_repaintNode(_parent);
		}
		_expandNode(_parent);
		SelectNode(key,attribute);
	}
	
	return true;
}

function _equals(firstNode,secondNode) {
	if (_getKey(firstNode)==_getKey(secondNode)) {
		return true;
	}
	return false;
}
function delTreeNode(key) {
	var _needSelect = false;
	var _node = _getNode(key);
	if (_SelectedNode!=null) {
		if (_equals(_SelectedNode,_node)) {
			_needSelect = true;
			_SelectedNode=null;
		}
	}
	if (_node==null) {
		alert("??????????key1="+key+"??");
		return;
	}
	var _parent = _getParent(_node);
	var _previous  = _getPrevious(_node);
	var _next = _getNext(_node);
	_node.outerHTML="";
	if (_hasShown==true) {
		if (_next!=null) {
			_repaintNode(_next);
			if (_needSelect==true) {
				SelectNode(_getKey(_next),_next.attribute);
			}
		} else if (_previous!=null) {
			_repaintNode(_previous);
			if (_needSelect==true) {
				SelectNode(_getKey(_previous),_previous.attribute);
			}
		} else if (_parent!=null) {
			_repaintNode(_parent);
			if (_needSelect==true) {
				SelectNode(_getKey(_parent),_parent.attribute) ;
			}
		}
	}
}

function _displayNode(node) {
	node.style.display="inline";
	_paintNode(node);
}
function _paintNode(node) {
	var _key = _getKey(node);
	if (_getDisplay(node)!=null) {
		return;
	}
	var txt = "";
	var _parent = _getParent(node);
	//??????????box??????
	var parentISRoot = false;
	while (_parent!=null) {
		var _pp = _getParent(_parent);
		if (_pp!=null) {
			if (_hasNext(_parent)==true) {
				txt = "<TD width='19'><img src='"+_imagePath+"4_none.gif' align='absMiddle' width='19' height='19'/></TD>\n"+txt;
			} else {
				txt = "<TD width='19'><img src='"+_imagePath+"4_clos.gif' align='absMiddle' width='19' height='19'/></TD>\n"+txt;
			}
		}
		_parent = _pp;
	}
	//?????
	if (_getParent(node)!=null) {
		var _children = _getChildren(node);
		if (_children.length!=0) {
			if (_hasNext(node)==true){
				if (_children[0].style.display!="none") {
					txt += "<TD width='19' onclick=\"_onNodeExpand(_getNode('"+_key+"'))\"><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"2_open.gif' align='absMiddle' width='19' height='19'/></TD>\n"
				} else {
					txt += "<TD width='19' onclick=\"_onNodeExpand(_getNode('"+_key+"'))\"><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"2_clos.gif' align='absMiddle' width='19' height='19'/></TD>\n"
				}
			} else {
				if (_children[0].style.display!="none") {
					txt += "<TD width='19' onclick=\"_onNodeExpand(_getNode('"+_key+"'))\"><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"3_open.gif' align='absMiddle' width='19' height='19'/></TD>\n"
				} else {
					txt += "<TD width='19' onclick=\"_onNodeExpand(_getNode('"+_key+"'))\"><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"3_clos.gif' align='absMiddle' width='19' height='19'/></TD>\n"
				}
			}
		} else {
			if (_hasNext(node)==true) {
				txt += "<TD width='19'><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"2_none.gif' align='absMiddle' width='19' height='19'/></TD>\n"
			} else {
				txt += "<TD width='19'><img id='_tv_box_img_"+_key+"' src='"+_imagePath+"3_none.gif' align='absMiddle' width='19' height='19'/></TD>\n"
			}
		}
	}
	//??
	if (node._icon!=null && node._icon!="") {
	    
		txt += "<TD width='19' ondblclick=\"window.event.cancelBubble=true;_onNodeExpand(_getNode('"+_key+"'))\" onclick=\"SelectNode('"+_key+"','"+node._attribute+"')\"><img src='"+node._icon+"' align='absMiddle' width='19' height='19'/></TD>\n"
	} else {
		txt += "<TD width='19' ondblclick=\"window.event.cancelBubble=true;_onNodeExpand(_getNode('"+_key+"'))\" onclick=\"SelectNode('"+_key+"','"+node._attribute+"'))\"><img src='"+_imagePath+"0001.gif' align='absMiddle' width='19' height='19'/></TD>\n"
	}
	//??
	txt += "<TD id='_tv_label_"+_key+"' style='CURSOR: hand; padding-left: 4; padding-right: 4' ondblclick=\"window.event.cancelBubble=true;_onNodeExpand(_getNode('"+_key+"'))\" onclick=\"SelectNode('"+_key+"','"+node._attribute+"')\" title='"+node._label+"' noWrap>"+node._label+"</TD>\n"
	txt = "<table cellspacing='0' cellpadding='0' border='0' style='font-size:9pt'>\n<TR>\n"+txt+"</TR>\n</table>\n";
	node.insertAdjacentHTML("afterBegin",txt);
}
//?????????
function _contractNode(node){
	var boximg = _getBoxImg(node);
	var _children = _getChildren(node);
	if (_children.length==0) {
		return;
	}
	for (var i=0;i<_children.length;i++) {
		_children[i].style.display="none";
	}
	if (_getParent(node)!=null) {
		if (_hasNext(node)==true){
			boximg.src=_imagePath+"2_clos.gif";
		} else {
			boximg.src=_imagePath+"3_clos.gif";
		}
	}
}
//???
function showTree(projectPath) {
   //???????????
  _imagePath = projectPath+_imagePath
  
	//??????????
	_dealTempNodeArray();

	_displayNode(_getRoot());
	_hasShown = true;
	_expandNode(_getRoot());
	
	for(var i = 0; i < _tempNodeArray.length; i++) {
	  if(_tempNodeArray[i][0] != null) {
	    //alert("???????key2="+_tempNodeArray[i][0]+"?????");
	  }
	}
}

//????????
var _dealTempNodeTime = 0;//??????????
var _dealTag = true;//??????,???????
function _dealTempNodeArray() {

  //parentKey, key, label, icon
  for(var i = 0; i < _tempNodeArray.length; i++) {
    var _curNode = _tempNodeArray[i];
    if(_curNode[0] != null) {
      
      //??????
      var _isDeal = addTreeNode(_curNode[0], _curNode[1], _curNode[2], _curNode[3], _curNode[4], true);
      if(_isDeal) {
        _curNode[0] = null;//????
      }
      _dealTag = false;
    }
  }

  if(_dealTempNodeTime < 5 && _dealTag == false) {
    _dealTag = true;//??????
    _dealTempNodeTime++;//??????
    _dealTempNodeArray();
  }

}

//?????????
function _repaintNode(node) {
	var _display = _getDisplay(node);
	if (_display!=null) {
		_display.outerHTML="";
		_paintNode(node);
	}
	var _children = _getChildren(node);
	for (var i=0;i<_children.length;i++) {
		_repaintNode(_children[i]);
	}
}
//???????
function _expandNode(node) {
	var _parent = _getParent(node);
	if (_parent!=null) {
		if (_isExpanded(_parent)==false) {
			_expandNode(_parent);
		}
	}
	var boximg = _getBoxImg(node);
	var _children=_getChildren(node);
	if (_children.length==0) {
		return;
	}
	for (var i=0;i<_children.length;i++) {
		_displayNode(_children[i]);
	}
	if (_getParent(node)!=null) {
		if (_hasNext(node)==true){
			boximg.src=_imagePath+"2_open.gif";
		} else {
			boximg.src=_imagePath+"3_open.gif";
		}
	}
}
//?????
function _getRoot() {
	var _root = document.all("TVTable").rows[0].cells[0];
	if (_root.children.length>0)
		if ( _root.children[0].tagName.toLowerCase()=="span") {
		return _root.children[0];
	} else {
		alert("???????");
		return null;
	}
}
//??????????????????
function _getNode(key) {
	if (key == "") {
		return _getRoot();
	}
	return document.all("_tv_key_"+key,0);
}
//???????
function _getKey(node) {
	return node.id.substring(8,node.id.length);
}
function _getBoxImg(node) {
	return document.all("_tv_box_img_"+_getKey(node));
}
//???????????????????
function _getChildren(node) {
	//?????????????tagName=span???
	var _children = new Array();
	for (var i=0;i<node.children.length;i++) {
		if (node.children[i].tagName.toLowerCase()=="span") {
			_children[_children.length]=node.children[i];
		}
	}
	return _children;
}
//?????
function _getParent(node) {
	//???????tagName=span???
	if (node.parentNode==null) {
		return null;
	} else if (node.parentNode.id=="TVTable") {
		return null;
	} else if (node.parentNode.tagName.toLowerCase()=="span") {
		return node.parentNode;
	} else {
		return _getParent(node.parentNode);
	}
}
//?????????
function _getNext(node) {
	//???????tagName=span???
	if (node.nextSibling==null) {
		return null;
	} else if (node.nextSibling.tagName.toLowerCase()=="span") {
		return node.nextSibling;
	} else {
		return _getNext(node.nextSibling);
	}
}
//?????????
function _getPrevious(node) {
	//???????tagName=span???
	if (node.previousSibling==null) {
		return null;
	} else if (node.previousSibling.tagName.toLowerCase()=="span") {
		return node.previousSibling;
	} else {
		return _getPrevious(node.previousSibling);
	}
}
//???????HTML????table???????????????????????
function _getDisplay(node) {
	if (node.children.length!=0) {
		if (node.children[0].tagName.toLowerCase()=="table") {
			return node.children[0];
		} 
	}
	return null;
}
//??????
function _hasChild(node) {
	if (_getChildren(node).length==0) {
		return null;
	}
	return true;
}
//??????
function _hasParent(node){
	if (_getParent(node)==null) {
		return false;
	}
	return true;
}
//???????????
function _hasNext(node) {
	if (_getNext(node)==null) {
		return false;
	}
	return true;
}
//??????????
function _hasPrevious(node) {
	if (_getPrevious(node)==null) {
		return false;
	}
	return true;
}
//?????????????????
function _isExpanded(node) {
	//???????????????
	var _children = _getChildren(node);
	if (_children.length!=0) {
		return _children[0].style.display!="none";
	} else {
		return false;
	}
}
//????????????
function _onNodeExpand(node) {
	if (_isExpanded(node)==true) {
		_contractNode(node);
	} else {
		_expandNode(node);
	}
}

//??????????????
//function _onNodeSelect(node) {
//	SelectNode(_getKey(node));
//}
//??????
function SelectNode(key,attribute) {
  var _node = _getNode(key);
  if (_SelectedNode==null) {
      if (_node==null) {
          return;
      }
  } else {
    if (_equals(_SelectedNode,_node)==true) {
        return ;
    }
  }
	_SelectNode(key);
	for (var i=0;i<_SelectListener.length;i++) {
		eval(_SelectListener[i]+"(key,attribute)");
	}
}
function _SelectNode(key) {
	if (_SelectedNode!=null) {
		var _old = document.all("_tv_label_"+_getKey(_SelectedNode),0);
		_old.style.cssText="CURSOR: hand; padding-left: 4; padding-right: 4; font-size:9pt";
	}
	var _node = _getNode(key);
	var _parent = _getParent(_node);
	if (_parent!=null) {
	    _expandNode(_parent);
	}
	_SelectedNode = _node;
	var _new = document.all("_tv_label_"+key,0);
	if (_new!=null) {
  	_new.style.cssText="CURSOR: hand; padding-left: 4; padding-right: 4; color: #FFFFFF; border-style: dotted; border-width: 1; background-color: #000080;font-size:9pt";
  }
}

//???????????????????????
function addSelectListener(listener) {
	for (var i=0;i<_SelectListener.length;i++) {
		if (_SelectListener[i]==listener) {
			return;
		}
	}
	_SelectListener[_SelectListener.length] = listener;
}
function updateTreeNode(oldKey, newKey, newLabel) {
	var _updateSelected = false;
	var _node = _getNode(oldKey);
	if (_equals(_SelectedNode,_node)==true) {
		_updateSelected = true;
	}
	var _display = _getDisplay(_node);
	_display.outerHTML="";
	_node.id="_tv_key_"+newKey;
	_node._label=newLabel;
	_paintNode(_node);
	if (_updateSelected) {
		_SelectNode(_getKey(_SelectedNode));
	}
}

