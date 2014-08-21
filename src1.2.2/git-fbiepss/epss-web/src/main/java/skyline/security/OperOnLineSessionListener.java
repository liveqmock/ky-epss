package skyline.security;

import skyline.platform.security.OnLineOpersManager;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-4-10
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public class OperOnLineSessionListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    /**
     * session销毁的操作
     * @param httpSessionEvent
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        OnLineOpersManager.removeSessionFromServer(session.getId(), session.getServletContext());
    }
}