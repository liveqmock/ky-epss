package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperResShow;
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
            "    operPkid, " +
            "    operName, " +
            "    infoType " +
            " from " +
            "    (select " +
            "       operid as operPkid , " +
            "       opername as operName , " +
            "       0 as infoType, " +
            "       0 as countnumer " +
            "     from " +
            "        ptoper " +
            "     where " +
            "        deptid=#{parentDeptid} " +
            "     union " +
            "     select " +
            "       deptid as operPkid , " +
            "       deptname as operName, " +
            "       1 as infoType , " +
            "       (select  count(deptid) as a from ptdept  where parentdeptid=ta.deptid) as countnumer " +
            "     from " +
            "       ptdept ta " +
            "     where " +
            "       parentdeptid=#{parentDeptid} " +
            "      ) ss " +
            "  order by  " +
            "  infoType")
    List<OperResShow> selectOperaRoleRecords(@Param("parentDeptid") String parentDeptid);
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

    @Select("select    " +
            "    t.pkid as pkid,   " +
            "    t.ctt_type  as cttType,   " +
            "    t.parent_pkid as parentPkid,   " +
            "    t.id as id,      " +
            "    t.name as name,   " +
            "    t.note as note,   " +
            "    t.end_flag as endFlag,   " +
            "    t.created_by as createdBy,   " +
            "    (select opername from ptoper where operid=t.created_by) as createdByName,   " +
            "    t.created_date  as createdDate" +
            "    from   " +
            "    ES_CTT_INFO t   " +
            "where    " +
            "    t.parent_pkid=#{parentPkid}   " +
            "order by    " +
            "    t.name")
    List<CttInfoShow> selectRecordsFromCtt(@Param("parentPkid") String parentPkidPara);
}
