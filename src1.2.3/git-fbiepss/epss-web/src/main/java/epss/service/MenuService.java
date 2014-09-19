package epss.service;

import epss.repository.dao.PtmenuMapper;
import epss.repository.dao.not_mybatis.MyMenuMapper;
import epss.repository.model.Ptmenu;
import epss.repository.model.PtmenuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MenuService {
    @Autowired
    private MyMenuMapper myMenuMapper;
    @Autowired
    private PtmenuMapper ptmenuMapper;

    public List<Ptmenu> selectListByModel(Ptmenu ptmenuPara) {
        PtmenuExample example= new PtmenuExample();
        PtmenuExample.Criteria criteria = example.createCriteria();
        criteria.andMenuidLike("%" + ToolUtil.getStrIgnoreNull(ptmenuPara.getMenuid()) + "%")
                .andMenulabelLike("%" + ToolUtil.getStrIgnoreNull(ptmenuPara.getMenulabel()) + "%")
                .andTargetmachineLike("%" + ToolUtil.getStrIgnoreNull(ptmenuPara.getTargetmachine()) + "%");
        if (!ToolUtil.getStrIgnoreNull(ptmenuPara.getMenudesc()).equals("")){
            criteria.andMenudescLike("%"+ptmenuPara .getMenudesc()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(ptmenuPara.getMenuType()).equals("")){
            criteria.andMenuTypeLike("%"+ptmenuPara .getMenuType()+"%");
        }
        example.setOrderByClause("MENUDESC ASC") ;
        return ptmenuMapper.selectByExample(example);
    }

    public Integer getStrMaxIdx(){
        return myMenuMapper.getStrMaxIdx();
    }

    public List<Ptmenu> selectListByModel() {
        PtmenuExample example= new PtmenuExample();
        PtmenuExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("ID ASC") ;
        return ptmenuMapper.selectByExample(example);
    }

    public boolean isExistInDb(Ptmenu ptmenuPara) {
        PtmenuExample example = new PtmenuExample();
        example.createCriteria().andMenuidEqualTo(ptmenuPara.getMenuid());
        return ptmenuMapper .countByExample(example) >= 1;
    }

    public void insertRecord(Ptmenu ptmenuPara) {
        ptmenuMapper.insert(ptmenuPara);
    }

    public void updateRecord(Ptmenu ptmenuPara){
        ptmenuMapper.updateByPrimaryKey(ptmenuPara);
    }

    public int deleteRecord(Ptmenu ptmenuPara){
        return ptmenuMapper.deleteByPrimaryKey(ptmenuPara.getPkid());
    }
}
