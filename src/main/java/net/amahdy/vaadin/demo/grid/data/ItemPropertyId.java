package net.amahdy.vaadin.demo.grid.data;

// This is needed for storing back-references
public class ItemPropertyId {

    Object itemId;
    Object propertyId;

    public ItemPropertyId(Object itemId, Object propertyId) {
        this.itemId = itemId;
        this.propertyId = propertyId;
    }

    public Object getItemId() {
        return itemId;
    }

    public Object getPropertyId() {
        return propertyId;
    }
}