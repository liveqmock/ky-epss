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
public interface MyTkcttItemMapper {
    @Update("Update TKCTT_ITEM set ORDERID=ORDERID+1 where ORDERID >= #{intOrderid}"+
            " and TKCTT_INFO_PKID = #{strTkcttInfoPkid}"+
            " and PARENT_PKID     = #{strParentPkid}"+
            " and GRADE           = #{intGrade}")
    void setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(@Param("strTkcttInfoPkid") String strTkcttInfoPkid,
                                                           @Param("strParentPkid") String strParentPkid,
                                                           @Param("intGrade") Integer intGrade,
														   @Param("intOrderid") Integer intOrderid);

    @Update("Update TKCTT_ITEM set ORDERID=ORDERID-1 where ORDERID >= #{intOrderid}"+
            " and TKCTT_INFO_PKID = #{strTkcttInfoPkid}"+
            " and PARENT_PKID     = #{strParentPkid}"+
            " and GRADE           = #{intGrade}")
    void setOrderidSubOneByInfoPkidAndParentPkidAndGrade(@Param("strTkcttInfoPkid") String strTkcttInfoPkid,
                                                          @Param("strParentPkid") String strParentPkid,
                                                          @Param("intGrade") Integer intGrade);
}
