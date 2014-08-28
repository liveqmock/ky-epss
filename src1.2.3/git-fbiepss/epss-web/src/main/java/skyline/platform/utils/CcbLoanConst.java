package skyline.platform.utils;

public class CcbLoanConst {

    // 序号类型
    /** 抵押管理 申请抵押编号 */
    public static final String MORTTYPE                 = "MORTREG";
    /** 合作项目标志 */
    public static final String COOPTYPE                 = "COOP";
    /** 流水号 */
    public static final String TASKSEQ                  = "TASK";
    /** 内部序号 */
    public static final String NBXH                     = "NBXH";
    /**
     * 业务类型，操作日志表专用
     */
    public static final String LOG_MORTREG              = "MORTREG";

    // 报表模板名称

    /** 基本查询 */
    public static final String BASIC_RPT_MODEL          = "basicRptModel";
    /** 快递输出excel模板 */
    public static final String RPT_MODEL_EXPRESS        = "expressRptModel";
    /** 综合查询统计一 */
    public static final String MISC_RPT_MODEL_01        = "miscRptModel01";
    /** 综合查询统计二 */
    public static final String MISC_RPT_MODEL_02        = "miscRptModel02";
    /** 综合查询统计三 */
    public static final String MISC_RPT_MODEL_03        = "miscRptModel03";
    /** 权证入库统计表 */
    public static final String MISC_RPT_PAPER_STAT      = "paperStatModel";

    /** 买单工资统计表模板1 */
    public static final String RPT_PAY_BILL_01          = "payBill01";
    /** 买单工资统计表模板2--- */
    public static final String RPT_PAY_BILL_02          = "payBill02";
    /** 买单工资统计表模板3--- */
    public static final String RPT_PAY_BILL_03          = "payBill03";
    /** 买单工资统计表模板4--- */
    public static final String RPT_PAY_BILL_04          = "payBill04";
    /** 买单工资统计表模板5--- */
    public static final String RPT_PAY_BILL_05          = "payBill05";
    /** 买单工资统计表模板6--- */
    public static final String RPT_PAY_BILL_06          = "payBill06";
    /** 买单工资统计表模板7--- */
    public static final String RPT_PAY_BILL_07          = "payBill07";
    /** 买单工资统计表模板8--- */
    public static final String RPT_PAY_BILL_08          = "payBill08";
    /** 买单工资统计表模板9--- */
    public static final String RPT_PAY_BILL_09          = "payBill09";
    /** 买单工资统计表模板10--- */
    public static final String RPT_PAY_BILL_10          = "payBill10";
    /** 买单工资统计表模板11--- */
    public static final String RPT_PAY_BILL_11          = "payBill11";
    /** 买单工资统计表模板12--- */
    public static final String RPT_PAY_BILL_12          = "payBill12";
    /** ODSB数据比对结果 */
    public static final String RPT_ODSB_CHECK           = "ODSBCheckRpt";
    /** ODSB合作项目数据报表检查模板 */
    public static final String RPT_ODSB_CHECK_PROJ      = "ODSBCheckProjRpt";

    // misc静态变量
    /** pulldown显示记录数 */
    public static final int    PULLDOWN_PAGE_CNT        = 10;
    /** 菜单公用一个页面，判断页面操作权限类型标志位 */
    public static final String MENU_ACTION              = "menuAction";
    /** 菜单公用一个页面，判断页面操作权限类型 */
    public static final String MENU_SELECT              = "select";

    // 操作类型
    /** 增加 */
    public static final String OPER_ADD                 = "ADD";
    /** 修改 */
    public static final String OPER_EDIT                = "EDIT";
    /** 删除 */
    public static final String OPER_DEL                 = "DELETE";
    /** 批量修改 仅限快递 */
    public static final String OPER_BATCHEDIT           = "BATCH_EDIT";
    /** 客户经理认领 */
    public static final String OPER_CLM                 = "CLAIM";
    /** 客户经理批量认领 */
    public static final String OPER_BATCH_CLM           = "BATCH_CLAIM";
    /** 退回 */
    public static final String OPER_BACK                = "BACK";
    /** 批量退回 */
    public static final String OPER_BATCH_BACK          = "BATCH_BACK";
}
