function tabbarhide(tabbarid) {
    for (var i = 0; i < layoutary.length; i++) {
        if (layoutary[i] != tabbarid) {
            document.getElementById(layoutary[i]).style.display = "none";
        }
    }
}
function tabbarclk(obj) {
    var active = obj.getAttribute("active");
    if (active == 'false') {
        var tabbarid = obj.getAttribute("id") + "layout";
        setclass(obj.getAttribute("id"));
        obj.setAttribute("active", "true");
        obj.className = "tabs-item-active";
        document.getElementById(tabbarid).style.display = "inline";
        tabbarhide(tabbarid);
    }
}
function setclass(activeid) {
    for (var i = 0; i < tabbarary.length; i++) {
        if (tabbarary[i] != activeid) {
            document.getElementById(tabbarary[i]).className = "tabs-item";
            document.getElementById(tabbarary[i]).setAttribute("active", "false");
        }
    }
}
