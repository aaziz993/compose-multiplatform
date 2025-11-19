package klib.data.type.collections.list.linked

public interface MutableLinkedList<T> : MutableIterable<Node<T>>, LinkedList<T> {

    public fun insertAtBeginning(data: T)

    // Insertion at End
    public fun insertAtEnd(data: T)

    // Insertion After Specific Node
    public fun insertAfter(prevNode: Node<T>, data: T)

    // Deletion at Beginning
    public fun deleteAtBeginning()

    // Deletion at End
    public fun deleteAtEnd()

    // Deletion of a specific node
    public fun deleteNode(node: Node<T>)
}
