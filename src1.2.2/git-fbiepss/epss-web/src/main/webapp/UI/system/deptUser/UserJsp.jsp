<%@include file="/global.jsp" %>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="skyline.platform.db.*"%>
<%@ page import="skyline.platform.html.*"%>
<html>
  <head>
    <title></title>
    <LINK href="<%=contextPath%>/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/basic.js"></script>
    <script language="javascript" src="<%=contextPath%>/js/xmlHttp.js"></script>
    <script language="javascript" src="<%=contextPath%>/js/dbgrid.js"></script>
    <script language="javascript" src="<%=contextPath%>/js/dropdownData.js"></script>
    <script language="javascript" src="<%=contextPath%>/js/dbutil.js"></script>
    <script language="javascript" src="userJsp.js"></script>
  </head>
  <%
        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("userTable");
        dbGrid.setGridType("edit");
        dbGrid
            .setfieldSQL("select operid as keycode,DeptID,operid,opername,issuper,sex,opertype from ptoper where  (1=1) ");
        dbGrid.setfieldcn("�û�ID,dd,�û�ID,����,�Ƿ��쵼,�Ա�,�û�����");
        dbGrid.setenumType("-1,-1,0,0,BOOLTYPE,SEX,opertype");
        dbGrid.setvisible("false,false,true,true,true,true,true");
        dbGrid.setfieldName("keycode,deptid,operid,opername,issuper,sex,opertype");
        dbGrid.setfieldWidth("5,0,0,15,20,20,20,20");
        dbGrid.setfieldType("text,text,text,text,dropdown,dropdown,dropdown");
        dbGrid.setfieldCheck(" ; ; isNull=false,textLength=4;textLength=20; ; ; ");
        dbGrid.setWhereStr("and(1>1)");

        dbGrid.setpagesize(30);
        dbGrid.setCheck(true);

        //////���ݼ���ť
        dbGrid.setdataPilotID("datapilot");
        ///dbGrid.setbuttons("moveFirst,prevPage,movePrev,moveNext,nextPage,moveLast,appendRecord,editRecord,deleteRecord,��ӡ=print");
        dbGrid.setbuttons("moveFirst,prevPage,movePrev,moveNext,nextPage,moveLast,appendRecord,editRecord,deleteRecord");
  %>
  <body bgcolor="#ffffff" onload="body_load()" onresize="body_load()" class="Bodydefault">
    <FIELDSET>
      <LEGEND>
        �û��б�
      </LEGEND>
      <table  width="100%" rules="border" class="title1">
        <tr>
          <td>
            <%=dbGrid.getDBGrid()%>
          </td>
        </tr>
      </table>
    </FIELDSET>
    <FIELDSET>
      <LEGEND>
        ����
      </LEGEND>
      <table width="100%" rules="border" class="title1">
        <tr>
          <td>
            <span id="title"></span>
          </td>
          <td align="right">
            <%=dbGrid.getDataPilot()%>
          </td>
        </tr>
      </table>
    </FIELDSET>
  </body>
</html>