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
    boolean persistence; // True if we need to save this value

    List<MenuLevel> items;
    int activeItemIndex;

    public static MenuLevel create(Map<String, Object> map) {
        Object stringType = map.get("type");
        MenuType type = stringType == null ? MenuType.action : MenuType.valueOf((String) stringType);
        return switch (type) {
            case value ->  new MenuLevelValue(map);
            default -> new MenuLevel( map);
        };
    }

    public boolean isSelectable() {
        return type.equals(MenuType.option) || type.equals(MenuType.value);
    }

    protected MenuLevel(Map<String, Object> map) {
        Object stringType = map.get("type");
        this.type = stringType == null ? MenuType.action : MenuType.valueOf((String) stringType);
        this.text = (String) map.get("text");
        String eventParam = (String) map.get("event");
        this.event = eventParam==null?this.text:eventParam;
        this.leave = (String) map.get("leave");
        this.persistence = map.get("persistence")!=null? (Boolean) map.get("persistence"):false;
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
                items.add(MenuLevel.create(item));
            }
        }
    }
    public MenuLevel getActiveItem() {
        return activeItemIndex<items.size()?items.get(activeItemIndex):null;
    }

    public int getItemCount() {
        return items.size();
    }

    public List<MenuItemView> getItemsText() {
        return items.stream()
                .map(menuLevel -> menuLevel.getItemView(menuLevel.equals(getActiveItem())))
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

    public void setParam(String param) {
        items.stream()
                .filter(menuLevel -> menuLevel.event.equals(param))
                .findFirst()
                .ifPresent(menuLevel -> activeItemIndex = items.indexOf(menuLevel));
    }

    public String getText() {
        return text;
    }

    public MenuItemView getItemView(boolean active) {
        return new MenuItemView(active, getText(), type.equals(MenuType.option)?items.get(activeItemIndex).getText():"");
    }

    List<MenuLevel> getItems() {
        return items;
    }

    String getParam() {
        return getActiveItem()==null?"":getActiveItem().event;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }
}
