package link.sigma5.menu;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

public class DumbMenu {
    enum DrawMode {AllLevels, LastLevel}

    Stack<MenuLevel> levels = new Stack<>();
    MenuLevel mainLevel;
    MenuListener listener;
    DrawMode drawMode = DrawMode.LastLevel;

    public DumbMenu(String resource, MenuListener listener) throws IOException {
        this(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(resource))), listener);
    }

    public DumbMenu(Reader inStream, MenuListener listener) {
        Yaml yaml = new Yaml();
        this.mainLevel = new MenuLevel(yaml.load(inStream));
        this.listener = listener;
        levels.push(mainLevel);
    }

    public void init() {
        traversOptions(mainLevel);
    }
    private void traversOptions(MenuLevel menuLevel) {
        for (MenuLevel item:menuLevel.getItems()) {
            if (MenuType.scroll.equals(item.type)) {
                listener.onEvent(new MenuEvent(item.event,item.getActiveItem().event));
            } else if (MenuType.list.equals(item.type)) {
                traversOptions(item);
            }
        }
    }

    public void performAction(MenuAction menuAction) {
        var activeLevel = levels.peek();
        var activeItem = activeLevel.getActiveItem();
        switch (menuAction) {
            case Enter -> {
                if (activeItem.type == MenuType.action) {
                    listener.onEvent(new MenuEvent(activeItem.event, ""));
                }
                if (activeItem.type == MenuType.scroll) {
                    activeItem.next();
                    listener.onEvent(new MenuEvent(activeItem.event, activeItem.getActiveItem().event));
                }
                if (activeItem.type == MenuType.list) {
                    activeItem.resetActive();
                    levels.push(activeItem);
                }
            }
            case Back -> {
                MenuLevel menuLevel = levels.pop();
                if (menuLevel.leave!=null) {
                    listener.onEvent(new MenuEvent(menuLevel.leave));
                }
            }
            case Next -> activeLevel.next();
            case Previous -> activeLevel.previous();
            case ScrollNext -> {
                if (activeItem.type.equals(MenuType.scroll)) {
                    activeItem.next();
                }
            }
            case ScrollPrevious -> {
                if (activeItem.type.equals(MenuType.scroll)) {
                    activeItem.previous();
                }
            }
        }

    }

    public void drawMenu(Consumer<MenuLevel> drawer) {
        if (DrawMode.AllLevels.equals(drawMode)) {
            levels.forEach(drawer::accept);
        } else {
            drawer.accept(levels.peek());
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
