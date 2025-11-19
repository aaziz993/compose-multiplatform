package klib.data.type.collections.list.linked

public class CircularLinkedList<T> : LinkedList<T>, MutableLinkedList<T> {

    override var head: Node<T>? = null
        private set
    override val tail: Node<T>? = null
    override var size: Int = 0
        private set

    // Insertion at Beginning.
    override fun insertAtBeginning(data: T) {
        val newNode = Node(data)
        if (head == null) {
            head = newNode
            newNode.next = head
        }
        else {
            var current = head
            while (current?.next != head) {
                current = current?.next
            }
            newNode.next = head
            current?.next = newNode
            head = newNode
        }
        size++
    }

    // Insertion at End.
    override fun insertAtEnd(data: T) {
        val newNode = Node(data)
        if (head == null) {
            head = newNode
            newNode.next = head
        }
        else {
            var current = head
            while (current?.next != head) {
                current = current?.next
            }
            newNode.next = head
            current?.next = newNode
        }
        size++
    }

    // Insertion After Specific Node.
    override fun insertAfter(prevNode: Node<T>, data: T) {
        if (head == null) return
        val newNode = Node(data)
        newNode.next = prevNode.next
        prevNode.next = newNode
        size++
    }

    // Deletion at Beginning.
    override fun deleteAtBeginning() {
        if (head == null) return
        if (head?.next == head) {
            head = null
            size--
            return
        }
        var current = head
        while (current?.next != head) {
            current = current?.next
        }
        head = head?.next
        current?.next = head
        size--
    }

    // Deletion at End.
    override fun deleteAtEnd() {
        if (head == null) return
        if (head?.next == head) {
            head = null
            size--
            return
        }
        var current = head
        var prev: Node<T>? = null
        while (current?.next != head) {
            prev = current
            current = current?.next
        }
        prev?.next = head
        size--
    }

    // Deletion of a specific node.
    override fun deleteNode(node: Node<T>) {
        if (head == null) return
        if (node == head) return deleteAtBeginning()

        var current = head

        while (current?.next != head && current?.next != node) {
            current = current?.next
        }

        if (current?.next == node) {
            current.next = node.next
            size--
        }
    }

    // Mutable Iterator over Nodes
    override fun iterator(): MutableIterator<Node<T>> = object : MutableIterator<Node<T>> {
        private var prev: Node<T>? = null
        private var current: Node<T>? = null
        private var next: Node<T>? = head

        override fun hasNext(): Boolean = next != null

        override fun next(): Node<T> {
            if (next == null) throw NoSuchElementException()
            prev = current
            current = next
            next = if (next?.next == head) null else next?.next
            return current!!
        }

        override fun remove() {
            val node = requireNotNull(current) { "Call next() before remove()" }

            if (node == head) deleteAtBeginning()
            else {
                prev?.next = node.next
                size--
            }

            current = null
        }
    }
}
