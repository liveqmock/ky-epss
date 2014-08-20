<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="skyline.platform.db.DBXML"%>
<%@ page import="skyline.platform.utils.Basic"%>
<%
  request.setCharacterEncoding("GBK");
  DBXML testxml = new DBXML();
  String xmnlStr = request.getParameter("xx");
  String rexml = testxml.getDropDownXML(Basic.decode(xmnlStr));
%>
<%=rexml%>
