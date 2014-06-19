package platform.view.build.utils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import platform.view.build.form.config.SystemAttributeNames;
import platform.view.build.form.control.SessionContext;
import platform.view.build.form.control.impl.SessionContextHttpImpl;
import platform.view.build.form.util.SessionAttributes;
import platform.view.build.security.OperatorManager;
import platform.view.build.system.manage.dao.*;


public class Tools {
     public static SessionContext getSc(HttpServletRequest req,HttpServletResponse res) {
          HttpSession session = req.getSession ( true ) ;

          SessionContext ctx = ( SessionContext )session.getAttribute ( SessionAttributes.SESSION_CONTEXT_NAME ) ;
          if ( ctx == null ) {
               ctx = new SessionContextHttpImpl ( req , res ) ;
               session.setAttribute ( SessionAttributes.SESSION_CONTEXT_NAME , ctx ) ;
          }

          return ctx;
     }
     public static PtOperBean getOper(HttpSession session) {
          OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
          if ( om == null )
               return null;
          else
               return om.getOperator();
     }
     public static String getOperid(HttpSession session) throws Exception {
          OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
          if ( om == null ) {
               throw new Exception("操作员签到超时，请重新签到！");
          } else {
               PtOperBean oper = om.getOperator();
               if ( oper == null ) {
                    throw new Exception("操作员签到超时，请重新签到！");
               }
               return oper.getOperid();
          }
     }
}
