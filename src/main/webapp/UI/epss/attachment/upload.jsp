<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.FileInputStream" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-9
  Time: 下午3:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" pageEncoding="utf-8" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
    <body>
    <%
       // request.setCharacterEncoding("gbk");
        int MAXSIZE = 102400*102400;
        String rootPath;
        DataInputStream in = null;
        FileOutputStream fileOut = null;
        String remoteAddr = request.getRemoteAddr();
        String serverName = request.getServerName();
        String realPath = request.getRealPath(serverName);
        realPath = realPath.substring(0,realPath.lastIndexOf("\\"));
        rootPath = realPath+"\\upload\\" ;
        String contentType = request.getContentType();
        try{
            if (contentType.indexOf("multipart/form-data")>=0){
                in = new DataInputStream(request.getInputStream()) ;
                int formDataLenth = request.getContentLength();
                if (formDataLenth>MAXSIZE){
                    out.println("<P>上传文件不能超过"+MAXSIZE+"<p>");
                    return;
                }
                byte dataBytes[] =  new byte[formDataLenth];
                int byteRead = 0;
                int totalBytesRead = 0;
                while (totalBytesRead<formDataLenth){
                    byteRead = in.read(dataBytes,totalBytesRead,formDataLenth);
                    totalBytesRead += byteRead ;
                }
                String file = new String(dataBytes);
                String saveFile = file.substring(file.indexOf("filename=\"")+10);
                saveFile = saveFile.substring(0,saveFile.indexOf("\n"));
                saveFile = saveFile.substring(saveFile.lastIndexOf("\\")
                        +1,saveFile.indexOf("\"") );
                int lastIndex = contentType.lastIndexOf("=");
                String boundary = contentType.substring(lastIndex
                        +1,contentType.length());
                String fileName = rootPath + saveFile;
                //out.print(fileName);
                int pos;
                pos = file.indexOf("filename=\"");
                pos = file.indexOf("\n",pos)+1;
                pos = file.indexOf("\n",pos)+1;
                pos = file.indexOf("\n",pos)+1;
                int boundaryLocation = file.indexOf(boundary,pos)-4;
                int startPos = ((file.substring(0,pos)).getBytes()).length ;
                int endPos = ((file.substring(0,boundaryLocation)).getBytes()).length ;
                File checkFile = new File(fileName);
                if (checkFile.exists()){
                    out.println("<p>"+saveFile+"文件已经存在</p>");
                }
                File fileDir = new File(rootPath);
                if (!fileDir.exists()){
                    fileDir.mkdir();
                }
                fileOut = new FileOutputStream(fileName);
                fileOut.write(dataBytes,startPos,(endPos - startPos));
                fileOut.close();
                out.println(saveFile+"文件上传成功");
            }else {
                String content = request.getContentType();
                out.println("<p>上传文件类型错误multipart/form-data</p>");
            }
        }catch (Exception ex){
            throw new ServletException(ex.getMessage());
        }

    %>
    </body>
</html>