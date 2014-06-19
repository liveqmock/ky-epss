<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="platform.view.build.db.DBXML"%>
<%@ page import="platform.view.build.utils.Basic"%>
<%
  request.setCharacterEncoding("GBK");
  DBXML testxml = new DBXML();
  String xmnlStr = request.getParameter("xx");
  String rexml = testxml.getDropDownXML(Basic.decode(xmnlStr));
%>
<%=rexml%>
