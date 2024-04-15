package link.sigma5.menu;

/**
 * Menu type
 * User will show the menu item according to this type
 */
public enum MenuType {
    action, // Action menu item (e.g. Enter, Back)
    submenu, // Submenu ( It will show submenu when selected)
    option, // Option menu item ( It will provide string option to select)
    value,  // Value menu item ( It will provide numeric value to select)
    custom  // Custom menu item
}
