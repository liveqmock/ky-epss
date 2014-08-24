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
            "   type," +
            "   statusFlag," +
            "   recordsCountInGroup" +
            " from" +
            "    (select  " +
            "      eci.CTT_TYPE as type," +
            "      eip.STATUS_FLAG as statusFlag," +
            "      count(1) as recordsCountInGroup" +
            "    from  " +
            "      ES_CTT_INFO eci " +
            "    inner join" +
            "      OPER_RES opr" +
            "    on" +
            "      opr.INFO_TYPE=eci.CTT_TYPE" +
            "    and " +
            "      opr.INFO_PKID=eci.PKID" +
            "    and " +
            "      opr.OPER_PKID=#{strOperPkid}" +
            "    left join  " +
            "      ES_INIT_POWER eip " +
            "    on" +
            "      eci.CTT_TYPE=eip.POWER_TYPE" +
            "    and " +
            "      eci.PKID=eip.POWER_PKID" +
            "    group by " +
            "      eci.CTT_TYPE,eip.STATUS_FLAG" +
            "    union" +
            "    select  " +
            "      eis.STL_TYPE as type," +
            "      eip.STATUS_FLAG as statusFlag," +
            "      count(1) as recordsCountInGroup" +
            "    from  " +
            "      ES_INIT_STL eis" +
            "    inner join" +
            "      OPER_RES opr" +
            "    on" +
            "      opr.INFO_TYPE=eis.STL_TYPE" +
            "    and " +
            "      opr.INFO_PKID=eis.STL_PKID" +
            "    and " +
            "      opr.OPER_PKID=#{strOperPkid}" +
            "    left join  " +
            "      ES_INIT_POWER eip" +
            "    on" +
            "      eis.STL_TYPE=eip.POWER_TYPE" +
            "    and " +
            "      eis.STL_PKID=eip.POWER_PKID" +
            "    and " +
            "      eis.PERIOD_NO=eip.PERIOD_NO" +
            "    group by " +
            "      eis.STL_TYPE,eip.STATUS_FLAG" +
            "   )" +
            " order by" +
            "   type,statusFlag nulls first")
    List<TaskShow> getTaskCountsInFlowGroup(@Param("strOperPkid") String strOperPkid);

    @Select(" select" +
            "   info.type," +
            "   info.pkid," +
            "   info.id," +
            "   info.name," +
            "   info.periodNo," +
            "   info.statusFlag," +
            "   info.preStatusFlag" +
            " from" +
            "   OPER_RES opr" +
            " inner join" +
            "   (select  " +
            "     eci.CTT_TYPE as type," +
            "     eci.PKID as pkid," +
            "     eci.ID as id," +
            "     eci.NAME as name," +
            "     eip.PERIOD_NO as periodNo," +
            "     eip.STATUS_FLAG as statusFlag," +
            "     eip.PRE_STATUS_FLAG as preStatusFlag" +
            "   from  " +
            "     ES_CTT_INFO eci " +
            "   left join  " +
            "     ES_INIT_POWER eip " +
            "   on" +
            "     eci.CTT_TYPE=eip.POWER_TYPE" +
            "   and " +
            "     eci.PKID=eip.POWER_PKID" +
            "   union" +
            "   select " +
            "     eis.STL_TYPE as type," +
            "     eis.STL_PKID as pkid," +
            "     eis.ID as id," +
            "     ecinfo.NAME as name," +
            "     eip.PERIOD_NO as periodNo," +
            "     eip.STATUS_FLAG as statusFlag," +
            "     eip.PRE_STATUS_FLAG as preStatusFlag" +
            "   from  " +
            "     ES_INIT_STL eis" +
            "   left join  " +
            "     ES_INIT_POWER eip" +
            "   on" +
            "     eis.STL_TYPE=eip.POWER_TYPE" +
            "   and " +
            "     eis.STL_PKID=eip.POWER_PKID" +
            "   and " +
            "     eis.PERIOD_NO=eip.PERIOD_NO" +
            "   left join  " +
            "     ES_CTT_INFO ecinfo" +
            "   on  " +
            "     eis.STL_PKID=ecinfo.PKID" +
            "   )info" +
            " on " +
            "   opr.INFO_TYPE=info.type" +
            " and " +
            "   opr.INFO_PKID=info.pkid" +
            " where" +
            "   opr.OPER_PKID=#{strOperPkid}" +
            " order by" +
            "   type,statusFlag,preStatusFlag")
    List<TaskShow> getDetailTaskShowList(@Param("strOperPkid") String strOperPkid);
}
