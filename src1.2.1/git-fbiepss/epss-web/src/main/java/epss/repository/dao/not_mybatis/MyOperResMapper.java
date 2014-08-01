package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.OperResShow;
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
public interface MyOperResMapper {
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
    @Select("select " +
            " t.pkid, " +
            " t.tid,  " +
            " t.oper_pkid as operPkid, " +
            " (select opername from ptoper where operid=t.oper_pkid) as operName, " +
            " t.flow_status as flowStatus, " +
            " t.info_type as infoType, " +
            " t.info_pkid as infoPkid, " +
            " (select name from es_ctt_info where pkid=t.info_pkid ) as infoPkidName, " +
            " t.archived_flag as archivedFlag, " +
            " t.created_by as createdBy, " +
            " (select opername from ptoper where operid=t.created_by) as createdByName, " +
            " t.created_time as createdTime, " +
            " t.last_upd_by as lastUpdBy, " +
            " (select opername from ptoper where operid=t.last_upd_by) as lastUpdByName,  " +
            " t.last_upd_time as lastUpdTime, " +
            " t.remark as remark, " +
            " t.recversion as recversion " +
            "from " +
            " OPER_RES t")
    List<OperResShow> selectOperaResRecords();

    List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara);
}
