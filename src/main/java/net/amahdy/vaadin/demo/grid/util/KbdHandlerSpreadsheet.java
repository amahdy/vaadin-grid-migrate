package net.amahdy.vaadin.demo.grid.util;

import net.amahdy.vaadin.demo.grid.data.Bean;
import net.amahdy.vaadin.demo.grid.data.ItemPropertyId;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

import java.util.HashMap;

// Keyboard navigation
public class KbdHandlerSpreadsheet implements Action.Handler {

    Action tab_next = new ShortcutAction("Shift",
            ShortcutAction.KeyCode.TAB, null);
    Action tab_prev = new ShortcutAction("Shift+Tab",
            ShortcutAction.KeyCode.TAB,
            new int[]{ShortcutAction.ModifierKey.SHIFT});
    Action cur_down = new ShortcutAction("Down",
            ShortcutAction.KeyCode.ARROW_DOWN, null);
    Action cur_up = new ShortcutAction("Up",
            ShortcutAction.KeyCode.ARROW_UP, null);
    Action enter = new ShortcutAction("Enter",
            ShortcutAction.KeyCode.ENTER, null);
    Action add = new ShortcutAction("Add Below",
            ShortcutAction.KeyCode.A, null);
    Action delete = new ShortcutAction("Delete",
            ShortcutAction.KeyCode.DELETE, null);

    private Table table;
    private HashMap<Object, HashMap<Object, Field<?>>> fields;
    private BeanItemContainer<Bean> beans;

    public KbdHandlerSpreadsheet(Table table, HashMap<Object, HashMap<Object, Field<?>>> fields,
                                 BeanItemContainer<Bean> beans) {
        this.table = table;
        this.fields = fields;
        this.beans = beans;
    }

    public Action[] getActions(Object target, Object sender) {
        return new Action[]{tab_next, tab_prev, cur_down,
                cur_up, enter, add, delete};
    }

    public void handleAction(Action action, Object sender,
                             Object target) {
        if (target instanceof TextField) {
            TextField tf = (TextField) target;
            ItemPropertyId ipId = (ItemPropertyId) tf.getData();

            // On enter, close the edit mode
            if (action == enter) {
                // Make the entire item read-only
                HashMap<Object, Field<?>> itemMap = fields.get(ipId.getItemId());
                for (Field<?> f : itemMap.values())
                    f.setReadOnly(true);
                table.select(ipId.getItemId());
                table.focus();

                // Updates the generated column
                table.refreshRowCache();
                return;
            }

            Object propertyId = ipId.getPropertyId();

            // Find the index of the property
            Object cols[] = table.getVisibleColumns();
            int pidIndex = 0;
            for (int i = 0; i < cols.length; i++)
                if (cols[i].equals(propertyId))
                    pidIndex = i;

            Object newItemId = null;
            Object newPropertyId = null;

            // Move according to keypress
            if (action == cur_down)
                newItemId = beans.nextItemId(ipId.getItemId());
            else if (action == cur_up)
                newItemId = beans.prevItemId(ipId.getItemId());
            else if (action == tab_next)
                newPropertyId = cols[Math.min(pidIndex + 1, cols.length - 1)];
            else if (action == tab_prev)
                newPropertyId = cols[Math.max(pidIndex - 1, 0)];

            // If tried to go past first or last, just stay there
            if (newItemId == null)
                newItemId = ipId.getItemId();
            if (newPropertyId == null)
                newPropertyId = ipId.getPropertyId();

            // On enter, just stay where you were. If we did
            // not catch the enter action, the focus would be
            // moved to wrong place.

            Field<?> newField = fields.get(newItemId).get(newPropertyId);
            if (newField != null)
                newField.focus();
        } else if (target instanceof Table) {
            Table table = (Table) target;
            Object selected = table.getValue();

            if (selected == null)
                return;

            if (action == enter) {
                // Make the entire item editable
                HashMap<Object, Field<?>> itemMap = fields.get(selected);
                for (Field<?> f : itemMap.values())
                    f.setReadOnly(false);

                // Focus the first column
                itemMap.get(table.getVisibleColumns()[0]).focus();
            } else if (action == add) {
                // TODO
            } else if (action == delete) {
                Item item = table.getItem(selected);
                if (item != null && item instanceof BeanItem<?>) {
                    // Change selection
                    Object newselected = table.nextItemId(selected);
                    if (newselected == null)
                        newselected = table.prevItemId(selected);
                    table.select(newselected);
                    table.focus();

                    // Remove the item from the container
                    beans.removeItem(selected);

                    // Remove from the map
                    // TODO
                }
            }
        }
    }
}
