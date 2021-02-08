package com.joker.basic.java.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class HashSetTest {
    
    @Test
    public void testAdd() {
        Set<String> testHashSet = new HashSet<>();
        boolean flag1 = testHashSet.add("abc");
        System.out.println(flag1);
        boolean flag2 = testHashSet.add("abc");
        System.out.println(flag2);
    }
}