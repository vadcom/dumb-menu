package link.sigma5.menu;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

/**
 * DumbMenu is a simple menu system that can be used to create a menu system from a yaml file.
 */
public class DumbMenu {
    public enum DrawMode {AllLevels, LastLevel}

    Deque<MenuLevel> levels = new ArrayDeque<>();
    MenuLevel mainLevel;
    MenuListener listener;
    DrawMode drawMode = DrawMode.LastLevel;

    private final Preferences prefs;

    /**
     * Create a new DumbMenu from a YAML file.
     * @param resource resource file    (e.g. "menu.yaml")
     * @param listener  listener for menu events
     * @throws IOException if the resource file is not found
     */
    public DumbMenu(String resource, MenuListener listener, Preferences prefsPreferences) throws IOException {
        this(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(resource))), listener, prefsPreferences);
    }

    /**
     * Create a new DumbMenu from a YAML file.
     * @param inStream input stream
     * @param listener listener for menu events
     */
    public DumbMenu(Reader inStream, MenuListener listener, Preferences prefsPreferences) {
        Yaml yaml = new Yaml();
        this.mainLevel = MenuLevel.create(yaml.load(inStream), listener);
        this.listener = listener;
        this.prefs = prefsPreferences;
        levels.addFirst(mainLevel);
    }

    public void init() {
        traversOptions(mainLevel);
    }

    private void traversOptions(MenuLevel menuLevel) {
        for (MenuLevel item : menuLevel.getItems()) {
            if (item.isSelectable()) {
                if (item.isPersistence()) {
                    item.setParam(prefs.get(item.event, item.getParam()));
                }
                listener.onEvent(new MenuEvent(item.event, item.getParam(), item));
            } else if (MenuType.submenu.equals(item.type)) {
                traversOptions(item);
            }
        }
    }

    public void performAction(MenuAction menuAction) {
        var activeLevel = levels.getFirst();
        var activeItem = activeLevel.getActiveItem();
        switch (menuAction) {
            case Enter -> {
                switch (activeItem.type) {

                    case action -> {
                        listener.onEvent(new MenuEvent(activeItem.event, "", activeItem));
                    }
                    case submenu -> {
                        activeItem.resetActive();
                        levels.addFirst(activeItem);
                    }
                    case option, value -> {
                        activeItem.next();
                        prefs.put(activeItem.event, activeItem.getParam());
                        listener.onEvent(new MenuEvent(activeItem.event, activeItem.getParam(),activeItem));
                    }
                }
            }
            case Back -> {
                MenuLevel menuLevel = levels.removeFirst();
                if (menuLevel.leave != null) {
                    listener.onEvent(new MenuEvent(menuLevel.leave));
                }
            }
            case Next -> activeLevel.next();
            case Previous -> activeLevel.previous();
            case ScrollNext -> {
                if (activeItem.type.equals(MenuType.option) || activeItem.type.equals(MenuType.value)) {
                    activeItem.next();
                    prefs.put(activeItem.event, activeItem.getParam());
                    listener.onEvent(new MenuEvent(activeItem.event, activeItem.getParam(),activeItem));
                }
            }
            case ScrollPrevious -> {
                if (activeItem.type.equals(MenuType.option) || activeItem.type.equals(MenuType.value)) {
                    activeItem.previous();
                    prefs.put(activeItem.event, activeItem.getParam());
                    listener.onEvent(new MenuEvent(activeItem.event, activeItem.getParam(),activeItem));
                }
            }
        }

    }

    public void drawMenu(Consumer<MenuLevel> drawer) {
        if (DrawMode.AllLevels.equals(drawMode)) {
            levels.forEach(drawer);
        } else {
            drawer.accept(levels.getFirst());
        }
    }

    public MenuLevel getCurrentLevel() {
        return levels.peek();
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }
}
