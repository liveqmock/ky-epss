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
public interface CttInfoMapper {
    @Select("select max(id) from ES_CTT_INFO where ctt_type = #{strCttType}")
    String getStrMaxCttId(@Param("strCttType") String strCttType);

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
                 " eip.STATUS_FLAG = #{strStatusFlag}" +
            " order by eic.NAME ")
    List<CttInfoShow> getCttInfoListByCttType_Status(@Param("strCttType") String strCttType,
                                                     @Param("strStatusFlag") String strStatusFlag);
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
                " eic.PARENT_PKID = #{strParentPkid}" +
            " and" +
                " eip.STATUS_FLAG = #{strStatusFlag}" +
            " order by " +
                " eic.NAME ")
    List<CttInfoShow> getCttInfoListByCttType_ParentPkid_Status(@Param("strCttType") String strCttType,
                                                                @Param("strParentPkid") String strParentPkid,
                                                                @Param("strStatusFlag") String strStatusFlag);

}
