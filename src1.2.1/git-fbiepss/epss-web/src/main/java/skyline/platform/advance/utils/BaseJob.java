package skyline.platform.advance.utils;

/**
 * <p>Title: 后台业务组件</p>
 *
 * <p>Description: 后台业务组件</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 中天信息技术有限责任公司</p>
 *
 * @author leonwoo
 * @version 1.0
 */



import java.sql.Connection;

import skyline.platform.db.ConnectionManager;
import skyline.platform.db.DatabaseConnection;

public abstract class BaseJob  {
  public void action(String param) throws Exception {
    boolean isOK = true;
    DatabaseConnection dc = null;

    try {
    	//在缓冲池中取数据
      dc = ConnectionManager.getInstance().getConnection();
      dc.begin();
      Connection con = dc.getConnection();
      doing(con,param,dc);

    }
    catch (Exception e) {
      isOK = false;
      throw e;

    }
    finally {
      if (dc != null) {
        if (isOK) {
          dc.commit();
        }
        else {
          dc.rollback();
        }
        ConnectionManager.getInstance().releaseConnection(dc);
      }
    }
  }

  protected abstract void doing(Connection con, String param,DatabaseConnection dc) throws Exception;
}
