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
            "           ci.ID," +
            "           ci.NAME," +
            "           opr.FLOW_STATUS as flowStatus," +
            "           opr.TASKDONE_FLAG as taskDoneFlag" +
            "      FROM" +
            "           OPER_RES opr" +
            "      INNER JOIN" +
            "           CTT_INFO ci" +
            "      ON" +
            "           ci.PKID=opr.INFO_PKID" +
            "      AND " +
            "           ci.flow_status='3'" +
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
            "            ci.PKID as pkid," +
            "            opr.FLOW_STATUS as operResFlowStatus," +
            "            ci.ID as id," +
            "            ci.NAME as name," +
            "            '' as periodNo," +
            "            ci.FLOW_STATUS as flowStatus," +
            "            ci.FLOW_STATUS_REASON as flowStatusReason" +
            "        from" +
            "            OPER_RES opr" +
            "        inner join  " +
            "            CTT_INFO ci" +
            "        on" +
            "            opr.INFO_TYPE=ci.CTT_TYPE" +
            "        and" +
            "            opr.INFO_PKID=ci.PKID" +
            "        where" +
            "            opr.OPER_PKID=#{strOperPkid}" +
            "        and" +
            "            opr.TYPE='business'" +
            "    ) " +
            " union " +
            "    (" +
            "         select" +
            "             distinct" +
            "             opr.INFO_TYPE as type," +
            "             stl.PKID as pkid," +
            "             opr.FLOW_STATUS as operResFlowStatus," +
            "             stl.ID as id," +
            "             ci.name as name," +
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
            "         inner join " +
            "             CTT_INFO ci" +
            "         on" +
            "             ci.PKID=stl.STL_PKID" +
            "         where  " +
            "             opr.OPER_PKID=#{strOperPkid}" +
            "         and" +
            "             opr.TYPE='business'" +
            "    )" +
            " order by" +
            "    flowStatus,name,periodNo")
    List<TaskShow> getDetailTodoTaskShowList(@Param("strOperPkid") String strOperPkid);

    @Select(" select" +
            "    type, " +
            "    pkid, " +
            "    stlPkid," +
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
            "               '' as stlPkid," +
            "               ci.ID as id," +
            "               ci.NAME as name," +
            "               opr.FLOW_STATUS as operResFlowStatus," +
            "               '' as periodNo," +
            "               ci.FLOW_STATUS as flowStatus," +
            "               ci.FLOW_STATUS_REASON as flowStatusReason" +
            "           from" +
            "               OPER_RES opr" +
            "           inner join  " +
            "               CTT_INFO ci" +
            "           on" +
            "               opr.INFO_TYPE=ci.CTT_TYPE" +
            "           and" +
            "               opr.INFO_PKID=ci.PKID" +
            "           left join" +
            "               SIGN_PART sp" +
            "           on" +
            "               sp.PKID=ci.SIGN_PART_B" +
            "           where" +
            "               opr.OPER_PKID=#{strOperPkid}" +
            "           and" +
            "               ci.FLOW_STATUS is not null" +
            "           and" +
            "               opr.TYPE='business'" +
            "        ) " +
            "        union " +
            "        (" +
            "            select" +
            "                distinct" +
            "                opr.INFO_TYPE as type," +
            "                stl.PKID as pkid," +
            "                stl.STL_PKID as stlPkid,    " +
            "                stl.ID as id," +
            "                ci.name as name," +
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
            "            inner join " +
            "                CTT_INFO ci" +
            "            on" +
            "                ci.PKID=stl.STL_PKID" +
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
			"    stlPkid," +
            "    id," +
            "    name," +
            "    periodNo," +
            "    flowStatus," +
            "    flowStatusReason" +
            " order by" +
            "    flowStatus,type,periodNo desc")
    List<TaskShow> getDetailDoneTaskShowList(@Param("strOperPkid") String strOperPkid);
}