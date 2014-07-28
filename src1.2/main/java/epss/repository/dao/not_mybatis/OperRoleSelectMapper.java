package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.OperRoleSelectShow;
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
public interface OperRoleSelectMapper {
    @Select("select " +
            "    selid, " +
            "    slename, " +
            "    seltype, " +
            "    countnumer " +
            " from " +
            "    (select " +
            "       operid as selid , " +
            "       opername as slename , " +
            "       0 as seltype, " +
            "       0 as countnumer " +
            "     from " +
            "        ptoper " +
            "     where " +
            "        deptid=#{parentDeptid} " +
            "     union " +
            "     select " +
            "       deptid as selid , " +
            "       deptname as slename, " +
            "       1 as seltype , " +
            "       (select  count(deptid) as a from ptdept  where parentdeptid=ta.deptid) as countnumer " +
            "     from " +
            "       ptdept ta " +
            "     where " +
            "       parentdeptid=#{parentDeptid} " +
            "      ) ss " +
            "  order by  " +
            "  seltype")
    List<OperRoleSelectShow> selectOperaRoleRecords(@Param("parentDeptid") String parentDeptid);

}
