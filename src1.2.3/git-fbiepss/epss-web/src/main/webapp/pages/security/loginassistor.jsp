<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="skyline.platform.form.config.SystemAttributeNames" %>
<%@ page import="skyline.platform.security.OnLineOpersManager" %>
<%@ page import="skyline.platform.security.OperatorManager" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%
    String path = request.getContextPath();
    String username = request.getParameter("username");
    if (username == null) {
        out.println("<script language=\"javascript\">if(top){ top.location.href='" +
                path + "/pages/security/loginPage.jsp'; } else { location.href = '" +
                path + "/pages/security/loginPage.jsp';} </script>");
        return;
    }

    String password = request.getParameter("password");
    String cookieFlag = request.getParameter("DropExpiration");

    Cookie cookies[] = request.getCookies();
    int cookieMaxAge = 0;
    if ("Month".equalsIgnoreCase(cookieFlag)) {
        cookieMaxAge = 30 * 24 * 60 * 60;
    } else if ("Year".equalsIgnoreCase(cookieFlag)) {
        cookieMaxAge = 365 * 24 * 60 * 60;
    }
    if (!"None".equalsIgnoreCase(cookieFlag)) {
        boolean isFound = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("usernamecookie").equals(cookie.getName())) {
                    if (!username.equals(cookie.getValue())) {
                        cookie.setValue(username);
                        cookie.setPath(request.getContextPath() + "/pages");
                        cookie.setMaxAge(cookieMaxAge);
                        response.addCookie(cookie);
                    }
                    isFound = true;
                }
            }
        }
        if (!isFound) {
            Cookie cookie = new Cookie("usernamecookie", username);
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath(request.getContextPath() + "/pages");
            response.addCookie(cookie);
        }
    }
%>

<%
    OperatorManager om = new OperatorManager();
    session.setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
    boolean isLogin = false;
    om.setRemoteAddr(request.getRemoteAddr());
    om.setRemoteHost(request.getRemoteHost());
    isLogin = om.login(username, password);
    if (!isLogin) {
        out.println("<script language=\"javascript\">alert ('输入用户名或密码有误！'); if(top){ top.location.href='" + path + "/pages/security/loginPage.jsp'; } else { location.href = '" + path + "/pages/security/loginPage.jsp';} </script>");
    } else {
        if (!OnLineOpersManager.isHasUserList(application)) {
            OnLineOpersManager.setUserListToServer(application);
            OnLineOpersManager.addOperToServer(session.getId() + username, om, application);
            application.setAttribute("sessions",new HashMap<String, HttpSession>());
            ((HashMap<String, HttpSession>)application.getAttribute("sessions")).put(session.getId(),session);
        } else {
            OnLineOpersManager.addOperToServer(session.getId() + username, om, application);
            ((HashMap<String, HttpSession>)application.getAttribute("sessions")).put(session.getId(),session);
        }
    }
%>
