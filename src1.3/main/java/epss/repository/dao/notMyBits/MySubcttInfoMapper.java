package epss.repository.dao.notMyBits;

import epss.repository.model.model_show.SubcttInfoShow;
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
public interface MySubcttInfoMapper {
    @Select("select max(id) from subctt_info")
    String getStrMaxSubcttInfoId();

    List<SubcttInfoShow> getSubcttInfoListByFlowStatusBegin_End(SubcttInfoShow tkcttInfoShowPara);
    List<SubcttInfoShow> getSubcttInfoShowListByShowModel(SubcttInfoShow tkcttInfoShowPara);
}