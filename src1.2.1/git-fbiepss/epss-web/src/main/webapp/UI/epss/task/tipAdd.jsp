<%@ page import="skyline.platform.db.ConnectionManager" %>
<%@ page import="skyline.platform.db.DatabaseConnection" %>
<%@ page import="skyline.platform.db.RecordSet" %>
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
    response.setContentType("text/html; charset=GBK");
    String contextPath = request.getContextPath();
    int tkcttCount = 0;
    int cstplCount = 0;
    int subcttCount = 0;
    int subcttQCount = 0;
    int subcttMCount = 0;
    int tkcttQCount = 0;
    int tkcttMCount = 0;


    List tkcttList = new ArrayList();
    List cstplList = new ArrayList();
    List subcttList = new ArrayList();
    List subcttQList = new ArrayList();
    List subcttMList = new ArrayList();
    List tkcttQList = new ArrayList();
    List tkcttMList = new ArrayList();
    DatabaseConnection conn = ConnectionManager.getInstance().get();
    RecordSet rsCttInfo = conn.executeQuery("select t.tkctt_upd,t.cstpl_upd,t.subctt_upd,t.subctt_q_upd,t.subctt_m_upd,t.tkctt_qs_upd,t.tkctt_qm_upd from ctt_upd_info t ");

    while (rsCttInfo.next()) {
        tkcttList.add(rsCttInfo.getString("tkctt_upd"));
        cstplList.add(rsCttInfo.getString("cstpl_upd"));
        subcttList.add(rsCttInfo.getString("subctt_upd"));
        subcttQList.add(rsCttInfo.getString("subctt_q_upd"));
        subcttMList.add(rsCttInfo.getString("subctt_m_upd"));
        tkcttQList.add(rsCttInfo.getString("tkctt_qs_upd"));
        tkcttMList.add(rsCttInfo.getString("tkctt_qm_upd"));
    }
    ConnectionManager.getInstance().release();
    for (int i = 0; i < tkcttList.size(); i++) {
        tkcttCount += Integer.parseInt(tkcttList.get(i).toString());
    }
    for (int i = 0; i < cstplList.size(); i++) {
        cstplCount += Integer.parseInt(cstplList.get(i).toString());
    }
    for (int i = 0; i < subcttList.size(); i++) {
        subcttCount += Integer.parseInt(subcttList.get(i).toString());
    }
    for (int i = 0; i < subcttQList.size(); i++) {
        subcttQCount += Integer.parseInt(subcttQList.get(i).toString());
    }
    for (int i = 0; i < subcttMList.size(); i++) {
        subcttMCount += Integer.parseInt(subcttMList.get(i).toString());
    }
    for (int i = 0; i < tkcttQList.size(); i++) {
        tkcttQCount += Integer.parseInt(tkcttQList.get(i).toString());
    }
    for (int i = 0; i < tkcttMList.size(); i++) {
        tkcttMCount += Integer.parseInt(tkcttMList.get(i).toString());
    }
%>
<html>
<head>
<meta http-equiv="refresh" content="180">
<title></title>
<script>
function CLASS_MSN_MESSAGE(id, width, height, caption, title, message, target, action) {
    this.id = id;
    this.title = title;
    this.caption = caption;
    this.message = message;
    this.target = target;
    this.action = action;
    this.width = width ? width : 200;
    this.height = height ? height : 120;
    this.timeout = 150;
    this.speed = 20;
    this.step = 1;
    this.right = screen.width - 1;
    this.bottom = screen.height;
    this.left = this.right - this.width;
    this.top = this.bottom - this.height;
    this.timer = 0;
    this.pause = false;
    this.close = false;
    this.autoHide = true;
}

/*
 *    隐藏消息方法
 */
CLASS_MSN_MESSAGE.prototype.hide = function () {
    if (this.onunload()) {
        var offset = this.height > this.bottom - this.top ? this.height : this.bottom - this.top;
        var me = this;
        if (this.timer > 0) {
            window.clearInterval(me.timer);
        }
        var fun = function () {
            if (me.pause == false || me.close) {
                var x = me.left;
                var y = 0;
                var width = me.width;
                var height = 0;
                if (me.offset > 0) {
                    height = me.offset;
                }
                y = me.bottom - height;
                if (y >= me.bottom) {
                    window.clearInterval(me.timer);
                    me.Pop.hide();
                } else {
                    me.offset = me.offset - me.step;
                }
                me.Pop.show(x, y, width, height);
            }
        }
        this.timer = window.setInterval(fun, this.speed)
    }
}
/*
 *    消息卸载事件，可以重写
 */
CLASS_MSN_MESSAGE.prototype.onunload = function () {
    return true;
}
/*
 *    消息命令事件，要实现自己的连接，请重写它
 *
 */
CLASS_MSN_MESSAGE.prototype.oncommand = function () {
    //this.close = true;
    this.hide();
    window.open("/homePage.jsp");
}
CLASS_MSN_MESSAGE.prototype.show = function () {
    var oPopup = window.createPopup(); //IE5.5+
    this.Pop = oPopup;
    var w = this.width;
    var h = this.height;
    var str = "<DIV style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX: 99999; LEFT: 0px; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: " + w + "px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: " + h + "px; BACKGROUND-COLOR: #c9d3f3'>"
    str += "<TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0>"
    str += "<TR>"
    str += "<TD style='FONT-SIZE: 12px;COLOR: #0f2c8c' width=30 height=24></TD>"
    str += "<TD style='PADDING-LEFT: 4px; FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px' vAlign=center width='100%'>" + this.caption + "</TD>"
    str += "<TD style='PADDING-RIGHT: 2px; PADDING-TOP: 2px' vAlign=center align=right width=19>"
    str += "<SPAN title=关闭 style='FONT-WEIGHT: bold; FONT-SIZE: 12px; CURSOR: hand; COLOR: red; MARGIN-RIGHT: 4px' id='btSysClose' >×</SPAN></TD>"
    str += "</TR>"
    str += "<TR>"
    str += "<TD style='PADDING-RIGHT: 1px;PADDING-BOTTOM: 1px' colSpan=3 height=" + (h - 28) + ">"
    str += "<DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 8px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 8px; FONT-SIZE: 12px; PADDING-BOTTOM: 8px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 8px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%'>" + this.title + "</div><br/>";
    str += "<DIV style='WORD-BREAK: break-all' align=left><A href='javascript:void(0)' hidefocus=false id='btCommand'><FONT color=#ff0000>" + this.message + "</FONT></A><A href='http:' hidefocus=false id='ommand'><FONT color=#ff0000></FONT></A></DIV>"
    str += "</DIV>"
    str += "</TD>"
    str += "</TR>"
    str += "</TABLE>"
    str += "</DIV>"

    oPopup.document.body.innerHTML = str;
    this.offset = 0;
    var me = this;
    oPopup.document.body.onmouseover = function () {
        me.pause = true;
    }
    oPopup.document.body.onmouseout = function () {
        me.pause = false;
    }
    var fun = function () {
        var x = me.left;
        var y = 0;
        var width = me.width;
        var height = me.height;
        if (me.offset > me.height) {
            height = me.height;
        } else {
            height = me.offset;
        }
        y = me.bottom - me.offset;
        if (y <= me.top) {
            me.timeout--;
            if (me.timeout == 0) {
                window.clearInterval(me.timer);
                if (me.autoHide) {
                    me.hide();
                }
            }
        } else {
            me.offset = me.offset + me.step;
        }
        me.Pop.show(x, y, width, height);
    }
    this.timer = window.setInterval(fun, this.speed);
    var btClose = oPopup.document.getElementById("btSysClose");
    btClose.onclick = function () {
        me.close = true;
        me.hide();
    }
    var btCommand = oPopup.document.getElementById("btCommand");
    btCommand.onclick = function () {
        me.oncommand();
    }
    var ommand = oPopup.document.getElementById("ommand");
    ommand.onclick = function () {
        //this.close = true;
        me.hide();
        window.open(ommand.href);
    }
}
/**//*
 ** 设置速度方法
 **/
CLASS_MSN_MESSAGE.prototype.speed = function (s) {
    var t = 20;
    try {
        t = praseInt(s);
    } catch (e) {
    }
    this.speed = t;
}
/**//*
 ** 设置步长方法
 **/
CLASS_MSN_MESSAGE.prototype.step = function (s) {
    var t = 1;
    try {
        t = praseInt(s);
    } catch (e) {
    }
    this.step = t;
}
CLASS_MSN_MESSAGE.prototype.rect = function (left, right, top, bottom) {
    try {
        this.left = left != null ? left : this.right - this.width;
        this.right = right != null ? right : this.left + this.width;
        this.bottom = bottom != null ? (bottom > screen.height ? screen.height : bottom) : screen.height;
        this.top = top != null ? top : this.bottom - this.height;
    } catch (e) {
    }
}
function test() {
    var MSG1;
    switch (<%=tkcttCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一总包合同");
            break;
    }
    switch (<%=cstplCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一成本计划");
            break;
    }
    switch (<%=subcttCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一分包合同");
            break;
    }
    switch (<%=subcttQCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一分包数量结算");
            break;
    }
    switch (<%=subcttMCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一分包材料结算");
            break;
    }
    switch (<%=tkcttQCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一总包数量统计");
            break;
    }
    switch (<%=tkcttMCount%>) {
        case 1:
            MSG1 = new CLASS_MSN_MESSAGE("aa", 210, 126, "消息提示：", "新录入完成一总包数量计量");
            break;
    }
    MSG1.rect(null, null, null, screen.height - 50);
    MSG1.speed = 10;
    MSG1.step = 5;
    MSG1.show();
}
setTimeout(function () {
    test();
}, 200);
setInterval(function () {
    test();
}, 30000);
</script>
</head>
<body>

</body>
</html>