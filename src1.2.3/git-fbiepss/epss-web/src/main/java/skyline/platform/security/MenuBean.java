package skyline.platform.security;

import java.io.Serializable;
import java.util.List;

/**
 * ²Ëµ¥
 *
 * @author WangHaiLei
 * @version 1.2
 *          $UpdateDate: Y-M-D-H-M:
 *          2003-11-07-09-32
 *          2004-02-28-10-48
 *          $
 */
public class MenuBean
        implements Serializable {

    private transient DatabaseAgent database;

    /**
     * 20100820 zhanrui
     *
     * @param target
     * @return
     * @throws Exception
     */
    public String generateJsonStream(String target)
            throws Exception {
        database = new DatabaseAgent();
        List<MenuItemBean> menuItems = database.getMenuItems(target);
        TreeNode treenode = new TreeNode();
        treenode.setId("0");
        assembleTreeNode(treenode, menuItems);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(treenode);
        String json = jsonObject.toString();
        json = json.replaceAll("\"(\\w+)\"(\\s*:\\s*)", "$1$2");
        return json;
    }

    private void assembleTreeNode(TreeNode treenode, List<MenuItemBean> menuItems) {
        for (MenuItemBean item : menuItems) {
            if (item.getMenuItemPId().equals(treenode.getId())) {
                TreeNode node = new TreeNode();
                node.setId(item.getMenuItemId());
                node.setText(item.getLabel());
                node.setTooltip(item.getDescription());
                TreeUserDataBean udb = new TreeUserDataBean("url", item.getUrl());
                node.addUserData(udb);
                udb = new TreeUserDataBean("description", item.getDescription());
                node.addUserData(udb);
                treenode.addItem(node);
                assembleTreeNode(node, menuItems);
            }
        }
    }

    public static void main(String argv[]) {
        try {
            MenuBean mb = new MenuBean();
            System.out.println(mb.generateJsonStream( "default"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
