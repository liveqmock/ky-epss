package epss.repository.dao.common;

import epss.repository.model.model_show.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface QueryMapper {

    @Select("select ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
                " max(eicust.NAME) as strName," +
                " sum(ecitem.Contract_Unit_Price) as bdUnitPrice," +
                " sum(ecitem.Contract_Quantity) as bdQuantity," +
                " sum(ecitem.Contract_Amount) as bdAmount" +
            " from" +
                " ES_CTT_INFO ecinfo" +
                " inner join ES_INIT_POWER eip" +
                " on ecinfo.ctt_type=eip.power_type" +
                " and ecinfo.pkid=eip.power_pkid" +
                " and eip.period_no='NULL'" +
                " and eip.status_flag='3'" +
                " inner join ES_CTT_ITEM ecitem" +
                " on ecinfo.PKID=ecitem.BELONG_TO_PKID" +
                " and ecinfo.CTT_TYPE=ecitem.BELONG_TO_TYPE" +
                " join ES_INIT_CUST eicust" +
                " on ecinfo.SIGN_PART_B=eicust.PKID" +
            " where"+
                " ecinfo.CTT_TYPE = #{strCttType}"+
                " and ecinfo.PARENT_PKID = #{strParentPkid}" +
            " group by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B" +
            " order by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B")
    List<QryShow> getCSList(@Param("strCttType") String strCttType,
                                 @Param("strParentPkid") String strParentPkid);

    @Select("select" +
            " ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            " eissem.subctt_item_pkid as strItemPkid," +
            " nei.name as strName," +
            " max(ecitem.unit) as strUnit," +
            " sum(ecitem.contract_quantity) as bdQuantity," +
            " sum(eissem.current_period_m_qty) as bdCurrentPeriodQuantity," +
            " sum(eissem.begin_to_current_period_m_qty) as bdBeginToCurrentPeriodQuantity," +
            " sum(ecitem.contract_unit_price) as bdUnitPrice" +
            " from " +
            " (" +
            "   select " +
            "          eis.stl_type," +
            "          eis.stl_pkid," +
            "          eis.period_no," +
            "          eicust.name" +
            "   from" +
            "          ES_CTT_INFO ecinfo" +
            "   inner join " +
            "          ES_INIT_CUST eicust" +
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   inner join " +
            "          ES_INIT_POWER eip" +
            "   on " +
            "          eip.power_type=ecinfo.ctt_type" +
            "   and " +
            "          eip.power_pkid=ecinfo.pkid" +
            "   and " +
            "          eip.period_no= 'NULL'" +
            "   and " +
            "          eip.status_flag='3' " +
            "   inner join" +
            "          ES_INIT_STL eis" +
            "   on" +
            "          eis.stl_pkid=ecinfo.pkid" +
            "   and" +
            "          eis.stl_type='4'" +
            "   inner join " +
            "          ES_INIT_POWER eip2" +
            "   on " +
            "          eip2.power_type=eis.stl_type" +
            "   and " +
            "          eip2.power_pkid=eis.stl_pkid" +
            "   and " +
            "          eip2.period_no= eis.period_no" +
            "   and " +
            "          eip2.status_flag='3'" +
            "   where" +
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //联分包工程数量详细项
            "    （" +
            "       select" +
            "            subctt_pkid,period_no,subctt_item_pkid,current_period_m_qty," +
            "            (" +
            "               select max(begin_to_current_period_m_qty)" +
            "               from ES_ITEM_STL_SUBCTT_ENG_M eissemnei" +
            "               where eissemnei.subctt_pkid=eissemwai.subctt_pkid" +
            "                     and  eissemnei.subctt_item_pkid=eissemwai.subctt_item_pkid" +
            "                     and  eissemnei.period_no<=#{strPeriodNo}) as begin_to_current_period_m_qty" +
            "      from" +
            "          ES_ITEM_STL_SUBCTT_ENG_M eissemwai" +
            "    ）eissem" +
            " on" +
            "    eissem.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eissem.period_no=nei.period_no" +
            " inner join " +
            "    ES_CTT_ITEM ecitem" +
            " on" +
            "    ecitem.PKID=eissem.subctt_item_pkid" +
            " and " +
            "    ecitem.belong_to_pkid=eissem.subctt_pkid" +
            " and " +
            "    ecitem.belong_to_type='2'" +
            " group by" +
            "    eissem.subctt_pkid,nei.name,eissem.period_no,ecitem.CORRESPONDING_PKID,eissem.subctt_item_pkid" +
            " order by" +
            "    eissem.subctt_pkid,nei.name,eissem.period_no,ecitem.CORRESPONDING_PKID,eissem.subctt_item_pkid")
    List<QryShow> getCSStlMList(@Param("strParentPkid") String strParentPkid,
                                     @Param("strPeriodNo") String strPeriodNo);

    @Select("select" +
            " ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            " eisseq.subctt_item_pkid as strItemPkid," +
            " nei.name as strName," +
            " max(ecitem.unit) as strUnit," +
            " sum(ecitem.contract_quantity) as bdQuantity," +
            " sum(ecitem.contract_unit_price) as bdUnitPrice，" +
            " sum(eisseq.begin_to_current_period_e_qty) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            " (" +
            "   select " +
            "          eis.stl_type," +
            "          eis.stl_pkid," +
            "          eis.period_no," +
            "          eicust.name" +
            "   from" +
            "          ES_CTT_INFO ecinfo" +
            "   inner join " +
            "          ES_INIT_CUST eicust" +   //连客户信息
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   inner join " +                    //已经批准了的分包合同
            "          ES_INIT_POWER eip" +
            "   on " +
            "          eip.power_type=ecinfo.ctt_type" +
            "   and " +
            "          eip.power_pkid=ecinfo.pkid" +
            "   and " +
            "          eip.period_no= 'NULL'" +
            "   and " +
            "          eip.status_flag='3' " +
            "   inner join" +                    //已经登记了的工程数量结算
            "          ES_INIT_STL eis" +
            "   on" +
            "          eis.stl_pkid=ecinfo.pkid" +
            "   and" +
            "          eis.stl_type='3'" +
            "   inner join " +                  //已经批准了的工程数量结算
            "          ES_INIT_POWER eip2" +
            "   on " +
            "          eip2.power_type=eis.stl_type" +
            "   and " +
            "          eip2.power_pkid=eis.stl_pkid" +
            "   and " +
            "          eip2.period_no= eis.period_no" +
            "   and " +
            "          eip2.status_flag='3'" +
            "   where" +                       //根据成本计划号得到分包合同列表
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //联分包工程数量详细项
            "    （" +
            "       select" +
            "            subctt_pkid,period_no,subctt_item_pkid," +
            "            (" +
            "               select max(begin_to_current_period_e_qty)" +
            "               from ES_ITEM_STL_SUBCTT_ENG_Q eisseqnei" +
            "               where eisseqnei.subctt_pkid=eisseqwai.subctt_pkid" +
            "                     and   eisseqnei.subctt_item_pkid=eisseqwai.subctt_item_pkid" +
            "                     and   eisseqnei.period_no<=#{strPeriodNo}) as begin_to_current_period_e_qty" +
            "      from" +
            "          ES_ITEM_STL_SUBCTT_ENG_Q eisseqwai" +
            "    ）eisseq" +
            " on" +
            "    eisseq.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eisseq.period_no=nei.period_no" +
            " inner join " +                 //联分包合同关系表找到对应项
            "    ES_CTT_ITEM ecitem" +
            " on" +
            "    ecitem.PKID=eisseq.subctt_item_pkid" +
            " and " +
            "    ecitem.belong_to_pkid=eisseq.subctt_pkid" +
            " and " +
            "    ecitem.belong_to_type='2'" +
            " group by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.period_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid" +
            " order by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.period_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid")
    List<QryShow> getCSStlQList(@Param("strParentPkid") String strParentPkid,
                                     @Param("strPeriodNo") String strPeriodNo);

    @Select(" select " +
            "      eisPerEdItem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "      subPerEd.SIGN_PART_B_NAME as strName," +
            "      max(eisPerEdItem.CONTRACT_UNIT_PRICE) as bdUnitPrice," +
            "      sum(eisPerEdItem.CURRENT_PERIOD_E_QTY) as bdCurrentPeriodQuantity," +
            "      sum(eisPerEdItem.BEGIN_TO_CURRENT_PERIOD_E_QTY) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            "      ( " +
            "          select " +
            "            subctt.PKID, " +
            "            subctt.SIGN_PART_B_NAME " +
            "          from  " +
            "             ( " +
            "               select  " +
            "                      " +
            "                      PKID " +
            "               from " +
            "                      ES_CTT_INFO " +
            "               where                      " +
            "                      CTT_TYPE = '1' " +
            "               and  " +
            "                      PARENT_PKID = #{strParentPkid}" +
            "             )cstpl " +
            "          inner join " +
            "             ( " +
            "               select  " +
            "                      ecinfo.PKID, " +
            "                      ecinfo.PARENT_PKID, " +
            "                      eicust.NAME AS SIGN_PART_B_NAME " +
            "               from " +
            "                      ES_CTT_INFO ecinfo " +
            "               inner join " +
            "                      ES_INIT_POWER eip " +
            "               on " +
            "                      eip.POWER_TYPE=ecinfo.CTT_TYPE " +
            "               and " +
            "                      eip.POWER_PKID=ecinfo.PKID " +
            "               and " +
            "                      eip.PERIOD_NO='NULL' " +
            "               and " +
            "                      eip.STATUS_FLAG='3' " +
            "               left outer join " +
            "                      ES_INIT_CUST eicust " +
            "               on " +
            "                      eicust.PKID=ecinfo.SIGN_PART_B " +
            "               where                       " +
            "                      ecinfo.CTT_TYPE = '2' " +
            "             )subctt " +
            "          on " +
            "             subctt.PARENT_PKID=cstpl.PKID " +
            "      )subPerEd " +
            "   inner join" +
            "      ( " +
            "         select " +
            "            eisPered.STL_PKID," +
            "            eisPered.PERIOD_NO," +
            "            ecitem.CORRESPONDING_PKID," +
            "            ecitem.CONTRACT_UNIT_PRICE," +
            "            eisseq.CURRENT_PERIOD_E_QTY," +
            "            eisseq.BEGIN_TO_CURRENT_PERIOD_E_QTY " +
            "         from" +
            "            (" +
            "               select " +
            "                  eis.STL_TYPE," +
            "                  eis.STL_PKID," +
            "                  (" +
            "                   select " +
            "                      max(PERIOD_NO) " +
            "                   from " +
            "                      ES_INIT_POWER eip " +
            "                   where " +
            "                      eip.POWER_TYPE=eis.STL_TYPE " +
            "                   and " +
            "                      eip.POWER_PKID=eis.STL_PKID " +
            "                   and " +
            "                      eip.STATUS_FLAG='3'" +
            "                   and" +
            "                      eip.PERIOD_NO<=#{strPeriodNo}" +
            "                 )as PERIOD_NO" +
            "               from" +
            "                  ES_INIT_STL eis " +
            "            )eisPered" +
            "         left outer join" +
            "            ES_CTT_ITEM ecitem" +
            "         on" +
            "            ecitem.belong_to_type='2'" +
            "         and" +
            "            ecitem.belong_to_pkid=eisPered.stl_pkid " +
            "         left outer join" +
            "            ES_ITEM_STL_SUBCTT_ENG_Q eisseq" +
            "         on" +
            "            eisseq.subctt_pkid=ecitem.belong_to_pkid" +
            "         and" +
            "            eisseq.subctt_item_pkid=ecitem.pkid" +
            "         where" +
            "            eisPered.STL_TYPE='3' " +
            "         order by STL_PKID,PERIOD_NO" +
            "       )eisPerEdItem " +
            "    on" +
            "         eisPerEdItem.STL_PKID=subPerEd.PKID" +
            "    group by eisPerEdItem.CORRESPONDING_PKID,subPerEd.SIGN_PART_B_NAME " +
            "    order by eisPerEdItem.CORRESPONDING_PKID,subPerEd.SIGN_PART_B_NAME")
    List<QryShow> getCSStlQBySignPartList(@Param("strParentPkid") String strParentPkid,
                                           @Param("strPeriodNo") String strPeriodNo);

    @Select("select " +
            "    ecitem.pkid as pkid," +
            "    ecitem.corresponding_pkid as correspondingPkid," +
            "    ecitem.contract_unit_price as contractUnitPrice," +
            "    ecitem.contract_quantity as contractQuantity," +
            "    ecitem.contract_amount as contractAmount" +
            " from" +
            "    ES_CTT_INFO ecinfo" +
            " inner join " +                    //已经批准了的分包合同
            "    ES_INIT_POWER eip" +
            " on " +
            "    eip.power_type=ecinfo.ctt_type" +
            " and " +
            "    eip.power_pkid=ecinfo.pkid" +
            " and " +
            "    eip.period_no= 'NULL'" +
            " and " +
            "    eip.status_flag='3' " +
            " inner join " +
            "    ES_CTT_ITEM ecitem" +
            " on" +
            "    ecitem.belong_to_pkid=ecinfo.pkid" +
            " and " +
            "    ecitem.belong_to_type=ecinfo.ctt_type" +
            " where" +                       //根据成本计划号得到分包合同列表
            "    ecinfo.CTT_TYPE = '1'" +
            " and " +
            "    ecinfo.PARENT_PKID = #{strParentPkid}"
         )
    public List<CttItemShow> getEsItemHieRelapOfCstplList(@Param("strParentPkid") String strParentPkid);
}
