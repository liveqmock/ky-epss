<%@ page contentType="text/html; charset=GBK" %>
<%@ include file="/pages/security/loginassistor.jsp" %>
<%
    response.setContentType("text/html; charset=GBK");
    String contextPath = request.getContextPath();

    /*2011-4-7 Cookie设置添加到loginassistor.jsp中*/
    String jsonDefaultMenu = null;
    String jsonSystemMenu = null;
    try {
        jsonDefaultMenu = om.getJsonString("default");
        jsonSystemMenu = om.getJsonString("system");
    } catch (Exception e) {
        System.out.println("jsp" + e + "\n");
    }
    String operid = null;
    if (om != null) {
        if (om.getOperator() != null) {
            username = om.getOperatorName();
            operid = om.getOperator().getId();
        }
    }
%>
<%--<!DOCTYPE html>--%>
<html>
    <head>
        <title>工程项目结算管理系统</title>
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
        <link href="<%=contextPath%>/css/diytabbar.css" type="text/css" rel="stylesheet">
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
            function doOnLoad() {
                bizdhxLayout = new dhtmlXLayoutObject("bizlayout", "2U", "dhx_skyblue");
                doBizLoad();
                sysdhxLayout = new dhtmlXLayoutObject("syslayout", "2U", "dhx_skyblue");
                doSysLoad();
                tabbarhide("todotasklayout");
                document.getElementById("todotask").setAttribute("active", "true");
                document.getElementById("todotask").className = "tabs-item-active";
            }
            function relogin() {
                parent.window.reload = "true";
                parent.window.location.replace("<%=contextPath%>/pages/security/logout.jsp");
            }
            // 控制加载页面时间长时，给用户友好提示
            document.onreadystatechange = subSomething;//当页面加载状态改变的时候执行这个方法.
            function subSomething() {
                if (document.readyState == "complete" &&
                    window.parent.frames["todoWorkFrame"].document.readyState == "complete") {
                    document.getElementById('todoloading').style.display = 'none';
                }
            }
            function myRequest() {
                var url = "ajaxRequest.do?time=" + Math.random();
                //要提交到服务器的数据
                var content = "userName=ni";
                //调用异常请求提交的函数
                sendRequest("POST", url, content, "TEXT", myProcessResponse);
            }
            function myProcessResponse() {
                // 请求已完成
                if (http_request.readyState == 4) {
                    // 信息已经成功返回，开始处理信息
                    if (http_request.status == 200) {
                        //返回的是文本格式信息
                        myDoBizLoad(http_request.responseText);
                    } else { //页面不正常
                        //"您所请求的页面有异常"
                    }
                }
            }
        </script>
    </head>
    <body onload="doOnLoad()">
        <%--关闭tab时返回上一个浏览的tab--%>
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
                        <span>您好,<%=operid%>[<%=username%>]! </span>
                        <span onclick="changepwd()" onMouseOver="this.style.cursor='hand'">修改密码</span>
                        <span onclick="relogin()" onMouseOver="this.style.cursor='hand'">| 退出&nbsp;&nbsp;</span>
                    </td>
                </tr>
                <tr style="width:100%; height:25px">
                    <td colspan="5" style="height:25px;">
                        <div onclick="tabbarclk(this);document.frames('todoWorkFrame').location.href='<%=contextPath%>/UI/epss/task/todoTask.xhtml;'" active="true" id="todotask" class="tabs-item-active"
                             style="float:left;width:80px;margin-left:12px;">
                            <span style="width:100%;">待办业务</span>
                        </div>
                        <div style="float:left;width:2px;"></div>
                        <div onclick="document.frames('donetaskworkFrame').location.href='<%=contextPath%>/UI/epss/task/doneTask.xhtml';tabbarclk(this);" active="false" id="donetask" class="tabs-item"
                             style="float:left;width:80px;">
                            <span style="width:100%;">已办业务</span>
                        </div>
                        <div style="float:left;width:2px;"></div>
                        <div onclick="myRequest();tabbarclk(this);" active="false" id="biz" class="tabs-item"
                             style="float:left;width:80px;">
                            <span style="width:100%;">业务管理</span>
                        </div>
                        <div style="float:left;width:2px;"></div>
                        <div onclick="tabbarclk(this);" active="false" id="sys" class="tabs-item"
                             style="float:left;width:80px;">
                            <span style="width:100%;">系统管理</span>
                        </div>
                        <div style="float:left;width:2px;"></div>
                        <div style="float:left;width:2px;"></div>
                        <div id="dynamicInfo" style="float:right;width:300px;">
                            <iframe id="scrollInfoWorkFrame"
                                    src="<%=contextPath%>/UI/epss/scrollInfo/scrollInfo.xhtml"
                                    width="100%"
                                    height="25px;"
                                    frameborder="no"
                                    marginwidth="0" marginheight="8"
                                    scrolling="no"
                                    allowtransparency="true">
                            </iframe>
                        </div>
                    </td>
                </tr>
                <tr style="width:100%; height:4px">
                    <td width="100%" style="height:4px;background-color: #3169AD;" colspan="4"></td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" colspan="4">
                        <div class="divlayout" id="todotasklayout">
                            <div class="divlayout" id="todoloading">
                                <div style="position:absolute;top:30%;left: 40%;text-align: center;">
                                    <img src="<%=contextPath%>/images/ajaxloadingbar.gif"/>
                                    <br>
                                    <span>页面正在加载中...</span>
                                </div>
                            </div>
                            <iframe id="todoWorkFrame"
                                    src="<%=contextPath%>/UI/epss/task/todoTask.xhtml"
                                    width="100%" height="100%"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="0"
                                    scrolling="no">
                            </iframe>
                        </div>
                        <div class="divlayout" id="donetasklayout">
                            <iframe id="donetaskworkFrame" name="workFrame"
                                    src="<%=contextPath%>/UI/epss/task/doneTask.xhtml"
                                    width="100%" height="100%"
                                    frameborder="no"
                                    border="0"
                                    marginwidth="0" marginheight="0"
                                    scrolling="no">
                            </iframe>
                        </div>
                        <div class="divlayout" id="bizlayout"></div>
                        <div class="divlayout" id="syslayout">
                            <br/>系统帮助信息...
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>