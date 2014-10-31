package epss.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: обнГ8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MySignPartMapper {
    @Select("select max(id) from SIGN_PART")
    String strMaxCustId();
}
