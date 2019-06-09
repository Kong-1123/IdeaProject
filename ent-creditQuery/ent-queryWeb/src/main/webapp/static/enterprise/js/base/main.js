
//---------------------------------------------------------------//
// ROLL(翻页)工作区
//
//---------------------------------------------------------------//
var _ROLLWS = new _ROLLWS();
function _ROLLWS() {
	this._WhereClause = 0;//查询条件
	this._CurPage = 0;//当前页，缺省值为0，即没有页
	this._PageCount = 0;//总页数
	this._PageSize = 5;//每页记录数，缺省值为5
	this._TotalCount = 0;//总记录数
}


//借款人属性
var ATTRIBUTE = null;