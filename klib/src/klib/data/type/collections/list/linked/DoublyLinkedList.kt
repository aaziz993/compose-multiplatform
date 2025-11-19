package klib.data.type.collections.list.linked

public class DoublyLinkedList<T> : MutableLinkedList<T> {

    override var head: Node<T>? = null
        private set
    override var tail: Node<T>? = null
        private set
    override var size: Int = 0
        private set

    // Insertion at Beginning
    override fun insertAtBeginning(data: T) {
        val newNode = Node(data)
        if (head == null) {
            head = newNode
            tail = newNode
        }
        else {
            newNode.next = head
            head?.prev = newNode
            head = newNode
        }
        size++
    }

    // Insertion at End
    override fun insertAtEnd(data: T) {
        val newNode = Node(data)
        if (tail == null) {
            head = newNode
            tail = newNode
        }
        else {
            newNode.prev = tail
            tail?.next = newNode
            tail = newNode
        }
        size++
    }

    // Insertion After Specific Node
    override fun insertAfter(prevNode: Node<T>, data: T) {
        if (head == null) return
        val newNode = Node(data)
        newNode.next = prevNode.next
        newNode.prev = prevNode
        prevNode.next?.prev = newNode
        prevNode.next = newNode
        size++
    }

    // Deletion at Beginning
    override fun deleteAtBeginning() {
        if (head == null) return
        head = head?.next
        head?.prev = null
        if (head == null) {
            tail = null
        }
        size--
    }

    // Deletion at End
    override fun deleteAtEnd() {
        if (tail == null) return
        tail = tail?.prev
        tail?.next = null
        if (tail == null) head = null
        size--
    }

    override fun deleteNode(node: Node<T>) {
        when (node) {
            head -> deleteAtBeginning()
            tail -> deleteAtEnd()
            else -> {
                node.prev!!.next = node.next
                node.next!!.prev = node.prev
                size--
            }
        }
    }

    override fun iterator(): MutableIterator<Node<T>> = DoublyMutableIterator(this)
}

