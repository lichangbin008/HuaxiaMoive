package com.lcb.study.hashmap;

import java.util.Map;

/**
 * Created by ${lichangbin} on 2020/9/1.
 */
public class MyHashMap {

    //     扩冲
    float loadFactor = 0.75f;
    //16  1<<4
    static int threshold = 0;
//    Node[] table = null;
    // 2. 实际用到table 存储容量 大小
    int size;
    public MyHashMap() {
        threshold = 1 << 4;
    }

    //   5 %3   效果差
//    位运算  15
//     2   4   8  16   32
//    14   hashmap     查找速度是最有
    public MyHashMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        threshold = tableSizeFor(initialCapacity);
    }

    //    数组容量的确定   hash
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return  n + 1;
    }
}
