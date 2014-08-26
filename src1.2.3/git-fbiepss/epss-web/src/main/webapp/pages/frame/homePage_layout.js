var sysdhxLayout;
var sysdhxAccord;
var systabbar;

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

var layoutary = new Array('todotasklayout', 'donetasklayout', 'syslayout', 'helplayout');
var tabbarary = new Array('todotask', 'donetask', 'sys', 'help');

function changepwd() {
    var sfeature = "dialogwidth:400px; dialogheight:200px;center:yes;help:no;resizable:no;scroll:no;status:no";
    window.showModalDialog(contextPath + "/UI/system/deptUser/passwordEdit.jsp", "test", sfeature);
}

