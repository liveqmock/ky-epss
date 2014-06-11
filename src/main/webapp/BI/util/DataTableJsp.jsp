<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="platform.view.build.db.DBXML"%>
<%@ page import="platform.view.build.utils.Basic"%>
<%
	request.setCharacterEncoding("GBK");
    DBXML  testxml =  new DBXML();
    String xmnlStr = request.getParameter("tabStr");
    String rexml = testxml.getDataTableXML(Basic.decode(xmnlStr));

%>

<%=rexml%>
