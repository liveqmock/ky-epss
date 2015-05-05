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
 * Time: ����6:31
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
     * 2015-03-24׷��
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
                    //ȡ����һ�ε�endTime ������εĿ�ʼʱ��
                    String benginTime = flowCtrlshowHises.get(i-1).getEndTime();
                    //ȡ����ǰ���createTime������εĽ���ʱ��
                    String endTime = flowCtrlshowHis.getCreatedTime();
                    flowCtrlshowHis.setCreatedTime(benginTime);
                    flowCtrlshowHis.setEndTime(endTime);
                    flowCtrlHisShowList.add(flowCtrlshowHis);
                }else{
                     //�ж���һ�ε�FlowStatus�Ƿ�Ϊ3
                    //������������ݿ���FlowStatus���������
                    // ��˵���������û��ִ���꣬
                    //ҪΪ��һ״̬��ӿ�ʼʱ��
                    if(!"3".equals(flowCtrlshowHises.get(i-1).getFlowStatus())){
                        //ȡ����һ��FlowCtrlShow
                        FlowCtrlHisShow fcs = flowCtrlshowHises.get(i-1);
                        FlowCtrlHisShow fcsParam = new FlowCtrlHisShow();
                        //��ֵʱһ����Ҫд�� fcsParam = fcs ���д�����������List.addʱ�����һ�����ݸı�
                        fcsParam.setInfoPkid(fcs.getInfoPkid());
                        fcsParam.setInfoType(fcs.getInfoType());
                        //��������
                        fcsParam.setPeriodNo(fcs.getPeriodNo());
                        //�O���_ʼ�r�g�����ϴεĽY���r�g
                        fcsParam.setCreatedTime(fcs.getEndTime());
                        //״̬������һ��״̬��1,��Ϊ��һ�ε�״̬
                        fcsParam.setFlowStatus("".equals(fcs.getFlowStatus())||
                                                         fcs.getFlowStatus()==null?"" :
                                                          String.valueOf(Integer.parseInt(fcs.getFlowStatus()) + 1
                                                        )
                                              );
                        //������
                        fcsParam.setCreatedByName(
                                myOperResMapper.getCreateByNameByFlowStatusAndInfoPkid(fcsParam.getInfoPkid(),
                                        fcsParam.getFlowStatus(),
                                        fcsParam.getInfoType()));
                        flowCtrlHisShowList.add(fcsParam);
                        //���FlowStatus��0ʱ�����԰�������Ŀ�ʼʱ������Ϊ����Ľ���ʱ�䣬Ϊ��һ״̬ʹ��
                        if("0".equals(flowCtrlshowHis.getFlowStatus())){
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                        }else{
                            //�������0��ʱ��Ҫ�����ݿ���������ݵ�creattime������Ľ���ʱ�䣬���ҿ�ʼʱ��Ϊ��
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                            //flowCtrlshow.setCreatedTime("");
                        }
                    }else{
                        //��һ��FlowStatus��3ʱ
                        //���ұ������Ϊ0��
                        // "5".equals(flowCtrlshow.getInfoType())
                        //if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshowHis.setEndTime(flowCtrlshowHis.getCreatedTime());
                        //}
                    }
                    flowCtrlHisShowList.add(flowCtrlshowHis);
                }
            }

            //��ֹflowStatusΪ��ͬ����flowStatus������3 Ҫ�����һ��������һ״̬
            if(i== flowCtrlshowHises.size()-1&&!"3".equals(flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus())){
                FlowCtrlHisShow fcsParam = new FlowCtrlHisShow();
                try {
                    fcsParam = (FlowCtrlHisShow) BeanUtils.cloneBean(flowCtrlshowHises.get(flowCtrlshowHises.size()-1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //״̬��������δ�״̬��1����Ϊ��һ�ε�״̬
                fcsParam.setFlowStatus("".equals
                                            (flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus())
                                                 || flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus()==null?"" :
                                                 String.valueOf(Integer.parseInt(flowCtrlshowHises.get(flowCtrlshowHises.size()-1).getFlowStatus()) + 1
                                            )
                                       );
                //������
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

        //ȥ��FlowStatusΪ0 Ҳ���ǳ�ʼ
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

                //ȥ���ְ���������ͷְ����Ͻ������׼
                if ("3".equals(flowCtrlHisShow.getInfoType()) || "4".equals(flowCtrlHisShow.getInfoType())) {
                    if ("3".equals(flowCtrlHisShow.getFlowStatus())) {
                        continue;
                    }
                }
                //�����е�������ɸѡ����ҳ���������Ĵ�����¼��
                //��Ϊ�����Ͳ����ʱ���г�ͻ��
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
    }//׷�ӽ���

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
