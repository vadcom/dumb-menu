package link.sigma5.menu;

import java.util.Map;

public class MenuLevelValue extends MenuLevel{

    int from;
    int to;
    int value;

    protected MenuLevelValue(Map<String, Object> map) {
        super(map);
        this.type=MenuType.value;
        this.from = (Integer) map.get("from");
        this.to = (Integer) map.get("to");
        this.value = (Integer) map.get("default");
    }

    @Override
    public void next() {
        if (value<to) value++;
        else value=from;
    }

    @Override
    public void previous() {
        if (value>from) value--;
        else value=to;
    }

    @Override
    public MenuItemView getItemView(boolean active) {
        return new MenuItemView(active,getText(),String.valueOf(value));
    }

    @Override
    String getParam() {
        return String.valueOf(value);
    }
}
