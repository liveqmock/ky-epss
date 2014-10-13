package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.DeptOperShow;
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
    @Select("select max(id) from DEPT")
    String getStrMaxDeptId();

    @Select("select max(id) from OPER")
    String getStrMaxOperId();

    @Select("   (select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        1 as type," +
            "        type as operType " +
            "     from " +
            "        oper " +
            "     where " +
            "        dept_pkid=#{parentPkid}" +
            "     and" +
            "        type !='0'" +
            "    ) " +
            " union " +
            "    (select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        0 as type," +
            "        '' as operType" +
            "     from " +
            "        dept ta " +
            "     where  " +
            "        parentpkid=#{parentPkid}" +
            "     ) " +
            " order by type,name desc")
    List<DeptOperShow> selectDeptAndOperRecords(@Param("parentPkid") String parentPkidPara);
}
