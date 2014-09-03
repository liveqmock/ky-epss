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

    /*@Select(" (" +
            "      select" +
            "            cttopr.type," +
            "            cttopr.pkid," +
            "            cttopr.id," +
            "            cttopr.name," +
            "            cttopr.operResFlowStatus," +
            "            cttopr.periodNo," +
            "            cttopr.flowStatus," +
            "            cttopr.preFlowStatus" +
            "      from" +
            "          (" +
            "                select" +
            "                    cttofoper.INFO_TYPE as type," +
            "                    cttofoper.INFO_PKID as pkid," +
            "                    cttofoper.ID as id," +
            "                    cttofoper.NAME as name," +
            "                    cttofoper.flow_status as operResFlowStatus," +
            "                    '' as periodNo," +
            "                    eip.STATUS_FLAG as flowStatus," +
            "                    eip.PRE_STATUS_FLAG as preFlowStatus" +
            "                from  " +
            "                   (select  " +
            "                      distinct  " +
            "                      opr.INFO_TYPE,  " +
            "                      opr.INFO_PKID,  " +
            "                      opr.flow_status, " +
            "                      eci.ID,  " +
            "                      eci.NAME  " +
            "                    from  " +
            "                      OPER_RES opr  " +
            "                    inner join  " +
            "                      ES_CTT_INFO eci   " +
            "                    on  " +
            "                      opr.INFO_TYPE=eci.CTT_TYPE  " +
            "                    and  " +
            "                      opr.INFO_PKID=eci.PKID  " +
            "                    where  " +
            "                      opr.OPER_PKID=#{strOperPkid} " +
            "                    and" +
            "                      opr.FLOW_STATUS='0'" +
            "                    and" +
            "                      opr.TYPE='business'" +
            "                    )cttofoper                 " +
            "                left join    " +
            "                    ES_INIT_POWER eip   " +
            "                on  " +
            "                    cttofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "                and  " +
            "                    cttofoper.INFO_PKID=eip.POWER_PKID " +
            "          )cttopr" +
            "      where " +
            "          cttopr.flowStatus is null" +
            " ) " +
            " union " +
            " (" +
            "     select  " +
            "            stlopr.type," +
            "            stlopr.pkid," +
            "            stlopr.id," +
            "            stlopr.name," +
            "            stlopr.operResFlowStatus," +
            "            stlopr.periodNo," +
            "            stlopr.flowStatus," +
            "            stlopr.preFlowStatus" +
            "     from" +
            "           (" +
            "                 select" +
            "                     stlofoper.INFO_TYPE as type," +
            "                     stlofoper.PKID as pkid," +
            "                     stlofoper.ID as id," +
            "                     stlofoper.NAME as name," +
            "                     stlofoper.flow_status as operResFlowStatus," +
            "                     stlofoper.PERIOD_NO as periodNo," +
            "                     eip.STATUS_FLAG as flowStatus," +
            "                     eip.PRE_STATUS_FLAG as preFlowStatus" +
            "                  from" +
            "                    (" +
            "                        select" +
            "                          distinct  " +
            "                          opr.INFO_TYPE,  " +
            "                          opr.INFO_PKID,  " +
            "                          stl.PKID," +
            "                          opr.flow_status, " +
            "                          stl.ID as ID,  " +
            "                          (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as NAME," +
            "                          stl.PERIOD_NO" +
            "                        from  " +
            "                          OPER_RES opr  " +
            "                        inner join  " +
            "                          ES_INIT_STL stl" +
            "                        on  " +
            "                          opr.INFO_TYPE=stl.stl_type " +
            "                        and  " +
            "                          opr.INFO_PKID=stl.stl_pkid  " +
            "                        where  " +
            "                          opr.OPER_PKID=#{strOperPkid}" +
            "                        and" +
            "                          opr.FLOW_STATUS='0'" +
            "                        and " +
            "                          opr.TYPE='business' " +
            "                     )stlofoper                 " +
            "                  left join    " +
            "                     ES_INIT_POWER eip   " +
            "                  on  " +
            "                     stlofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "                  and  " +
            "                     stlofoper.INFO_PKID=eip.POWER_PKID" +
            "          )stlopr" +
            "     where " +
            "          stlopr.flowStatus is null" +
            ")                 " +
            "order by  " +
            "  flowStatus,TYPE,preFlowStatus")*/
        @Select(
            " select" +
            "     opr.INFO_TYPE as type," +
            "     opr.INFO_PKID as pkid," +
            "     opr.INFO_NAME as name" +
            " from  " +
            "     OPER_RES_TASK opr" +
            " where" +
            "     opr.OPER_PKID=#{strOperPkid}" +
            "order by  " +
            "     type")
    List<TaskShow> getRencentlyPowerDetailTaskShowList(@Param("strOperPkid") String strOperPkid);

    @Select(" (" +
            "     select" +
            "            cttopr.type," +
            "            cttopr.pkid," +
            "            cttopr.id," +
            "            cttopr.name," +
            "            cttopr.operResFlowStatus," +
            "            cttopr.periodNo," +
            "            cttopr.flowStatus," +
            "            cttopr.preFlowStatus" +
            "     from" +
            "          (" +
            "                select" +
            "                    cttofoper.INFO_TYPE as type," +
            "                    cttofoper.INFO_PKID as pkid," +
            "                    cttofoper.ID as id," +
            "                    cttofoper.NAME as name," +
            "                    cttofoper.flow_status as operResFlowStatus," +
            "                    '' as periodNo," +
            "                    eip.STATUS_FLAG as flowStatus," +
            "                    eip.PRE_STATUS_FLAG as preFlowStatus" +
            "                from  " +
            "                   (select  " +
            "                      distinct  " +
            "                      opr.INFO_TYPE,  " +
            "                      opr.INFO_PKID,  " +
            "                      opr.flow_status, " +
            "                      eci.ID,  " +
            "                      eci.NAME  " +
            "                    from  " +
            "                      OPER_RES opr  " +
            "                    inner join  " +
            "                      ES_CTT_INFO eci   " +
            "                    on  " +
            "                      opr.INFO_TYPE=eci.CTT_TYPE  " +
            "                    and  " +
            "                      opr.INFO_PKID=eci.PKID  " +
            "                    where  " +
            "                      opr.OPER_PKID=#{strOperPkid} " +
            "                    and" +
            "                      opr.TYPE='business'" +
            "                    )cttofoper                 " +
            "                left join    " +
            "                    ES_INIT_POWER eip   " +
            "                on  " +
            "                    cttofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "                and  " +
            "                    cttofoper.INFO_PKID=eip.POWER_PKID " +
            "          )cttopr" +
            "     where " +
            "          cttopr.flowStatus is not null" +
            " ) " +
            " union " +
            " (" +
            "     select  " +
            "            stlopr.type," +
            "            stlopr.pkid," +
            "            stlopr.id," +
            "            stlopr.name," +
            "            stlopr.operResFlowStatus," +
            "            stlopr.periodNo," +
            "            stlopr.flowStatus," +
            "            stlopr.preFlowStatus" +
            "     from" +
            "          (" +
            "                select" +
            "                    stlofoper.INFO_TYPE as type," +
            "                    stlofoper.PKID as pkid," +
            "                    stlofoper.ID as id," +
            "                    stlofoper.NAME as name," +
            "                    stlofoper.flow_status as operResFlowStatus," +
            "                    stlofoper.PERIOD_NO as periodNo," +
            "                    eip.STATUS_FLAG as flowStatus," +
            "                    eip.PRE_STATUS_FLAG as preFlowStatus" +
            "                 from" +
            "                   (" +
            "                       select" +
            "                         distinct  " +
            "                         opr.INFO_TYPE,  " +
            "                         opr.INFO_PKID,  " +
            "                         stl.PKID," +
            "                         opr.flow_status, " +
            "                         stl.ID as ID,  " +
            "                         (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as NAME," +
            "                         stl.PERIOD_NO" +
            "                       from  " +
            "                         OPER_RES opr  " +
            "                       inner join  " +
            "                         ES_INIT_STL stl" +
            "                       on  " +
            "                         opr.INFO_TYPE=stl.stl_type " +
            "                       and  " +
            "                         opr.INFO_PKID=stl.stl_pkid  " +
            "                       where  " +
            "                         opr.OPER_PKID=#{strOperPkid}" +
            "                       and " +
            "                         opr.TYPE='business' " +
            "                    )stlofoper                 " +
            "                 left join    " +
            "                    ES_INIT_POWER eip   " +
            "                 on  " +
            "                    stlofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "                 and  " +
            "                    stlofoper.INFO_PKID=eip.POWER_PKID" +
            "          )stlopr" +
            "     where " +
            "          stlopr.flowStatus is not null" +
            ")                 " +
            "order by  " +
            "  flowStatus,TYPE,preFlowStatus")
    List<TaskShow> getDetailTaskShowList(@Param("strOperPkid") String strOperPkid);
}