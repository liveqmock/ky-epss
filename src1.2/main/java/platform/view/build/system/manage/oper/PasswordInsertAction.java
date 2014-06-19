package platform.view.build.system.manage.oper;

import platform.view.build.form.control.*;
import platform.view.build.system.manage.dao.*;
import platform.common.MD5Helper;

public class PasswordInsertAction extends Action {
     PtOperBean operbean = new PtOperBean();

     public int doBusiness() {
         //operbean.setOperpasswd(this.req.getFieldValue("newpwd"));
         operbean.setOperpasswd(MD5Helper.getMD5String(this.req.getFieldValue("newpwd")));

         this.res.setType(0);
           
           if (operbean.updateByWhere(" where (deptid='"+this.req.getFieldValue("deptid")+"')and(operid='"+this.req.getFieldValue("operid")+"')") <0){

             this.res.setResult(false);
             this.res.setMessage("更新密码失败！");
             this.dc.close();
             return -1;
           }

          this.res.setResult(true);
          this.res.setMessage("更新密码成功！");
          this.dc.close();
          return 0;
     }
}
