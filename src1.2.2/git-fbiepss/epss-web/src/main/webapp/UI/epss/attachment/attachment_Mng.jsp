<%@ page import="epss.service.CttInfoService" %>
<%@ page import="org.springframework.context.ApplicationContext" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="epss.common.utils.ApplicationContextUtil" %>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>附件维护</title>
        <style type="text/css">
            img {
                width: 15px;
                height: 15px;
                margin: 2px;
                border: 0px;
            }

            td {
                padding-top: 3px;
                padding-bottom: 3px;
            }
        </style>
        <script type="text/javascript">
            function addFile(){
                var tabNode = document.getElementsByTagName("table")[0];
                var trNode = tabNode.insertRow();
                var tdNode_file = trNode.insertCell();
                var tdNode_del = trNode.insertCell();
                tdNode_file.innerHTML = "<input type = 'file' name='file_name'/>";
                tdNode_del.innerHTML = "<a href = 'javascript:void(0)' onclick = 'delFile(this)'>取消</a>";
            }

            function delFile(node) {
                var trNode = node.parentNode.parentNode;
                trNode.parentNode.removeChild(trNode);
            }

            window.onload = function(){
                var tabNode = document.getElementById("mytab");
                var trs = tabNode.rows;
                for(var i = 0;i<trs.length;i++){
                    if(i % 2 == 0 && i != 0){
                        trs[i].style.backgroundColor = "#00FFFF";
                    }
                }
            };
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/servlet/UploadServlet?strPkid=<%=request.getParameter("strPkid")%>"
              method="post" enctype="multipart/form-data" style="text-align:center">
            <table align="center" bordercolordark="#666666" bordercolorlight="#FF0000" border="1" cellpadding="0"
                   cellspacing="0">
                <tr>
                    <td><input type="file" name="file"/></td>
                </tr>
            </table>
            <div align="center">
                <input type="button" onclick="addFile()" value="添加附件"/>
                <input type="submit" value="上传"/>
            </div>
        </form>

        <table id="mytab" width="90%" align="center" bordercolordark="#666666"
               bordercolorlight="#FF0000" border="1" cellpadding="1"
               cellspacing="1">
            <caption>附件列表</caption>
            <tr align="center" style="background-color: cornflowerblue">
                <td width="70%">附件名</td>
                <td width="15%">下载</td>
                <td width="15%">删除</td>
            </tr>
            <c:forEach items="${fileNames}" var="fileName" >
                <tr align="center"  >
                    <td>${fileName}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/servlet/DownloadServlet?fileName=${fileName}">
                            <img src="${pageContext.request.contextPath}/images/download.jpg"/>
                        </a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/upload/${fileName}" target="_blank">
                            预览
                        </a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/servlet/DeleteServlet?fileName=${fileName}&strPkid=<%=request.getParameter("strPkid")%>"
                            onclick="delFile(this);">
                            <img src="${pageContext.request.contextPath}/images/delete.jpg"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>