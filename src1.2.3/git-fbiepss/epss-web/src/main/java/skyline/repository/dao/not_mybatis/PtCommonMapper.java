package skyline.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import skyline.repository.model.Ptmenu;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface PtCommonMapper {

    @Select("select " +
            "   distinct pm.* " +
            "from " +
            "   PTOPERROLE po " +
            "join " +
            "   PTROLERES pro " +
            "on " +
            "   po.ROLEID=pro.ROLEID " +
            "join " +
            "   PTRESOURCE prs " +
            "on " +
            "   pro.RESID=prs.RESID " +
            "join " +
            "   PTMENU pm " +
            "on " +
            "   prs.RESNAME=pm.MENUID " +
            "where " +
            "   po.OPERID=#{strOperId} " +
            "order by " +
            "   pm.Levelidx")
    List<Ptmenu> getPtmenuList(@Param("strOperId") String strOperId);
}
