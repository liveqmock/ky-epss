package epss.repository.dao.not_mybatis;

import epss.repository.model.EsCttItem;
import epss.repository.model.EsItemStlSubcttEngQ;
import epss.repository.model.model_show.TaskShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
public interface SignPartMapper {
    @Select("select max(id) from es_init_cust")
    String strMaxCustId();
}
