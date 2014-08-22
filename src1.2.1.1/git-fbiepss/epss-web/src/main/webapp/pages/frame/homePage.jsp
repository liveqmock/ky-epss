<%@ page import="skyline.platform.db.ConnectionManager" %>
<%@ page import="skyline.platform.db.DatabaseConnection" %>
<%@ page import="skyline.platform.db.RecordSet" %>
<%@ page contentType="text/html; charset=GBK" %>
<%@ include file="/pages/security/loginassistor.jsp" %>
<%
    response.setContentType("text/html; charset=GBK");
    String contextPath = request.getContextPath();

    /*2011-4-7 Cookie�������ӵ�loginassistor.jsp��*/
    String jsonDefaultMenu = null;
    String jsonSystemMenu = null;
    try {
        jsonDefaultMenu = om.getJsonString("default");
        jsonSystemMenu = om.getJsonString("system");
    } catch (Exception e) {
        System.out.println("jsp" + e + "\n");
    }
    String deptname = "";
    String operid = "";

    String rolesall = null;

    if (om != null) {
        if (om.getOperator() != null) {
            username = om.getOperatorName();
            operid = om.getOperator().getOperid();
            if (om.getOperator().getPtDeptBean() != null){
                deptname = om.getOperator().getPtDeptBean().getDeptname();
            }
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
<%--<!DOCTYPE html>--%>
<html>
    <head>
        <title>������Ŀ�������ϵͳ</title>
        <meta http-equiv="X-UA-Compatible" content="IE=5" />

        <script src="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxcommon.js" type="text/javascript"></script>
        <script src="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxcontainer.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxLayout/codebase/dhtmlxlayout.css"/>
        <link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxLayout/codebase/skins/dhtmlxlayout_dhx_skyblue.css"/>
        <script src="../../dhtmlx/dhtmlxLayout/codebase/dhtmlxlayout.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxAccordion/codebase/skins/dhtmlxaccordion_dhx_skyblue.css"/>
        <script src="../../dhtmlx/dhtmlxAccordion/codebase/dhtmlxaccordion.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxTree/codebase/dhtmlxtree.css"/>
        <script src="../../dhtmlx/dhtmlxTree/codebase/dhtmlxtree.js" type="text/javascript"></script>
        <script src="../../dhtmlx/dhtmlxTree/codebase/ext/dhtmlxtree_json.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.css"/>
        <script src="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.js" type="text/javascript"></script>
        <script type="text/javascript" src="homePage_layout.js"></script>
        <script type="text/javascript" src="homePage_tab.js"></script>
        <script type="text/javascript" src="myAjax.js"></script>
        <LINK href="<%=contextPath%>/css/diytabbar.css" type="text/css" rel="stylesheet">
        <style type="text/css">
            html, body {
                margin: 0px;
                width: 100%;
                height: 100%;
                padding: 0px;
                overflow: hidden;
            }

                .divlayout {
                    position: relative;
                    top: 0px;
                    left: 0px;
                    width: 100%;
                    height: 100%;
                    margin: 0px;
                    padding: 0px;
                    overflow: hidden;
                }

                .headfont {
                    font-size: 12px;
                    font-family: SimSun;
                    color: #7387A0;
                }

                .skin-top-right {
                    background-position: top right;
                    background-repeat: no-repeat;
                    background-image: url(../../images/top_right.jpg)
                }
            #tip {
                position: absolute;
                right: 0px;
                bottom: 0px;
                height: 0px;
                width: 180px;
                border: 1px solid #CCCCCC;
                background-color: #eeeeee;
                padding: 1px;
                overflow: hidden;
                display: none;
                font-size: 12px;
                z-index: 10;
            }
            #tip p {
                padding: 6px;
            }
            #tip h1, #detail h1 {
                font-size: 14px;
                height: 25px;
                line-height: 25px;
                background-color: #0066CC;
                color: #FFFFFF;
                padding: 0px 3px 0px 3px;
                filter: Alpha(Opacity=100);
            }
            #tip h1 a, #detail h1 a {
                float: right;
                text-decoration: none;
                color: #FFFFFF;
            }
            #shadow {
                position: absolute;
                width: 100%;
                height: 100%;
                background-color: #000000;
                z-index: 11;
                filter: Alpha(Opacity=70);
                display: none;
                overflow: hidden;
            }
            #detail {
                width: 500px;
                height: 200px;
                border: 3px double #ccc;
                background-color: #FFFFFF;
                position: absolute;
                z-index: 30;
                display: none;
                left: 30%;
                top: 30%
            }
            </style>
        <script type="text/javascript">
            var contextPath = '<%=contextPath%>';
            var defaultMenuStr = '<%=jsonDefaultMenu%>';
            var systemMenuStr = '<%=jsonSystemMenu%>';
            var handle;
            function start() {
                var obj = document.getElementById("tip");
                if (parseInt(obj.style.height) == 0) {
                    obj.style.display = "block";
                    handle = setInterval("changeH('up')", 2);
                } else {
                    handle = setInterval("changeH('down')", 2)
                }
            }
            function changeH(str) {
                var obj = document.getElementById("tip");
                if (str == "up") {
                    if (parseInt(obj.style.height) > 150)
                        clearInterval(handle);
                    else
                        obj.style.height = (parseInt(obj.style.height) + 8).toString() + "px";
                }
                if (str == "down") {
                    if (parseInt(obj.style.height) < 8) {
                        clearInterval(handle);
                        obj.style.display = "none";
                    }else{
                        obj.style.height = (parseInt(obj.style.height) - 8).toString() + "px";
                    }
                }
            }
            function myTimer() {
                start();
                window.setTimeout("myTimer()", 5000);//����ѭ��ʱ��
            }
            //TODO (add by yxy,2014-08-17,*start)
        var currentDefaultMenuStr;
        var lastDefaultMenuStr = '<%=jsonDefaultMenu%>';
        function myRequest() {
            var url = "ajaxRequest.do?time=" + Math.random();
            //Ҫ�ύ��������������
            var content = "userName=ni";
            //�����쳣�����ύ�ĺ���
            sendRequest("POST", url, content, "TEXT", myProcessResponse);
        }
        function myProcessResponse() {
            // ���������
            if (http_request.readyState == 4) {
                // ��Ϣ�Ѿ��ɹ����أ���ʼ������Ϣ
                if (http_request.status == 200) {
                    //���ص����ı���ʽ��Ϣ
                    if(http_request.responseText==""){
                        alert("hdahhd");
                    }else{
                        currentDefaultMenuStr = http_request.responseText;
                        if (lastDefaultMenuStr != currentDefaultMenuStr) {
                            myDoBizLoad(currentDefaultMenuStr);
                            lastDefaultMenuStr = currentDefaultMenuStr;
                        }
                    }

                } else { //ҳ�治����
                    //"���������ҳ�����쳣"
                }
            }
        }
        function run() {
            myRequest();
            window.setTimeout("run()", 3000);
        }
        //TODO (add by yxy,2014-08-17,*end)

        function doOnLoad() {
            bizdhxLayout = new dhtmlXLayoutObject("bizlayout", "2U", "dhx_skyblue");
            doBizLoad();
            sysdhxLayout = new dhtmlXLayoutObject("syslayout", "2U", "dhx_skyblue");
            doSysLoad();
            tabbarhide("tasklayout");
            document.getElementById("task").setAttribute("active", "true");
            document.getElementById("task").className = "tabs-item-active";
        }
        function Relogin() {
            parent.window.reload = "true";
            parent.window.location.replace("<%=contextPath%>/pages/security/logout.jsp");
        }
        //TODO (add by yxy,2014-08-17,*start)
        // ���Ƽ���ҳ��ʱ�䳤ʱ�����û��Ѻ���ʾ
        document.onreadystatechange = subSomething;//��ҳ�����״̬�ı��ʱ��ִ���������.
        function subSomething() {
            if (document.readyState == "complete" && window.parent.frames["scrollInfoWorkFrame"].document.readyState == "complete") {
                document.getElementById('loading').style.display = 'none';
                //window.setTimeout("myTimer()", 5000);
            }
        }
        var forTabClickStr=1;
        function forTabClick() {
            if(forTabClickStr==1){
                forTabClickStr=0;
                window.setTimeout("run()", 1000);
            }
        }
        //TODO (add by yxy,2014-08-17,*end)
        </script>
    </head>
    <body onload="doOnLoad()" onResize="doOnResize();">
        <%--�ر�tabʱ������һ�������tab--%>
        <input type="hidden" id="lasttabdivid">
        <div class="skin-top-right">
            <table width="100%" cellpadding="0" cellspacing="0" style="margin:0px;padding:0px;height:100%;">
                <tr style="width:100%; height:45px">
                    <td width="5%" rowspan="2">
                        &nbsp;
                        <img src="../../images/epss.jpg" height="40px">
                    </td>
                    <td colspan="2">
                        <img src="../../images/epss_title.png" height="50px" style="margin-left: 5px">
                    </td>
                    <td style="text-align:right" class="headfont">
                        <span>����,<%=username%>! </span>
                        <span><%= " | [" + rolesall + "] |" %></span>
                        <span onclick="changepwd()" onMouseOver="this.style.cursor='hand'">�޸�����</span>
                        <span onclick="Relogin()" onMouseOver="this.style.cursor='hand'">| �˳�&nbsp;&nbsp;</span>
                    </td>
                </tr>
                <tr style="width:100%; height:25px">
                    <td colspan="5" style="height:25px;">
                                <div onclick="tabbarclk(this);" active="true" id="task" class="tabs-item-active"
                                     style="float:left;width:80px;margin-left:12px;">
                                    <span style="width:100%;">����ҵ��</span>
                                </div>
                                <div style="float:left;width:2px;"></div>
                                <div onclick="tabbarclk(this);forTabClick()" active="false" id="biz" class="tabs-item"
                                     style="float:left;width:80px;">
                                    <span style="width:100%;">ҵ�����</span>
                                </div>
                                <div style="float:left;width:2px;"></div>
                                <div onclick="tabbarclk(this);" active="false" id="sys" class="tabs-item"
                                     style="float:left;width:80px;">
                                    <span style="width:100%;">ϵͳ����</span>
                                </div>
                                <div style="float:left;width:2px;"></div>
                                <div onclick="tabbarclk(this);" active="false" id="help" class="tabs-item"
                                     style="float:left;width:80px;">
                                    <span style="width:100%;">��������</span>
                                </div>
                                <div style="float:left;width:2px;"></div>
                        <%--TODO (add by yxy,2014-08-17,*start)--%>
                        <div style="float:left;width:2px;"></div>
                        <div id="dynamicInfo"
                             style="float:right;width:838px;">
                            <iframe id="scrollInfoWorkFrame" name="scrollInfoWorkFrame"
                                    src="<%=contextPath%>/UI/epss/scrollInfo/ScrollInfo.xhtml"
                                    width="100%"
                                    height="25px;"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="8"
                                    scrolling="no"
                                    allowtransparency="true">
                            </iframe>
                        </div>
                        <%--TODO (add by yxy,2014-08-17,*end)--%>
                    </td>
                </tr>
                <tr style="width:100%; height:4px">
                    <td width="100%" style="height:4px;background-color: #3169AD;" colspan="4"></td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" colspan="4">
                        <%--TODO (add by yxy,2014-08-02,Total:18 line)--%>
                        <div class="divlayout" id="tasklayout">
                            <div class="divlayout" id="loading">
                                <div style="position:absolute;top:30%;left: 40%;text-align: center;">
                                    <img src="<%=contextPath%>/images/ajaxloadingbar.gif"/>
                                    <br>
                                    <span>ҳ�����ڼ�����...</span>
                                </div>
                            </div>
                            <iframe id="workFrame" name="workFrame"
                                    src="<%=contextPath%>/UI/epss/task/task.xhtml"
                                    width="100%" height="100%"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="0"
                                    scrolling="no">
                            </iframe>
                        </div>
                        <div class="divlayout" id="bizlayout"></div>
                        <div class="divlayout" id="syslayout"></div>
                        <div class="divlayout" id="helplayout">
                            <br/>ϵͳ������Ϣ...
                        </div>
                        <%--<div id="tip" style="display: none;height: 0px">
                            <h1>
                                <a href="javascript:void(0)" onclick="start()">��</a>
                                ��������Ϣ
                            </h1>
                            <iframe id="dynamicDialogInfoWorkFrame" name="dynamicDialogInfoWorkFrame"
                                    src="<%=contextPath%>/UI/epss/scrollInfo/Dialog.xhtml"
                                    width="180"
                                    height="180"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="0"
                                    scrolling="no">
                            </iframe>
                        </div>
                        <%--
                        <div style="display: none" >
                            <iframe id="tipAdd" name="tipAdd"
                                    src="<%=contextPath%>/UI/epss/task/tipAdd.jsp"
                                    width="0" height="0"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="0"
                                    scrolling="no">
                            </iframe>
                        </div>--%>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>