package redis;

import carmine_scratch.core;

public class MainCarmineClj {

	public static void main(String[] args) {
		
		//SET ===============================
		long start = System.currentTimeMillis();
		Object res = core.set("key-2", "val-2");
		System.out.println("SET:" + (System.currentTimeMillis() - start));
		System.out.println(res);
		/*for (Object obj : res) {
			System.out.println(obj);
		}*/
		
		//GET =============================== 
		start = System.currentTimeMillis();
		Object val = core.get("key-2");
		System.out.println("GET:" + (System.currentTimeMillis() - start));
		System.out.println(val);
	}

}
