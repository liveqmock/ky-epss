package epss.repository.dao.common;

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
public interface FlowMapper {
    List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShow);
    List<ProgInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgInfoShow progInfoShow);
    
    @Select(" select" +
                 " eic.PKID" +
                 ",eic.NAME" +
            " from" +
                 " ES_CTT_INFO eic" +
            " inner join" +
                 " ES_INIT_POWER eip" +
            " on" +
                 " eic.CTT_TYPE=eip.POWER_TYPE" +
            " and" +
                 " eic.PKID=eip.POWER_PKID" +
            " where" +
                 " eic.CTT_TYPE = #{strCttType}" +
            " and" +
                 " eip.STATUS_FLAG = #{strStatusFlag}")
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
            ",eispower.STATUS_FLAG as statusFlag"+
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
                     ",eip.STATUS_FLAG " +
                 " from" +
                     " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                 " on" +
                     " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO " +
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
                        ",eisqm1.STATUS_FLAG" +
                    " from" +
                        "(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eip.STATUS_FLAG " +
                            " from" +
                                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                            " on" +
                                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO " +
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
                                ",eip.STATUS_FLAG " +
                            " from" +
                                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                            " on" +
                                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO " +
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
                        " (eisqm1.STATUS_FLAG='2' and eisqm2.STATUS_FLAG='2')" +
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
                ",eispower.STATUS_FLAG as statusFlag"+
            " from"+
                " ES_CTT_INFO ecinfo" +
            " inner join" +
                "(" +
                    " select" +
                        " min(eisqm1.PKID) as PKID" +
                        ",eisqm1.STL_PKID" +
                        ",eisqm1.PERIOD_NO" +
                        ",eisqm1.STATUS_FLAG" +
                    " from" +
                        "(" +
                            "select" +
                                " eis.PKID " +
                                ",eis.ID " +
                                ",eis.STL_TYPE " +
                                ",eis.STL_PKID " +
                                ",eis.PERIOD_NO " +
                                ",eip.STATUS_FLAG " +
                            " from" +
                                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                            " on" +
                                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO " +
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
                                ",eip.STATUS_FLAG " +
                            " from" +
                                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                            " on" +
                                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO " +
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
                        " (eisqm1.STATUS_FLAG='2' and eisqm2.STATUS_FLAG='2')" +
                    " group by eisqm1.STL_PKID,eisqm1.PERIOD_NO,eisqm1.STATUS_FLAG" +
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
            ",eispower.POWER_TYPE as powerType" +
            ",eispower.POWER_PKID as powerPkid" +
            ",eispower.POWER_PERIOD_NO as powerPeriodNo" +
            ",eispower.STATUS_FLAG as statusFlag" +
            ",eispower.PRE_STATUS_FLAG as preStatusFlag" +
            ",eispower.SPARE_FIELD as spareField"+
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
                ",eip.POWER_TYPE" +
                ",eip.POWER_PKID" +
                ",eip.PERIOD_NO AS POWER_PERIOD_NO" +
                ",eip.STATUS_FLAG" +
                ",eip.PRE_STATUS_FLAG" +
                ",eip.SPARE_FIELD"+
                " from" +
                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                " on" +
                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO" +
                " where eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
                " and eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
                " and eis.STL_TYPE='5'" +
                " and eip.STATUS_FLAG>='2'" +
            " )eispower" +
            " on ecinfo.PKID=eispower.STL_PKID" +
            " left outer join ES_INIT_CUST eicust" +
            " on ecinfo.SIGN_PART_B=eicust.PKID" +
            " where ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by eispower.STATUS_FLAG£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
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
                ",eispower.POWER_TYPE as powerType" +
                ",eispower.POWER_PKID as powerPkid" +
                ",eispower.POWER_PERIOD_NO as powerPeriodNo" +
                ",eispower.STATUS_FLAG as statusFlag" +
                ",eispower.PRE_STATUS_FLAG as preStatusFlag" +
                ",eispower.SPARE_FIELD as spareField"+
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
                ",eip.POWER_TYPE" +
                ",eip.POWER_PKID" +
                ",eip.PERIOD_NO AS POWER_PERIOD_NO" +
                ",eip.STATUS_FLAG" +
                ",eip.PRE_STATUS_FLAG" +
                ",eip.SPARE_FIELD"+
                " from" +
                " ES_INIT_STL eis left outer join ES_INIT_POWER eip" +
                " on" +
                " eis.STL_TYPE=eip.POWER_TYPE and eis.STL_PKID=eip.POWER_PKID and eis.PERIOD_NO=eip.PERIOD_NO" +
                " where eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
                " and eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
                " and eis.STL_TYPE='5'" +
                " )eispower" +
            " on ecinfo.PKID=eispower.STL_PKID" +
            " left outer join ES_INIT_CUST eicust" +
            " on ecinfo.SIGN_PART_B=eicust.PKID" +
            " where ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by eispower.STATUS_FLAG£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
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
            " from ES_INIT_POWER" +
            " where POWER_TYPE = #{strPowerType}" +
            " and POWER_PKID = #{strPowerPkid}" +
            " and STATUS_FLAG='2'")
    String getLatestDoubleCkeckedPeriodNo(@Param("strPowerType") String strPowerType,
                                     @Param("strPowerPkid") String strPowerPkid);
    @Select("select max(PERIOD_NO)" +
            " from ES_INIT_POWER" +
            " where POWER_TYPE = #{strPowerType}" +
            " and POWER_PKID = #{strPowerPkid}" +
            " and STATUS_FLAG='3'")
    String getLatestApprovedPeriodNo(@Param("strPowerType") String strPowerType,
                                     @Param("strPowerPkid") String strPowerPkid);

    @Select("select max(PERIOD_NO)" +
            " from ES_INIT_POWER" +
            " where POWER_TYPE = #{strPowerType}" +
            " and POWER_PKID = #{strPowerPkid}" +
            " and PERIOD_NO <= #{strEndPeriodNo}" +
            " and STATUS_FLAG='3'")
    String getLatestApprovedPeriodNoByEndPeriod(@Param("strPowerType") String strPowerType,
                                                @Param("strPowerPkid") String strPowerPkid,
                                                @Param("strEndPeriodNo") String strEndPeriodNo);

    //TODO add by yxy
    @Select("select max(s.period_no)"+
            " from es_init_stl s left outer join es_init_power p"+
            " on s.stl_pkid=p.power_pkid"+
            " and s.stl_type=p.power_type"+
            " and s.period_no=p.period_no" +
            " and (p.status_flag <='2' or p.status_flag is null)"+
            " where s.stl_type=#{stlType}"+
            " and s.stl_pkid=#{subCttPkid}")
    String getMaxPeriodNo(@Param("stlType") String stlType,
                          @Param("subCttPkid") String subCttPkid);

    @Select("select p.status_flag" +
            " from es_init_stl s,es_init_power p" +
            " where s.stl_pkid=p.power_pkid" +
            " and s.stl_type=p.power_type" +
            " and s.period_no=p.period_no" +
            " and s.stl_type=#{stlType}" +
            " and s.stl_pkid=#{subCttPkid}" +
            " and s.period_no=#{periodNo}")
    String getStatusFlag(@Param("stlType") String stlType,
                         @Param("subCttPkid") String subCttPkid,
                         @Param("periodNo") String periodNo);
}
