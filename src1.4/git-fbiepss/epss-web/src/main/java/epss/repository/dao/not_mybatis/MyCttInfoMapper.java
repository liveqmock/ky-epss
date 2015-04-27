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
 * Time: ����8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyCttInfoMapper {
    List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShow);

    @Select("select max(id) from CTT_INFO where ctt_type = #{strCttType}")
    String getStrMaxCttId(@Param("strCttType") String strCttType);

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

    @Select(" select" +
            "    t.PKID as pkid," +
            "    t.CTT_TYPE as cttType," +
            "    t.PARENT_PKID as parentPkid," +
            "    t.ID as id," +
            "    t.NAME as name," +
            "    t.REMARK as remark," +
            "    t.CREATED_BY as createdBy," +
            "    (select NAME from oper where PKID=t.created_by) as createdByName," +
            "    t.CREATED_TIME as createdTime " +
            " from" +
            "    CTT_INFO t" +
            " where" +
            "    t.parent_pkid = #{parentPkid}" +
            " and" +
            "    t.NAME like concat(concat('%',(case when #{tkcttInfoName} = '' then t.NAME else #{tkcttInfoName} end)),'%')" +
            " order by" +
            "    t.name")
    List<CttInfoShow> selectRecordsFromCtt(@Param("parentPkid") String parentPkidPara,
                                           @Param("tkcttInfoName") String tkcttInfoNamePara);
}
