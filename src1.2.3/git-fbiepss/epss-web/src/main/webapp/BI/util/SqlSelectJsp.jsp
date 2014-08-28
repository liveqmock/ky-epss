<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="skyline.platform.form.control.*"%>
<%@ page import="skyline.platform.form.control.impl.*"%>
<%@ page import="skyline.platform.form.util.SessionAttributes"%>
<%@ page import="skyline.platform.utils.*"%>
<%
	request.setCharacterEncoding("GBK");
    ServiceProxy sp = new SeviceProxyHttpImpl();
    sp.proxyService(request,response);
    SessionContext sc = (SessionContext)session.getAttribute(SessionAttributes.SESSION_CONTEXT_NAME);
    SQLResponse sqlReq = (SQLResponse)sc.getRequestAttribute(NewActionController.RESPONSE_XML_NAME);
%>
<%=sqlReq.getXmlStr()%>
