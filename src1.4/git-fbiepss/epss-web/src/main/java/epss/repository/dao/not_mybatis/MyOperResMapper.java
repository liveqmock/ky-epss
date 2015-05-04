package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperResShow;
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
public interface MyOperResMapper {
    List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara);

    @Select("select distinct" +
            "   opr.INFO_PKID as infoPkid, " +
            "   eci.NAME as infoName" +
            " from " +
            "   OPER_RES opr" +
            " inner join" +
            "   CTT_INFO eci" +
            " on" +
            "   opr.INFO_PKID=eci.PKID" +
            " where " +
            "   opr.INFO_TYPE=#{strInfoType}" +
            " and" +
            "   opr.FLOW_STATUS=#{strFlowStatus}"+
            " and" +
            "   opr.OPER_PKID=#{strOperPkid}")
    List<OperResShow> getInfoListByOperFlowPkid(@Param("strInfoType") String strInfoType,
                                                @Param("strFlowStatus") String strFlowStatus,
                                                @Param("strOperPkid") String strOperPkid);
    @Select(" select" +
            "      ma.PKID                                           as pkid," +
            "      ma.TID                                            as tid," +
            "      ma.OPER_PKID                                      as operPkid," +
            "      o.ID                                              as operId," +
            "      o.NAME                                            as operName," +
            "      ma.INFO_PKID                                      as infoPkid," +
            "      pm.MENULABEL                                      as infoName," +
            "      ma.ARCHIVED_FLAG                                  as archivedFlag," +
            "      ma.CREATED_BY                                     as createdBy," +
            "      (select name from oper where PKID=ma.CREATED_BY)  as createdByName," +
            "      ma.CREATED_TIME                                   as createdTime," +
            "      ma.LAST_UPD_BY                                    as lastUpdBy," +
            "      (select name from oper where PKID=ma.LAST_UPD_BY) as lastUpdByName," +
            "      ma.LAST_UPD_TIME                                  as lastUpdTime," +
            "      ma.REMARK                                         as remark," +
            "      ma.REC_VERSION                                    as recVersion" +
            " from" +
            "     OPER_RES ma" +
            " left join" +
            "     OPER o" +
            " on" +
            "     ma.OPER_PKID=o.PKID" +
            " left join " +
            "     PTMENU pm" +
            " on" +
            "     ma.INFO_PKID=pm.PKID" +
            " where " +
            "     (case when #{operPkid} ='' then ma.OPER_PKID else #{operPkid} end)=ma.OPER_PKID" +
            " and" +
            "     (case when #{infoPkid} ='' then ma.INFO_PKID else #{infoPkid} end)=ma.INFO_PKID" +
            " and" +
            "     ma.type='system'")
    List<OperResShow> getMenuAppointShowList(@Param("operPkid") String operPkidPara,
                                                 @Param("infoPkid") String infoPkidPara);

    //hu×·¼Ó
    @Select("select Group_concat(distinct o.name) from oper_res  operres ,oper o " +
            "where " +
            "    operres.oper_pkid=o.pkid  " +
            "and " +
            "    operres.info_pkid =#{infoPkid} " +
            "and " +
            "    operres.flow_status =#{flowStatus} " +
            "and operres.info_type = #{infoType}")
    String getCreateByNameByFlowStatusAndInfoPkid(@Param("infoPkid") String infoPkid,@Param("flowStatus") String flowStatus,@Param("infoType") String infoType);
}
