<%@ page contentType="text/html; charset=UTF-8"%>

<%@ page import="platform.view.build.form.control.*"%>
<%@ page import="platform.view.build.form.control.impl.*"%>
<%@ page import="platform.view.build.form.util.SessionAttributes"%>
<%@ page import="platform.view.build.utils.*"%>


<%
	request.setCharacterEncoding("GBK");
    ServiceProxy sp = new SeviceProxyHttpImpl();
    sp.proxyService(request,response);
    SessionContext sc = (SessionContext)session.getAttribute(SessionAttributes.SESSION_CONTEXT_NAME);
    SQLResponse sqlReq = (SQLResponse)sc.getRequestAttribute(NewActionController.RESPONSE_XML_NAME);
%>
<%=sqlReq.getXmlStr()%>
