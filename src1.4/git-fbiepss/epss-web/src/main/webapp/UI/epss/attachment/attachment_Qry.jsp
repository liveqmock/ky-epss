<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>附件下载</title>
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
        <table id="mytab" width="90%" align="center" bordercolordark="#666666"
           bordercolorlight="#FF0000" border="1" cellpadding="1"
           cellspacing="1">
            <caption>附件列表</caption>
            <tr align="center" style="background-color: cornflowerblue">
                <td width="85%">附件名</td>
                <td width="15%">下载</td>
            </tr>
            <c:forEach items="${fileNames}" var="fileName">
                <tr align="center">
                    <td>${fileName}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/servlet/DownloadServlet?fileName=${fileName}">
                            <img src="${pageContext.request.contextPath}/images/download.jpg"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>