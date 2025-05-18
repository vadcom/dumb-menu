package link.sigma5.menu;

import java.util.Map;
/**
 * Represents level of menu with a value
 *
 */
public class MenuLevelValue extends MenuLevel{

    int from;
    int to;
    int intValue;

    protected MenuLevelValue(Map<String, Object> map,MenuListener listener) {
        super(map,listener);
        this.type=MenuType.value;
        this.from = (Integer) map.get("from");
        this.to = (Integer) map.get("to");
        this.intValue = (Integer) map.get("default");
    }

    @Override
    public void next() {
        if (intValue <to) intValue++;
        else intValue =from;
    }

    @Override
    public void previous() {
        if (intValue >from) intValue--;
        else intValue =to;
    }

    @Override
    public MenuItemView getItemView(boolean active) {
        return new MenuItemView(active,getText(),getValue());
    }

    @Override
    public String getValue() {
        return customValueEvent==null?String.valueOf(intValue): listener.onCustomValueEvent(new CustomValueEvent(customValueEvent,String.valueOf(intValue)));
    }

    @Override
    String getParam() {
        return String.valueOf(intValue);
    }

    @Override
    public void setParam(String param) {
        intValue =Integer.parseInt(param);
    }
}
