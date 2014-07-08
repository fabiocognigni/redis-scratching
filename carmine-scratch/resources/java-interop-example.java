package redis;

import carmine_scratch.core;

public class Hello {

    public static void main(String[] args) {
      System.out.println("Hello!");
                            
      clojure.lang.PersistentVector res = core.set("key-2", "val-2");     
      System.out.println("SET:");
      for (Object obj : res) 
        System.out.println(obj);
                                                    
      Object val = core.get("key-2");
      System.out.println("GET:");
      System.out.println(val);
                                                                        }
}
