package epss.service;

import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.OperMapper;
import epss.repository.dao.not_mybatis.MyFlowCtrlHisMapper;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import epss.repository.model.Oper;
import epss.repository.model.OperExample;
import epss.repository.model.model_show.FlowCtrlShow;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FlowCtrlHisService {
    @Resource
    private FlowCtrlHisMapper flowCtrlHisMapper;
    @Resource
    private MyFlowCtrlHisMapper myFlowCtrlHisMapper;
    @Resource
    private OperMapper operMapper;

    public List<FlowCtrlHis> selectListByModel(FlowCtrlHis flowCtrlHis) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoType()) + "%")
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getPeriodNo()) + "%");
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%"+ flowCtrlHis.getFlowStatus()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatusReason()).equals("")){
            criteria.andFlowStatusReasonLike("%"+ flowCtrlHis.getFlowStatusReason()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowCtrlHis.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ flowCtrlHis.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ flowCtrlHis.getRemark()+"%");
        }
        example.setOrderByClause("INFO_TYPE ASC,INFO_PKID ASC,PERIOD_NO ASC,FLOW_STATUS ASC,CREATED_TIME ASC") ;
        return flowCtrlHisMapper.selectByExample(example);
    }

    /**
     * 2015-03-24追加
     * auto:hu
     * @param flowCtrlShowParam
     * @return
     */
    public List<FlowCtrlShow> selectListByFlowCtrlHis(FlowCtrlShow flowCtrlShowParam) {
        int i=0;
        List<FlowCtrlShow> flowCtrlshows = myFlowCtrlHisMapper.selectListByFlowCtrlHis(flowCtrlShowParam);
        List<FlowCtrlShow> flowCtrlShowList = new ArrayList<>();
        List<FlowCtrlShow> returnFlowCtrlShowList = new ArrayList<>();

        for (FlowCtrlShow flowCtrlshow: flowCtrlshows) {
            if(i==0){
                flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                flowCtrlShowList.add(flowCtrlshow);
            }else{
                if(flowCtrlShowList.get(i-1).getInfoPkid().equals(flowCtrlshow.getInfoPkid())&&flowCtrlShowList.get(i-1).getInfoType().equals(flowCtrlshow.getInfoType())){
                    //取出上一次的endTime 就是这次的开始时间
                    String benginTime = flowCtrlShowList.get(i-1).getEndTime();
                    //取出当前这次createTime就是这次的结束时间
                    String endTime = flowCtrlshow.getCreatedTime();
                    flowCtrlshow.setCreatedTime(benginTime);
                    flowCtrlshow.setEndTime(endTime);
                    flowCtrlShowList.add(flowCtrlshow);
                }else{
                    flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());

                    flowCtrlShowList.add(flowCtrlshow);
                }
            }
           i++;
        }
        //去掉FlowStatus为0 也就是初始
        for(FlowCtrlShow flowCtrlShow: flowCtrlShowList){
            if(!"0".equals(flowCtrlShow.getFlowStatus())){
                returnFlowCtrlShowList.add(flowCtrlShow);
            }

        }

        return returnFlowCtrlShowList;

    }//追加结束


    public List<FlowCtrlHis> getSubStlListByFlowCtrlHis(String powerPkid,String periodNo){
        return myFlowCtrlHisMapper.getSubStlListByFlowCtrlHis(powerPkid,periodNo);
    }

    public List<FlowCtrlHis> selectListByModel(String strInfoType,String strInfoPkid) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + strInfoType + "%")
                .andInfoPkidLike("%" + strInfoPkid + "%");
        return flowCtrlHisMapper.selectByExample(example);
    }

    public void insertRecord(FlowCtrlHis flowCtrlHisPara) {
        String strOperatorPkidTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strOperatorNameTemp=ToolUtil.getOperatorManager().getOperator().getName();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        flowCtrlHisPara.setCreatedBy(strOperatorPkidTemp);
        flowCtrlHisPara.setCreatedByName(strOperatorNameTemp);
        flowCtrlHisPara.setCreatedTime(strLastUpdTimeTemp);
        flowCtrlHisPara.setArchivedFlag("0");
        flowCtrlHisMapper.insertSelective(flowCtrlHisPara);
    }

    public List<Oper> selectOperByExample(String strId){
        OperExample example= new OperExample();
        OperExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + strId + "%");
        return operMapper.selectByExample(example);
    }
}
