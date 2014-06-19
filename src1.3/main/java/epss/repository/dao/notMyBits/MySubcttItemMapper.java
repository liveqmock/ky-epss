package epss.repository.dao.notMyBits;

import org.apache.ibatis.annotations.Param;
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
public interface MySubcttItemMapper {
    @Update("Update SUBCTT_ITEM set ORDERID=ORDERID+1 where ORDERID >= #{intOrderid}"+
            " and SUBCTT_INFO_PKID = #{strSubcttInfoPkid}"+
            " and PARENT_PKID     = #{strParentPkid}"+
            " and GRADE           = #{intGrade}")
    void setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                           @Param("strParentPkid") String strParentPkid,
                                                           @Param("intGrade") Integer intGrade);

    @Update("Update SUBCTT_ITEM set ORDERID=ORDERID-1 where ORDERID >= #{intOrderid}"+
            " and SUBCTT_INFO_PKID = #{strSubcttInfoPkid}"+
            " and PARENT_PKID      = #{strParentPkid}"+
            " and GRADE            = #{intGrade}")
    void setOrderidSubOneByInfoPkidAndParentPkidAndGrade(@Param("strSubcttInfoPkid") String strSubcttInfoPkid,
                                                          @Param("strParentPkid") String strParentPkid,
                                                          @Param("intGrade") Integer intGrade);
}
