package epss.repository.dao.not_mybatis;

import epss.repository.model.ProgStlItemSubQ;
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
public interface MyProgWorkqtyItemMapper {
    @Select(
            " select" +
            "   eisseq.subctt_item_pkid as subcttItemPkid," +
            "   eisseq.period_no as periodNo," +
            "   eisseq.current_period_e_qty as currentPeriodEQty" +
            " from" +
            "   PROG_STL_ITEM_SUB_Q eisseq" +
            " inner join" +
            "   PROG_STL_INFO eis" +
            " on" +
            "   eis.STL_TYPE='3'" +
            " and" +
            "   eis.STL_PKID=eisseq.subctt_pkid" +
            " and" +
            "   eis.PERIOD_NO=eisseq.period_no" +
            " and" +
            "   eis.FLOW_STATUS='2'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttPkid}"+
            " and " +
            "   eisseq.period_no >= #{strPeriodNoBegin}"+
            " and " +
            "   eisseq.period_no <= #{strPeriodNoEnd}" +
            " order by " +
            "   eisseq.subctt_item_pkid,eisseq.period_no")
    List<ProgStlItemSubQ> getProgWorkqtyItemListByPeriod(
            @Param("strSubcttPkid") String strSubcttPkid,
            @Param("strPeriodNoBegin") String strPeriodNoBegin,
            @Param("strPeriodNoEnd") String strPeriodNoEnd);

    @Select("select " +
            "   distinct eisseq.period_no" +
            " from" +
            "   PROG_STL_ITEM_SUB_Q eisseq" +
            " inner join" +
            "   PROG_STL_INFO eis" +
            " on" +
            "   eis.STL_TYPE='3'" +
            " and" +
            "   eis.STL_PKID=eisseq.subctt_pkid" +
            " and" +
            "   eis.PERIOD_NO=eisseq.period_no" +
            " and" +
            "   eis.FLOW_STATUS='2'" +
            " where" +
            "   eisseq.subctt_pkid = #{strSubcttPkid}"+
            "   and eisseq.period_no >= #{strPeriodNoBegin}"+
            "   and eisseq.period_no <= #{strPeriodNoEnd}" +
            " order by eisseq.period_no")
    List<String> getProgWorkqtyItemPeriodsByPeriod(
            @Param("strSubcttPkid") String strSubcttPkid,
            @Param("strPeriodNoBegin") String strPeriodNoBegin,
            @Param("strPeriodNoEnd") String strPeriodNoEnd);
}
