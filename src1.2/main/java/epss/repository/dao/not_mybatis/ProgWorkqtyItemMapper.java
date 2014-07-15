package epss.repository.dao.not_mybatis;

import epss.repository.model.EsItemStlSubcttEngP;
import epss.repository.model.EsItemStlSubcttEngQ;
import epss.repository.model.model_show.QryShow;
import epss.repository.model.model_show.QryTkMeaCSStlQShow;
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
public interface ProgWorkqtyItemMapper {
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
    List<EsItemStlSubcttEngQ> getProgWorkqtyItemListByPeriod(
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
    List<String> getProgWorkqtyItemPeriodsByPeriod(
            @Param("strSubcttPkid") String strSubcttPkid,
            @Param("strPeriodNoBegin") String strPeriodNoBegin,
            @Param("strPeriodNoEnd") String strPeriodNoEnd);
}
