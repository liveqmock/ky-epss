package epss.repository.dao.not_mybatis;

import epss.repository.model.ProgStlInfo;
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
public interface MyProgStlInfoMapper {
    @Select("select max(id) from PROG_STL_INFO where stl_type = #{strStlType}")
    String getStrMaxStlId(@Param("strStlType") String strStlType);

    List<ProgStlInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShow);

    List<ProgStlInfoShow> selectTkcttStlSMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShowPara);

    @Select("select distinct" +
            "      eispower.PKID as pkid" +
            "     ,eispower.ID" +
            "     ,eispower.STL_TYPE as stlType" +
            "     ,eispower.STL_PKID as stlPkid" +
            "     ,eispower.PERIOD_NO as periodNo" +
            "     ,ecinfo.NAME as stlName" +
            "     ,eicust.NAME as signPartBName" +
            "     ,eispower.FLOW_STATUS as flowStatus"+
            " from"+
            "     CTT_INFO ecinfo" +
            " inner join" +
            "     (" +
            "          select" +
            "               eis.PKID " +
            "              ,eis.ID " +
            "              ,eis.STL_TYPE " +
            "              ,eis.STL_PKID " +
            "              ,eis.PERIOD_NO " +
            "              ,eis.FLOW_STATUS " +
            "          from" +
            "              PROG_STL_INFO eis" +
            "          where" +
            "              eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%')) " +
            "          and " +
            "              eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%')) " +
            "          and " +
            "              (eis.STL_TYPE='3' or eis.STL_TYPE='4')" +
            "     )eispower" +
            " on"+
            "     ecinfo.PKID=eispower.STL_PKID" +
            " left outer join" +
            "     SIGN_PART eicust" +
            " on" +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where" +
            "     ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by" +
            "     eispower.STL_PKID,eispower.PERIOD_NO")
    List<ProgStlInfoShow> selectNotFormEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
                                                        @Param("strStlPkid") String strStlPkid,
                                                        @Param("strPeriodNo") String strPeriodNo);

    @Select("select" +
            " eispower.PKID as pkid" +
            " from"+
            " CTT_INFO ecinfo" +
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
            " PROG_STL_INFO eis" +
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
            " PROG_STL_INFO eis" +
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
    List<ProgStlInfoShow> selectFormPreEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
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
            " CTT_INFO ecinfo" +
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
            " PROG_STL_INFO eis" +
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
            " PROG_STL_INFO eis" +
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
            " SIGN_PART eicust" +
            " on" +
            " ecinfo.SIGN_PART_B=eicust.PKID" +
            " where" +
            " ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by" +
            " eispower.STL_PKID,eispower.PERIOD_NO")
    List<ProgStlInfoShow> selectFormingEsInitSubcttStlP(@Param("strParentPkid") String strParentPkid,
                                                        @Param("strStlPkid") String strStlPkid,
                                                        @Param("strPeriodNo") String strPeriodNo);

    @Select("select " +
            "      eispower.PKID as pkid" +
            "     ,eispower.ID as id" +
            "     ,eispower.STL_TYPE as stlType" +
            "     ,eispower.STL_PKID as stlPkid" +
            "     ,eispower.PERIOD_NO as periodNo" +
            "     ,eispower.ATTACHMENT as attachment" +
            "     ,ecinfo.NAME as stlName" +
            "     ,eicust.NAME as signPartBName"+
            "     ,eispower.REMARK as remark" +
            "     ,eispower.ARCHIVED_FLAG as archivedFlag" +
            "     ,eispower.CREATED_BY as createdBy" +
            "     ,eispower.CREATED_TIME as createdTime"+
            "     ,eispower.LAST_UPD_BY as lastUpdBy" +
            "     ,eispower.LAST_UPD_TIME as lastUpdTime" +
            "     ,eispower.FLOW_STATUS as flowStatus" +
            "     ,eispower.FLOW_STATUS_REASON as flowStatusReason" +
            " from " +
            "     CTT_INFO ecinfo " +
            " inner join"+
            "     (" +
            "         select " +
            "               eis.PKID" +
            "               ,eis.ID" +
            "               ,eis.STL_TYPE" +
            "               ,eis.STL_PKID" +
            "               ,eis.PERIOD_NO" +
            "               ,eis.ATTACHMENT" +
            "               ,eis.REMARK" +
            "               ,eis.ARCHIVED_FLAG" +
            "               ,eis.CREATED_BY" +
            "               ,eis.CREATED_TIME"+
            "               ,eis.LAST_UPD_BY" +
            "               ,eis.LAST_UPD_TIME" +
            "               ,eis.FLOW_STATUS" +
            "               ,eis.FLOW_STATUS_REASON" +
            "         from" +
            "               PROG_STL_INFO eis" +
            "         where " +
            "               eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
            "         and " +
            "               eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
            "         and " +
            "               eis.STL_TYPE='5'" +
            "         and " +
            "               eis.FLOW_STATUS>='2'" +
            "     )eispower" +
            " on " +
            "     ecinfo.PKID=eispower.STL_PKID" +
            " left outer join " +
            "     SIGN_PART eicust" +
            " on " +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where " +
            "     ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by " +
            "     eispower.FLOW_STATUS£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
    List<ProgStlInfoShow> selectFormedEsInitSubcttStlPList(@Param("strParentPkid") String strParentPkid,
                                                           @Param("strStlPkid") String strStlPkid,
                                                           @Param("strPeriodNo") String strPeriodNo);

    @Select(" select " +
            "      eispower.PKID as pkid" +
            "     ,eispower.ID as id" +
            "     ,eispower.STL_TYPE as stlType" +
            "     ,eispower.STL_PKID as stlPkid" +
            "     ,eispower.PERIOD_NO as periodNo" +
            "     ,eispower.ATTACHMENT as attachment" +
            "     ,ecinfo.NAME as stlName" +
            "     ,eicust.NAME as signPartBName"+
            "     ,eispower.REMARK as remark" +
            "     ,eispower.ARCHIVED_FLAG as archivedFlag" +
            "     ,eispower.CREATED_BY as createdBy" +
            "     ,eispower.CREATED_TIME as createdTime"+
            "     ,eispower.LAST_UPD_BY as lastUpdBy" +
            "     ,eispower.LAST_UPD_TIME as lastUpdTime" +
            "     ,eispower.FLOW_STATUS as flowStatus" +
            "     ,eispower.FLOW_STATUS_REASON as flowStatusReason" +
            " from " +
            "     CTT_INFO ecinfo " +
            " inner join"+
            "     (" +
            "         select " +
            "              eis.PKID" +
            "             ,eis.ID" +
            "             ,eis.STL_TYPE" +
            "             ,eis.STL_PKID" +
            "             ,eis.PERIOD_NO" +
            "             ,eis.ATTACHMENT" +
            "             ,eis.REMARK" +
            "             ,eis.ARCHIVED_FLAG" +
            "             ,eis.CREATED_BY" +
            "             ,eis.CREATED_TIME"+
            "             ,eis.LAST_UPD_BY" +
            "             ,eis.LAST_UPD_TIME" +
            "             ,eis.FLOW_STATUS" +
            "             ,eis.FLOW_STATUS_REASON" +
            "         from" +
            "             PROG_STL_INFO eis" +
            "         where " +
            "             eis.STL_PKID like CONCAT('%',CONCAT(trim(#{strStlPkid}),'%'))" +
            "         and " +
            "             eis.PERIOD_NO like CONCAT('%',CONCAT(trim(#{strPeriodNo}),'%'))" +
            "         and " +
            "             eis.STL_TYPE='5'" +
            "         and " +
            "             eis.FLOW_STATUS>='3'"+
            "     )eispower" +
            " on " +
            "     ecinfo.PKID=eispower.STL_PKID" +
            " left outer join " +
            "     SIGN_PART eicust" +
            " on " +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where " +
            "     ecinfo.PARENT_PKID = #{strParentPkid}" +
            " order by " +
            "     eispower.FLOW_STATUS£¬eispower.PERIOD_NO desc£¬eispower.ID asc")
    List<ProgStlInfoShow> getFormedAfterEsInitSubcttStlPList(@Param("strParentPkid") String strParentPkid,
                                                             @Param("strStlPkid") String strStlPkid,
                                                             @Param("strPeriodNo") String strPeriodNo);

    @Select("select " +
            "      max(s.period_no)"+
            " from " +
            "      PROG_STL_INFO s"+
            " where"+
            "      s.stl_type=#{stlType}"+
            " and " +
            "      s.stl_pkid=#{subCttPkid}")
    String getMaxPeriodNo(@Param("stlType") String stlType,
                          @Param("subCttPkid") String subCttPkid);

    @Select("select"+
            "      PKID as pkid" +
            "     ,ID as id" +
            "     ,STL_TYPE as stlType" +
            "     ,STL_PKID as subcttPkid" +
            "     ,PERIOD_NO as periodNo" +
            "     ,REMARK as remark" +
            "     ,ATTACHMENT as attachment" +
            " from" +
            "     PROG_STL_INFO"+
            " where" +
            "     STL_PKID = #{strStlPkid}" +
            " and" +
            "     (STL_TYPE='3' or STL_TYPE='4')")
    List<ProgStlInfo> selectIsUsedInQMPBySubcttPkid(@Param("strStlPkid") String strStlPkid);

    @Select("select " +
            "      max(PERIOD_NO)" +
            " from " +
            "      PROG_STL_INFO" +
            " where " +
            "      STL_TYPE = #{strStlType}" +
            " and " +
            "      STL_PKID = #{strStlPkid}" +
            " and " +
            "      FLOW_STATUS='2'")
    String getLatestDoubleCkeckedPeriodNo(@Param("strStlType") String strStlType,
                                          @Param("strStlPkid") String strStlPkid);
    @Select("select " +
            "      max(PERIOD_NO)" +
            " from " +
            "      PROG_STL_INFO" +
            " where " +
            "      STL_TYPE = #{strStlType}" +
            " and " +
            "      STL_PKID = #{strStlPkid}" +
            " and " +
            "      FLOW_STATUS='3'")
    String getLatestApprovedPeriodNo(@Param("strStlType") String strStlType,
                                     @Param("strStlPkid") String strStlPkid);

    @Select("select " +
            "      max(PERIOD_NO)" +
            " from " +
            "      PROG_STL_INFO" +
            " where " +
            "      STL_TYPE = #{strStlType}" +
            " and " +
            "      STL_PKID = #{strStlPkid}" +
            " and " +
            "      PERIOD_NO <= #{strEndPeriodNo}" +
            " and " +
            "      FLOW_STATUS='3'")
    String getLatestApprovedPeriodNoByEndPeriod(@Param("strStlType") String strStlType,
                                                @Param("strStlPkid") String strStlPkid,
                                                @Param("strEndPeriodNo") String strEndPeriodNo);
}
