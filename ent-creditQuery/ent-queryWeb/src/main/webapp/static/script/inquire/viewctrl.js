/*
0 没按键 1 按左键 
2 按右键 3 按左和右键 
4 按中间键 5 按左和中间键 
6 按右和中间键 7 按所有的键 
*/
window.status = "" ;
if (window.Event)
    document.captureEvents(Event.MOUSEUP);
function nocontextmenu()
{
    event.cancelBubble = true ;
    event.returnValue = false;

    return false;
}
function norightclick(e)
{
    if (window.Event)
    {
        if (e.which == 2 || e.which == 3)
            return false;
    }
    else
        if (event.button == 2 || event.button == 3)
        {
            event.cancelBubble = true
            event.returnValue = false;
            return false;
        }

}
document.oncontextmenu = nocontextmenu;
document.onmousedown = norightclick;

function noCtrlAltShift(){
	if(window.event.ctrlKey || window.event.altKey || window.event.shiftKey)
	{
		return false;
	}
}
document.onkeydown=noCtrlAltShift;

