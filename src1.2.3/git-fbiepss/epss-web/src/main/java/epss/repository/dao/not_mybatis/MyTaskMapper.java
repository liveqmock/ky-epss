package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.TaskShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time:
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyTaskMapper {
    @Select(" select " +
            "   distinct opr.FLOW_STATUS as flowStatus" +
            " from" +
            "   OPER_RES opr" +
            " where" +
            "   opr.TYPE='business'" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getTaskFlowGroup();

    @Select(" select " +
            "   distinct opr.FLOW_STATUS as flowStatus" +
            " from" +
            "   OPER_RES opr" +
            " where" +
            "   opr.OPER_PKID=#{strOperPkid}" +
            " and " +
            "   opr.TYPE='business'" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getOwnTaskFlowGroup(@Param("strOperPkid") String strOperPkid);

    @Select(
            " (" +
            "      SELECT" +
            "           DISTINCT" +
            "           opr.PKID," +
            "           opr.INFO_TYPE as type," +
            "           eci.ID," +
            "           eci.NAME," +
            "           opr.FLOW_STATUS as flowStatus," +
            "           opr.TASKDONE_FLAG as taskDoneFlag" +
            "      FROM" +
            "           OPER_RES opr" +
            "      INNER JOIN" +
            "           CTT_INFO eci" +
            "      ON" +
            "           opr.INFO_PKID=eci.PKID" +
            "      where" +
            "           opr.OPER_PKID=#{strOperPkid}" +
            "      and" +
            "           opr.INFO_TYPE>='3'" +
            "      and" +
            "           opr.FLOW_STATUS='0'" +
            "      and " +
            "           opr.TYPE='business'" +
            " )" +
            " order by  " +
            "    taskDoneFlag,flowStatus,type")
    List<TaskShow> getRencentlyPowerDetailTaskShowList(@Param("strOperPkid") String strOperPkid);

    @Select(
            "    (" +
            "        select" +
            "            distinct" +
            "            opr.INFO_TYPE as type," +
            "            opr.INFO_PKID as pkid," +
            "            eci.ID as id," +
            "            eci.NAME as name," +
            "            '' as periodNo," +
            "            eci.FLOW_STATUS as flowStatus," +
            "            eci.FLOW_STATUS_REASON as flowStatusReason" +
            "        from" +
            "            OPER_RES opr" +
            "        inner join  " +
            "            CTT_INFO eci" +
            "        on" +
            "            opr.INFO_TYPE=eci.CTT_TYPE" +
            "        and" +
            "            opr.INFO_PKID=eci.PKID" +
            "        where" +
            "            opr.OPER_PKID=#{strOperPkid}" +
         /*   "        and" +
            "            eci.FLOW_STATUS is not null" +*/
            "        and" +
            "            opr.TYPE='business'" +
            "    ) " +
            " union " +
            "    (" +
            "         select" +
            "             distinct" +
            "             opr.INFO_TYPE as type," +
            "             stl.PKID as pkid," +
            "             stl.ID as id," +
            "             (select name from CTT_INFO where pkid=stl.stl_pkid) as name," +
            "             stl.PERIOD_NO as periodNo," +
            "             stl.FLOW_STATUS as flowStatus," +
            "             stl.FLOW_STATUS_REASON as flowStatusReason" +
            "         from" +
            "             OPER_RES opr" +
            "         inner join  " +
            "             PROG_STL_INFO stl" +
            "         on" +
            "             opr.INFO_TYPE=stl.stl_type" +
            "         and" +
            "             opr.INFO_PKID=stl.stl_pkid" +
            "         where  " +
            "             opr.OPER_PKID=#{strOperPkid}" +
          /*  "         and" +
            "             stl.FLOW_STATUS is not null" +*/
            "         and" +
            "             opr.TYPE='business'" +
            "    )" +
            " order by" +
            "    flowStatus,name,periodNo")
    List<TaskShow> getDetailTodoTaskShowList(@Param("strOperPkid") String strOperPkid);

    @Select(" select" +
            "    type, " +
            "    pkid, " +
            "    id, " +
            "    name, " +
            "    max(operResFlowStatus) as operResFlowStatus, " +
            "    periodNo, " +
            "    flowStatus, " +
            "    flowStatusReason " +
            " from" +
            "   (" +
            "       (" +
            "           select" +
            "               distinct" +
            "               opr.INFO_TYPE as type," +
            "               opr.INFO_PKID as pkid," +
            "               eci.ID as id," +
            "               eci.NAME as name," +
            "               opr.FLOW_STATUS as operResFlowStatus," +
            "               '' as periodNo," +
            "               eci.FLOW_STATUS as flowStatus," +
            "               eci.FLOW_STATUS_REASON as flowStatusReason" +
            "           from" +
            "               OPER_RES opr" +
            "           inner join  " +
            "               CTT_INFO eci" +
            "           on" +
            "               opr.INFO_TYPE=eci.CTT_TYPE" +
            "           and" +
            "               opr.INFO_PKID=eci.PKID" +
            "           where" +
            "               opr.OPER_PKID=#{strOperPkid}" +
            "           and" +
            "               eci.FLOW_STATUS is not null" +
            "           and" +
            "               opr.TYPE='business'" +
            "        ) " +
            "        union " +
            "        (" +
            "            select" +
            "                distinct" +
            "                opr.INFO_TYPE as type," +
            "                stl.PKID as pkid," +
            "                stl.ID as id," +
            "                (select name from CTT_INFO where pkid=stl.stl_pkid) as name," +
            "                opr.FLOW_STATUS as operResFlowStatus," +
            "                stl.PERIOD_NO as periodNo," +
            "                stl.FLOW_STATUS as flowStatus," +
            "                stl.FLOW_STATUS_REASON as flowStatusReason" +
            "            from" +
            "                OPER_RES opr" +
            "            inner join  " +
            "                PROG_STL_INFO stl" +
            "            on" +
            "                opr.INFO_TYPE=stl.stl_type" +
            "            and" +
            "                opr.INFO_PKID=stl.stl_pkid" +
            "            where  " +
            "                opr.OPER_PKID=#{strOperPkid}" +
            "            and" +
            "                stl.FLOW_STATUS is not null" +
            "            and" +
            "                opr.TYPE='business'" +
            "        )" +
            "    )" +
            " where" +
            "    operResFlowStatus<=flowStatus" +
            " group by" +
            "    type," +
            "    pkid," +
            "    id," +
            "    name," +
            "    periodNo," +
            "    flowStatus," +
            "    flowStatusReason" +
            " order by" +
            "    flowStatus,name,periodNo")
    List<TaskShow> getDetailDoneTaskShowList(@Param("strOperPkid") String strOperPkid);
}