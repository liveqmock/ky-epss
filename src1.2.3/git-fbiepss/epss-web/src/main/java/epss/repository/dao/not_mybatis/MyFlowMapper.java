package epss.repository.dao.not_mybatis;

import epss.repository.model.FlowCtrlHis;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.model.EsInitStl;
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
public interface MyFlowMapper {
    List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShow);
    List<ProgInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgInfoShow progInfoShow);
    
    @Select(" select" +
                 " eic.PKID" +
                 ",eic.NAME" +
            " from" +
                 " ES_CTT_INFO eic" +
            " where" +
                 " eic.CTT_TYPE = #{strCttType}" +
            " and" +
                 " eic.FLOW_STATUS = #{strStatusFlag}")
    List<CttInfoShow> getCttInfoListByCttType_Status(@Param("strCttType") String strCttType,
                                                     @Param("strStatusFlag") String strStatusFlag);

    List<ProgInfoShow> selectTkcttStlSMByStatusFlagBegin_End(ProgInfoShow progInfoShowPara);
    
    @Select("select distinct" +
            " eispower.PKID as pkid" +
            ",eispower.ID" +
            ",eispower.STL_TYPE as stlType" +
            ",eispower.STL_PKID as stlPkid" +
            ",eispower.PERIOD_NO as periodNo" +
            ",ecinfo.NAME as stlName" +
            ",eicust.NAME as signPartBName" +
            ",eispower.FLOW_STATUS as flowStatus"+
        " from"+
            " ES_CTT_INFO ecinfo" +
        " inner join" +
            "(" +
                 "select" +
                     " eis.PKID " +
                     ",eis.ID " +
                     ",eis.STL_TYPE " +
                     ",eis.STL_PKID " +
                     ",eis.PERIOD_NO " +
                     ",eis.FLOW_STATUS " +
                 " from" +
                     " ES_INIT_STL eis" +
                 " where" +
                     " eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
                     " and eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
                     " and (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
            ")eispower" +
         " on"+
            " ecinfo.PKID=eispower.STL_PKID" +
        " left outer join" +
            " ES_INIT_CUST eicust" +
        " on" +
            " ecinfo.SIGN_PART_B=eicust.PKID" +
        " where" +
            " ecinfo.PARENT_PKID = #{strParentPkid}" +
        " order by" +
            " eispower.STL_PKID,eispower.PERIOD_NO")
    List<ProgInfoShow> selectNotFormEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
                                                     @Param("strStlPkid") String strStlPkid,
                                                     @Param("strPeriodNo") String strPeriodNo);

    @Select("select" +
                " eispower.PKID as pkid" +
            " from"+
                " ES_CTT_INFO ecinfo" +
            " inner join" +
                "(" +
                    " select" +
                        " eisqm1.PKID" +
                        ",eisqm1.STL_PKID" +
                        ",eisqm1.PERIOD_NO" +
                        ",eisqm1.FLOW_STATUS" +
                    " from" +
                        "(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eis.FLOW_STATUS " +
                            " from" +
                                " ES_INIT_STL eis" +
                            " where" +
                                " eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
                            " and" +
                                " eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
                            " and" +
                                " (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
                        ")eisqm1" +
                        ",(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eis.FLOW_STATUS " +
                            " from" +
                                " ES_INIT_STL eis" +
                            " where" +
                                " eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
                            " and" +
                                " eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
                            " and" +
                                " (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
                        ")eisqm2" +
                    " where" +
                        " eisqm1.STL_TYPE!=eisqm2.STL_TYPE" +
                    " and" +
                        " eisqm1.STL_PKID=eisqm2.STL_PKID" +
                    " and" +
                        " eisqm1.PERIOD_NO=eisqm2.PERIOD_NO" +
                    " and" +
                        " (eisqm1.FLOW_STATUS='2' and eisqm2.FLOW_STATUS='2')" +
                ")eispower" +
            " on"+
                " ecinfo.PKID=eispower.STL_PKID" +
            " where" +
                " ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by" +
                " eispower.STL_PKID,eispower.PERIOD_NO")
    List<ProgInfoShow> selectFormPreEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
                                                     @Param("strStlPkid") String strStlPkid,
                                                     @Param("strPeriodNo") String strPeriodNo);

    @Select("select distinct" +
                " eispower.PKID as pkid" +
                ",eispower.STL_PKID as stlPkid" +
                ",eispower.PERIOD_NO as periodNo" +
                ",ecinfo.NAME as stlName" +
                ",eicust.NAME as signPartBName" +
                ",eispower.FLOW_STATUS as flowStatus"+
            " from"+
                " ES_CTT_INFO ecinfo" +
            " inner join" +
                "(" +
                    " select" +
                        " min(eisqm1.PKID) as PKID" +
                        ",eisqm1.STL_PKID" +
                        ",eisqm1.PERIOD_NO" +
                        ",eisqm1.FLOW_STATUS" +
                    " from" +
                        "(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eis.FLOW_STATUS " +
                            " from" +
                                " ES_INIT_STL eis" +
                            " where" +
                                " eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
                            " and" +
                                " eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
                            " and" +
                            " (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
                        ")eisqm1" +
                        ",(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eis.FLOW_STATUS " +
                            " from" +
                                " ES_INIT_STL eis" +
                            " where" +
                                " eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
                            " and" +
                                " eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
                            " and" +
                                " (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
                        ")eisqm2" +
                    " where" +
                        " eisqm1.STL_TYPE!=eisqm2.STL_TYPE" +
                    " and" +
                        " eisqm1.STL_PKID=eisqm2.STL_PKID" +
                    " and" +
                        " eisqm1.PERIOD_NO=eisqm2.PERIOD_NO" +
                    " and" +
                        " (eisqm1.FLOW_STATUS='2' and eisqm2.FLOW_STATUS='2')" +
                    " group by eisqm1.STL_PKID,eisqm1.PERIOD_NO,eisqm1.FLOW_STATUS" +
                ")eispower" +
            " on"+
                " ecinfo.PKID=eispower.STL_PKID" +
            " left outer join" +
                " ES_INIT_CUST eicust" +
            " on" +
                " ecinfo.SIGN_PART_B=eicust.PKID" +
            " where" +
                " ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by" +
                " eispower.STL_PKID,eispower.PERIOD_NO")
    List<ProgInfoShow> selectFormingEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
                                                     @Param("strStlPkid") String strStlPkid,
                                                     @Param("strPeriodNo") String strPeriodNo);

    @Select("select eispower.PKID as pkid" +
            ",eispower.ID as id" +
            ",eispower.STL_TYPE as stlType" +
            ",eispower.STL_PKID as stlPkid" +
            ",eispower.PERIOD_NO as periodNo" +
            ",eispower.ATTACHMENT as attachment" +
            ",ecinfo.NAME as stlName" +
            ",eicust.NAME as signPartBName"+
            ",eispower.NOTE as note" +
            ",eispower.END_FLAG as endFlag" +
            ",eispower.DELETED_FLAG as deletedFlag" +
            ",eispower.CREATED_BY as createdBy" +
            ",eispower.CREATED_DATE as createdDate"+
            ",eispower.LAST_UPD_BY as lastUpdBy" +
            ",eispower.LAST_UPD_DATE as lastUpdDate" +
            ",eispower.FLOW_STATUS as flowStatus" +
            ",eispower.FLOW_STATUS_REASON as preStatusFlag" +
            " from ES_CTT_INFO ecinfo inner join"+
            " (select eis.PKID" +
                ",eis.ID" +
                ",eis.STL_TYPE" +
                ",eis.STL_PKID" +
                ",eis.PERIOD_NO" +
                ",eis.ATTACHMENT" +
                ",eis.NOTE" +
                ",eis.END_FLAG" +
                ",eis.DELETED_FLAG" +
                ",eis.CREATED_BY" +
                ",eis.CREATED_DATE"+
                ",eis.LAST_UPD_BY" +
                ",eis.LAST_UPD_DATE" +
                ",eis.FLOW_STATUS" +
                ",eis.FLOW_STATUS_REASON" +
                " from" +
                " ES_INIT_STL eis" +
                " where eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
                " and eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
                " and eis.STL_TYPE='5'" +
                " and eis.FLOW_STATUS>='2'" +
            " )eispower" +
            " on ecinfo.PKID=eispower.STL_PKID" +
            " left outer join ES_INIT_CUST eicust" +
            " on ecinfo.SIGN_PART_B=eicust.PKID" +
            " where ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by eispower.FLOW_STATUS£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
    List<ProgInfoShow> selectFormedEsInitSubcttStlPList(@Param("strParentPkid") String strParentPkid,
                                                        @Param("strStlPkid") String strStlPkid,
                                                        @Param("strPeriodNo") String strPeriodNo);

    @Select("select eispower.PKID as pkid" +
                ",eispower.ID as id" +
                ",eispower.STL_TYPE as stlType" +
                ",eispower.STL_PKID as stlPkid" +
                ",eispower.PERIOD_NO as periodNo" +
                ",eispower.ATTACHMENT as attachment" +
                ",ecinfo.NAME as stlName" +
                ",eicust.NAME as signPartBName"+
                ",eispower.NOTE as note" +
                ",eispower.END_FLAG as endFlag" +
                ",eispower.DELETED_FLAG as deletedFlag" +
                ",eispower.CREATED_BY as createdBy" +
                ",eispower.CREATED_DATE as createdDate"+
                ",eispower.LAST_UPD_BY as lastUpdBy" +
                ",eispower.LAST_UPD_DATE as lastUpdDate" +
                ",eispower.FLOW_STATUS as flowStatus" +
                ",eispower.FLOW_STATUS_REASON as preStatusFlag" +
            " from ES_CTT_INFO ecinfo inner join"+
                " (select eis.PKID" +
                ",eis.ID" +
                ",eis.STL_TYPE" +
                ",eis.STL_PKID" +
                ",eis.PERIOD_NO" +
                ",eis.ATTACHMENT" +
                ",eis.NOTE" +
                ",eis.END_FLAG" +
                ",eis.DELETED_FLAG" +
                ",eis.CREATED_BY" +
                ",eis.CREATED_DATE"+
                ",eis.LAST_UPD_BY" +
                ",eis.LAST_UPD_DATE" +
                ",eis.FLOW_STATUS" +
                ",eis.FLOW_STATUS_REASON" +
                " from" +
                " ES_INIT_STL eis" +
                " where eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
                " and eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
                " and eis.STL_TYPE='5'" +
                " and eis.FLOW_STATUS>='3'"+
                " )eispower" +
            " on ecinfo.PKID=eispower.STL_PKID" +
            " left outer join ES_INIT_CUST eicust" +
            " on ecinfo.SIGN_PART_B=eicust.PKID" +
            " where ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by eispower.FLOW_STATUS£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
    List<ProgInfoShow> getFormedAfterEsInitSubcttStlPList(@Param("strParentPkid") String strParentPkid,
                                                          @Param("strStlPkid") String strStlPkid,
                                                          @Param("strPeriodNo") String strPeriodNo);

    @Select("select count(1)" +
            " from ES_CTT_INFO" +
            " where CTT_TYPE = #{strCttType}" +
            " and PARENT_PKID = #{strParentPkid}")
    Integer getChildrenOfThisRecordInEsInitCtt(@Param("strCttType") String strCttType,
                                               @Param("strParentPkid") String strParentPkid);

    @Select("select"+
                " PKID as pkid" +
                ",ID as id" +
                ",STL_TYPE as stlType" +
                ",STL_PKID as subcttPkid" +
                ",PERIOD_NO as periodNo" +
                ",NOTE as note" +
                ",ATTACHMENT as attachment" +
            " from" +
                " ES_INIT_STL"+
            " where" +
                " STL_PKID = #{strStlPkid}" +
            " and" +
                " (STL_TYPE='3' or STL_TYPE='4')")
    List<EsInitStl> selectIsUsedInQMPBySubcttPkid(@Param("strStlPkid") String strStlPkid);

    @Select("select max(PERIOD_NO)" +
            " from ES_INIT_STL" +
            " where STL_TYPE = #{strStlType}" +
            " and STL_PKID = #{strStlPkid}" +
            " and FLOW_STATUS='2'")
    String getLatestDoubleCkeckedPeriodNo(@Param("strStlType") String strStlType,
                                          @Param("strStlPkid") String strStlPkid);
    @Select("select max(PERIOD_NO)" +
            " from ES_INIT_STL" +
            " where STL_TYPE = #{strStlType}" +
            " and STL_PKID = #{strStlPkid}" +
            " and FLOW_STATUS='3'")
    String getLatestApprovedPeriodNo(@Param("strStlType") String strStlType,
                                     @Param("strStlPkid") String strStlPkid);

    @Select("select max(PERIOD_NO)" +
            " from ES_INIT_STL" +
            " where STL_TYPE = #{strStlType}" +
            " and STL_PKID = #{strStlPkid}" +
            " and PERIOD_NO <= #{strEndPeriodNo}" +
            " and FLOW_STATUS='3'")
    String getLatestApprovedPeriodNoByEndPeriod(@Param("strStlType") String strStlType,
                                                @Param("strStlPkid") String strStlPkid,
                                                @Param("strEndPeriodNo") String strEndPeriodNo);

    @Select("select max(s.period_no)"+
            " from es_init_stl s"+
            " where"+
            " and (s.FLOW_STATUS <='2' or s.FLOW_STATUS is null)"+
            " where s.stl_type=#{stlType}"+
            " and s.stl_pkid=#{subCttPkid}")
    String getMaxPeriodNo(@Param("stlType") String stlType,
                          @Param("subCttPkid") String subCttPkid);

    @Select("select " +
            "     p.FLOW_STATUS" +
            " from " +
            "     es_init_stl s" +
            " where" +
            " and s.stl_type=#{stlType}" +
            " and s.stl_pkid=#{subCttPkid}" +
            " and s.period_no=#{periodNo}")
    String getStatusFlag(@Param("stlType") String stlType,
                         @Param("subCttPkid") String subCttPkid,
                         @Param("periodNo") String periodNo);

    @Select("select DISTINCT" +
            "  eipwai.power_type as powerType, " +
            "  eipwai.FLOW_STATUS as flowStatus,  " +
            "  eipwai.note as note, " +
            "  eipwai.created_by as createdBy, " +
            "  eipwai.created_date as createdDate    " +
            "from " +
            "  ES_INIT_POWER_HIS eipwai  " +
            "inner join " +
            "( " +
            "select  " +
            "  max(created_date) as maxDate, " +
            "  power_type, " +
            "  FLOW_STATUS " +
            "from " +
            "  ES_INIT_POWER_HIS  " +
            "where  " +
            "  power_type in ('3','4','5') " +
            "  and power_pkid=#{powerPkid} " +
            "  and period_no=#{periodNo} " +
            "group by power_type,FLOW_STATUS " +
            ")maxcreated_date  " +
            "on   " +
            "  eipwai.power_type=maxcreated_date.power_type " +
            "  and eipwai.FLOW_STATUS=maxcreated_date.FLOW_STATUS  " +
            "  and NVL(eipwai.created_date,'NOTNULL')=NVL(maxcreated_date.maxDate,'NOTNULL')  "+
            "where " +
            "  eipwai.power_pkid=#{powerPkid} " +
            "  and eipwai.period_no=#{periodNo} " +
            "order by eipwai.power_type,eipwai.FLOW_STATUS")
    List<FlowCtrlHis> getMngFromPowerHisForSubcttStlList(@Param("powerPkid") String powerPkid,
                                                            @Param("periodNo") String periodNo);
}
