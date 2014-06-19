<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="platform.view.build.form.config.SystemAttributeNames" %>

<%@ page import="platform.view.build.security.OperatorManager" %>
   
<%
   
  try {
    	
       session.invalidate();      
   
  } catch ( Exception ex ) { 
    
  }

%>
