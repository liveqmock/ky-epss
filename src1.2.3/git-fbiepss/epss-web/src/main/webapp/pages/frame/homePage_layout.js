var bizdhxLayout;
var bizdhxAccord;
var biztabbar;
function doBizLoad() {
    bizdhxLayout.cells("a").setWidth(200);
    bizdhxLayout.cells("a").hideHeader();
    bizdhxLayout.cells("b").hideHeader();
    bizdhxLayout.setAutoSize("a;b", "a;b");
    bizdhxAccord = bizdhxLayout.cells("a").attachAccordion();
    biztabbar = bizdhxLayout.cells("b").attachTabbar();
    biztabbar.setSkin("dhx_skyblue");
    biztabbar.setImagePath(contextPath + "/dhtmlx/dhtmlxTabbar/codebase/imgs/");
    biztabbar.setHrefMode("iframes-on-demand");
    biztabbar.setSkinColors("#FCFBFC", "#F4F3EE");
    biztabbar.enableTabCloseButton(true);
    biztabbar.addTab("a1", "首页", "100px");
    biztabbar.setContentHref("a1", "trackMisc.xhtml");
    biztabbar.setTabActive("a1");
    biztabbar.attachEvent("onSelect", function (id, last_id) {
        biztree.selectItem(id);
        if (id != last_id)
            document.getElementById("lasttabdivid").value = last_id;
        return true;
    });
    biztabbar.attachEvent("onTabClose", function () {
        biztabbar.setTabActive(document.getElementById("lasttabdivid").value);
        return true;
    });
    bizdhxAccord.setSkin("dhx_skyblue");
    bizdhxAccord.setIconsPath(contextPath + "/dhtmlx/dhtmlxAccordion/codebase/icons/");
    bizdhxAccord.addItem("a1", "业务功能");
    bizdhxAccord.openItem("a1");
    bizdhxAccord._enableOpenEffect = true;
    bizdhxAccord.cells("a1").setIcon("accord_biz.png");

    var biztree = bizdhxAccord.cells("a1").attachTree();
    var treeDefaultJson = eval('(' + defaultMenuStr + ')');
    biztree.setSkin('dhx_skyblue');
    biztree.setImagePath(contextPath + "/dhtmlx/dhtmlxTree/codebase/imgs/csh_books/");
    biztree.loadJSONObject(treeDefaultJson);
    biztree.attachEvent("onClick",
        function (id) {
            var action = (biztree.getUserData(id, "url"));
            if (action == "#") {
                biztree.openItem(id);
            } else {
                var text = biztree.getSelectedItemText();
                bizaddtabbar(id, text, contextPath + action);
            }
            return true;
        });
}
function bizaddtabbar(divID, tabname, url) {
    var tabbarCell = biztabbar.cells(divID);
    if (tabbarCell == undefined) {
        biztabbar.addTab(divID, tabname, "*");
        biztabbar.setContentHref(divID, url);
        biztabbar.setTabActive(divID);
    } else {
        biztabbar.setTabActive(divID);
        biztabbar.forceLoad(divID, url);
    }
}
var layoutary = new Array('todotasklayout', 'donetasklayout', 'bizlayout', 'helplayout');
var tabbarary = new Array('todotask', 'donetask', 'biz', 'help');

function changepwd() {
    var sfeature = "dialogwidth:400px; dialogheight:200px;center:yes;help:no;resizable:no;scroll:no;status:no";
    window.showModalDialog(contextPath + "/UI/epss/deptOper/passwordEdit.jsp", "test", sfeature);
}

