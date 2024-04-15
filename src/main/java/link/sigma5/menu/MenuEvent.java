package link.sigma5.menu;

/**
 * Menu event
 * @param event event name
 * @param param event parameter (optional)
 */
public record MenuEvent(String event, String param) {

    public MenuEvent (String event) {
        this(event,"");
    }
}
