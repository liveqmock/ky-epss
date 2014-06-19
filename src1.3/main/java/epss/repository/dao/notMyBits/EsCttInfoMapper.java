package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.SubcttInfoShow;
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
public interface EsCttInfoMapper {
    @Select(" select" +
                 " eic.PKID" +
                 ",eic.NAME" +
            " from" +
                 " ES_CTT_INFO eic" +
            " inner join" +
                 " FLOW_CTRL fc" +
            " on" +
                 " eic.CTT_TYPE=fc.INFO_TYPE" +
            " and" +
                 " eic.PKID=fc.INFO_PKID" +
            " where" +
                 " eic.CTT_TYPE = #{strCttType}" +
            " and" +
                 " fc.FLOW_STATUS = #{strFlowStatus}" +
            " order by eic.NAME ")
    List<SubcttInfoShow> getCttInfoListByCttType_Status(@Param("strCttType") String strCttType,
                                                     @Param("strFlowStatus") String strFlowStatus);
    @Select(" select" +
                " eic.PKID" +
                ",eic.NAME" +
            " from" +
                " ES_CTT_INFO eic" +
            " inner join" +
                " FLOW_CTRL fc" +
            " on" +
                " eic.CTT_TYPE=fc.INFO_TYPE" +
            " and" +
                " eic.PKID=fc.INFO_PKID" +
            " where" +
                " eic.CTT_TYPE = #{strCttType}" +
            " and" +
                " eic.PARENT_PKID = #{strParentPkid}" +
            " and" +
                " fc.FLOW_STATUS = #{strFlowStatus}" +
            " order by " +
                " eic.NAME ")
    List<SubcttInfoShow> getCttInfoListByCttType_ParentPkid_Status(@Param("strCttType") String strCttType,
                                                                @Param("strParentPkid") String strParentPkid,
                                                                @Param("strFlowStatus") String strFlowStatus);


}
