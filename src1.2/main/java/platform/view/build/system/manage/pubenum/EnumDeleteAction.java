package platform.view.build.system.manage.pubenum;

import platform.view.build.form.control.Action;


public class EnumDeleteAction extends Action
{
    public int doBusiness() {

        String SQLStr = "select  EnuType from  ptEnuDetail where (EnuType='"+this.req.getFieldValue("keycode")+"') ";


        this.rs = this.dc.executeQuery(SQLStr);

	   if (!this.rs.next()){
             SQLStr = "delete from ptEnuMain where (EnuType='"+this.req.getFieldValue("keycode")+"')";
             int retcount =this.dc.executeUpdate(SQLStr);
             if (retcount <0){
                  this.res.setType(0);
                  this.res.setResult(false);
                  this.res.setMessage("ɾ����¼ʧ��");
                  return retcount;
             } else {

                  //��������ö������
                  platform.view.build.form.config.EnumerationType.reload();

                  this.res.setType(0);
                  this.res.setResult(true);
                  this.res.setMessage("ɾ����¼�ɹ�");
                  return 0;
             }
	   }else{
             this.res.setType(0);
             this.res.setResult(false);
             this.res.setMessage("�����¼�ö����Ϣ");
             return -1;

        }

    }

}
