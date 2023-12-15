# dumb-menu

If you're seeking a menu for your pet project, you'll find it right here.

## Yaml file format

The menu is defined in a yaml file. The file is composed of a list of items. 
Each item has a text and an event. 
The event is raised when the user selects the item.

```yaml
text: Text to display before the menu
leave: Event raised when the user leaves the menu
type: Type of the menu (action, submenu, option, value, custom)
items:
  - text: Text for the item
    event: Event raised when the user selects the item
  - name: Text for the item
    event: Event raised when the user selects the item    
```

Look at the [example menu](examples/resource/SimpleMenu.yaml) file for more details.

Set persistence to Yes to save option or value.

```yaml
persistence: Yes
```

