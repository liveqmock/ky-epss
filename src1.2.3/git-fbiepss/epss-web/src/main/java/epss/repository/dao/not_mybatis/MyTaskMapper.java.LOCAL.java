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
            " and" +
            "   opr.FLOW_STATUS='0'" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getOwnRencentlyPowerTaskFlowGroup(@Param("strOperPkid") String strOperPkid);

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
            /*"    (" +
            "         select" +
            "             opr.INFO_TYPE as type," +
            "             opr.INFO_PKID as pkid," +
            "             eci.ID as id," +
            "             eci.NAME as name," +
            "             opr.FLOW_STATUS as operResFlowStatus," +
            "             '' as periodNo," +
            "             eci.FLOW_STATUS as flowStatus," +
            "             eci.FLOW_STATUS_REASON as flowStatusReason" +
            "         from  " +
            "             OPER_RES opr  " +
            "         inner join  " +
            "             ES_CTT_INFO eci" +
            "         on  " +
            "             eci.CTT_TYPE=opr.INFO_TYPE" +
            "         and  " +
            "             eci.PKID=opr.INFO_PKID" +
            "         and" +
            "             eci.FLOW_STATUS is null" +
            "         where  " +
            "             opr.OPER_PKID=#{strOperPkid} " +
            "         and" +
            "             opr.FLOW_STATUS='0'" +
            "         and" +
            "             opr.TYPE='business'" +
            "    ) " +
            " union " +*/
            "    (" +
            "         select" +
            "              distinct  " +
            "              opr.INFO_TYPE as type," +
            "              stl.PKID as pkid," +
            "              stl.ID as ID," +
            "              opr.flow_status as operResFlowStatus," +
            "              (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as NAME," +
            "              stl.PERIOD_NO as periodNo," +
            "              stl.FLOW_STATUS as flowStatus," +
            "              stl.FLOW_STATUS_REASON as flowStatusReason" +
            "         from  " +
            "              OPER_RES opr  " +
            "         inner join  " +
            "              ES_INIT_STL stl" +
            "         on  " +
            "              opr.INFO_TYPE=stl.stl_type " +
            "         and  " +
            "              opr.INFO_PKID=stl.stl_pkid  " +
            "         where" +
            "              opr.OPER_PKID=#{strOperPkid}" +
            "         and" +
            "              opr.FLOW_STATUS='0'" +
            "         and " +
            "              opr.TYPE='business' " +
            "     )" +
            "order by  " +
            "     flowStatus,TYPE,flowStatusReason")
    List<TaskShow> getRencentlyPowerDetailTaskShowList(@Param("strOperPkid") String strOperPkid);

    @Select(
            "    (" +
            "        select" +
            "            distinct" +
            "            opr.INFO_TYPE as type," +
            "            opr.INFO_PKID as pkid," +
            "            eci.ID as id," +
            "            eci.NAME as name," +
            "            opr.FLOW_STATUS as operResFlowStatus," +
            "            '' as periodNo," +
            "            eci.FLOW_STATUS as flowStatus," +
            "            eci.FLOW_STATUS_REASON as flowStatusReason" +
            "        from" +
            "            OPER_RES opr" +
            "        inner join  " +
            "            ES_CTT_INFO eci" +
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
            "             (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as name," +
            "             opr.FLOW_STATUS as operResFlowStatus," +
            "             stl.PERIOD_NO as periodNo," +
            "             stl.FLOW_STATUS as flowStatus," +
            "             stl.FLOW_STATUS_REASON as flowStatusReason" +
            "         from" +
            "             OPER_RES opr" +
            "         inner join  " +
            "             ES_INIT_STL stl" +
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
            "    flowStatus,TYPE,flowStatusReason")
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
            "               ES_CTT_INFO eci" +
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
            "                (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as name," +
            "                opr.FLOW_STATUS as operResFlowStatus," +
            "                stl.PERIOD_NO as periodNo," +
            "                stl.FLOW_STATUS as flowStatus," +
            "                stl.FLOW_STATUS_REASON as flowStatusReason" +
            "            from" +
            "                OPER_RES opr" +
            "            inner join  " +
            "                ES_INIT_STL stl" +
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
            "    flowStatus,TYPE,flowStatusReason")
    List<TaskShow> getDetailDoneTaskShowList(@Param("strOperPkid") String strOperPkid);
}