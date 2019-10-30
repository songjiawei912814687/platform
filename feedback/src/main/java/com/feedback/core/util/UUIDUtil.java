package com.feedback.core.util;

import java.util.UUID;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:57 2018/10/18
 * @modified by:
 */
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
