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
            "     eipwai.REMARK as remark," +
            "     eipwai.CREATED_BY as createdBy, " +
            "     IFNULL(eipwai.CREATED_BY_NAME," +
             "        (select NAME from OPER where PKID=eipwai.CREATED_BY)) as createdByName, " +
            "     eipwai.CREATED_TIME as createdTime" +
            " from " +
            "     FLOW_CTRL_HIS eipwai  " +
            " inner join " +
            "     ( " +
            "         select  " +
            "            max(CREATED_TIME) as maxTime, " +
            "            INFO_TYPE, " +
            "            FLOW_STATUS " +
            "         from " +
            "            FLOW_CTRL_HIS  " +
            "         where  " +
            "            INFO_TYPE in ('3','4','5') " +
            "         and " +
            "            INFO_PKID=#{infoPkid} " +
            "         and " +
            "            PERIOD_NO=#{periodNo} " +
            "         group by INFO_TYPE,FLOW_STATUS " +
            "     )maxcreated_date  " +
            " on   " +
            "     eipwai.INFO_TYPE=maxcreated_date.INFO_TYPE " +
            " and " +
            "     eipwai.FLOW_STATUS=maxcreated_date.FLOW_STATUS  " +
            " and " +
            "     IFNULL(eipwai.CREATED_TIME,'NOTNULL')=IFNULL(maxcreated_date.maxTime,'NOTNULL')  "+
            " where " +
            "     eipwai.INFO_PKID=#{infoPkid} " +
            " and " +
            "     eipwai.period_no=#{periodNo} " +
            " order by eipwai.INFO_TYPE,eipwai.FLOW_STATUS")
    List<FlowCtrlHis> getSubStlListByFlowCtrlHis(@Param("infoPkid") String infoPkid,
                                                 @Param("periodNo") String periodNo);
}
