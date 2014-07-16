package epss.repository.dao.not_mybatis;

import epss.repository.model.EsCttItem;
import epss.repository.model.EsItemStlSubcttEngQ;
import epss.repository.model.model_show.TaskShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface TaskMapper {
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
    List<TaskShow> getTaskCountsInFlowGroup();

    @Select(" select  " +
            "   eci.CTT_TYPE as type," +
            "   eci.PKID as pkid," +
            "   eci.ID as id," +
            "   eci.NAME as name," +
            "   eip.PERIOD_NO as periodNo," +
            "   eip.STATUS_FLAG as statusFlag," +
            "   eip.PRE_STATUS_FLAG as preStatusFlag" +
            " from  " +
            "   ES_CTT_INFO eci " +
            " left join  " +
            "   ES_INIT_POWER eip " +
            " on" +
            "   eci.CTT_TYPE=eip.POWER_TYPE" +
            " and " +
            "   eci.PKID=eip.POWER_PKID" +
            " union" +
            " select  " +
            "   eis.STL_TYPE as type," +
            "   eis.PKID as pkid," +
            "   eis.ID as id," +
            "   ecinfo.NAME as name," +
            "   eip.PERIOD_NO as periodNo," +
            "   eip.STATUS_FLAG as statusFlag," +
            "   eip.PRE_STATUS_FLAG as preStatusFlag" +
            " from  " +
            "   ES_INIT_STL eis" +
            " left join  " +
            "   ES_INIT_POWER eip" +
            " on" +
            "   eis.STL_TYPE=eip.POWER_TYPE" +
            " and " +
            "   eis.STL_PKID=eip.POWER_PKID" +
            " and " +
            "   eis.PERIOD_NO=eip.PERIOD_NO" +
            " left join  " +
            "   ES_CTT_INFO ecinfo" +
            " on  " +
            "   eis.STL_PKID=ecinfo.PKID" +
            " order by" +
            "   type,statusFlag")
    List<TaskShow> getTaskShowList();
}
