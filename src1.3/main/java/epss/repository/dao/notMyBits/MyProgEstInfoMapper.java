package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.ProgEstInfoShow;
import epss.repository.model.model_show.TkcttInfoShow;
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
public interface MyProgEstInfoMapper {
    @Select("select max(ID) from PROG_EST_INFO")
    String getStrMaxProgEstInfoId();

    List<ProgEstInfoShow> getProgEstInfoListByFlowStatusBegin_End(ProgEstInfoShow progEstInfoShowPara);

    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_EST_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{strTkcttInfoPkid}" +
            " and " +
            "    FLOW_STATUS = #{strFlowStatus}")
    String getLatestStageNo(@Param("strTkcttInfoPkid") String strTkcttInfoPkid,
                            @Param("strFlowStatus") String strFlowStatus);

    @Select("select max(STAGE_NO)" +
            " from " +
            "     PROG_EST_INFO" +
            " where " +
            "     TKCTT_INFO_PKID = #{strTkcttInfoPkid}" +
            " and " +
            "     STAGE_NO <= #{strEndStageNo}" +
            " and " +
            "     FLOW_STATUS=#{strFlowStatus}")
    String getLatestStageNoByEndStage(@Param("strTkcttInfoPkid") String strTkcttInfoPkid,
                                      @Param("strEndStageNo") String strEndStageNo,
                                      @Param("strFlowStatus") String strFlowStatus);
    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_EST_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{tkCttInfoPkid}")
    String getMaxStageNo(@Param("tkCttInfoPkid") String tkCttInfoPkid);

    @Select(" select max(FLOW_STATUS)" +
            " from " +
            "    PROG_EST_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{tkCttInfoPkid}" +
            " and " +
            "    STAGE_NO = #{strStaQtyMaxStageNo}")
    String getFlowStatus(@Param("tkCttInfoPkid") String tkCttInfoPkid,
                         @Param("strStaQtyMaxStageNo") String strStaQtyMaxStageNo);
}