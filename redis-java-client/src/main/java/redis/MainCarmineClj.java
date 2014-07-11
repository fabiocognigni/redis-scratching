package redis;

import carmine_scratch.core;

public class MainCarmineClj {

	public static void main(String[] args) {
		//SET ===============================
		// first. really slow ... why? Clj bootstraping / dynamic compilation (shouldn't be since AOT ) / classloading
		// http://nicholaskariniemi.github.io/2014/03/19/solving-clojure-boot-time.html
		long start1 = System.currentTimeMillis();
		Object res = core.set("key-2", "val-2");
		System.out.println("SET-1:" + (System.currentTimeMillis() - start1) + "\n");
		
		//second. fast
		long start2 = System.currentTimeMillis();
		res = core.set("key-20", "val-20");
		System.out.println("SET-2:" + (System.currentTimeMillis() - start2) + "\n");
		
		long start3 = System.currentTimeMillis();
		res = core.set("key-21", "val-21");
		System.out.println("SET-3:" + (System.currentTimeMillis() - start3) + "\n");
		
		//System.out.println(res);
		/*for (Object obj : res) {
			System.out.println(obj);
		}*/
		
		//GET =============================== 
		long startGet = System.currentTimeMillis();
		Object val = core.get("key-2");
		System.out.println("GET:" + (System.currentTimeMillis() - startGet));
		System.out.println(val + "\n");
	}

}
