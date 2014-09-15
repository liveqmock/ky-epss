package epss.repository.dao.not_mybatis;

import epss.repository.model.FlowCtrlHis;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public interface MyFlowCtrlHisMapper {
     @Select(" select DISTINCT" +
            "     eipwai.INFO_TYPE as infoType, " +
            "     eipwai.FLOW_STATUS as flowStatus,  " +
            "     eipwai.note as note," +
            "     eipwai.created_by as createdBy, " +
            "     eipwai.created_date as createdDate    " +
            " from " +
            "     FLOW_CTRL_HIS eipwai  " +
            " inner join " +
            "     ( " +
            "         select  " +
            "            max(created_date) as maxDate, " +
            "            INFO_TYPE, " +
            "            FLOW_STATUS " +
            "         from " +
            "            FLOW_CTRL_HIS  " +
            "         where  " +
            "            INFO_TYPE in ('3','4','5') " +
            "         and " +
            "            power_pkid=#{powerPkid} " +
            "         and " +
            "            period_no=#{periodNo} " +
            "         group by INFO_TYPE,FLOW_STATUS " +
            "     )maxcreated_date  " +
            " on   " +
            "     eipwai.INFO_TYPE=maxcreated_date.INFO_TYPE " +
            " and " +
            "     eipwai.FLOW_STATUS=maxcreated_date.FLOW_STATUS  " +
            " and " +
            "     NVL(eipwai.created_date,'NOTNULL')=NVL(maxcreated_date.maxDate,'NOTNULL')  "+
            " where " +
            "     eipwai.power_pkid=#{powerPkid} " +
            " and " +
            "     eipwai.period_no=#{periodNo} " +
            " order by eipwai.INFO_TYPE,eipwai.FLOW_STATUS")
    List<FlowCtrlHis> getSubStlListByFlowCtrlHis(@Param("powerPkid") String powerPkid,
                                                 @Param("periodNo") String periodNo);
}
