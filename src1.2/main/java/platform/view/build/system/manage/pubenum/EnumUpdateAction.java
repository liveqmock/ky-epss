package platform.view.build.system.manage.pubenum;

import platform.view.build.form.control.Action;
import platform.view.build.system.manage.dao.*;

public class EnumUpdateAction extends Action
{

    PtEnuMainBean ptmainbean = new PtEnuMainBean();

     public int doBusiness() {


		for (int i=0; i<this.req.getRecorderCount();i++ ){


              ptmainbean.setEnutype(this.req.getFieldValue(i,"EnuType"));
              ptmainbean.setEnuname(this.req.getFieldValue(i,"EnuName"));
              ptmainbean.setValuetype(this.req.getFieldValue(i,"ValueType"));
              ptmainbean.setEnudesc(this.req.getFieldValue(i,"EnuDesc"));



             if (ptmainbean.updateByWhere(" where (EnuType='"+this.req.getFieldValue(i,"keycode")+"')") <0){
                  this.res.setType(0);
                  this.res.setResult(false);
                  this.res.setMessage("���¼�¼ʧ��");
                  return -1;
             }
       }

       //��������ö������
       platform.view.build.form.config.EnumerationType.reload();

       this.res.setType(0);
       this.res.setResult(true);
       this.res.setMessage("���¼�¼�ɹ�");

       return 0;
   }


}
