package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.dao.EsInitStlMapper;
import epss.repository.dao.common.CommonMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgSubstlItemShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsInitStlService {
    @Resource
    private EsInitStlMapper esInitStlMapper;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private EsInitPowerService esInitPowerService;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsItemStlSubcttEngPService esItemStlSubcttEngPService;

    /**
     * 判断记录是否已存在
     *
     * @param progInfoShowPara
     * @return
     */
    public List<EsInitStl> getExistedEsInitStlSubcttEngInDb(ProgInfoShow progInfoShowPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        return esInitStlMapper.selectByExample(example);
    }

    private EsInitStl fromModelShowToModel(ProgInfoShow progInfoShowPara){
        EsInitStl esInitStlTemp =new EsInitStl();
        esInitStlTemp.setPkid(progInfoShowPara.getPkid());
        esInitStlTemp.setStlType(progInfoShowPara.getStlType());
        esInitStlTemp.setStlPkid(progInfoShowPara.getStlPkid());
        esInitStlTemp.setId(progInfoShowPara.getId());
        esInitStlTemp.setPeriodNo(progInfoShowPara.getPeriodNo());
        esInitStlTemp.setNote(progInfoShowPara.getNote());
        esInitStlTemp.setAttachment(progInfoShowPara.getAttachment());
        esInitStlTemp.setDeletedFlag(progInfoShowPara.getDeletedFlag());
        esInitStlTemp.setEndFlag(progInfoShowPara.getEndFlag());
        esInitStlTemp.setCreatedBy(progInfoShowPara.getCreatedBy());
        esInitStlTemp.setCreatedDate(progInfoShowPara.getCreatedDate());
        esInitStlTemp.setLastUpdBy(progInfoShowPara.getLastUpdBy());
        esInitStlTemp.setLastUpdDate(progInfoShowPara.getLastUpdDate());
        esInitStlTemp.setModificationNum(progInfoShowPara.getModificationNum());
        return esInitStlTemp;
    }

    public EsInitStl selectRecordsByPrimaryKey(String strPkId){
        return esInitStlMapper.selectByPrimaryKey(strPkId);
    }
    public List<EsInitStl> selectRecordsByRecord(EsInitStl esInitStlPara){
        EsInitStlExample esInitStlExample=new EsInitStlExample();
        EsInitStlExample.Criteria criteria = esInitStlExample.createCriteria();
        criteria.andStlTypeEqualTo(esInitStlPara.getStlType()).
                andStlPkidEqualTo(esInitStlPara.getStlPkid()).
                andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        return esInitStlMapper.selectByExample(esInitStlExample);
    }

    public void insertRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setCreatedDate(platformService.getStrLastUpdDate());
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.insert(fromModelShowToModel(progInfoShowPara)) ;
    }
    public void insertRecord(EsInitStl esInitStlPara){
        esInitStlPara.setCreatedBy(platformService.getStrLastUpdBy());
        esInitStlPara.setCreatedDate(platformService.getStrLastUpdDate());
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.insert(esInitStlPara) ;
    }
    @Transactional
    public void insertStlAndPowerRecord(EsInitStl esInitStlPara){
        esInitStlPara.setCreatedBy(platformService.getStrLastUpdBy());
        esInitStlPara.setCreatedDate(platformService.getStrLastUpdDate());
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.insert(esInitStlPara) ;
        EsInitPower esInitPowerTemp=new EsInitPower();
        esInitPowerTemp.setPowerType(ESEnum.ITEMTYPE5.getCode());
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
        esInitPowerTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
        esInitPowerService.insertRecordByStl(esInitPowerTemp);
    }
    @Transactional
    public void updateRecordForSubCttPApprovePass(EsInitStl esInitStlPara,ArrayList<ProgSubstlItemShow> progSubstlItemShowListForApprovePara){
        //结算登记表更新
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
        //Power表更新
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(esInitStlPara.getStlType());
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
        esInitPowerTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
        esInitPowerService.updateRecordByStl(esInitPowerTemp);
        //将价格结算的完整数据插入至es_item_stl_subctt_eng_p表
        for (int i=0;i<progSubstlItemShowListForApprovePara.size();i++){
            ProgSubstlItemShow itemUnit=progSubstlItemShowListForApprovePara.get(i);
            itemUnit.setEngPMng_RowNo(i);
            esItemStlSubcttEngPService.insertRecordDetail(itemUnit);
        }
    }
    public void updateRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(progInfoShowPara.getModificationNum())+1);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(fromModelShowToModel(progInfoShowPara)) ;
    }
    public void updateRecord(EsInitStl esInitStlPara){
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
    }

    @Transactional
    public void deleteRecord(ProgInfoShow progInfoShowPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        esInitPowerService.deleteRecord(
                progInfoShowPara.getStlType()
                , progInfoShowPara.getStlPkid()
                , progInfoShowPara.getPeriodNo());
    }
    public void deleteRecord(EsInitStl esInitStlPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        esInitPowerService.deleteRecord(
                esInitStlPara.getStlType()
                , esInitStlPara.getStlPkid()
                , esInitStlPara.getPeriodNo());

    }
    @Transactional
    public void deleteRecordForSubCttPApprovePass(EsInitStl esInitStlPara,String powerType){
        //删除stl表中stl_type为5的记录
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);
        //删除power表中power_type为5的记录
        esInitPowerService.deleteRecord(
                esInitStlPara.getStlType()
                , esInitStlPara.getStlPkid()
                , esInitStlPara.getPeriodNo());
        //更新power表中power_type为3或者4的记录状态为审核状态
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(powerType);
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        EsInitPower esInitPower = esInitPowerService.selectByPrimaryKey(esInitPowerTemp);
        esInitPower.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
        esInitPower.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
        esInitPowerService.updateRecordByPower(esInitPower);
        //删除es_item_stl_subctt_eng_p表中的相应记录
        esItemStlSubcttEngPService.deleteRecordByExample(esInitStlPara.getStlPkid(),esInitStlPara.getPeriodNo());
    }
    public int deleteRecord(String strPkId){
        return esInitStlMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxStlId(String strCttType){
        return commonMapper.getStrMaxStlId(strCttType) ;
    }
}
