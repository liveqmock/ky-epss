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
     * 2015-03-24׷��
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
                    //ȡ����һ�ε�endTime ������εĿ�ʼʱ��
                    String benginTime = flowCtrlshows.get(i-1).getEndTime();
                    //ȡ����ǰ���createTime������εĽ���ʱ��
                    String endTime = flowCtrlshow.getCreatedTime();
                    flowCtrlshow.setCreatedTime(benginTime);
                    flowCtrlshow.setEndTime(endTime);
                    flowCtrlShowList.add(flowCtrlshow);
                }else{
                     //�ж���һ�ε�FlowStatus�Ƿ�Ϊ3
                    //������������ݿ���FlowStatus���������
                    // ��˵���������û��ִ���꣬
                    //ҪΪ��һ״̬��ӿ�ʼʱ��
                    if(!"3".equals(flowCtrlshows.get(i-1).getFlowStatus())){
                        //ȡ����һ��FlowCtrlShow
                        FlowCtrlShow fcs = flowCtrlshows.get(i-1);
                        FlowCtrlShow fcsParam = new FlowCtrlShow();
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
                        flowCtrlShowList.add(fcsParam);
                        //���FlowStatus��0ʱ�����԰�������Ŀ�ʼʱ������Ϊ����Ľ���ʱ�䣬Ϊ��һ״̬ʹ��
                        if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                        }else{
                            //�������0��ʱ��Ҫ�����ݿ���������ݵ�creattime������Ľ���ʱ�䣬���ҿ�ʼʱ��Ϊ��
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                            //flowCtrlshow.setCreatedTime("");
                        }
                    }else{
                        //��һ��FlowStatus��3ʱ
                        //���ұ������Ϊ0��
                        // "5".equals(flowCtrlshow.getInfoType())
                        //if("0".equals(flowCtrlshow.getFlowStatus())){
                            flowCtrlshow.setEndTime(flowCtrlshow.getCreatedTime());
                        //}
                    }
                    flowCtrlShowList.add(flowCtrlshow);
                }
            }

            //��ֹflowStatusΪ��ͬ����flowStatus������3 Ҫ�����һ��������һ״̬
            if(i==flowCtrlshows.size()-1&&!"3".equals(flowCtrlshows.get(flowCtrlshows.size()-1).getFlowStatus())){
                FlowCtrlShow fcsParam = new FlowCtrlShow();
                try {
                    fcsParam = (FlowCtrlShow) BeanUtils.cloneBean(flowCtrlshows.get(flowCtrlshows.size()-1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //״̬��������δ�״̬��1����Ϊ��һ�ε�״̬
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

        //ȥ��FlowStatusΪ0 Ҳ���ǳ�ʼ
        for(FlowCtrlShow flowCtrlShow: flowCtrlShowList){
            if(!"0".equals(flowCtrlShow.getFlowStatus())){
                //�����е�������ɸѡ����ҳ���������Ĵ�����¼��
                //��Ϊ�����Ͳ����ʱ���г�ͻ��
                if(StringUtils.isNotBlank(flowCtrlShowParam.getCreatedByName())){
                    if(StringUtils.isNotBlank(flowCtrlShow.getCreatedByName())&&
                       flowCtrlShow.getCreatedByName().contains(flowCtrlShowParam.getCreatedByName().trim())){
                        returnFlowCtrlShowList.add(flowCtrlShow);
                    }
                }else{
                    //ȥ���ְ���������ͷְ����Ͻ������׼
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
}
