# 目标
首先从最简单的HashMap开始实现，其次是HashTable，最后实现concurrentHashMap，因为不同版本的concurrentHashMap实现不同，此处选用JDK1.8版本的实现。
因为这几个Map均实现了Map接口，所以我这里实现去针对Map中的通用方法进行筛选。

# HashMap
## 整体结构
以HashMap为例，需要实现的方法如下：
```java
public class MyHashMap<K,V>{

    public V get(Object key) {
        return null;
    }

    public V put(K key, V value) {
        return null;
    }
}

```
比较关键的静态属性（后续未必会使用）
```java
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
```
会用到的类的属性
```java
// 用于存放Node节点的数组，可以理解为哈希桶，仅存放发生冲突的首节点
Node<K,V>[] table;
// 当前所有的Node数量，用于与加载因子配合，决定是否扩容
int size;
```
## 结点
HashMap中的每个结点使用Node来表示,Node实现了Entry接口。可以先看下修改实现Entry哪些方法。
节点分为两种
一种是Node,一种是TreeNode，暂时先不考虑树化的情况。
普通Node的代码如下：
```java
static class Node<K,V> implements Map.Entry<K,V>{

    @Override
    public K getKey() {
        return null;
    }

    @Override
    public V getValue() {
        return null;
    }

    @Override
    public V setValue(V value) {
        return null;
    }
}
```
属性比较好理解
```java
int hash; //用于快速查找比较
K key; //键
V value; //值
Node<K,V> next; //使用拉链表处理冲突，所以记录next结点
```

## 实现get方法
### get的框架
map中的get的目标是根据key获取value。
```java
// 通俗写法
public V get(Object key) {
    Node<K, V> e = getNode(hash(key), key);
    if (e == null) return null;
    return e.value;
}
// hashmap中的实现，更简洁
public V get(Object key) {
    Node<K, V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

### 实现getNode函数
其实主要的逻辑在getNode中目前还没有实现。
想法也比较简单，如果存储有Node的数组（可以理解哈希桶）不为空，则去计算当前key所在的位置，如果当前key所以位置的hash，和key都符合要求，则返回。
如果不符合，则考虑可能发生了冲突。根据Node节点是TreeNode还是普通Node，分别进行遍历。
```java
// 通俗实现
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
        if(first instanceof TreeNode){
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
// hashmap中实现，比较多的将赋值判断写在一起，代码整体长度减少
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                // 此处写树的遍历逻辑...
                return null;
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

### 实现hash函数
其中`hash()`方法定义如何去计算哈希值。
这里可以看到几点：
* key不存在时，返回的哈希值是0.
* key存在的话，则使用java本身的`hashCode()`方法，为什么要按位与(h>>>16)呢？
```java
static int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
至此，一个不考虑树化情况下的get方法就实现了。

## 实现put方法
### put的框架
put方法用于新增或更新数据，包含的逻辑比较复杂：包含初始化、扩容等问题。
```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, flase, false);
}
```
hash方法在get中已经提到了，putVal和getVal对应，主要逻辑也如何去操作table, 后面两个false参数不好理解，我们先来看putVal方法具体做了什么。

### 实现putVal
大体的流程如下：
1. 判断是否需要初始化
2. 判断哈希槽的位置是否已有值，如果无值，直接插入即可
3. 若哈希槽位置有值，判断是否是要修改的值，如果是，直接修改
4. 如果不是，则遍历链表，并且计数，如果超出`TREEIFY_THRESHOLD`需要树化。
5. 找到相应的结点，则直接修改结点的value，并返回oldValue。
6. 如果未找到相应结点，则需要插入新结点。但是在插入前，需要判断是否需要扩容。
```java
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
        table[i] = newNode(hash, key, value, null);
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
    
    afterNodeInsertion(evict);
    return null;
}
```
其中扩容函数`resize()`,onlyIfAbsent,evict参数，树化都没考虑。