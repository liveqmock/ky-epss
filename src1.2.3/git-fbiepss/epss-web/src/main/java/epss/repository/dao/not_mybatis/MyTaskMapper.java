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
            "   opr.OPER_PKID=#{strOperPkid}" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getTaskFlowGroup(@Param("strOperPkid") String strOperPkid);

    @Select("(select  " +
            "                cttofoper.INFO_TYPE as type,  " +
            "                cttofoper.INFO_PKID as pkid,  " +
            "                cttofoper.ID as id,  " +
            "                cttofoper.NAME as name,  " +
            "                cttofoper.flow_status as operResFlowStatus,  " +
            "                eip.STATUS_FLAG as flowStatus,  " +
            "                eip.PRE_STATUS_FLAG as preFlowStatus  " +
            "             from  " +
            "               (select  " +
            "                  distinct  " +
            "                  opr.INFO_TYPE,  " +
            "                  opr.INFO_PKID,  " +
            "                  opr.flow_status, " +
            "                  eci.ID,  " +
            "                  eci.NAME  " +
            "                from  " +
            "                  OPER_RES opr  " +
            "                inner join  " +
            "                  ES_CTT_INFO eci   " +
            "                on  " +
            "                  opr.INFO_TYPE=eci.CTT_TYPE  " +
            "                and  " +
            "                  opr.INFO_PKID=eci.PKID  " +
            "                where  " +
            "                  opr.OPER_PKID=#{strOperPkid} " +
            "                )cttofoper                 " +
            "             left join    " +
            "                ES_INIT_POWER eip   " +
            "             on  " +
            "                cttofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "             and  " +
            "                cttofoper.INFO_PKID=eip.POWER_PKID " +
            ") " +
            "union " +
            "(select  " +
            "                stlofoper.INFO_TYPE as type,  " +
            "                stlofoper.INFO_PKID as pkid,  " +
            "                stlofoper.ID as id,  " +
            "                stlofoper.NAME as name,  " +
            "                stlofoper.flow_status as operResFlowStatus,  " +
            "                eip.STATUS_FLAG as flowStatus,  " +
            "                eip.PRE_STATUS_FLAG as preFlowStatus  " +
            "             from  " +
            "               (select  " +
            "                  distinct  " +
            "                  opr.INFO_TYPE,  " +
            "                  opr.INFO_PKID,  " +
            "                  opr.flow_status, " +
            "                  stl.ID as ID,  " +
            "                  (select name from ES_CTT_INFO where pkid=stl.stl_pkid) as NAME    " +
            "                from  " +
            "                  OPER_RES opr  " +
            "                inner join  " +
            "                  ES_INIT_STL stl   " +
            "                on  " +
            "                  opr.INFO_TYPE=stl.stl_type " +
            "                and  " +
            "                  opr.INFO_PKID=stl.stl_pkid  " +
            "                where  " +
            "                  opr.OPER_PKID=#{strOperPkid} " +
            "                )stlofoper                 " +
            "             left join    " +
            "                ES_INIT_POWER eip   " +
            "             on  " +
            "                stlofoper.INFO_TYPE=eip.POWER_TYPE  " +
            "             and  " +
            "                stlofoper.INFO_PKID=eip.POWER_PKID  " +
            ")                 " +
            "order by  " +
            "  flowStatus,TYPE,preFlowStatus")
    List<TaskShow> getDetailTaskShowList(@Param("strOperPkid") String strOperPkid);
}