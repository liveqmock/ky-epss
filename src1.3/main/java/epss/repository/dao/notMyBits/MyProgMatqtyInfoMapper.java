package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.ProgMatqtyInfoShow;
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
public interface MyProgMatqtyInfoMapper {
    @Select("select max(ID) from PROG_MATQTY_INFO")
    String getStrMaxProgMatqtyInfoId();

    List<ProgMatqtyInfoShow> getProgMatqtyInfoListByFlowStatusBegin_End(ProgMatqtyInfoShow progMatqtyInfoShowPara);

    @Select(" select max(STAGE_NO)" +
            " from " +
            "    PROG_MATQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{strSubcttInfoPkid}" +
            " and " +
            "    FLOW_STATUS = #{strFlowStatus}")
    String getLatestStageNo(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                            @Param("strFlowStatus") String strFlowStatus);

    @Select("select max(STAGE_NO)" +
            " from " +
            "     PROG_MATQTY_INFO" +
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
            "    PROG_MATQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{subcttInfoPkid}")
    String getMaxStageNo(@Param("subcttInfoPkid") String subcttInfoPkid);

    @Select(" select max(FLOW_STATUS)" +
            " from " +
            "    PROG_MATQTY_INFO" +
            " where" +
            "    SUBCTT_INFO_PKID = #{subcttInfoPkid}" +
            " and " +
            "    STAGE_NO = #{materialMaxPeriod}")
    String getFlowStatus(@Param("subcttInfoPkid") String subcttInfoPkid,
                         @Param("materialMaxPeriod") String materialMaxPeriod);

}