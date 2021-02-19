package com.joker.basic.java.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class HashSetTest {
    
    @Test
    public void testAdd() {
        Set<String> testHashSet = new HashSet<>();
        boolean flag1 = testHashSet.add("abc");
        System.out.println(flag1);
        boolean flag2 = testHashSet.add("abc");
        System.out.println(flag2);
        assertTrue( true );
    }
}