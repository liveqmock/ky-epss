package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.ProgWorkqtyInfoShow;
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
public interface MyProgWorkqtyInfoMapper {
    @Select("select max(ID) from PROG_WORKQTY_INFO")
    String getStrMaxProgWorkqtyInfoId();

    List<ProgWorkqtyInfoShow> getProgWorkqtyInfoListByFlowStatusBegin_End(ProgWorkqtyInfoShow progWorkqtyInfoShowPara);

    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_WORKQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and " +
            "    FLOW_STATUS = #{strFlowStatus}")
    String getLatestStageNo(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                            @Param("strFlowStatus") String strFlowStatus);

    @Select("select max(STAGE_NO)" +
            " from " +
            "     PROG_WORKQTY_INFO" +
            " where " +
            "     SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and " +
            "     STAGE_NO <= #{strEndStageNo}" +
            " and " +
            "     FLOW_STATUS=#{strFlowStatus}")
    String getLatestStageNoByEndStage(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                      @Param("strEndStageNo") String strEndStageNo,
                                      @Param("strFlowStatus") String strFlowStatus);

    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_WORKQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{subcttInfoPkid}")
    String getMaxStageNo(@Param("subcttInfoPkid") String subcttInfoPkid);

    @Select(" select max(FLOW_STATUS)" +
            " from " +
            "    PROG_WORKQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{subcttInfoPkid}" +
            " and " +
            "    STAGE_NO = #{quantityMaxPeriod}")
    String getFlowStatus(@Param("subcttInfoPkid") String subcttInfoPkid,
                         @Param("quantityMaxPeriod") String quantityMaxPeriod);
 @Select(" select PKID              as pkid" +
                   ",ID                 as id" +
                   ",SUBCTT_INFO_PKID   as subcttInfoPkid" +
                   ",STAGE_NO           as stageNo" +
                   ",ARCHIVED_FLAG      as archivedFlag" +
                   ",ORIGIN_FLAG        as originFlag" +
                   ",FLOW_STATUS        as flowStatus" +
                   ",FLOW_STATUS_REMARK as flowStatusRemark" +
                   ",CREATED_BY         as createdBy" +
                   ",CREATED_TIME       as createdTime" +
                   ",UPDATED_BY         as updatedBy" +
                   ",UPDATED_TIME       as updatedTime" +
                   ",REC_VERSION        as recVersion" +
                   ",ATTACHMENT         as attachment" +
                   ",REMARK             as remark" +
                   ",TID                as tid" +
            " from " +
            "     PROG_WORKQTY_INFO" +
            " where " +
            "     SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and" +
            "     STAGE_NO =" +
            "(select max(STAGE_NO)" +
            " from " +
            "     PROG_WORKQTY_INFO" +
            " where " +
            "     SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and " +
            "     STAGE_NO <= #{strEndStageNo}" +
            " and " +
            "     FLOW_STATUS=#{strFlowStatus})")
    List<ProgWorkqtyInfoShow> getLatestStageProgWorkqtyInfoShowList(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                                    @Param("strEndStageNo") String strEndStageNo,
                                                                    @Param("strFlowStatus") String strFlowStatus);

    @Select(" select PKID              as pkid" +
                   ",ID                 as id" +
                   ",SUBCTT_INFO_PKID   as subcttInfoPkid" +
                   ",STAGE_NO           as stageNo" +
                   ",ARCHIVED_FLAG      as archivedFlag" +
                   ",ORIGIN_FLAG        as originFlag" +
                   ",FLOW_STATUS        as flowStatus" +
                   ",FLOW_STATUS_REMARK as flowStatusRemark" +
                   ",CREATED_BY         as createdBy" +
                   ",CREATED_TIME       as createdTime" +
                   ",UPDATED_BY         as updatedBy" +
                   ",UPDATED_TIME       as updatedTime" +
                   ",REC_VERSION        as recVersion" +
                   ",ATTACHMENT         as attachment" +
                   ",REMARK             as remark" +
                   ",TID                as tid" +
            " from " +
            "     PROG_WORKQTY_INFO" +
            " where " +
            "     SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and " +
            "     STAGE_NO >= #{strBeginStageNo}" +
            " and " +
            "     STAGE_NO <= #{strEndStageNo}")
    List<ProgWorkqtyInfoShow> getSgageListByStageBegin_End(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                           @Param("strBeginStageNo") String strBeginStageNo,
                                                           @Param("strEndStageNo") String strEndStageNo);
}