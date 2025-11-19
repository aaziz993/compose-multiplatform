package klib.data.type.collections.list.linked

public class DoublyCircularLinkedList<T> : LinkedList<T>, MutableLinkedList<T> {

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
        val newNode = Node(data)
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
    override fun deleteNode(node: Node<T>) {
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

    override fun iterator(): MutableIterator<Node<T>> = DoublyMutableIterator(this)
}
