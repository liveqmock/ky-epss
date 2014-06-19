<%@ page contentType="text/html; charset=GBK" %>

<%
    platform.view.build.security.OperatorManager _om = (platform.view.build.security.OperatorManager)session.getAttribute(platform.view.build.form.config.SystemAttributeNames.USER_INFO_NAME);
  if ( _om == null ) {
      String path = request.getContextPath();
      out.println("<script language=\"javascript\">alert ('操作员超时，请重新签到！'); if(top){ top.location.href='"+path+"/pages/security/loginPage.jsp'; } else { location.href = '"+path+"/pages/security/loginPage.jsp';} </script>");
//      out.println("<script language=\"javascript\">alert ('操作员超时，请重新签到！'); if(top){ top.location.href='/login.jsp'; } else { location.href = '/login.jsp';} </script>");
      return;
  }%>
