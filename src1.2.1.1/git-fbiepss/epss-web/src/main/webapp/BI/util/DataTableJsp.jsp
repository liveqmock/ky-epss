<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="skyline.platform.db.DBXML"%>
<%@ page import="skyline.platform.utils.Basic"%>
<%
	request.setCharacterEncoding("GBK");
    DBXML  testxml =  new DBXML();
    String xmnlStr = request.getParameter("tabStr");
    String rexml = testxml.getDataTableXML(Basic.decode(xmnlStr));

%>

<%=rexml%>
