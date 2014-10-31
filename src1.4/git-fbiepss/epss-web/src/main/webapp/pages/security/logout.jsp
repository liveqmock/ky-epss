<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="skyline.platform.form.config.SystemAttributeNames" %>
<%@ page import="skyline.platform.security.OnLineOpersManager" %>
<%@page import="skyline.platform.security.OperatorManager" %>
<%@ page import="java.util.Enumeration" %>

<html>
<head>
    <title>Logout</title>
</head>
<body bgcolor="#cccccc" link="#3366cc" vlink="#9999cc" alink="#0000cc">
<table border=0 cellspacing="18" cellpadding="0">
    <tr>
        <td valign="top">
            <%
                OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (om != null) {
                    OnLineOpersManager.removeOperFromServer(session.getId()+om.getOperator().getId(),application);
                    om.logout();
                }
                Enumeration p_enum = session.getAttributeNames();
                while (p_enum.hasMoreElements()) {
                    String removeStr = (String) p_enum.nextElement();
                    session.removeAttribute(removeStr);
                }
            %>
            <%
                String isClose = request.getParameter("isclose");
                if (isClose == null || isClose.trim().equals("")) {
            %>
            <jsp:forward page="/pages/security/loginPage.jsp"/>
            <%
                } else {
                    out.println("<script>window.close();</script>");
                }
            %>
        </td>
    </tr>
</table>
</body>
</html>