package net.amahdy.vaadin.demo.grid.util;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

import java.util.HashMap;

// Keyboard navigation
public class KbdHandlerFooter implements Action.Handler {

    Action tab_next = new ShortcutAction("Tab",
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

    private Table table;
    private HashMap<Integer, TextField> valueFields;

    public KbdHandlerFooter(Table table, HashMap<Integer, TextField> valueFields) {
        this.table = table;
        this.valueFields = valueFields;
    }

    public Action[] getActions(Object target, Object sender) {
        return new Action[]{tab_next, tab_prev, cur_down,
                cur_up, enter};
    }

    public void handleAction(Action action, Object sender,
                             Object target) {
        if (target instanceof TextField) {
            // Move according to keypress
            int itemid = (Integer) ((TextField) target).getData();
            if (action == tab_next || action == cur_down)
                itemid++;
            else if (action == tab_prev || action == cur_up)
                itemid--;
            // On enter, just stay where you were. If we did
            // not catch the enter action, the focus would be
            // moved to wrong place.

            // TODO: Can't wrap cursor navigation because only
            // the next and previous item fields are in the cache,
            // the one at the other end of the table may not be.
            if (itemid >= 0 && itemid < table.size()) {
                TextField newTF = valueFields.get(itemid);
                if (newTF != null)
                    newTF.focus();
            }
        }
    }
}
