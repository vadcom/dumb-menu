package link.sigma5.menu;

/**
 * Menu event listener
 * User application should implement this interface to receive menu events
 * */
public interface MenuListener {
    void onEvent(MenuEvent event);
    default String onCustomValueEvent(CustomValueEvent event){
        return event.value();
    }
}
