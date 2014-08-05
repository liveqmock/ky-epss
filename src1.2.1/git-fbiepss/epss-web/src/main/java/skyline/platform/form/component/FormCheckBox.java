//Source file: e:\\java\\zt\\platform\\form\\component\\FormCheckBox.java

package skyline.platform.form.component;

import skyline.platform.form.config.ElementBean;

/**
 *@author     ���滻
 *@created    2003��10��11��
 *@version    1.0
 */
public class FormCheckBox extends AbstractFormComponent {

    /**
     *  Constructor for the FormCheckBox object
     *
     *@param  element  Description of the Parameter
     */
    public FormCheckBox(ElementBean element) {
        super(element);
    }



    /**
     *  ����FormCheckBox��HTML�ű� ����value��nameset��valueste�������½ű���
     *  <td class="page_form_title_td">
     *
     *  </td>
     *
     *  <td class="page_form_td">
     *    $headstr <input type="checkbox" name="" value="" checked>$middlestr
     *    <input type="checkbox" name="" value="" checked>$middlestr <input
     *    type="checkbox" name="" value="" checked>$afterstr
     *  </td>
     *
     *
     *@return     String
     *@roseuid    3F73AADA02EC
     */
    public String toHTML() {
        StringBuffer sb=new StringBuffer();
        sb.append(getHeader());
        for (int i = 0; i < nameset.length; i++) {
            if(i!=0){
                sb.append(element.getMiddleStr());
            }
            sb.append(nameset[i]+"<input type=\"checkbox\" name=\""+element.getName()+"\" value=\""+valueset[i]+"\" class=\""+AbstractFormComponent.CSS_PAGE_FORM_CHECKBOX+"\""+checked(valueset[i])+otherStr()+">");
        }
        sb.append(GetFooter());
        return sb.toString();
    }

    private String checked(String value){
        String[] v=getValues();
        for (int i = 0; i < v.length; i++) {
            if(v[i].equals(value.trim())){
                return " checked";
            }
        }
        return "";
    }


    /**
     *  ��ʼ�� �㷨���£� 1.super(e) 2.����valuetype��valueset��ʼ������nameset��valueset
     *
     *@param  e
     *@roseuid    3F7EA09E01E9
     */
    protected void init(ElementBean e) { }
}