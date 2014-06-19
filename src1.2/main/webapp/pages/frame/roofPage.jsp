<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="platform.view.build.security.OperatorManager" %>
<%@ page import="platform.view.build.form.config.SystemAttributeNames" %>
<%@ page import="platform.view.build.db.ConnectionManager" %>
<%@ page import="platform.view.build.db.DatabaseConnection" %>
<%@ page import="platform.view.build.db.RecordSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String contextPath = request.getContextPath();

    if (om == null) {
        om = new OperatorManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
    }

    String username = "";
    String deptname = "";
    String operid = "";

    String rolesall = null;

    if (om != null) {
        if (om.getOperator() != null) {
            username = om.getOperator().getOpername();
            operid = om.getOperator().getOperid();
            if (om.getOperator().getPtDeptBean() != null)
                deptname = om.getOperator().getPtDeptBean().getDeptname();

            //��ɫ
            List roles = new ArrayList();
            DatabaseConnection conn = ConnectionManager.getInstance().get();
            RecordSet rs = conn.executeQuery("select a.roledesc from ptoperrole b right join ptrole a on b.roleid = a.roleid  where b.operid='" + operid + "'");
            while (rs.next()) {
                roles.add(rs.getString("roledesc"));
            }
            ConnectionManager.getInstance().release();
            rolesall = " ";
            for (int i = 0; i < roles.size(); i++) {
                rolesall += roles.get(i) + " ";
            }

        }

    }
%>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=gb2312">
    <LINK href="<%=contextPath%>/css/ccb.css" type="text/css" rel="stylesheet">
    <script type="text/javascript">

        function Relogin() {
            parent.window.reload = "true";
            parent.window.location.replace("<%=contextPath%>/pages/security/logout.jsp");
        }
        function changepwd() {
            var sfeature = "dialogwidth:400px; dialogheight:200px;center:yes;help:no;resizable:no;scroll:no;status:no";
            window.showModalDialog("<%=contextPath%>/UI/system/deptUser/Passwordedit.jsp", "test", sfeature);
        }
       /* function goFirst() {
            parent.window.workFrame.location.replace("<%=contextPath%>/pages/frame/trackMisc.xhtml");
        }*/
        function goFirst() {
            parent.window.workFrame.location.replace("<%=contextPath%>/UI/epss/task/task.xhtml");
        }
    </script>

    <style type="text/css">
        body {
            background-color: #FFF;
        }

        div#nifty1 {
            margin: 0 1px; /*background: #9BD1FA;*/
            background: #7387A0;
        }

        div#nifty2 {
            margin: 0 1px;
            background: #7387A0;
        }

        div#nifty3 {
            margin: 0 1px;
            background: #7387A0;
        }

        div#nifty4 {
            margin: 0 1px;
            background: #7387A0;
        }

        div#nifty5 {
            margin: 0 1px;
            background: #7387A0;
        }

        b.rtop, b.rbottom {
            display: block;
            background: #FFF
        }

        b.rtop b, b.rbottom b {
            display: block;
            height: 1px;
            overflow: hidden;
            background: #7387A0
        }

        b.r1 {
            margin: 0 5px
        }

        b.r2 {
            margin: 0 3px
        }

        b.r3 {
            margin: 0 2px
        }

        b.rtop b.r4, b.rbottom b.r4 {
            margin: 0 1px;
            height: 2px
        }
    </style>


</head>
<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" onunload="Relogin()" >
<input id="hhidOperatorID" type="hidden" value="<%=operid%>">
<table width="100%" cellpadding="0" cellspacing="0">
    <tr width="100%" height="50px" style="margin-bottom: 0px;margin-top:0px;margin:0px;padding:0px">
        <td width="50%" style="height:50" colspan="4">
            <img  src="../../images/epss.jpg" width="110px" height="50px" style="margin-left:40px; " >
            <img  src="../../images/epss_title.png" width="310px" height="50px" style="margin-left:5px; " >
        </td>
        <td width="50%" style="height:50;text-align:right" colspan="1">
            <font color="#5F6A78"> <span>����,<%=username%>! </span> </font>
            <font color="#5F6A78"> <span onclick="changepwd()"
                                         onMouseOver="this.style.cursor='hand'">|&nbsp;&nbsp;�޸�����</span> </font>
            <font color="#5F6A78"> <span onclick="goFirst() "
                                         onMouseOver="this.style.cursor='hand'">|&nbsp;&nbsp;����ҳ</span> </font>
            <font color="#5F6A78"> <span onclick="Relogin()"
                                         onMouseOver="this.style.cursor='hand'">|  �˳� &nbsp;&nbsp;</span> </font>
        </td>
    </tr>


    <tr>
        <td colspan="5">
            <div id="nifty1">
                <b class="rtop">
                    <b class="r1"></b>
                    <b class="r2"></b>
                    <b class="r3"></b>
                    <b class="r4"></b>
                </b>

                <div style="height:15px;text-align:right">
                    <font color="#ECEFF2">
                        <%=" " + deptname + " | " + operid + " | <" + rolesall + ">" %>   &nbsp;&nbsp;&nbsp;
                    </font>
                </div>
                <b class="rbottom">
                    <b class="r4"></b>
                    <b class="r3"></b>
                    <b class="r2"></b>
                    <b class="r1"></b>
                </b>
            </div>
        </td>
    </tr>
</table>


</body>
</html>