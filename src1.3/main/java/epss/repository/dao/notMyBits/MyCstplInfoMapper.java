package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.CstplInfoShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: обнГ8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyCstplInfoMapper {
    @Select("select max(id) from cstpl_info")
    String getStrMaxCstplInfoId();

    List<CstplInfoShow> getCstplInfoListByFlowStatusBegin_End(CstplInfoShow cstplInfoShowPara);
}