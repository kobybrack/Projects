/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        int hash = (key.hashCode() % getCapacity());
        if (hash < 0) {
            hash = hash* -1;
        }
        if (buckets[hash] == null) {
            return null;
        } else {
            Pair node = buckets[hash];
            while (node != null) {
                if (node.key == key) {
                    return node.value;
                }
                node = node.next;
            }
            return null;
        }
        
        //do this together with put.
        
        //throw new UnsupportedOperationException();
    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
        int hash = (key.hashCode() % getCapacity());
        if (hash < 0) {
            hash = hash * -1;
        }
        Pair newNode = new Pair(key, val);
        if (buckets[hash] == null) {
            buckets[hash] = newNode;
            size++;
            growIfNeeded();
            return null;
        } else {
            Pair oldNode = buckets[hash];
            Pair prev = null;
            while (oldNode != null) {
                if (oldNode.key == key) {
                    V retunThis = oldNode.value;
                    oldNode.value = val;
                    return retunThis;
                }
                prev = oldNode;
                oldNode = oldNode.next;
            }
            
            prev.next = newNode;
            size++;
            growIfNeeded();
            return null;
        }

        //   do this together with get. For now, don't worry about growing the
        //   array and rehashing.
        //   Tips:
        //     - Use the key's hashCode method to find which bucket it belongs in.
        //     - It's possible for hashCode to return a negative integer.
        //
        // modify this method to grow and rehash if the load factor
        //            exceeds 0.8.
        
        //throw new UnsupportedOperationException();
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        if (getSize() == 0) {
            return false;
        }
        int hash = (key.hashCode() % getCapacity());
        if (hash < 0) {
            hash = hash * -1;
        }
        if (buckets[hash] == null) {
            return false;
        } else {
            Pair node = buckets[hash];
            while (node != null) {
                if (node.key == key) {
                    return true;
                }
                node = node.next;
            }
            return false;    
        }
        
        //throw new UnsupportedOperationException();
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
        int hash = (key.hashCode() % getCapacity());
        if (hash < 0) {
            hash = hash * -1;
        }
        if (buckets[hash] == null) {
            return null;
        }
        Pair node = buckets[hash];
        if (node.key == key) {
            if (node.next != null) {
                buckets[hash] = node.next;
                size--;
                return node.value;
            }
            buckets[hash] = null;
            size--;
            return node.value;
        }
        Pair parent = node;
        while (node.key != key) {
            if (node.next == null) {
                return null;
            }
            parent = node;
            node = node.next;
        }
        if (node.next != null) {
            parent.next = node.next;
            size--;
            return node.value;
        }
        parent.next = null;
        size--;
        return node.value;
        
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the array size
     * (capacity) and rehash values from the old array to the new array */
    private void growIfNeeded() {
        double loadFactor = ((double) getSize())/getCapacity();
        if (loadFactor > 0.8) {
            Pair[] copy = buckets;
            int oldCapacity = getCapacity();
            int newCapacity = (getCapacity()*2);
            
            this.buckets = createBucketArray(newCapacity);
            for (int i = 0; i < oldCapacity; i++) {
                Pair node = copy[i];
                if (node != null) {
                    while (node.next != null) {
                        fastPut(node.key, node.value);
                        node = node.next;
                    }
                    fastPut(node.key, node.value);
                }
            }
        }
    }

    //instead of searching the entire array for duplicates, this sets
    //the newest node as the parent
    private void fastPut(K key, V value) {
        int hash = (key.hashCode() % getCapacity());
        if (hash < 0) {
            hash = hash * -1;
        }
        Pair node = new Pair(key, value);
        if (buckets[hash] == null) {
            buckets[hash] = node;
        } else {
            Pair temp = buckets[hash];
            buckets[hash] = node;
            node.next = temp;
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
