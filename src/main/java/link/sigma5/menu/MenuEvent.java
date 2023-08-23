package link.sigma5.menu;

public record MenuEvent(String event, String param) {

    public MenuEvent (String event) {
        this(event,"");
    }
}
