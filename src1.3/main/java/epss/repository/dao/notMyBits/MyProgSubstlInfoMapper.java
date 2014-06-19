package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.ProgSubstlInfoShow;
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
public interface MyProgSubstlInfoMapper {
    @Select("select max(ID) from PROG_SUBSTL_INFO")
    String getStrMaxProgSubstlInfoId();

    @Select("select PKID            as pkid," +
            "psi.SUBCTT_INFO_PKID   as subcttInfoPkid," +
            "psi.ID                 as id," +
            "psi.STAGE_NO           as stageNo," +
            "psi.ARCHIVED_FLAG      as archivedFlag," +
            "psi.ORIGIN_FLAG        as originFlag" +
            "psi.FLOW_STATUS        as flowStatus," +
            "psi.FLOW_STATUS_REMARK as flowStatusRemark," +
            "psi.CREATED_BY         as createdBy," +
            "(select Max(OPERNAME) from PTOPER where OPERID=psi.CREATED_BY) as createdByName," +
            "psi.CREATED_TIME       as createdTime," +
            "psi.UPDATED_BY         as updatedBy," +
            "(select Max(OPERNAME) from PTOPER where OPERID=psi.UPDATED_BY) as updatedByName," +
            "psi.UPDATED_TIME       as updatedTime," +
            "psi.REC_VERSION        as recVerpeion," +
            "psi.ATTACHMENT         as attachment," +
            "psi.REMARK             as remark," +
            "psi.TID                as tid" +
            " from PROG_SUBSTL_INFO psi" +
            " where " +
            " (case when #{strFlowStatusBegin} is null " +
            " then nvl(psi.FLOW_STATUS,' ') " +
            " else #{strFlowStatusBegin} end)" +
            " <=nvl(psi.FLOW_STATUS,' ')" +
            " and " +
            " (case when #{strFlowStatusEnd} is null " +
            " then nvl(psi.FLOW_STATUS,' ') " +
            " else #{strFlowStatusEnd} end)" +
            " >=nvl(psi.FLOW_STATUS,' ')")
    List<ProgSubstlInfoShow> getProgSubstlInfoShowListByFlowStatusBegin_End(@Param("strFlowStatusBegin") String strFlowStatusBegin,
                                                                            @Param("strFlowStatusEnd") String strFlowStatusEnd);

    @Select("select distinct" +
            "    pi.PKID as pkid" +
            "   ,pi.ID" +
            "   ,pi.substlType" +
            "   ,pi.SUBCTT_INFO_PKID as stlPkid" +
            "   ,pi.STAGE_NO as stageNo" +
            "   ,si.NAME as stlName" +
            "   ,(select Max(Name) from SIGN_PART where PKID=si.SIGN_PART_PKID_B) as signPartNameB" +
            "   ,pi.FLOW_STATUS as flowStatus"+
            " from"+
            "     SUBCTT_INFO si" +
            " inner join" +
            "   (" +
            "    select" +
            "          pwi.PKID " +
            "         ,'3' as substlType" +
            "         ,pwi.ID " +
            "         ,pwi.SUBCTT_INFO_PKID " +
            "         ,pwi.STAGE_NO " +
            "         ,pwi.FLOW_STATUS " +
            "    from" +
            "         PROG_WORKQTY_INFO pwi" +
            "    where" +
            "         pwi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pwi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "    union all " +
            "    select" +
            "          pmi.PKID " +
            "         ,'4' as substlType" +
            "         ,pmi.ID " +
            "         ,pmi.SUBCTT_INFO_PKID " +
            "         ,pmi.STAGE_NO " +
            "         ,pmi.FLOW_STATUS " +
            "    from" +
            "         PROG_MATQTY_INFO pmi" +
            "    where" +
            "         pmi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pmi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "   )pi" +
            " on"+
            "   si.PKID=pi.SUBCTT_INFO_PKID" +
            " where" +
            "   si.CSTPL_INFO_PKID = #{strCstplInfoPkid}" +
            " order by" +
            "   pi.SUBCTT_INFO_PKID,pi.STAGE_NO")
    List<ProgSubstlInfoShow> getNotFormProgSubstlInfoShowList(@Param("strCstplInfoPkid") String strCstplInfoPkid,
                                                              @Param("strSubInfoPkid") String strSubInfoPkid,
                                                              @Param("strStageNo") String strStageNo);

    @Select("select" +
            " eispower.PKID as pkid" +
            " from"+
            " ES_CTT_INFO si" +
            " inner join" +
            "(" +
            " select" +
            " eisqm1.PKID" +
            ",eisqm1.SUBCTT_INFO_PKID" +
            ",eisqm1.STAGE_NO" +
            ",eisqm1.FLOW_STATUS" +
            " from" +
            "   (" +
            "    select" +
            "          pwi.PKID " +
            "         ,'3' as substlType" +
            "         ,pwi.ID " +
            "         ,pwi.SUBCTT_INFO_PKID " +
            "         ,pwi.STAGE_NO " +
            "         ,pwi.FLOW_STATUS " +
            "    from" +
            "         PROG_WORKQTY_INFO pwi" +
            "    where" +
            "         pwi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pwi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "    union all " +
            "    select" +
            "          pmi.PKID " +
            "         ,'4' as substlType" +
            "         ,pmi.ID " +
            "         ,pmi.SUBCTT_INFO_PKID " +
            "         ,pmi.STAGE_NO " +
            "         ,pmi.FLOW_STATUS " +
            "    from" +
            "         PROG_MATQTY_INFO pmi" +
            "    where" +
            "         pmi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pmi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            ")eisqm1" +
            ",(" +
            "    select" +
            "          pwi.PKID " +
            "         ,'3' as substlType" +
            "         ,pwi.ID " +
            "         ,pwi.SUBCTT_INFO_PKID " +
            "         ,pwi.STAGE_NO " +
            "         ,pwi.FLOW_STATUS " +
            "    from" +
            "         PROG_WORKQTY_INFO pwi" +
            "    where" +
            "         pwi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pwi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "    union all " +
            "    select" +
            "          pmi.PKID " +
            "         ,'4' as substlType" +
            "         ,pmi.ID " +
            "         ,pmi.SUBCTT_INFO_PKID " +
            "         ,pmi.STAGE_NO " +
            "         ,pmi.FLOW_STATUS " +
            "    from" +
            "         PROG_MATQTY_INFO pmi" +
            "    where" +
            "         pmi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pmi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            ")eisqm2" +
            " where" +
            " eisqm1.substlType!=eisqm2.substlType" +
            " and" +
            " eisqm1.SUBCTT_INFO_PKID=eisqm2.SUBCTT_INFO_PKID" +
            " and" +
            " eisqm1.STAGE_NO=eisqm2.STAGE_NO" +
            " and" +
            " (eisqm1.FLOW_STATUS='2' and eisqm2.FLOW_STATUS='2')" +
            ")eispower" +
            " on"+
            " si.PKID=eispower.SUBCTT_INFO_PKID" +
            " where" +
            " si.PARENT_PKID = #{strParentPkid}" +
            " order by" +
            " eispower.SUBCTT_INFO_PKID,eispower.STAGE_NO")
    List<ProgSubstlInfoShow> getFormPreProgSubstlInfoShowList(@Param("strParentPkid") String strParentPkid,
                                                              @Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                              @Param("strStageNo") String strStageNo);

    @Select("select distinct" +
            " eispower.PKID as pkid" +
            ",eispower.SUBCTT_INFO_PKID as stlPkid" +
            ",eispower.STAGE_NO as stageNo" +
            ",si.NAME as stlName" +
            ",eicust.NAME as signPartNameB" +
            ",eispower.FLOW_STATUS as flowStatus"+
            " from"+
            " ES_CTT_INFO si" +
            " inner join" +
            "(" +
            " select" +
            " min(eisqm1.PKID) as PKID" +
            ",eisqm1.SUBCTT_INFO_PKID" +
            ",eisqm1.STAGE_NO" +
            ",eisqm1.FLOW_STATUS" +
            " from" +
            "(" +
            "    select" +
            "          pwi.PKID " +
            "         ,'3' as substlType" +
            "         ,pwi.ID " +
            "         ,pwi.SUBCTT_INFO_PKID " +
            "         ,pwi.STAGE_NO " +
            "         ,pwi.FLOW_STATUS " +
            "    from" +
            "         PROG_WORKQTY_INFO pwi" +
            "    where" +
            "         pwi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pwi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "    union all " +
            "    select" +
            "          pmi.PKID " +
            "         ,'4' as substlType" +
            "         ,pmi.ID " +
            "         ,pmi.SUBCTT_INFO_PKID " +
            "         ,pmi.STAGE_NO " +
            "         ,pmi.FLOW_STATUS " +
            "    from" +
            "         PROG_MATQTY_INFO pmi" +
            "    where" +
            "         pmi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pmi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            ")eisqm1" +
            ",(" +
            "    select" +
            "          pwi.PKID " +
            "         ,'3' as substlType" +
            "         ,pwi.ID " +
            "         ,pwi.SUBCTT_INFO_PKID " +
            "         ,pwi.STAGE_NO " +
            "         ,pwi.FLOW_STATUS " +
            "    from" +
            "         PROG_WORKQTY_INFO pwi" +
            "    where" +
            "         pwi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pwi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            "    union all " +
            "    select" +
            "          pmi.PKID " +
            "         ,'4' as substlType" +
            "         ,pmi.ID " +
            "         ,pmi.SUBCTT_INFO_PKID " +
            "         ,pmi.STAGE_NO " +
            "         ,pmi.FLOW_STATUS " +
            "    from" +
            "         PROG_MATQTY_INFO pmi" +
            "    where" +
            "         pmi.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubInfoPkid}),'%')) " +
            "    and " +
            "         pmi.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%')) " +
            ")eisqm2" +
            " where" +
            " eisqm1.substlType!=eisqm2.substlType" +
            " and" +
            " eisqm1.SUBCTT_INFO_PKID=eisqm2.SUBCTT_INFO_PKID" +
            " and" +
            " eisqm1.STAGE_NO=eisqm2.STAGE_NO" +
            " and" +
            " (eisqm1.FLOW_STATUS='2' and eisqm2.FLOW_STATUS='2')" +
            " group by eisqm1.SUBCTT_INFO_PKID,eisqm1.STAGE_NO,eisqm1.FLOW_STATUS" +
            ")eispower" +
            " on"+
            " si.PKID=eispower.SUBCTT_INFO_PKID" +
            " left outer join" +
            " ES_INIT_CUST eicust" +
            " on" +
            " si.SIGN_PART_B=eicust.PKID" +
            " where" +
            " si.PARENT_PKID = #{strParentPkid}" +
            " order by" +
            " eispower.SUBCTT_INFO_PKID,eispower.STAGE_NO")
    List<ProgSubstlInfoShow> getFormingProgSubstlInfoShowList(@Param("strParentPkid") String strParentPkid,
                                                              @Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                              @Param("strStageNo") String strStageNo);

    @Select("select eispower.PKID as pkid" +
            ",eispower.ID as id" +
            ",eispower.substlType as stlType" +
            ",eispower.SUBCTT_INFO_PKID as stlPkid" +
            ",eispower.STAGE_NO as stageNo" +
            ",eispower.ATTACHMENT as attachment" +
            ",si.NAME as stlName" +
            ",eicust.NAME as signPartNameB"+
            ",eispower.NOTE as note" +
            ",eispower.END_FLAG as endFlag" +
            ",eispower.DELETED_FLAG as deletedFlag" +
            ",eispower.CREATED_BY as createdBy" +
            ",eispower.CREATED_DATE as createdDate"+
            ",eispower.UPDATED_BY as lastUpdBy" +
            ",eispower.UPDATED_TIME as lastUpdDate" +
            ",eispower.POWER_TYPE as powerType" +
            ",eispower.POWER_PKID as powerPkid" +
            ",eispower.POWER_STAGE_NO as powerStageNo" +
            ",eispower.FLOW_STATUS as flowStatus" +
            ",eispower.FLOW_STATUS_REMARK as flowStatusRemark" +
            ",eispower.SPARE_FIELD as spareField"+
            " from ES_CTT_INFO si inner join"+
            " (select eis.PKID" +
            ",eis.ID" +
            ",eis.substlType" +
            ",eis.SUBCTT_INFO_PKID" +
            ",eis.STAGE_NO" +
            ",eis.ATTACHMENT" +
            ",eis.NOTE" +
            ",eis.END_FLAG" +
            ",eis.DELETED_FLAG" +
            ",eis.CREATED_BY" +
            ",eis.CREATED_DATE"+
            ",eis.UPDATED_BY" +
            ",eis.UPDATED_TIME" +
            ",eis.POWER_TYPE" +
            ",eis.POWER_PKID" +
            ",eis.STAGE_NO AS POWER_STAGE_NO" +
            ",eis.FLOW_STATUS" +
            ",eis.FLOW_STATUS_REMARK" +
            ",eis.SPARE_FIELD"+
            " from" +
            " ES_INIT_STL eis" +
            " where eis.SUBCTT_INFO_PKID like CONCAT('%',CONCAT(trim(#{strSubcttInfoPkid}),'%'))" +
            " and eis.STAGE_NO like CONCAT('%',CONCAT(trim(#{strStageNo}),'%'))" +
            " and eis.substlType='5'" +
            " and eis.FLOW_STATUS='3'" +
            " )eispower" +
            " on si.PKID=eispower.SUBCTT_INFO_PKID" +
            " left outer join ES_INIT_CUST eicust" +
            " on si.SIGN_PART_B=eicust.PKID" +
            " where si.PARENT_PKID = #{strParentPkid}" +
            " order by eispower.FLOW_STATUS£¬eispower.STAGE_NO desc£¬eispower.ID asc")
    List<ProgSubstlInfoShow> getFormedProgSubstlInfoShowList(@Param("strParentPkid") String strParentPkid,
                                                             @Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                             @Param("strStageNo") String strStageNo);
}