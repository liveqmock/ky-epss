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
 * Time: ����8:10
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
            " inner join" +                   //���ְ�����������ϸ��
            "    ��" +
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
            "    ��eissem" +
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
            " sum(ecitem.contract_unit_price) as bdUnitPrice��" +
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
            "          ES_INIT_CUST eicust" +   //���ͻ���Ϣ
            "   on " +
            "          ecinfo.SIGN_PART_B=eicust.PKID" +
            "   inner join " +                    //�Ѿ���׼�˵ķְ���ͬ
            "          ES_INIT_POWER eip" +
            "   on " +
            "          eip.power_type=ecinfo.ctt_type" +
            "   and " +
            "          eip.power_pkid=ecinfo.pkid" +
            "   and " +
            "          eip.period_no= 'NULL'" +
            "   and " +
            "          eip.status_flag='3' " +
            "   inner join" +                    //�Ѿ��Ǽ��˵Ĺ�����������
            "          ES_INIT_STL eis" +
            "   on" +
            "          eis.stl_pkid=ecinfo.pkid" +
            "   and" +
            "          eis.stl_type='3'" +
            "   inner join " +                  //�Ѿ���׼�˵Ĺ�����������
            "          ES_INIT_POWER eip2" +
            "   on " +
            "          eip2.power_type=eis.stl_type" +
            "   and " +
            "          eip2.power_pkid=eis.stl_pkid" +
            "   and " +
            "          eip2.period_no= eis.period_no" +
            "   and " +
            "          eip2.status_flag='3'" +
            "   where" +                       //���ݳɱ��ƻ��ŵõ��ְ���ͬ�б�
            "          ecinfo.CTT_TYPE = '2'" +
            "   and " +
            "          ecinfo.PARENT_PKID = #{strParentPkid}" +
            " )nei" +
            " inner join" +                   //���ְ�����������ϸ��
            "    ��" +
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
            "    ��eisseq" +
            " on" +
            "    eisseq.subctt_pkid=nei.stl_pkid" +
            " and" +
            "    eisseq.period_no=nei.period_no" +
            " inner join " +                 //���ְ���ͬ��ϵ���ҵ���Ӧ��
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
            "    strCorrespondingPkid," +
            "    strName," +
            "    bdUnitPrice," +
            "    bdCurrentPeriodQuantity," +
            "    bdBeginToCurrentPeriodQuantity" +
            " from " +
            "    (" +
            "       select " +
            "            subPerEd.CORRESPONDING_PKID as strCorrespondingPkid," +
            "            subPerEd.SIGN_PART_B_NAME as strName," +
            "            max(eissep.UNIT_PRICE) as bdUnitPrice," +
            "            sum(eissep.THIS_STAGE_QTY) as bdCurrentPeriodQuantity," +
            "            sum(eissep.ADD_UP_QTY) as bdBeginToCurrentPeriodQuantity" +
            "       from " +
                          // ĳһ���ɱ��ƻ��¶�Ӧ����׼�˵ķְ���ͬ����ϸ����
            "            ( " +
            "                 select" +
            "                      aprdsubctt.PKID as INFO_PKID," +
            "                      aprdsubctt.SIGN_PART_B_NAME," +
            "                      aprdsubctt.MAX_PERIOD_NO_INP," +
            "                      ecitem.PKID as ITEM_PKID," +
            "                      ecitem.CORRESPONDING_PKID" +
            "                 from " +
                                     // ĳһ���ɱ��ƻ��¶�Ӧ����׼�˵ķְ���ͬ �������ǲ��������һ����׼�˵ļ۸������ںż�¼����
            "                     (" +
            "                         select  " +
            "                                ecinfo.PKID, " +
            "                                (" +
            "                                    select " +
            "                                         eicust.NAME " +
            "                                    from " +
            "                                         ES_INIT_CUST eicust " +
            "                                    where " +
            "                                         eicust.PKID = ecinfo.SIGN_PART_B " +
            "                                )as SIGN_PART_B_NAME," +
            "                                (" +
            "                                    select " +
            "                                           max(PERIOD_NO)" +
            "                                    from " +
            "                                       ES_INIT_POWER eip " +
            "                                    where " +
            "                                       eip.POWER_TYPE='5'" +
            "                                    and " +
            "                                       eip.POWER_PKID=ecinfo.PKID " +
            "                                    and " +
            "                                       eip.STATUS_FLAG>='3'" +
            "                                    and" +
            "                                       eip.PERIOD_NO<=#{strPeriodNo}" +
            "                                ) as MAX_PERIOD_NO_INP" +
            "                         from " +
            "                                ES_CTT_INFO ecinfo " +
            "                         inner join " +
            "                                ES_INIT_POWER eip " +
            "                         on " +
            "                                eip.POWER_TYPE=ecinfo.CTT_TYPE " +
            "                         and " +
            "                                eip.POWER_PKID=ecinfo.PKID " +
            "                         and " +
            "                                eip.PERIOD_NO='NULL' " +
            "                         and " +
            "                                eip.STATUS_FLAG='3' " +
            "                         where                       " +
            "                                ecinfo.CTT_TYPE = '2' " +
            "                         and" +
            "                                ecinfo.PARENT_PKID = #{strCstplInfoPkid}" +
            "                     ) aprdsubctt" +
                                     // ĳһ���ɱ��ƻ��¶�Ӧ����׼�˵ķְ���ͬ �������ǲ��������һ����׼�˵ļ۸������ںż�¼����
            "                inner join" +
            "                     ES_CTT_ITEM ecitem" +
            "                on" +
            "                   ecitem.belong_to_type='2'" +
            "                and" +
            "                   ecitem.belong_to_pkid=aprdsubctt.pkid " +
            "            )subPerEd " +
                          // ĳһ���ɱ��ƻ��¶�Ӧ����׼�˵ķְ���ͬ����ϸ����
            "       inner join" +
            "            ES_ITEM_STL_SUBCTT_ENG_P eissep" +
            "       on" +
            "            eissep.SUBSTL_TYPE='3'" +
            "       and" +
            "            eissep.SUBCTT_PKID=subPerEd.INFO_PKID" +
            "       and " +
            "            eissep.PERIOD_NO=subPerEd.MAX_PERIOD_NO_INP" +
            "       and" +
            "            eissep.SUBCTT_ITEM_PKID=subPerEd.ITEM_PKID" +
            "       group by subPerEd.CORRESPONDING_PKID,subPerEd.SIGN_PART_B_NAME " +
            "    ) t" +
            " where " +
            "    t.bdBeginToCurrentPeriodQuantity is not null" +
            " order by t.strCorrespondingPkid")
    List<QryShow> getCSStlQBySignPartList(@Param("strCstplInfoPkid") String strCstplInfoPkid,
                                          @Param("strPeriodNo") String strPeriodNo);
}
