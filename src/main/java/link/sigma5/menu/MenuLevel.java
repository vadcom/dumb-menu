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
    String value; // By default it's menu option value
    String event; // By default it's menu text
    String leave; // By default it's menu text
    String customValueEvent; // call this event to get custom text for the menu item
    boolean persistence; // True if we need to save this value
    protected final MenuListener listener;

    List<MenuLevel> items;
    int activeItemIndex;

    public static MenuLevel create(Map<String, Object> map,MenuListener listener) {
        Object stringType = map.get("type");
        MenuType type = stringType == null ? MenuType.action : MenuType.valueOf((String) stringType);
        return switch (type) {
            case value ->  new MenuLevelValue(map,listener);
            default -> new MenuLevel( map,listener);
        };
    }

    public boolean isSelectable() {
        return type.equals(MenuType.option) || type.equals(MenuType.value);
    }

    protected MenuLevel(Map<String, Object> map,MenuListener listener) {
        this.listener=listener;
        Object stringType = map.get("type");
        this.type = stringType == null ? MenuType.action : MenuType.valueOf((String) stringType);
        this.text = (String) map.get("text");
        String valueString = (String) map.get("value");
        this.value = valueString==null?"":valueString;
        String eventParam = (String) map.get("event");
        this.event = eventParam==null?this.text:eventParam;
        this.customValueEvent = (String) map.get("valueEvent");
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
                items.add(MenuLevel.create(item,listener));
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
        return new MenuItemView(active, getText(), type.equals(MenuType.option)? getOptionText() :getValue());
    }

    private String getOptionText() {
        return items.get(activeItemIndex).value.isBlank()
                ?items.get(activeItemIndex).getText()
                :items.get(activeItemIndex).getValue();
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

    public String getValue() {
        return customValueEvent==null?value: listener.onCustomValueEvent(new CustomValueEvent(customValueEvent,value));
    }

    public void setValue(String value) {
        this.value = value;
    }
}
