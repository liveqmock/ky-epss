package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.ProgEstInfoShow;
import epss.repository.model.model_show.ProgMeaInfoShow;
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
public interface MyProgMeaInfoMapper {
    @Select("select max(ID) from PROG_Mea_INFO")
    String getStrMaxProgMeaInfoId();

    List<ProgMeaInfoShow> getProgMeaInfoListByFlowStatusBegin_End(ProgMeaInfoShow progEstInfoShowPara);

    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_MEA_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{strTkcttInfoPkid}" +
            " and " +
            "    FLOW_STATUS = #{strFlowStatus}")
    String getLatestStageNo(@Param("strTkcttInfoPkid") String strTkcttInfoPkid,
                            @Param("strFlowStatus") String strFlowStatus);

    @Select("select max(STAGE_NO)" +
            " from " +
            "     PROG_MEA_INFO" +
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
            "    PROG_MEA_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{tkCttInfoPkid}")
    String getMaxStageNo(@Param("tkCttInfoPkid") String tkCttInfoPkid);

    @Select(" select max(FLOW_STATUS)" +
            " from " +
            "    PROG_MEA_INFO" +
            " where" +
            "    TKCTT_INFO_PKID = #{tkCttInfoPkid}" +
            " and " +
            "    STAGE_NO = #{strMeaQtyMaxStageNo}")
    String getFlowStatus(@Param("tkCttInfoPkid") String tkCttInfoPkid,
                         @Param("strMeaQtyMaxStageNo") String strMeaQtyMaxStageNo);


}