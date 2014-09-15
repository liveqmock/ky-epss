package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.CttInfoShow;
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
public interface MyCttInfoMapper {
    List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShow);

    @Select("select max(id) from CTT_INFO where ctt_type = #{strCttType}")
    String getStrMaxCttId(@Param("strCttType") String strCttType);

    @Select(" select" +
                 " eci.PKID" +
                 ",eci.NAME" +
            " from" +
                 " CTT_INFO eci" +
            " where" +
                 " eci.CTT_TYPE = #{strCttType}" +
            " and" +
                 " eci.FLOW_STATUS = #{strFlowStatus}" +
            " order by eci.NAME ")
    List<CttInfoShow> getCttInfoListByCttType_Status(@Param("strCttType") String strCttType,
                                                     @Param("strFlowStatus") String strFlowStatus);
    @Select(" select" +
                " eci.PKID" +
                ",eci.NAME" +
            " from" +
                " CTT_INFO eci" +
            " where" +
                " eci.CTT_TYPE = #{strCttType}" +
            " and" +
                " eci.PARENT_PKID = #{strParentPkid}" +
            " and" +
                " eci.FLOW_STATUS = #{strFlowStatus}" +
            " order by " +
                " eci.NAME ")
    List<CttInfoShow> getCttInfoListByCttType_ParentPkid_Status(@Param("strCttType") String strCttType,
                                                                @Param("strParentPkid") String strParentPkid,
                                                                @Param("strFlowStatus") String strFlowStatus);

    @Select("select " +
            "      count(1)" +
            " from " +
            "      CTT_INFO" +
            " where " +
            "      CTT_TYPE = #{strCttType}" +
            " and " +
            "      PARENT_PKID = #{strParentPkid}")
    Integer getChildrenOfThisRecordInEsInitCtt(@Param("strCttType") String strCttType,
                                               @Param("strParentPkid") String strParentPkid);
}
