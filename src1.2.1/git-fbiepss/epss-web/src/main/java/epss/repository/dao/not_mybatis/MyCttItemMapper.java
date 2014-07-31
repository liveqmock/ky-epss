package epss.repository.dao.not_mybatis;

import epss.repository.model.EsCttItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyCttItemMapper {
    @Update("Update ES_CTT_ITEM set ORDERID=ORDERID+1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidPlusOneByNode(@Param("strBelongToType") String strBelongToType,
                                          @Param("strBelongToPkid") String strBelongToPkid,
                                          @Param("strParentPkid") String strParentPkid,
                                          @Param("intGrade") Integer intGrade,
                                          @Param("intOrderid") Integer intOrderid);

    @Update("Update ES_CTT_ITEM set ORDERID=ORDERID-1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidSubOneByNode(@Param("strBelongToType") String strBelongToType,
                                         @Param("strBelongToPkid") String strBelongToPkid,
                                         @Param("strParentPkid") String strParentPkid,
                                         @Param("intGrade") Integer intGrade,
                                         @Param("intOrderid") Integer intOrderid);

    @Update("Update ES_CTT_ITEM set PKID = #{strOldPkid} where PKID = #{strNewPkid}")
    void updateRecordPkid(@Param("strNewPkid") String strNewPkid,
                          @Param("strOldPkid") String strOldPkid);

    @Select("select * from ES_CTT_ITEM where"+
            "     BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}"+
            " and ORDERID             = #{intOrderid}")
    EsCttItem getEsItemHieRelapByBusinessPk(@Param("strBelongToType") String strBelongToType,
                                            @Param("strBelongToPkid") String strBelongToPkid,
                                            @Param("strParentPkid") String strParentPkid,
                                            @Param("intGrade") Integer intGrade,
                                            @Param("intOrderid") Integer intOrderid);
}
