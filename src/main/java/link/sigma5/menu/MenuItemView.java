package link.sigma5.menu;

/**
 * Represents a menu item view
 * @param active    true if the item is active
 * @param text    text to display
 * @param value   value to display (optional)
 */
public record MenuItemView(boolean active, String text, String value) {
}
