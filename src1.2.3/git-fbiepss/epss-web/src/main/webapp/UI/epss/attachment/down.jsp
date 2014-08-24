<%@ page import="java.net.URLEncoder" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-9
  Time: 下午1:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <%
        response.setContentType("application/x-download");//设置为下载application/x-download
        String filedownload = "/images/xml.png";//即将下载的文件的相对路径
        String filedisplay = "xml";//下载文件时显示的文件保存名称
        filedisplay = URLEncoder.encode(filedisplay, "utf-8");
        response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);

        try
        {
            RequestDispatcher dis = request.getRequestDispatcher(filedownload);
            if(dis!= null)
            {
                dis.forward(request,response);
            }
            response.flushBuffer();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            out.clear();
            out = pageContext.pushBody();
        }
    %>
</body>
</html>