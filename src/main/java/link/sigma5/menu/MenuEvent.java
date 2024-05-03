package link.sigma5.menu;

/**
 * Menu event
 * @param event event name
 * @param param event parameter (optional)
 * @param level menu level
 *              For example, if the event is "select", param is the selected item
 */
public record MenuEvent(String event, String param, MenuLevel level) {

    public MenuEvent (String event) {
        this(event,"",null);
    }
}
