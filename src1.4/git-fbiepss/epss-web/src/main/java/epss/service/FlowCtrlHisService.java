package epss.service;

import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.OperMapper;
import epss.repository.dao.not_mybatis.MyFlowCtrlHisMapper;
import epss.repository.dao.not_mybatis.MyOperResMapper;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import epss.repository.model.Oper;
import epss.repository.model.OperExample;
import epss.repository.model.model_show.FlowCtrlHisShow;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
    @Resource
    private MyOperResMapper myOperResMapper;

    public List<FlowCtrlHis> selectListByModel(FlowCtrlHisShow flowCtrlHisShow) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getInfoType()) + "%")
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getInfoPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getPeriodNo()) + "%");
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%"+ flowCtrlHisShow.getFlowStatus()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getFlowStatusReason()).equals("")){
            criteria.andFlowStatusReasonLike("%"+ flowCtrlHisShow.getFlowStatusReason()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowCtrlHisShow.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ flowCtrlHisShow.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHisShow.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ flowCtrlHisShow.getRemark()+"%");
        }
        example.setOrderByClause("INFO_TYPE ASC,INFO_PKID ASC,PERIOD_NO ASC,FLOW_STATUS ASC,CREATED_TIME ASC") ;
        return flowCtrlHisMapper.selectByExample(example);
    }

    /**
     * 2015-03-24追加
     * auto:hu
     * @param flowCtrlHisShowParam
     * @return
     */
    public List<FlowCtrlHisShow> selectListByFlowCtrlHis(FlowCtrlHisShow flowCtrlHisShowParam) {
        int i=0;
        List<FlowCtrlHisShow> flowCtrlshowHises = myFlowCtrlHisMapper.selectListByFlowCtrlHis(flowCtrlHisShowParam);
        List<FlowCtrlHisShow> flowCtrlHisShowList = new ArrayList<>();
        List<FlowCtrlHisShow> returnFlowCtrlHisShowList = new ArrayList<>();
        for (FlowCtrlHisShow flowCtrlshowHis : flowCtrlshowHises) {
            if(i==0){
                flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                flowCtrlHisShowList.add(flowCtrlshowHis);
            }else{
                String perioNo = flowCtrlshowHises.get(i-1).getPeriodNo()==null?"": flowCtrlshowHises.get(i-1).getPeriodNo();
                String perioNo2 = flowCtrlshowHis.getPeriodNo()==null?"": flowCtrlshowHis.getPeriodNo();
                if(flowCtrlshowHises.get(i-1).getInfoPkid().equals(flowCtrlshowHis.getInfoPkid())
                        && flowCtrlshowHises.get(i-1).getInfoType().equals(flowCtrlshowHis.getInfoType())
                        &&perioNo.equals(perioNo2)){
                    //取出上一次的endTime 就是这次的开始时间
                    String benginTime = flowCtrlshowHises.get(i-1).getEndTime();
                    //取出当前这次createTime就是这次的结束时间
                    String endTime = flowCtrlshowHis.getCreatedTime();
                    flowCtrlshowHis.setCreatedTime(benginTime);
                    flowCtrlshowHis.setEndTime(endTime);
                    flowCtrlHisShowList.add(flowCtrlshowHis);
                }else{
                     //判断上一次的FlowStatus是否为3
                    //如果不等于数据库中FlowStatus的最大数，
                    // 则说明这个操作没有执行完，
                    //要为下一状态添加开始时间
                    if(!"3".equals(flowCtrlshowHises.get(i-1).getFlowStatus())){
                        //取出上一個FlowCtrlShow
                        FlowCtrlHisShow fcs = flowCtrlshowHises.get(i-1);
                        FlowCtrlHisShow fcsParam = new FlowCtrlHisShow();
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
                        //负责人
                        fcsParam.setCreatedByName(
                                myOperResMapper.getCreateByNameByFlowStatusAndInfoPkid(fcsParam.getInfoPkid(),
                                        fcsParam.getFlowStatus(),
                                        fcsParam.getInfoType()));
                        flowCtrlHisShowList.add(fcsParam);
                        //如果FlowStatus是0时，可以把它本身的开始时间设置为本身的结束时间，为下一状态使用
                        if("0".equals(flowCtrlshowHis.getFlowStatus())){
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                        }else{
                            //如果不是0的时候要把数据库的这条数据的creattime变成它的结束时间，并且开始时间为空
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                            //flowCtrlshow.setCreatedTime("");
                        }
                    }else{
                        //上一次FlowStatus是3时
                        //并且本身这次为0是
                        // "5".equals(flowCtrlshow.getInfoType())
                        //if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                        //}
                    }
                    flowCtrlHisShowList.add(flowCtrlshowHis);
                }
            }

            //防止flowStatus为相同并且flowStatus不等于3 要在最后一条加上下一状态
            if(i== flowCtrlshowHises.size()-1&&!"3".equals(flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus())){
                FlowCtrlHisShow fcsParam = new FlowCtrlHisShow();
                try {
                    fcsParam = (FlowCtrlHisShow) BeanUtils.cloneBean(flowCtrlshowHises.get(flowCtrlshowHises.size()-1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //状态就是上这次次状态加1，作为下一次的状态
                fcsParam.setFlowStatus("".equals
                                            (flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus())
                                                 || flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus()==null?"" :
                                                 String.valueOf(Integer.parseInt(flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus()) + 1
                                            )
                                       );
                //负责人
                fcsParam.setCreatedByName(
                        myOperResMapper.getCreateByNameByFlowStatusAndInfoPkid(fcsParam.getInfoPkid(),
                                                                                 fcsParam.getFlowStatus(),
                                                                                 fcsParam.getInfoType()));
                fcsParam.setCreatedTime(fcsParam.getEndTime());
                fcsParam.setEndTime("");
                flowCtrlHisShowList.add(fcsParam);
            }
           i++;
        }

        //去掉FlowStatus为0 也就是初始
        for(FlowCtrlHisShow flowCtrlHisShow : flowCtrlHisShowList){
            if(!"0".equals(flowCtrlHisShow.getFlowStatus())){
                if("".equals(flowCtrlHisShowParam.getFinishFlag())) {

                }else if("0".equals(flowCtrlHisShowParam.getFinishFlag())) {
                    if (StringUtils.isNotBlank(flowCtrlHisShow.getEndTime())) {
                        continue;
                    }
                }else if("1".equals(flowCtrlHisShowParam.getFinishFlag())) {
                    if (!StringUtils.isNotBlank(flowCtrlHisShow.getEndTime())) {
                        continue;
                    }
                }

                //去除分包数量结算和分包材料结算的批准
                if ("3".equals(flowCtrlHisShow.getInfoType()) || "4".equals(flowCtrlHisShow.getInfoType())) {
                    if ("3".equals(flowCtrlHisShow.getFlowStatus())) {
                        continue;
                    }
                }
                //从所有的数据中筛选符合页面上搜索的创建记录名
                //因为这样就不会和时间有冲突了
                if(StringUtils.isNotBlank(flowCtrlHisShowParam.getCreatedByName())){
                    if(StringUtils.isNotBlank(flowCtrlHisShow.getCreatedByName())&&
                            !flowCtrlHisShow.getCreatedByName().contains(flowCtrlHisShowParam.getCreatedByName().trim())){
                        continue;
                    }
                }
                returnFlowCtrlHisShowList.add(flowCtrlHisShow);
            }
        }
        return returnFlowCtrlHisShowList;
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

    public FlowCtrlHis fromModelShowToModel(FlowCtrlHisShow flowCtrlHisShowPara) {
        FlowCtrlHis cttInfoTemp = new FlowCtrlHis();
        cttInfoTemp.setPkid(flowCtrlHisShowPara.getPkid());
        cttInfoTemp.setTid(flowCtrlHisShowPara.getTid());
        cttInfoTemp.setInfoType(flowCtrlHisShowPara.getInfoType());
        cttInfoTemp.setInfoPkid(flowCtrlHisShowPara.getInfoPkid());
        cttInfoTemp.setInfoId(flowCtrlHisShowPara.getInfoId());
        cttInfoTemp.setInfoName(flowCtrlHisShowPara.getInfoName());
        cttInfoTemp.setPeriodNo(flowCtrlHisShowPara.getPeriodNo());
        cttInfoTemp.setFlowStatus(flowCtrlHisShowPara.getFlowStatus());
        cttInfoTemp.setFlowStatusReason(flowCtrlHisShowPara.getFlowStatusReason());
        cttInfoTemp.setFlowStatusRemark(flowCtrlHisShowPara.getFlowStatusRemark());
        cttInfoTemp.setCreatedBy(flowCtrlHisShowPara.getCreatedBy());
        cttInfoTemp.setCreatedByName(flowCtrlHisShowPara.getCreatedByName());
        cttInfoTemp.setCreatedTime(flowCtrlHisShowPara.getCreatedTime());
        cttInfoTemp.setRemark(flowCtrlHisShowPara.getRemark());
        cttInfoTemp.setOperType(flowCtrlHisShowPara.getOperType());
        cttInfoTemp.setArchivedFlag(flowCtrlHisShowPara.getArchivedFlag());
        return cttInfoTemp;
    }
    public FlowCtrlHisShow fromModelToModelShow(FlowCtrlHis flowCtrlHisPara) {
        FlowCtrlHisShow cttInfoShowTemp = new FlowCtrlHisShow();
        cttInfoShowTemp.setPkid(flowCtrlHisPara.getPkid());
        cttInfoShowTemp.setTid(flowCtrlHisPara.getTid());
        cttInfoShowTemp.setInfoType(flowCtrlHisPara.getInfoType());
        cttInfoShowTemp.setInfoPkid(flowCtrlHisPara.getInfoPkid());
        cttInfoShowTemp.setInfoId(flowCtrlHisPara.getInfoId());
        cttInfoShowTemp.setInfoName(flowCtrlHisPara.getInfoName());
        cttInfoShowTemp.setPeriodNo(flowCtrlHisPara.getPeriodNo());
        cttInfoShowTemp.setFlowStatus(flowCtrlHisPara.getFlowStatus());
        cttInfoShowTemp.setFlowStatusReason(flowCtrlHisPara.getFlowStatusReason());
        cttInfoShowTemp.setFlowStatusRemark(flowCtrlHisPara.getFlowStatusRemark());
        cttInfoShowTemp.setCreatedBy(flowCtrlHisPara.getCreatedBy());
        cttInfoShowTemp.setCreatedByName(flowCtrlHisPara.getCreatedByName());
        cttInfoShowTemp.setCreatedTime(flowCtrlHisPara.getCreatedTime());
        cttInfoShowTemp.setRemark(flowCtrlHisPara.getRemark());
        cttInfoShowTemp.setOperType(flowCtrlHisPara.getOperType());
        cttInfoShowTemp.setArchivedFlag(flowCtrlHisPara.getArchivedFlag());
        return cttInfoShowTemp;
    }
}
