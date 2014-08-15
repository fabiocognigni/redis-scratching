package redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BenchmarkJedisPool {
	
	/*
	 * look at http://www.programcreek.com/java-api-examples/index.php?api=redis.clients.jedis.JedisPool
	 * try to implement that same with executors
	 */

	public static void main(String[] args) {
		final int TOTAL_OPERATIONS = 1000;
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(100);
		//default value of the WhenExhaustedAction (WHEN_EXHAUSTED_BLOCK), ??? 
		final JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 1000);
		
		writeFixtureData(pool);
		
		List<Thread> tds = new ArrayList<Thread>();

	    final AtomicInteger ind = new AtomicInteger();
	    for (int i = 0; i < 1000; i++) {
	        Thread hj = new Thread(new Runnable() {
	        	int threadId = ind.getAndIncrement();
	            public void run() {
	                //for (int i = 0; (i = ind.getAndIncrement()) < TOTAL_OPERATIONS;) {
	                for (int i = 0; i < TOTAL_OPERATIONS; i++) {	
	                    try {
	                        Jedis jedis = pool.getResource();
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-1"));
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-2"));
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-3"));
	                        pool.returnResource(jedis);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        });
	        tds.add(hj);
	        hj.start();
	    }

	    for (Thread t : tds)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	    pool.destroy();
	}
	
	public static void writeFixtureData(JedisPool pool){
		Jedis jedis = pool.getResource();
		
		jedis.set("key-1", "value-1");
		jedis.set("key-2", "value-2");
		jedis.set("key-3", "value-3");
		
		pool.returnResource(jedis);
	}
}
