package MyMap;

import javax.swing.tree.TreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author qxy
 * @Date 2023/10/16 16:28
 * @Version 1.0
 */
public class MyHashMap<K, V> {
    /* ------------Static property ------------- */
    // 初始的容量，可以暂时理解为初始的数组长度
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    // 最大的容量
    static final int MAXIMUM_CAPACITY = 1 << 30;
    // 加载因子，决定了扩容的时机
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    // 树化的阈值，冲突时链表增长，链表长度增高时，转为红黑树，减少插入查找的时间复杂度
    static final int TREEIFY_THRESHOLD = 8;
    // 去树化的阈值，之所以不是和TREEIFY_THRESHOLD不一致，是为了防止反复建树/去树的开销
    static final int UNTREEIFY_THRESHOLD = 6;
    // 树化的条件：需要数组长度不小于一个阈值，才考虑树化；否则，先resize数组。
    static final int MIN_TREEIFY_CAPACITY = 64;

    /* ------------Other Property ------------- */
    Node<K, V>[] table;
    int size;
    int threshold;
    float loadFactor;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public V get(Object key) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, false);
    }


    /* ------------Utils------------- */
    final Node<K, V> getNode(int hash, Object key) {
        // 保证数组不为空
        if (table != null && table.length > 0) {
            /**
             获取对应哈希桶位置的头结点。
             其中(table.length - 1) & hash的效果等于hash%table.length,只要满足长度是2的幂，这也是hashmap会对初始长度扩容调整至2的幂的原因。
             */
            Node<K, V> first = table[(table.length - 1) & hash];
            /**
             * 检测头结点是否为要找的entry
             * 1. hash值要相同
             * 2. key值相同：要不地址相同，要么满足equals方法的定义
             */
            if (first.hash == hash && (Objects.equals(key, first.key))) {
                return first;
            }
            /**
             * 如果头结点不是，则继续遍历链表或树
             * 需要根据firstNode所属的类，判断其遍历方式
             */
            if (first instanceof TreeNode) {
                // 此处写树的遍历逻辑...
                return null;
            }
            first = first.next;

            while (first != null) {
                // 和前面判断头结点的逻辑一样
                if (first.hash == hash && (Objects.equals(key, first.key))) {
                    return first;
                }
                first = first.next;
            }
        }
        return null;
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        // 先判断是否需要初始化
        if (table == null || table.length == 0) {
            table = resize();
        }

        int n = table.length; // 哈希槽的长度
        /**
         * 计算要插入的位置
         * 1. 如果为空，直接插入到该结点
         * 2. 如果不为空，则需要遍历到尾结点（也会出现Node和TreeNode的问题）
         */
        int i = (n - 1) & hash;
        Node<K, V> p = table[i];
        Node<K, V> e = null; // 用于承接返回值
        if (p == null)
//            table[i] = newNode(hash, key, value, null);
            table[i] = new Node<>(hash, key, value, null);
        else {
            if (p.hash == hash && Objects.equals(key, p.key))
                e = p;
            else if (p instanceof TreeNode) {
                // 树结点的处理逻辑...
            } else {
                // 此处也不考虑树化的逻辑
                while (p != null) {
                    if (p.hash == hash && Objects.equals(key, p.key)) {
                        e = p;
                        break;
                    }
                    p = p.next;
                }
            }
            // 判断是否有存在的key
            if (e != null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        // 考虑是否需要扩容
        if (++size > threshold)
            resize();
//        afterNodeInsertion(evict);
        return null;
    }

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap = 0, newThr = 0;
        // 确定新的容量大小
        if (oldCap > 0) {
            // 初始化过的
            if (oldCap > MAXIMUM_CAPACITY) {
                /**
                 * 这种情况就是不希望再扩容了
                 * 1. 将扩容的实际设置为最大值
                 * 2. 直接返回之前的map（不做处理）
                 */
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                /**
                 * 扩容后不超出最大范围
                 * 1. 容量双倍
                 * 2. 控制resize实际的threshold也双倍
                 */
                newCap = oldCap << 1;
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            // 这应该是在初始化的场景下，但是这一块和下面一块都不是很懂
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (newThr == 0) {
            // resize阈值的初始化
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;

        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        // 填充新的哈希槽
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; j++) {
                Node<K, V> e = oldTab[j];
                if (e != null) {
                    // 不为null则迁移
                    oldTab[j] = null;
                    if (e.next == null) // 没有哈希冲突的好处理，直接重新计算在数组中的位置即可
                        newTab[e.hash & (newCap - 1)] = e;
                    else{
                        // 出现了哈希冲突要分情况，是链表or红黑树
                        if(e instanceof TreeNode){
                            // 写红黑树迁移的逻辑...
                        }else{
                            // 写链表迁移的逻辑
                            /**
                             * 扩容两倍后，要么在原位置j，要么在oldCap+j
                             * 所以可以根据这个特点，分为两个链表，保证链表顺序的同时，插入到不同的位置
                              */
                            Node<K,V> loHead = null, loTail = null;
                            Node<K,V> hiHead = null, hiTail = null;
                            Node<K,V> next;
                            do {
                                next = e.next;
                                if ((e.hash & oldCap) == 0) {
                                    if (loTail == null)
                                        loHead = e;
                                    else
                                        loTail.next = e;
                                    loTail = e;
                                }
                                else {
                                    if (hiTail == null)
                                        hiHead = e;
                                    else
                                        hiTail.next = e;
                                    hiTail = e;
                                }
                            } while ((e = next) != null);
                            if (loTail != null) {
                                loTail.next = null;
                                newTab[j] = loHead;
                            }
                            if (hiTail != null) {
                                hiTail.next = null;
                                newTab[j + oldCap] = hiHead;
                            }
                        }
                    }

                }
            }
        }

        return newTab;
    }

    /* ------------Static utils------------- */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /* ------------Node节点------------- */
    static class Node<K, V> implements Map.Entry<K, V> {

        int hash; //用于快速查找比较
        K key; //键
        V value; //值
        Node<K, V> next; //使用拉链表处理冲突，所以记录next结点

        Node(int hash, K key, V value, Node<K, V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            //设置新的value，并返回旧值
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
}
