package platform.service;

import epss.repository.model.model_show.CommColModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.repository.dao.*;
import platform.repository.dao.common.PtCommonMapper;
import platform.repository.model.*;
import platform.view.build.form.config.SystemAttributeNames;
import platform.view.build.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-11-16
 * Time: 20:58:24
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PlatformService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PtoperMapper ptoperMapper;
    @Autowired
    private PtdeptMapper ptdeptMapper;
    @Autowired
    private PtoperroleMapper ptoperroleMapper ;
    @Autowired
    private PtmenuMapper ptmenuMapper ;
    @Autowired
    private PtenudetailMapper enudetailMapper;
    @Autowired
    private PtmenuMapper menuMapper;
    @Autowired
    private SysSchedulerLogMapper sysSchedulerLogMapper;
    @Autowired
    private PtCommonMapper ptCommonMapper;

    private String strLastUpdBy;
    private String strLastUpdTime;

    public OperatorManager getOperatorManager(){
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("�û�δ��¼��");
        }
        return om;
    }

    public List<Ptmenu> getPtmenuList(){
        List<CommColModel> commColModelList =new ArrayList<CommColModel>();
        OperatorManager om=getOperatorManager();
        return ptCommonMapper.getPtmenuList(om.getOperatorId());
    }

    /**
     * ����ʱ���ʽ��
     * @param date
     * @param strPattern
     * @return
     */
    public static String dateFormat(Date date,String strPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(strPattern);
        return formatter.format(date);
    }

    /**
     * ����ָ��ö���嵥
     *
     * @param enuid
     * @return
     */
    public List<Ptenudetail> selectEnuDetail(String enuid) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuid);
        example.setOrderByClause(" dispno ");
        return enudetailMapper.selectByExample(example);
    }

    /**
     * ���Ҳ˵���
     *
     * @param strMenuAction
     * @return
     */
    public Ptmenu getPtMenu(String strMenuAction) {
        PtmenuExample example = new PtmenuExample();
        example.createCriteria().andMenuactionLike("%" + strMenuAction+ "%");
        return ptmenuMapper.selectByExample(example).get(0);
    }

    /**
     * ������ָ����Աͬ����Ĺ�Ա�б�
     *
     * @param tellerid ָ����ԱID
     * @return list ��not null��
     */
    public List<Ptoper> selectBranchTellers(String tellerid) {
        PtoperExample example = new PtoperExample();
        example.createCriteria().andOperidEqualTo(tellerid);
        List<Ptoper> records = ptoperMapper.selectByExample(example);
        if (records.size() == 0) {
            return records;
        } else {
            String deptid = records.get(0).getDeptid();
            example.clear();
            example.createCriteria().andDeptidEqualTo(deptid);
            return ptoperMapper.selectByExample(example);
        }
    }

    /**
     * ��ȡö�ٱ���ĳһ��� ��չ����ֵ
     *
     * @param enuType
     * @param enuItemValue
     * @return
     */
    public String selectEnuExpandValue(String enuType, String enuItemValue) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuType).andEnuitemvalueEqualTo(enuItemValue);
        //TODO �����жϣ�
        return enudetailMapper.selectByExample(example).get(0).getEnuitemexpand();
    }

    /**
     * ���� ö��ֵ����չֵ�Ķ�Ӧ��ϵ MAP
     *
     * @param enuType
     * @return
     */
    public Map<String, String> selectEnuItemValueToExpandValueMap(String enuType) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuType);
        List<Ptenudetail> records = enudetailMapper.selectByExample(example);
        Map<String, String> enuMap = new HashMap<String, String>();
        for (Ptenudetail record : records) {
            enuMap.put(record.getEnuitemvalue(), record.getEnuitemexpand());
        }
        return enuMap;
    }

    /**
     * ��ѯĳһ��ĵ�����־
     *
     * @param curryear
     * @return
     * @throws java.text.ParseException
     */
    public List<SysSchedulerLog> selectSchedulerLogByYear(String curryear) throws ParseException {
        SysSchedulerLogExample example = new SysSchedulerLogExample();
        String firstDayThisYear = new SimpleDateFormat("yyyy-01-01").format(new Date());
        //Date date = new SimpleDateFormat("yyyy-MM-dd").parse(firstDayThisYear);
        //example.createCriteria().andTimeGreaterThanOrEqualTo(date);
        example.createCriteria();
        List<SysSchedulerLog> sysSchedulerLogs = sysSchedulerLogMapper.selectByExample(example);
        return sysSchedulerLogs;
    }

    /*�����ֶ�_Start*/
    public PtoperMapper getPtoperMapper() {
        return ptoperMapper;
    }

    public void setPtoperMapper(PtoperMapper ptoperMapper) {
        this.ptoperMapper = ptoperMapper;
    }

    public PtdeptMapper getPtdeptMapper() {
        return ptdeptMapper;
    }

    public void setPtdeptMapper(PtdeptMapper ptdeptMapper) {
        this.ptdeptMapper = ptdeptMapper;
    }

    public PtoperroleMapper getPtoperroleMapper() {
        return ptoperroleMapper;
    }

    public void setPtoperroleMapper(PtoperroleMapper ptoperroleMapper) {
        this.ptoperroleMapper = ptoperroleMapper;
    }

    public PtenudetailMapper getEnudetailMapper() {
        return enudetailMapper;
    }

    public void setEnudetailMapper(PtenudetailMapper enudetailMapper) {
        this.enudetailMapper = enudetailMapper;
    }

    public PtmenuMapper getMenuMapper() {
        return menuMapper;
    }

    public void setMenuMapper(PtmenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    public SysSchedulerLogMapper getSysSchedulerLogMapper() {
        return sysSchedulerLogMapper;
    }

    public void setSysSchedulerLogMapper(SysSchedulerLogMapper sysSchedulerLogMapper) {
        this.sysSchedulerLogMapper = sysSchedulerLogMapper;
    }

    public PtmenuMapper getPtmenuMapper() {
        return ptmenuMapper;
    }

    public void setPtmenuMapper(PtmenuMapper ptmenuMapper) {
        this.ptmenuMapper = ptmenuMapper;
    }

    public String getStrLastUpdBy() {
        OperatorManager om = getOperatorManager();
        strLastUpdBy=om.getOperatorId();
        return strLastUpdBy;
    }

    public String getStrLastUpdTime() {
        Date date =new Date();
        strLastUpdTime= dateFormat(date, "yyyy-MM-dd-HH:mm:ss:SSS");
        return strLastUpdTime;
    }

    /*�����ֶ�_End*/
}
