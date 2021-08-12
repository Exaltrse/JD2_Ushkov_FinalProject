package com.ushkov.validation;

public interface ValidationGroup {
    //Interface fo grouping at validation for new objects. Use in Create CRUD operation.
    interface NewObject {}
    //Interface for grouping at validation for existing objects. Use in all operations except Create CRUD operation.
    interface ExistingObject {}

}
