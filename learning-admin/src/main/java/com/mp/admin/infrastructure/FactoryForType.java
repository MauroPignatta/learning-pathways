package com.mp.admin.infrastructure;

/** An interface that defines a factory for a specific type.
 *
 * This is used by jackson to instantiate subclasses of dtos instead of the swagger generated dtos.
 */
public interface FactoryForType<T> {

    /** Declares the specific type of objects that this class will create.
     *
     * @return the class that this objects creates. Never null.
     */
    Class<T> getType();

    /** Declares the type object to replace when creating objects through this factory.
     * @return the replaced class is the direct superclass of the replacement class. Never null.
     */
    default Class<? super T> getTypeToReplace() {
        return getType().getSuperclass();
    }

    /** Creates an instance of the object, of type T or any subclass.
     *
     * @return a new object. Never null. */
    T create();
}
