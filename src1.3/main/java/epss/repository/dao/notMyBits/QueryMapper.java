package epss.repository.dao.notMyBits;

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
            "     max(eicust.NAME) as strName," +
            "     sum(ecitem.Unit_Price) as bdUnitPrice," +
            "     sum(ecitem.Qty) as bdQty," +
            "     sum(ecitem.Amt) as bdAmt" +
            " from" +
            "     SUBCTT_INFO subinfo" +
            " on " +
            "     si.FLOW_STATUS='3'" +
            " inner join " +
            "     SUBCTT_ITEM subitem" +
            " on " +
            "     ecinfo.PKID=ecitem.BELONG_TO_PKID" +
            " and " +
            "     ecinfo.CTT_TYPE=ecitem.BELONG_TO_TYPE" +
            " left join " +
            "     ES_INIT_CUST eicust" +
            " on " +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where"+
            "   ecinfo.PARENT_PKID = #{strParentPkid}" +
            " group by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B" +
            " order by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B")
    List<QryShow> getCSList(@Param("strParentPkid") String strParentPkid);

    @Select("select" +
            " ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            " eissem.subctt_item_pkid as strItemPkid," +
            " nei.name as strName," +
            " max(ecitem.unit) as strUnit," +
            " sum(ecitem.qty) as bdQuantity," +
            " sum(eissem.THIS_STAGE_QTY) as bdThisStageQty," +
            " sum(eissem.ADD_UP_QTY) as bdAddUpQty," +
            " sum(ecitem.UNIT_PRICE) as bdUnitPrice" +
            " from " +
            " (" +
            "   select " +
            "          eis.stl_type," +
            "          eis.stl_pkid," +
            "          eis.stage_no," +
            "          eicust.name" +
            "   from" +
            "          ES_CTT_INFO ecinfo" +
            "   inner join " +
            "          ES_INIT_CUST eicust" +
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   inner join " +
            "          FLOW_CTRL fc" +
            "   on " +
            "          fc.INFO_TYPE=ecinfo.ctt_type" +
            "   and " +
            "          fc.INFO_PKID=ecinfo.pkid" +
            "   and " +
            "          fc.stage_no= 'NULL'" +
            "   and " +
            "          fc.FLOW_STATUS='3' " +
            "   inner join" +
            "          ES_INIT_STL eis" +
            "   on" +
            "          eis.stl_pkid=ecinfo.pkid" +
            "   and" +
            "          eis.stl_type='4'" +
            "   inner join " +
            "          FLOW_CTRL eip2" +
            "   on " +
            "          eip2.INFO_TYPE=eis.stl_type" +
            "   and " +
            "          eip2.INFO_PKID=eis.stl_pkid" +
            "   and " +
            "          eip2.stage_no= eis.stage_no" +
            "   and " +
            "          eip2.FLOW_STATUS='3'" +
            "   where" +
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //联分包工程数量详细项
            "    （" +
            "       select" +
            "            subctt_pkid,stage_no,subctt_item_pkid,THIS_STAGE_QTY," +
            "            (" +
            "               select max(ADD_UP_QTY)" +
            "               from ES_ITEM_STL_SUBCTT_ENG_M eissemnei" +
            "               where eissemnei.subctt_pkid=eissemwai.subctt_pkid" +
            "                     and  eissemnei.subctt_item_pkid=eissemwai.subctt_item_pkid" +
            "                     and  eissemnei.stage_no<=#{strStageNo}) as ADD_UP_QTY" +
            "      from" +
            "          ES_ITEM_STL_SUBCTT_ENG_M eissemwai" +
            "    ）eissem" +
            " on" +
            "    eissem.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eissem.stage_no=nei.stage_no" +
            " inner join " +
            "    ES_CTT_ITEM ecitem" +
            " on" +
            "    ecitem.PKID=eissem.subctt_item_pkid" +
            " and " +
            "    ecitem.belong_to_pkid=eissem.subctt_pkid" +
            " and " +
            "    ecitem.belong_to_type='2'" +
            " group by" +
            "    eissem.subctt_pkid,nei.name,eissem.stage_no,ecitem.CORRESPONDING_PKID,eissem.subctt_item_pkid" +
            " order by" +
            "    eissem.subctt_pkid,nei.name,eissem.stage_no,ecitem.CORRESPONDING_PKID,eissem.subctt_item_pkid")
    List<QryShow> getCSStlMList(@Param("strParentPkid") String strParentPkid,
                                     @Param("strStageNo") String strStageNo);

    @Select("select" +
            " ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            " eisseq.subctt_item_pkid as strItemPkid," +
            " nei.name as strName," +
            " max(ecitem.unit) as strUnit," +
            " sum(ecitem.qty) as bdQuantity," +
            " sum(ecitem.UNIT_PRICE) as bdUnitPrice，" +
            " sum(eisseq.begin_to_current_period_e_qty) as bdAddUpQty" +
            " from " +
            " (" +
            "   select " +
            "          eis.stl_type," +
            "          eis.stl_pkid," +
            "          eis.stage_no," +
            "          eicust.name" +
            "   from" +
            "          ES_CTT_INFO ecinfo" +
            "   inner join " +
            "          ES_INIT_CUST eicust" +   //连客户信息
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   inner join " +                    //已经批准了的分包合同
            "          FLOW_CTRL fc" +
            "   on " +
            "          fc.INFO_TYPE=ecinfo.ctt_type" +
            "   and " +
            "          fc.INFO_PKID=ecinfo.pkid" +
            "   and " +
            "          fc.stage_no= 'NULL'" +
            "   and " +
            "          fc.FLOW_STATUS='3' " +
            "   inner join" +                    //已经登记了的工程数量结算
            "          ES_INIT_STL eis" +
            "   on" +
            "          eis.stl_pkid=ecinfo.pkid" +
            "   and" +
            "          eis.stl_type='3'" +
            "   inner join " +                  //已经批准了的工程数量结算
            "          FLOW_CTRL eip2" +
            "   on " +
            "          eip2.INFO_TYPE=eis.stl_type" +
            "   and " +
            "          eip2.INFO_PKID=eis.stl_pkid" +
            "   and " +
            "          eip2.stage_no= eis.stage_no" +
            "   and " +
            "          eip2.FLOW_STATUS='3'" +
            "   where" +                       //根据成本计划号得到分包合同列表
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //联分包工程数量详细项
            "    （" +
            "       select" +
            "            subctt_pkid,stage_no,subctt_item_pkid," +
            "            (" +
            "               select max(begin_to_current_period_e_qty)" +
            "               from ES_ITEM_STL_SUBCTT_ENG_Q eisseqnei" +
            "               where eisseqnei.subctt_pkid=eisseqwai.subctt_pkid" +
            "                     and   eisseqnei.subctt_item_pkid=eisseqwai.subctt_item_pkid" +
            "                     and   eisseqnei.stage_no<=#{strStageNo}) as begin_to_current_period_e_qty" +
            "      from" +
            "          ES_ITEM_STL_SUBCTT_ENG_Q eisseqwai" +
            "    ）eisseq" +
            " on" +
            "    eisseq.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eisseq.stage_no=nei.stage_no" +
            " inner join " +                 //联分包合同关系表找到对应项
            "    ES_CTT_ITEM ecitem" +
            " on" +
            "    ecitem.PKID=eisseq.subctt_item_pkid" +
            " and " +
            "    ecitem.belong_to_pkid=eisseq.subctt_pkid" +
            " and " +
            "    ecitem.belong_to_type='2'" +
            " group by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.stage_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid" +
            " order by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.stage_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid")
    List<QryShow> getCSStlQList(@Param("strParentPkid") String strParentPkid,
                                     @Param("strStageNo") String strStageNo);

    @Select(" select " +
            "      eisPerEdItem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "      subPerEd.SIGN_PART_NAME_B as strName," +
            "      max(eisPerEdItem.UNIT_PRICE) as bdUnitPrice," +
            "      sum(eisPerEdItem.THIS_STAGE_QTY) as bdThisStageQty," +
            "      sum(eisPerEdItem.ADD_UP_QTY) as bdAddUpQty" +
            " from " +
            "      ( " +
            "          select " +
            "            subctt.PKID, " +
            "            subctt.SIGN_PART_NAME_B " +
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
            "                      eicust.NAME AS SIGN_PART_NAME_B " +
            "               from " +
            "                      ES_CTT_INFO ecinfo " +
            "               inner join " +
            "                      FLOW_CTRL fc " +
            "               on " +
            "                      fc.INFO_TYPE=ecinfo.CTT_TYPE " +
            "               and " +
            "                      fc.INFO_PKID=ecinfo.PKID " +
            "               and " +
            "                      fc.STAGE_NO='NULL' " +
            "               and " +
            "                      fc.FLOW_STATUS='3' " +
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
            "            eisPered.STAGE_NO," +
            "            ecitem.CORRESPONDING_PKID," +
            "            ecitem.UNIT_PRICE," +
            "            eisseq.THIS_STAGE_QTY," +
            "            eisseq.ADD_UP_QTY " +
            "         from" +
            "            (" +
            "               select " +
            "                  eis.STL_TYPE," +
            "                  eis.STL_PKID," +
            "                  (" +
            "                   select " +
            "                      max(STAGE_NO) " +
            "                   from " +
            "                      FLOW_CTRL fc " +
            "                   where " +
            "                      fc.INFO_TYPE=eis.STL_TYPE " +
            "                   and " +
            "                      fc.INFO_PKID=eis.STL_PKID " +
            "                   and " +
            "                      fc.FLOW_STATUS='3'" +
            "                   and" +
            "                      fc.STAGE_NO<=#{strStageNo}" +
            "                 )as STAGE_NO" +
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
            "         order by STL_PKID,STAGE_NO" +
            "       )eisPerEdItem " +
            "    on" +
            "         eisPerEdItem.STL_PKID=subPerEd.PKID" +
            "    group by eisPerEdItem.CORRESPONDING_PKID,subPerEd.SIGN_PART_NAME_B " +
            "    order by eisPerEdItem.CORRESPONDING_PKID,subPerEd.SIGN_PART_NAME_B")
    List<QryShow> getCSStlQBySignPartList(@Param("strParentPkid") String strParentPkid,
                                           @Param("strStageNo") String strStageNo);

    @Select("select " +
            "    ecitem.pkid as pkid," +
            "    ecitem.corresponding_pkid as correspondingPkid," +
            "    ecitem.unit_price as unitPrice," +
            "    ecitem.qty as qty," +
            "    ecitem.amt as amt" +
            " from" +
            "    ES_CTT_INFO ecinfo" +
            " inner join " +                    //已经批准了的分包合同
            "    FLOW_CTRL fc" +
            " on " +
            "    fc.INFO_TYPE=ecinfo.ctt_type" +
            " and " +
            "    fc.INFO_PKID=ecinfo.pkid" +
            " and " +
            "    fc.stage_no= 'NULL'" +
            " and " +
            "    fc.FLOW_STATUS='3' " +
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
    public List<SubcttItemShow> getEsItemHieRelapOfCstplList(@Param("strParentPkid") String strParentPkid);
}
