package epss.repository.dao.not_mybatis;

import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.TaskShow;
import epss.repository.model.EsItemStlSubcttEngQ;
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

    @Select("select " +
            " POWER_TYPE as type," +
            " STATUS_FLAG as statusFlag," +
            " count(1) as recordsCountInGroup" +
            " from ES_INIT_POWER" +
            " group by POWER_TYPE,STATUS_FLAG" +
            " order by POWER_TYPE,STATUS_FLAG")
    List<TaskShow> getPowerListByPowerTypeAndStatusFlag();

    @Select("select " +
            "   eip.POWER_TYPE as type," +
            "   eip.POWER_PKID as pkid," +
            "   eip.PERIOD_NO as periodNo," +
            "   eip.STATUS_FLAG as statusFlag," +
            "   eip.PRE_STATUS_FLAG as preStatusFlag," +
            "   ecinfo.ID as id," +
            "   ecinfo.NAME as name" +
            " from " +
            "   ES_INIT_POWER eip" +
            " left join " +
            "   ES_CTT_INFO ecinfo" +
            " on " +
            "   eip.POWER_PKID=ecinfo.PKID" +
            " order by" +
            "   eip.POWER_TYPE,eip.STATUS_FLAG")
    List<TaskShow> getTaskModelList();

    @Select("select " +
            "   eci.ctt_type as type," +
            "   eci.pkid as pkid," +
            "   eci.ID as id," +
            "   eci.NAME as name" +
            " from " +
            "   ES_CTT_INFO eci" +
            " left join " +
            "   es_init_power eip" +
            " on" +
            "   eci.ctt_type=eip.power_type" +
            " and" +
            "   eci.pkid=eip.power_pkid" +
            " where " +
            "   eci.ctt_type=#{strCttType}"+
            " and " +
            "   eip.power_pkid is null")
    List<TaskShow> getTaskModelListOfCtt(@Param("strCttType") String strCttType);

    @Select("select " +
            "   eis.stl_type as type," +
            "   eis.pkid as pkid," +
            "   eis.id as id," +
            "   eci.name as name" +
            " from " +
            "   es_init_stl eis" +
            " left join " +
            "   es_init_power eip" +
            " on" +
            "   eis.stl_type=eip.power_type" +
            " and" +
            "   eis.stl_pkid=eip.power_pkid" +
            " left join " +
            "   ES_CTT_INFO eci" +
            " on" +
            "   eis.stl_pkid=eci.pkid" +
            " where " +
            "   eis.stl_type=#{strStlType}"+
            " and " +
            "   eip.power_pkid is null")
    List<TaskShow> getTaskModelListOfStl(@Param("strStlType") String strStlType);

    @Select("select max(id) from ES_CTT_INFO where ctt_type = #{strCttType}")
    String getStrMaxCttId(@Param("strCttType") String strCttType);

    @Select("select max(id) from ES_INIT_STL where stl_type = #{strStlType}")
    String getStrMaxStlId(@Param("strStlType") String strStlType);

    @Update("Update ES_CTT_ITEM set ORDERID=ORDERID+1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidPlusOneByNode(@Param("strBelongToType") String strBelongToType,
                                           @Param("strBelongToPkid") String strBelongToPkid,
                                           @Param("strParentPkid") String strParentPkid,
                                           @Param("intGrade") Integer intGrade,
                                           @Param("intOrderid") Integer intOrderid);

    @Update("Update ES_CTT_ITEM set ORDERID=ORDERID-1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidSubOneByNode(@Param("strBelongToType") String strBelongToType,
                                                                    @Param("strBelongToPkid") String strBelongToPkid,
                                                                    @Param("strParentPkid") String strParentPkid,
                                                                    @Param("intGrade") Integer intGrade,
                                                                    @Param("intOrderid") Integer intOrderid);

    @Update("Update ES_CTT_ITEM set PKID = #{strOldPkid} where PKID = #{strNewPkid}")
    void updateRecordPkid(@Param("strNewPkid") String strNewPkid,
                           @Param("strOldPkid") String strOldPkid);

    @Select("select * from ES_CTT_ITEM where"+
            "     BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}"+
            " and ORDERID             = #{intOrderid}")
    EsCttItem getEsItemHieRelapByBusinessPk(@Param("strBelongToType") String strBelongToType,
                                                 @Param("strBelongToPkid") String strBelongToPkid,
                                                 @Param("strParentPkid") String strParentPkid,
                                                 @Param("intGrade") Integer intGrade,
                                                 @Param("intOrderid") Integer intOrderid);

    @Select(
            " select" +
            "   eisseq.subctt_item_pkid as itemPkid," +
            "   eisseq.period_no as periodNo," +
            "   eisseq.current_period_e_qty as currentPeriodEQty" +
            " from" +
            "   ES_ITEM_STL_SUBCTT_ENG_Q eisseq" +
            " inner join" +
            "   ES_INIT_POWER eip" +
            " on" +
            "   eip.POWER_TYPE='3'" +
            " and" +
            "   eip.POWER_PKID=eisseq.subctt_pkid" +
            " and" +
            "   eip.PERIOD_NO=eisseq.period_no" +
            " and" +
            "   eip.STATUS_FLAG='3'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttPkid}"+
            "   and eisseq.period_no >= #{strPeriodNoBegin}"+
            "   and eisseq.period_no <= #{strPeriodNoEnd}" +
            " order by eisseq.subctt_item_pkid,eisseq.period_no")
    List<EsItemStlSubcttEngQ> selectEsItemStlSubcttEngQListByPeriod(
            @Param("strSubcttPkid") String strSubcttPkid,
            @Param("strPeriodNoBegin") String strPeriodNoBegin,
            @Param("strPeriodNoEnd") String strPeriodNoEnd);

     @Select("select " +
            "   distinct eisseq.period_no" +
            " from" +
            "   ES_ITEM_STL_SUBCTT_ENG_Q eisseq" +
            " inner join" +
            "   ES_INIT_POWER eip" +
            " on" +
            "   eip.POWER_TYPE='3'" +
            " and" +
            "   eip.POWER_PKID=eisseq.subctt_pkid" +
            " and" +
            "   eip.PERIOD_NO=eisseq.period_no" +
            " and" +
            "   eip.STATUS_FLAG='3'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttPkid}"+
            "   and eisseq.period_no >= #{strPeriodNoBegin}"+
            "   and eisseq.period_no <= #{strPeriodNoEnd}" +
            " order by eisseq.period_no")
    List<String> selectEsItemStlSubcttEngQPeriodsByPeriod(
            @Param("strSubcttPkid") String strSubcttPkid,
            @Param("strPeriodNoBegin") String strPeriodNoBegin,
            @Param("strPeriodNoEnd") String strPeriodNoEnd);

   /*@Select("SELECT COLUMN_ID,COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = #{strTableName} ORDER BY COLUMN_ID")*/
    /*@Select("SELECT column_name,comments,'true'as rendered_flag " +
            "FROM all_col_comments WHERE TABLE_NAME = #{strTableName}")
    List<ColumnModel> getColumnNameListByTableName(@Param("strTableName") String strTableName);*/
}
