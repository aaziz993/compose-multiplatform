package klib.data.type.collections.linkedlist

import klib.data.type.collections.linkedlist.model.DataNode

public interface MutableLinkedList<T> : MutableIterable<DataNode<T>>, LinkedList<T> {

    public fun insertAtBeginning(data: T)

    // Insertion at End
    public fun insertAtEnd(data: T)

    // Insertion After Specific Node
    public fun insertAfter(prevNode: DataNode<T>, data: T)

    // Deletion at Beginning
    public fun deleteAtBeginning()

    // Deletion at End
    public fun deleteAtEnd()

    // Deletion of a specific node
    public fun deleteNode(node: DataNode<T>)
}
