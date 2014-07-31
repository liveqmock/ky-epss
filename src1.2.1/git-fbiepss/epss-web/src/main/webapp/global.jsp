
<%@ include file="/pages/security/online.jsp" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setHeader("Expires", "0");

    String contextPath = request.getContextPath();
    String cssPath = contextPath + "/css";
    String jsPath = contextPath + "/js";
    String dhtmlxPath = contextPath + "/dhtmlx";
    String dojoPath = contextPath + "/dojo";
    String imagePath = contextPath + "/images";
%>

<LINK href="<%=contextPath %>/css/ccb.css" type="text/css" rel="stylesheet">
<%@ page import="skyline.platform.db.*" %>
<%@ page import="skyline.platform.html.*" %>
<%@ page import="skyline.platform.form.config.EnumerationType" %>
<%@ page import="skyline.platform.utils.*" %>
<%@ page import="java.util.*" %>
<%@ page import="skyline.platform.security.OperatorManager" %>
<%@ page import="skyline.platform.form.config.SystemAttributeNames" %>

<script type="text/javascript">
    var g_jsContextPath = "<%=contextPath%>";
</script>

<script language="javascript" src="<%=contextPath %>/js/basic.js"></script>
<script language="javascript" src="<%=contextPath %>/js/xmlHttp.js"></script>
<script language="javascript" src="<%=contextPath %>/js/dbgrid.js"></script>
<script language="javascript" src="<%=contextPath %>/js/dbutil.js"></script>
<script language="JavaScript" src="<%=contextPath %>/js/compack.js"></script>
<script language="javascript" src="<%=contextPath %>/js/menu.js"></script>
<script language="javascript" src="<%=contextPath %>/js/tree.js"></script>
<script language="javascript" src="<%=contextPath %>/js/loadform.js"></script>
<script language="javascript" src="<%=contextPath %>/js/dropdownData.js"></script>
<script language="javascript" type="text/javascript" src="<%=contextPath %>/DatePicker/WdatePicker.js"></script>




