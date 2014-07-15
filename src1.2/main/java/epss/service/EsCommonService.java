package epss.service;

import org.springframework.stereotype.Service;
import platform.view.build.utils.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ионГ8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsCommonService {
    public String getOperNameByOperId(String strOperId){
        return Util.getUserName(strOperId);
    }
}
