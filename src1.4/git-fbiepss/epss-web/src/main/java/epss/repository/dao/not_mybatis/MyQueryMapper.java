package epss.repository.dao.not_mybatis;

import epss.repository.model.ProgStlItemSubStlment;
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
public interface MyQueryMapper {
    @Select("select ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "     max(eicust.NAME) as strSignPartName," +
            "     max(ecitem.NAME) as strName," +
            "     sum(ecitem.Contract_Unit_Price) as bdUnitPrice," +
            "     sum(ecitem.Contract_Quantity) as bdQuantity," +
            "     sum(ecitem.Contract_Amount) as bdAmount" +
            " from" +
            "     CTT_INFO ecinfo" +
            " inner join " +
            "     CTT_ITEM ecitem" +
            " on " +
            "     ecinfo.PKID=ecitem.BELONG_TO_PKID" +
            " and " +
            "     ecinfo.CTT_TYPE=ecitem.BELONG_TO_TYPE" +
            " join " +
            "     SIGN_PART eicust" +
            " on " +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where"+
            "     ecinfo.CTT_TYPE = #{strCttType}"+
            "     and ecinfo.PARENT_PKID = #{strParentPkid}" +
            " group by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B,ecitem.PKID" +
            " order by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B")
    List<QryShow> getCSList(@Param("strCttType") String strCttType,
                            @Param("strParentPkid") String strParentPkid);

    @Select("select" +
            "     c_info_item.strCorrespondingPkid," +
            "     c_info_item.strItemPkid," +
            "     c_info_item.strSignPartName," +
            "     max(c_info_item.NAME) as strName," +
            "     max(c_info_item.UNIT) as strUnit," +
            "     sum(c_info_item.CONTRACT_UNIT_PRICE) as bdUnitPrice," +
            "     sum(c_info_item.SIGN_PART_A_PRICE) as bdSignPartAMPrice," +
            "     sum(c_info_item.CONTRACT_QUANTITY) as bdQuantity," +
            "     sum(ps_info_item.current_period_m_qty) as bdCurrentPeriodQuantity," +
            "     sum(ps_info_item.begin_to_current_period_m_qty) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            "     (" +
            "       select " +
            "              citem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "              citem.PKID as strItemPkid," +
            "              sp.NAME as strSignPartName," +
            "              citem.NAME," +
            "              citem.UNIT," +
            "              citem.CONTRACT_UNIT_PRICE," +
            "              citem.SIGN_PART_A_PRICE," +
            "              citem.CONTRACT_QUANTITY," +
            "              citem.BELONG_TO_PKID" +
            "       from" +
             "             CTT_INFO cinfo" +
            "       inner join " +
            "              SIGN_PART sp" +
            "       on " +
            "              cinfo.SIGN_PART_B=sp.PKID" +
            "       inner join" +
            "              CTT_ITEM citem" +
            "       on" +
            "              cinfo.CTT_TYPE=citem.belong_to_type" +
            "       and" +
            "              cinfo.PKID=citem.belong_to_pkid" +
            "       where" +
            "              cinfo.PARENT_PKID = #{strCstplInfoPkid}" +
            "       and " +  //已经批准了的分包合同
            "              cinfo.FLOW_STATUS='3'" +
               "     )c_info_item" +
            " inner join" +//联分包工程数量详细项
            "    (" +
            "       select" +
            "            t2.subctt_pkid,ta.period_no,t2.subctt_item_pkid," +
            "            t2.current_period_m_qty,t2.begin_to_current_period_m_qty" +
            "       from (" +
            "             select" +
            "                  psism.subctt_pkid,psism.subctt_item_pkid," +
//            "                  psism.current_period_m_qty,psism.begin_to_current_period_m_qty," +
            "                  max(psism.period_no) period_no"+
//            "                  row_number() over " +
//            "                       (partition by psism.subctt_pkid,psism.subctt_item_pkid" +
//            "                        order by psism.period_no desc)rn" +
            "            from " +
            "                  PROG_STL_ITEM_SUB_M psism" +
            "            inner join" +
            "                  PROG_STL_INFO eis" +
            "            on" +
            "                  psism.SUBCTT_PKID=eis.stl_pkid" +
            "            and" +
            "                  psism.PERIOD_NO=eis.PERIOD_NO" +
            "            and" +
            "                  eis.STL_TYPE='4'" +
            "            and" +
            "                  eis.FLOW_STATUS='2'" +
            "            and" +
            "                  eis.period_no<=#{strPeriodNo}" +
            "            group by " +
            "              psism.subctt_pkid,psism.subctt_item_pkid" +
            "           ) ta  ,PROG_STL_ITEM_SUB_M t2" +
            "           where  t2.subctt_pkid = ta.subctt_pkid " +
            "           and t2.subctt_item_pkid = ta.subctt_item_pkid " +
            "            and t2.period_no = ta.period_no " +
            "    )ps_info_item" +
            " on" +
            "    c_info_item.BELONG_TO_PKID=ps_info_item.subctt_pkid" +
            " and" +
            "    c_info_item.strItemPkid=ps_info_item.subctt_item_pkid" +
            " group by" +
            "    BELONG_TO_PKID,strSignPartName,strCorrespondingPkid,strItemPkid")
    List<QryShow> getCSStlMList(@Param("strCstplInfoPkid") String strCstplInfoPkid,
                                @Param("strPeriodNo") String strPeriodNo);

    /*@Select("select" +
            " ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            " eisseq.subctt_item_pkid as strItemPkid," +
            " nei.name as strSignPartName," +
            " max(ecitem.NAME) as strName," +
            " max(ecitem.unit) as strUnit," +
            " sum(ecitem.contract_quantity) as bdQuantity," +
            " sum(ecitem.contract_unit_price) as bdUnitPrice," +
            " sum(eisseq.begin_to_current_period_e_qty) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            " (" +
            "   select " +
            "          eis.stl_type," +
            "          eis.stl_pkid," +
            "          eis.period_no," +
            "          eicust.name" +
            "   from" +
            "          CTT_INFO ecinfo" +
            "   inner join " +
            "          SIGN_PART eicust" +   //连客户信息
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   and " +                            //已经批准了的分包合同
            "          ecinfo.FLOW_STATUS='3' " +
            "   inner join" +                    //已经登记了的工程数量结算
            "          PROG_STL_INFO eis" +
            "   on" +
            "          eis.STL_PKID=ecinfo.pkid" +
            "   and" +
            "          eis.STL_TYPE='3'" +
            "   and " +                        //已经复核了的工程数量结算
            "          eis.FLOW_STATUS='2'" +
            "   where" +                       //根据成本计划号得到分包合同列表
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //联分包工程数量详细项
            "    (" +
            "       select" +
            "            subctt_pkid,period_no,subctt_item_pkid," +
            "            (" +
            "               select max(begin_to_current_period_e_qty)" +
            "               from PROG_STL_ITEM_SUB_Q eisseqnei" +
            "               where eisseqnei.subctt_pkid=eisseqwai.subctt_pkid" +
            "                     and   eisseqnei.subctt_item_pkid=eisseqwai.subctt_item_pkid" +
            "                     and   eisseqnei.period_no<=#{strPeriodNo}) as begin_to_current_period_e_qty" +
            "      from" +
            "          PROG_STL_ITEM_SUB_Q eisseqwai" +
            "    )eisseq" +
            " on" +
            "    eisseq.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eisseq.period_no=nei.period_no" +
            " inner join " +                 //联分包合同关系表找到对应项
            "    CTT_ITEM ecitem" +
            " on" +
            "    ecitem.PKID=eisseq.subctt_item_pkid" +
            " and " +
            "    ecitem.belong_to_pkid=eisseq.subctt_pkid" +
            " and " +
            "    ecitem.belong_to_type='2'" +
            " group by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.period_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid" +
            " order by" +
            "    eisseq.subctt_pkid,nei.name,eisseq.period_no,ecitem.CORRESPONDING_PKID,eisseq.subctt_item_pkid")*/
    @Select("select" +
            "     c_info_item.strCorrespondingPkid," +
            "     c_info_item.strItemPkid," +
            "     c_info_item.strSignPartName," +
            "     max(c_info_item.NAME) as strName," +
            "     max(c_info_item.UNIT) as strUnit," +
            "     sum(c_info_item.CONTRACT_UNIT_PRICE) as bdUnitPrice," +
            "     sum(c_info_item.SIGN_PART_A_PRICE) as bdSignPartAMPrice," +
            "     sum(c_info_item.CONTRACT_QUANTITY) as bdQuantity," +
            "     sum(ps_info_item.current_period_e_qty) as bdCurrentPeriodQuantity," +
            "     sum(ps_info_item.begin_to_current_period_e_qty) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            "     (" +
            "       select " +
            "              citem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "              citem.PKID as strItemPkid," +
            "              sp.NAME as strSignPartName," +
            "              citem.NAME," +
            "              citem.UNIT," +
            "              citem.CONTRACT_UNIT_PRICE," +
            "              citem.SIGN_PART_A_PRICE," +
            "              citem.CONTRACT_QUANTITY," +
            "              citem.BELONG_TO_PKID" +
            "       from" +
            "             CTT_INFO cinfo" +
            "       inner join " +
            "              SIGN_PART sp" +
            "       on " +
            "              cinfo.SIGN_PART_B=sp.PKID" +
            "       inner join" +
            "              CTT_ITEM citem" +
            "       on" +
            "              cinfo.CTT_TYPE=citem.belong_to_type" +
            "       and" +
            "              cinfo.PKID=citem.belong_to_pkid" +
            "       where" +
            "              cinfo.PARENT_PKID = #{strCstplInfoPkid}" +
            "       and " +  //已经批准了的分包合同
            "              cinfo.FLOW_STATUS='3'" +
            "     )c_info_item" +
            " inner join" +//联分包工程数量详细项
            "    (" +
            "       select" +
            "            t2.subctt_pkid,t2.period_no,t2.subctt_item_pkid," +
            "            t2.current_period_e_qty,t2.begin_to_current_period_e_qty" +
            "       from (" +
            "             select" +
            "                  psisq.subctt_pkid,psisq.subctt_item_pkid," +
            "                   max(psisq.period_no) period_no " +
//            "                  psisq.current_period_e_qty,psisq.begin_to_current_period_e_qty," +
//            "                  row_number() over " +
//            "                       (partition by psisq.subctt_pkid,psisq.subctt_item_pkid" +
//            "                        order by psisq.period_no desc)rn" +
            "            from " +
            "                  PROG_STL_ITEM_SUB_Q psisq" +
            "            inner join" +
            "                  PROG_STL_INFO eis" +
            "            on" +
            "                  psisq.SUBCTT_PKID=eis.stl_pkid" +
            "            and" +
            "                  psisq.PERIOD_NO=eis.PERIOD_NO" +
            "            and" +
            "                  eis.STL_TYPE='3'" +
            "            and" +
            "                  eis.FLOW_STATUS='2'" +
            "            and" +
            "                  eis.period_no<=#{strPeriodNo}" +
            "           group by  psisq.subctt_pkid, psisq.subctt_item_pkid  ) ta " +
            "       , PROG_STL_ITEM_SUB_Q t2 " +
            "       where  " +
            "           t2.subctt_pkid=ta.subctt_pkid" +
            "       and  " +
            "           t2.subctt_item_pkid=ta.subctt_item_pkid " +
            "       and " +
            "           t2.period_no=ta.period_no " +
            "    )ps_info_item" +
            " on" +
            "    c_info_item.BELONG_TO_PKID=ps_info_item.subctt_pkid" +
            " and" +
            "    c_info_item.strItemPkid=ps_info_item.subctt_item_pkid" +
            " group by" +
            "    BELONG_TO_PKID,strSignPartName,strCorrespondingPkid,strItemPkid")
    List<QryShow> getCSStlQList(@Param("strCstplInfoPkid") String strCstplInfoPkid,
                                @Param("strPeriodNo") String strPeriodNo);

    @Select(" select " +
            "    CORRESPONDING_PKID as subcttItem_CorrPkid," +
            "    SIGN_PART_B_NAME as subcttItem_SignPartName," +
            "    SUBCTT_ITEM_NAME as subcttItem_Name," +
            "    UNIT_PRICE as subcttItem_UnitPrice," +
            "    THIS_STAGE_QTY as subcttStlItem_ThisStageQty," +
            "    ADD_UP_QTY as subcttStlItem_AddUpQty" +
            " from " +
            "    (" +
            "       select " +
            "            subPerEd.CORRESPONDING_PKID ," +
            "            subPerEd.SIGN_PART_B_NAME ," +
            "            eissep.SUBCTT_ITEM_NAME ," +
            "            max(eissep.UNIT_PRICE) as UNIT_PRICE," +
            "            sum(eissep.THIS_STAGE_QTY) as THIS_STAGE_QTY," +
            "            sum(eissep.ADD_UP_QTY) as ADD_UP_QTY" +
            "       from " +
                          // 某一个成本计划下对应的批准了的分包合同的详细内容
            "            ( " +
            "                 select" +
            "                      aprdsubctt.PKID as INFO_PKID," +
            "                      aprdsubctt.SIGN_PART_B_NAME," +
            "                      aprdsubctt.MAX_PERIOD_NO_INP," +
            "                      ecitem.PKID as ITEM_PKID," +
            "                      ecitem.CORRESPONDING_PKID" +
            "                 from " +
                                     // 某一个成本计划下对应的批准了的分包合同 并把它们产生的最近一期批准了的价格结算的期号记录下来
            "                     (" +
            "                         select  " +
            "                                ecinfo.PKID, " +
            "                                (" +
            "                                    select " +
            "                                         eicust.NAME " +
            "                                    from " +
            "                                         SIGN_PART eicust " +
            "                                    where " +
            "                                         eicust.PKID = ecinfo.SIGN_PART_B " +
            "                                )as SIGN_PART_B_NAME," +
            "                                (" +
            "                                    select " +
            "                                           max(eis.PERIOD_NO)" +
            "                                    from " +
            "                                       PROG_STL_INFO eis " +
            "                                    where " +
            "                                       eis.STL_TYPE='5'" +
            "                                    and " +
            "                                       eis.STL_PKID=ecinfo.PKID " +
            "                                    and " +
            "                                       eis.FLOW_STATUS>='3'" +
            "                                    and" +
            "                                       eis.PERIOD_NO<=#{strPeriodNo}" +
            "                                ) as MAX_PERIOD_NO_INP" +
            "                         from " +
            "                                CTT_INFO ecinfo " +
            "                         where                       " +
            "                                ecinfo.CTT_TYPE = '2' " +
            "                         and" +
            "                                ecinfo.PARENT_PKID = #{strCttInfoPkid}" +
            "                     ) aprdsubctt" +
                                     // 某一个成本计划下对应的批准了的分包合同 并把它们产生的最近一期批准了的价格结算的期号记录下来
            "                inner join" +
            "                     CTT_ITEM ecitem" +
            "                on" +
            "                   ecitem.belong_to_type='2'" +
            "                and" +
            "                   ecitem.belong_to_pkid=aprdsubctt.pkid " +
            "            )subPerEd " +
                          // 某一个成本计划下对应的批准了的分包合同的详细内容
            "       inner join" +
            "            PROG_STL_ITEM_SUB_STLMENT eissep" +
            "       on" +
            "            eissep.SUBSTL_TYPE='3'" +
            "       and" +
            "            eissep.SUBCTT_PKID=subPerEd.INFO_PKID" +
            "       and " +
            "            eissep.PERIOD_NO=subPerEd.MAX_PERIOD_NO_INP" +
            "       and" +
            "            eissep.SUBCTT_ITEM_PKID=subPerEd.ITEM_PKID" +
            "       group by subPerEd.CORRESPONDING_PKID,subPerEd.SIGN_PART_B_NAME,eissep.SUBCTT_ITEM_NAME" +
            "    ) t" +
            " where " +
            "    t.ADD_UP_QTY is not null" +
            " order by t.CORRESPONDING_PKID")
    List<QryTkMeaCSStlQShow> getCSStlQBySignPartList(@Param("strCttInfoPkid") String strCttInfoPkid,
                                                     @Param("strPeriodNo") String strPeriodNo);
    @Select(" select " +
            "    CORRESPONDING_PKID as subcttItem_CorrPkid," +
            "    SIGN_PART_B_NAME as subcttItem_SignPartName," +
            "    SUBCTT_ITEM_NAME as subcttItem_Name," +
            "    - UNIT_PRICE as subcttItem_UnitPrice," +  //  价格为负数
            "    THIS_STAGE_QTY as subcttStlItem_ThisStageMQty," +
            "    ADD_UP_QTY as subcttStlItem_AddUpMQty" +
            " from " +
            "    (" +
            "       select " +
            "            subPerEd.CORRESPONDING_PKID ," +
            "            subPerEd.SIGN_PART_B_NAME ," +
            "            eissep.SUBCTT_ITEM_NAME ," +
            "            max(eissep.UNIT_PRICE) as UNIT_PRICE," +
            "            sum(eissep.THIS_STAGE_QTY) as THIS_STAGE_QTY," +
            "            sum(eissep.ADD_UP_QTY) as ADD_UP_QTY" +
            "       from " +
            // 某一个成本计划下对应的批准了的分包合同的详细内容
            "            ( " +
            "                 select" +
            "                      aprdsubctt.PKID as INFO_PKID," +
            "                      aprdsubctt.SIGN_PART_B_NAME," +
            "                      aprdsubctt.MAX_PERIOD_NO_INP," +
            "                      ecitem.PKID as ITEM_PKID," +
            "                      ecitem.CORRESPONDING_PKID" +
            "                 from " +
            // 某一个成本计划下对应的批准了的分包合同 并把它们产生的最近一期批准了的价格结算的期号记录下来
            "                     (" +
            "                         select  " +
            "                                ecinfo.PKID, " +
            "                                (" +
            "                                    select " +
            "                                         eicust.NAME " +
            "                                    from " +
            "                                         SIGN_PART eicust " +
            "                                    where " +
            "                                         eicust.PKID = ecinfo.SIGN_PART_B " +
            "                                )as SIGN_PART_B_NAME," +
            "                                (" +
            "                                    select " +
            "                                           max(eis.PERIOD_NO)" +
            "                                    from " +
            "                                       PROG_STL_INFO eis " +
            "                                    where " +
            "                                       eis.STL_TYPE='5'" +
            "                                    and " +
            "                                       eis.STL_PKID=ecinfo.PKID " +
            "                                    and " +
            "                                       eis.FLOW_STATUS>='3'" +
            "                                    and" +
            "                                       eis.PERIOD_NO<=#{strPeriodNo}" +
            "                                ) as MAX_PERIOD_NO_INP" +
            "                         from " +
            "                                CTT_INFO ecinfo " +
            "                         where                       " +
            "                                ecinfo.CTT_TYPE = '2' " +
            "                         and" +
            "                                ecinfo.PARENT_PKID = #{strCttInfoPkid}" +
            "                     ) aprdsubctt" +
            // 某一个成本计划下对应的批准了的分包合同 并把它们产生的最近一期批准了的价格结算的期号记录下来
            "                inner join" +
            "                     CTT_ITEM ecitem" +
            "                on" +
            "                   ecitem.belong_to_type='2'" +
            "                and" +
            "                   ecitem.belong_to_pkid=aprdsubctt.pkid " +
            "            )subPerEd " +
            // 某一个成本计划下对应的批准了的分包合同的详细内容
            "       inner join" +
            "            PROG_STL_ITEM_SUB_STLMENT eissep" +
            "       on" +
            "            eissep.SUBSTL_TYPE='4'" +
            "       and" +
            "            eissep.SUBCTT_PKID=subPerEd.INFO_PKID" +
            "       and " +
            "            eissep.PERIOD_NO=subPerEd.MAX_PERIOD_NO_INP" +
            "       and" +
            "            eissep.SUBCTT_ITEM_PKID=subPerEd.ITEM_PKID" +
            "       group by subPerEd.CORRESPONDING_PKID,subPerEd.SIGN_PART_B_NAME,eissep.SUBCTT_ITEM_NAME" +
            "    ) t" +
            " where " +
            "    t.ADD_UP_QTY is not null" +
            " order by t.CORRESPONDING_PKID")
    List<QryTkMeaCSStlQShow> getCSStlMBySignPartList(@Param("strCttInfoPkid") String strCttInfoPkid,
                                                     @Param("strPeriodNo") String strPeriodNo);

    @Select("select PKID as pkid, "+
            "      SUBSTL_TYPE as substlType,"+
            "      ID as id,"+
            "      SUBCTT_PKID as subcttPkid,"+
            "      SUBCTT_NAME as subcttName,"+
            "      PERIOD_NO as periodNo,"+
            "      SUBCTT_ITEM_PKID as subcttItemPkid,"+
            "      SUBCTT_ITEM_NAME as subcttItemName,"+
            "      UNIT as unit,"+
            "      UNIT_PRICE as unitPrice,"+
            "      THIS_STAGE_QTY as thisStageQty,"+
            "      THIS_STAGE_AMT as thisStageAmt,"+
            "      ADD_UP_QTY as addUpQty,"+
            "      ADD_UP_AMT as addUpAmt,"+
            "      ARCHIVED_FLAG as archivedFlag,"+
            "      ORIGIN_FLAG as originFlag,"+
            "      CREATED_BY as createdBy,"+
            "      CREATED_BY_NAME as createdByName,"+
            "      CREATED_TIME as createdTime,"+
            "      LAST_UPD_BY as lastUpdBy,"+
            "      LAST_UPD_BY_NAME as lastUpdByName,"+
            "      LAST_UPD_TIME as lastUpdTime,"+
            "      REC_VERSION as recVersion,"+
            "      REMARK as remark,"+
            "      CONTRACT_QUANTITY as contractQuantity,"+
            "      CONTRACT_AMOUNT as contractAmount,"+
            "      STRNO as strno,"+
            "      ROW_NO as rowNo"+
            " from " +
            "      PROG_STL_ITEM_SUB_STLMENT" +
            " where " +
            "      (add_up_amt is not null or subctt_item_pkid like '%stl%')" +
            " and " +
            "      subctt_pkid=#{strSubcttPkid}"+
            " and " +
            "      period_no=#{strPeriodNo}"+
            " order by row_no")
    List<ProgStlItemSubStlment> getProgStlItemSubStlmentListAccount(@Param("strSubcttPkid") String strSubcttPkid,
                                                                    @Param("strPeriodNo") String strPeriodNo);
}
