var dhxLayout;
var bizdhxLayout;
var bizdhxAccord;
var biztabbar;
var sysdhxLayout;
var sysdhxAccord;
var systabbar;

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
    bizdhxAccord.addItem("a2", "常用功能");
    bizdhxAccord.addItem("a3", "待办事项");
    bizdhxAccord.addItem("a4", "实时消息");

    bizdhxAccord.openItem("a1");
    bizdhxAccord._enableOpenEffect = true;
    bizdhxAccord.cells("a1").setIcon("accord_biz.png");
    bizdhxAccord.cells("a2").setIcon("accord_manage.png");
    bizdhxAccord.cells("a3").setIcon("editor.gif");
    bizdhxAccord.cells("a4").setIcon("accord_support.png");

    var biztree = bizdhxAccord.cells("a1").attachTree();
    var treeDefaultJson = eval('(' + defaultMenuStr + ')');
    biztree.setSkin('dhx_skyblue');
    biztree.setImagePath(contextPath + "/dhtmlx/dhtmlxTree/codebase/imgs/csh_books/");
    //biztree.enableDragAndDrop(true);
    biztree.loadJSONObject(treeDefaultJson);
    biztree.attachEvent("onClick", function (id) {
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
function doSysLoad() {
    sysdhxLayout.cells("a").setWidth(200);
    sysdhxLayout.cells("a").hideHeader();
    sysdhxLayout.cells("b").hideHeader();
    sysdhxAccord = sysdhxLayout.cells("a").attachAccordion();
    systabbar = sysdhxLayout.cells("b").attachTabbar();

    systabbar.setSkin("dhx_skyblue");
    systabbar.setImagePath(contextPath + "/dhtmlx/dhtmlxTabbar/codebase/imgs/");
    systabbar.setHrefMode("iframes-on-demand");
    systabbar.setSkinColors("#FCFBFC", "#F4F3EE");
    systabbar.enableTabCloseButton(true);
    systabbar.addTab("a1", "首页", "100px");
    systabbar.setContentHref("a1", "trackMisc.xhtml");
    systabbar.setTabActive("a1");
    systabbar.attachEvent("onSelect", function (id, last_id) {
        managetree.selectItem(id);
        return true;
    });
    sysdhxAccord.setSkin("dhx_skyblue");
    sysdhxAccord.setIconsPath(contextPath + "/dhtmlx/dhtmlxAccordion/codebase/icons/");
    sysdhxAccord.addItem("a1", "系统管理");
    sysdhxAccord.openItem("a1");
    sysdhxAccord._enableOpenEffect = true;
    sysdhxAccord.cells("a1").setIcon("accord_manage.png");
    var managetree = sysdhxAccord.cells("a1").attachTree();
    var treeSystemJson = eval('(' + systemMenuStr + ')');
    managetree.setSkin('dhx_skyblue');
    managetree.setImagePath(contextPath + "/dhtmlx/dhtmlxTree/codebase/imgs/csh_books/");
    managetree.loadJSONObject(treeSystemJson);
    managetree.attachEvent("onClick", function (id) {
        var action = (managetree.getUserData(id, "url"));
        if (action == "#") {
            managetree.openItem(id);
        } else {
            var text = managetree.getSelectedItemText();
            sysaddtabbar(id, text, contextPath + action);
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
function sysaddtabbar(divID, tabname, url) {
    var tabbarCell = systabbar.cells(divID);
    if (tabbarCell == undefined) {
        systabbar.addTab(divID, tabname, "*");
        systabbar.setContentHref(divID, url);
        systabbar.setTabActive(divID);
    } else {
        systabbar.setTabActive(divID);
        systabbar.forceLoad(divID, url);
    }
}

var layoutary = new Array('bizlayout', 'syslayout', 'helplayout', 'verlayout');
var tabbarary = new Array('biz', 'sys', 'help', 'ver');

function changepwd() {
    var sfeature = "dialogwidth:400px; dialogheight:200px;center:yes;help:no;resizable:no;scroll:no;status:no";
    window.showModalDialog(contextPath + "/UI/system/deptUser/Passwordedit.jsp", "test", sfeature);
}

function doOnResize() {
    bizdhxLayout.setSizes();
    sysdhxLayout.setSizes();
}
