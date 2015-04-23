package epss.service;

import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.OperMapper;
import epss.repository.dao.not_mybatis.MyFlowCtrlHisMapper;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import epss.repository.model.Oper;
import epss.repository.model.OperExample;
import epss.repository.model.model_show.FlowCtrlShow;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import skyline.util.ToolUtil;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
                String perioNo = flowCtrlshows.get(i-1).getPeriodNo()==null?"":flowCtrlshows.get(i-1).getPeriodNo();
                String perioNo2 = flowCtrlshow.getPeriodNo()==null?"":flowCtrlshow.getPeriodNo();
                if(flowCtrlshows.get(i-1).getInfoPkid().equals(flowCtrlshow.getInfoPkid())
                        &&flowCtrlshows.get(i-1).getInfoType().equals(flowCtrlshow.getInfoType())
                        &&perioNo.equals(perioNo2)){
                    //取出上一次的endTime 就是这次的开始时间
                    String benginTime = flowCtrlshows.get(i-1).getEndTime();
                    //取出当前这次createTime就是这次的结束时间
                    String endTime = flowCtrlshow.getCreatedTime();
                    flowCtrlshow.setCreatedTime(benginTime);
                    flowCtrlshow.setEndTime(endTime);
                    flowCtrlShowList.add(flowCtrlshow);
                }else{
                     //判断上一次的FlowStatus是否为3
                    //如果不等于数据库中FlowStatus的最大数，
                    // 则说明这个操作没有执行完，
                    //要为下一状态添加开始时间
                    if(!"3".equals(flowCtrlshows.get(i-1).getFlowStatus())){
                        //取出上一個FlowCtrlShow
                        FlowCtrlShow fcs = flowCtrlshows.get(i-1);
                        FlowCtrlShow fcsParam = new FlowCtrlShow();
                        //赋值时一定不要写成 fcsParam = fcs 如果写成这样最后往List.add时会把上一条数据改变
                        fcsParam.setInfoPkid(fcs.getInfoPkid());
                        fcsParam.setInfoType(fcs.getInfoType());
                        //期数编码
                        fcsParam.setPeriodNo(fcs.getPeriodNo());
                        //設置開始時間就是上次的結束時間
                        fcsParam.setCreatedTime(fcs.getEndTime());
                        //状态就是上一次状态加1,作为下一次的状态
                        fcsParam.setFlowStatus("".equals(fcs.getFlowStatus())||
                                                         fcs.getFlowStatus()==null?"" :
                                                          String.valueOf(Integer.parseInt(fcs.getFlowStatus()) + 1
                                                        )
                                              );
                        flowCtrlShowList.add(fcsParam);
                        //如果FlowStatus是0时，可以把它本身的开始时间设置为本身的结束时间，为下一状态使用
                        if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                        }else{
                            //如果不是0的时候要把数据库的这条数据的creattime变成它的结束时间，并且开始时间为空
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                            //flowCtrlshow.setCreatedTime("");
                        }
                    }else{
                        //上一次FlowStatus是3时
                        //并且本身这次为0是
                        // "5".equals(flowCtrlshow.getInfoType())
                        //if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                        //}
                    }
                    flowCtrlShowList.add(flowCtrlshow);
                }
            }

            //防止flowStatus为相同并且flowStatus不等于3 要在最后一条加上下一状态
            if(i==flowCtrlshows.size()-1&&!"3".equals(flowCtrlshows.get(flowCtrlshows.size()-1).getFlowStatus())){
                FlowCtrlShow fcsParam = new FlowCtrlShow();
                try {
                    fcsParam = (FlowCtrlShow) BeanUtils.cloneBean(flowCtrlshows.get(flowCtrlshows.size()-1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //状态就是上这次次状态加1，作为下一次的状态
                fcsParam.setFlowStatus("".equals(flowCtrlshows.get(flowCtrlshows.size()-1).getFlowStatus())
                                                 ||flowCtrlshows.get(flowCtrlshows.size()-1).getFlowStatus()==null?"" :
                                                   String.valueOf(Integer.parseInt(flowCtrlshows.get(flowCtrlshows.size()-1).getFlowStatus()) + 1
                                                 )
                                       );
                fcsParam.setCreatedByName("");
                fcsParam.setCreatedTime(fcsParam.getEndTime());
                fcsParam.setEndTime("");
                flowCtrlShowList.add(fcsParam);
            }
           i++;
        }

        //去掉FlowStatus为0 也就是初始
        for(FlowCtrlShow flowCtrlShow: flowCtrlShowList){
            if(!"0".equals(flowCtrlShow.getFlowStatus())){
                //从所有的数据中筛选符合页面上搜索的创建记录名
                //因为这样就不会和时间有冲突了
                if(StringUtils.isNotBlank(flowCtrlShowParam.getCreatedByName())){
                    if(StringUtils.isNotBlank(flowCtrlShow.getCreatedByName())&&
                       flowCtrlShow.getCreatedByName().contains(flowCtrlShowParam.getCreatedByName().trim())){
                        returnFlowCtrlShowList.add(flowCtrlShow);
                    }
                }else{
                    //去除分包数量结算和分包材料结算的批准
                    if("3".equals(flowCtrlShow.getInfoType())||"4".equals(flowCtrlShow.getInfoType())){
                        if(!"3".equals(flowCtrlShow.getFlowStatus())){
                            returnFlowCtrlShowList.add(flowCtrlShow);
                        }
                    }else{
                        returnFlowCtrlShowList.add(flowCtrlShow);
                    }
                }
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
