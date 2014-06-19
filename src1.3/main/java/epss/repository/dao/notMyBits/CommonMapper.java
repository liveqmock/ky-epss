package epss.repository.dao.notMyBits;

import epss.common.enums.ESEnum;
import epss.repository.model.ProgWorkqtyItem;
import epss.repository.model.model_show.TaskShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import platform.repository.model.Ptoper;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface CommonMapper {

    @Select("select * from ptoper")
    List<Ptoper> getOperList();

    @Select("select * from ptoper" +
            "WHERE operid = #{strOperId}")
    Ptoper getOperListByOperId(@Param("strOperId") String strOperId);

    @Select("select max(id) from es_init_cust")
    String strMaxCustId();

    @Select("select INFO_TYPE,FLOW_STATUS,count(1) as GROUP_COUNTS" +
            " from FLOW_CTRL" +
            " group by INFO_TYPE,FLOW_STATUS" +
            " order by INFO_TYPE,FLOW_STATUS")
    List<TaskShow> getPowerListByPowerTypeAndFlowStatus();

    @Select("select fc.INFO_TYPE,fc.INFO_PKID,fc.INFO_ID,fc.FLOW_STATUS,fc.FLOW_STATUS_REMARK," +
            " ecinfo.ID,ecinfo.NAME,ecinfo.PARENT_PKID," +
            " NVL(eicName.NAME,' ') as parentName" +
            " from FLOW_CTRL fc" +
            " join ES_CTT_INFO ecinfo" +
            " on fc.INFO_PKID=ecinfo.PKID" +
            " left join (select PKID,NAME from ES_CTT_INFO)eicName" +
            " on eicName.pkid=ecinfo.PARENT_PKID" +
            " order by fc.INFO_TYPE,fc.FLOW_STATUS")
    List<TaskShow> getTaskModelList();

    @Select("select t.ctt_type as INFO_TYPE,t.pkid as INFO_PKID,t.ID as id,t.NAME as name," +
            " t.PARENT_PKID as belong_To_Pkid,NVL(eicName.NAME,' ') as parentName" +
            " from ES_CTT_INFO t left join (select PKID,NAME from ES_CTT_INFO)eicName" +
            " on eicName.pkid=t.PARENT_PKID" +
            " where t.ctt_type=#{strCttType}"+
            " and t.pkid not in (select p.INFO_PKID  from FLOW_CTRL p where p.INFO_TYPE=#{strCttType}"+
            " )" )
    List<TaskShow> getTaskModelListOfCtt(@Param("strCttType") String strCttType);

    @Select("select s.stl_type as INFO_TYPE,s.stl_pkid as INFO_PKID,s.id as id,NVL(eicName1.NAME,' ') as name," +
            " eicName1.PARENT_PKID as belong_To_Pkid,NVL(eicName2.NAME,' ') as parentName" +
            " from es_init_stl s left join (select PKID,NAME,PARENT_PKID from ES_CTT_INFO)eicName1" +
            " on eicName1.pkid=s.stl_pkid" +
            " left join (select PKID,NAME from ES_CTT_INFO)eicName2" +
            " on eicName2.pkid=eicName1.PARENT_PKID" +
            " where s.stl_type=#{strCttType}" +
            " and s.stl_pkid not in (select p.INFO_PKID  from FLOW_CTRL p where p.INFO_TYPE=#{strCttType}"+
            " )" )
    List<TaskShow> getTaskModelListOfStl(@Param("strCttType") String strCttType);

    @Select("select max(id) from ES_INIT_STL where stl_type = #{strStlType}")
    String getStrMaxStlId(@Param("strStlType") String strStlType);

    @Update("Update ES_CTT_ITEM set PKID = #{strOldPkid} where PKID = #{strNewPkid}")
    void updateRecordPkid(@Param("strNewPkid") String strNewPkid,
                           @Param("strOldPkid") String strOldPkid);

     @Select(
            " select" +
            "   eisseq.subctt_item_pkid as itemPkid," +
            "   eisseq.stage_no as stageNo," +
            "   eisseq.current_period_e_qty as currentPeriodEQty" +
            " from" +
            "   ES_ITEM_STL_SUBCTT_ENG_Q eisseq" +
            " inner join" +
            "   FLOW_CTRL fc" +
            " on" +
            "   fc.INFO_TYPE='3'" +
            " and" +
            "   fc.INFO_PKID=eisseq.subctt_pkid" +
            " and" +
            "   fc.INFO_ID=eisseq.stage_no" +
            " and" +
            "   fc.FLOW_STATUS='3'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttInfoPkid}"+
            "   and eisseq.stage_no >= #{strStageNoBegin}"+
            "   and eisseq.stage_no <= #{strStageNoEnd}" +
            " order by eisseq.subctt_item_pkid,eisseq.stage_no")
    List<ProgWorkqtyItem> selectEsItemStlSubcttEngQListByPeriod(
            @Param("strSubcttInfoPkid") String strSubcttInfoPkid,
            @Param("strStageNoBegin") String strStageNoBegin,
            @Param("strStageNoEnd") String strStageNoEnd);

     @Select("select " +
            "   distinct eisseq.stage_no" +
            " from" +
            "   ES_ITEM_STL_SUBCTT_ENG_Q eisseq" +
            " inner join" +
            "   FLOW_CTRL fc" +
            " on" +
            "   fc.INFO_TYPE='3'" +
            " and" +
            "   fc.INFO_PKID=eisseq.subctt_pkid" +
            " and" +
            "   fc.INFO_ID=eisseq.stage_no" +
            " and" +
            "   fc.FLOW_STATUS='3'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttInfoPkid}"+
            "   and eisseq.stage_no >= #{strStageNoBegin}"+
            "   and eisseq.stage_no <= #{strStageNoEnd}" +
            " order by eisseq.stage_no")
    List<String> selectEsItemStlSubcttEngQPeriodsByPeriod(
            @Param("strSubcttInfoPkid") String strSubcttInfoPkid,
            @Param("strStageNoBegin") String strStageNoBegin,
            @Param("strStageNoEnd") String strStageNoEnd);

    @Select("select" +
            "   Max(OPERNAME)"+
            " from"+
            "   PTOPER"+
            " where"+
            " OPERID=#{createdBy}")
    String selectOpernameByCreatedBy(
            @Param("createdBy") String createdBy);


   /*@Select("SELECT COLUMN_ID,COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = #{strTableName} ORDER BY COLUMN_ID")*/
    /*@Select("SELECT column_name,comments,'true'as rendered_flag " +
            "FROM all_col_comments WHERE TABLE_NAME = #{strTableName}")
    List<ColumnModel> getColumnNameListByTableName(@Param("strTableName") String strTableName);*/
}
