package klib.data.type.collections.linkedlist

import klib.data.type.collections.linkedlist.model.DataNode

public class DoublyCircularLinkedList<T> : LinkedList<T>, MutableLinkedList<T> {

    override var head: DataNode<T>? = null
        private set
    override var tail: DataNode<T>? = null
        private set
    override var size: Int = 0
        private set

    // Insertion at Beginning
    override fun insertAtBeginning(data: T) {
        val newNode = DataNode(data)
        if (head == null) {
            head = newNode
            tail = newNode
            newNode.next = head
            newNode.prev = tail
        }
        else {
            newNode.next = head
            newNode.prev = tail
            head?.prev = newNode
            tail?.next = newNode
            head = newNode
        }
        size++
    }

    // Insertion at End
    override fun insertAtEnd(data: T) {
        val newNode = DataNode(data)
        if (tail == null) {
            head = newNode
            tail = newNode
            newNode.next = head
            newNode.prev = tail
        }
        else {
            newNode.next = head
            newNode.prev = tail
            tail?.next = newNode
            head?.prev = newNode
            tail = newNode
        }
        size++
    }

    // Insertion After Specific Node
    override fun insertAfter(prevNode: DataNode<T>, data: T) {
        if (head == null) return
        val newNode = DataNode(data)
        newNode.next = prevNode.next
        newNode.prev = prevNode
        prevNode.next?.prev = newNode
        prevNode.next = newNode
        size++
    }

    // Deletion at Beginning
    override fun deleteAtBeginning() {
        if (head == null) return
        if (head == tail) {
            head = null
            tail = null
        }
        else {
            tail?.next = head?.next
            head?.next?.prev = tail
            head = head?.next
        }
        size--
    }

    // Deletion at End
    override fun deleteAtEnd() {
        if (tail == null) return
        if (head == tail) {
            head = null
            tail = null
        }
        else {
            head?.prev = tail?.prev
            tail?.prev?.next = head
            tail = tail?.prev
        }
        size--
    }

    // Deletion of a specific node
    override fun deleteNode(node: DataNode<T>) {
        when (node) {
            head -> deleteAtBeginning()
            tail -> deleteAtEnd()

            else -> {
                node.prev?.next = node.next
                node.next?.prev = node.prev
                size--
            }
        }
    }

    override fun iterator(): MutableIterator<DataNode<T>> = DoublyMutableIterator(this)
}
