package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.DeptAndOperShow;
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
public interface MyDeptAndOperMapper {
    @Select("select " +
            "    pkid, " +
            "    id, " +
            "    name, " +
            "    type " +
            "from " +
            "    (select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        1 as type " +
            "     from " +
            "        oper " +
            "     where " +
            "        dept_pkid=#{parentPkid} " +
            "     union " +
            "     select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        0 as type  " +
            "     from " +
            "        dept ta " +
            "     where  " +
            "        parentpkid=#{parentPkid}) ss " +
            "order by type ")
    List<DeptAndOperShow> selectDeptAndOperRecords(@Param("parentPkid") String parentPkidPara);
}
