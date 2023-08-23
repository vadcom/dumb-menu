package link.sigma5.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * It contains one level of menu
 */
public class MenuLevel {
    MenuType type; // How to draw submenu items
    String text;
    String event; // By default it's menu text
    String leave; // By default it's menu text

    List<MenuLevel> items;
    int activeItemIndex;
    public MenuLevel(Map<String, Object> map) {
        Object stringType = map.get("type");
        this.type = stringType == null ? MenuType.action : MenuType.valueOf((String) stringType);
        this.text = (String) map.get("text");
        String eventParam = (String) map.get("event");
        this.event = eventParam==null?this.text:eventParam;
        this.leave = (String) map.get("leave");
        Object active = map.get("active");
        if (active!=null) {
            this.activeItemIndex = (Integer) active;
        } else {
            this.activeItemIndex = 0;
        }
        items = new ArrayList<>();
        Object itemsList = map.get("items");
        if (itemsList instanceof List list) {
            for (Map<String, Object> item : (List<Map<String, Object>>) list) {
                items.add(new MenuLevel(item));
            }
        }
    }
    public MenuLevel getActiveItem() {
        return items.get(activeItemIndex);
    }

    public int getItemCount() {
        return items.size();
    }

    public List<MenuItemView> getItemsText() {
        return items.stream()
                .map(menuLevel -> new MenuItemView(getActiveItem().equals(menuLevel), menuLevel.getText(), menuLevel.type.equals(MenuType.scroll)?menuLevel.items.get(menuLevel.activeItemIndex).getText():""))
                .toList();
    }

    public void next() {
        activeItemIndex++;
        if (activeItemIndex >=items.size()) activeItemIndex =0;
    }

    public void previous() {
        activeItemIndex--;
        if (activeItemIndex <0) activeItemIndex =items.size()-1;
    }

    public void resetActive() {
        activeItemIndex =0;
    }

    public String getText() {
        return text;
    }

    List<MenuLevel> getItems() {
        return items;
    }


}
