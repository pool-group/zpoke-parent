package com.zren.platform.bout.common.util;


 public class LockUtil
 {
   private static Object[] locks;
   static
   {
     int concurrency = 512;
     locks = new Object[concurrency];
     for (int i = 0; i < concurrency; i++) {
       locks[i] = new Object();
     }
   }
   
   public static Object getLock(Object k)
   {
     if (null == k) {
       throw new NullPointerException("lock key is null");
     }
     return getLock(k.hashCode());
   }
   
   public static Object getLock(Object... keys)
   {
     if (null == keys) {
       throw new NullPointerException("lock key is null");
     }
     int h = 0;
     for (int i = 0; i < keys.length; i++) {
       if (null != keys[i]) {
         h += keys[i].hashCode();
       }
     }
     return getLock(h);
   }
 
   private static Object getLock(int i)
   {
     int index = Math.abs(i) % locks.length;
     return locks[index];
   }
   
   private LockUtil() {}
 }

