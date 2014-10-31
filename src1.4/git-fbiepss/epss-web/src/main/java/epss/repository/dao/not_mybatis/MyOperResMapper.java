package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.DeptOperShow;
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
    List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara);

    @Select("select distinct" +
            "   opr.INFO_PKID as infoPkid, " +
            "   eci.NAME as infoPkidName" +
            " from " +
            "   OPER_RES opr" +
            " inner join" +
            "   CTT_INFO eci" +
            " on" +
            "   opr.INFO_PKID=eci.PKID" +
            " where " +
            "   opr.INFO_TYPE=#{strInfoType}" +
            " and" +
            "   opr.FLOW_STATUS=#{strFlowStatus}"+
            " and" +
            "   opr.OPER_PKID=#{strOperPkid}")
    List<OperResShow> getInfoListByOperFlowPkid(@Param("strInfoType") String strInfoType,
                                                @Param("strFlowStatus") String strFlowStatus,
                                                @Param("strOperPkid") String strOperPkid);
}
