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
		final int TOTAL_THREADS = 1000;
		final int TOTAL_OPERATIONS_PER_THREAD = 10;
		final int POOL_SIZE = 100;
		final AtomicInteger numberOfFailures = new AtomicInteger();
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(POOL_SIZE);
		poolConfig.setBlockWhenExhausted(true);  //true by default 
		final JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 1000);
		
		writeFixtureData(pool);
		
		List<Thread> tds = new ArrayList<Thread>();

	    final AtomicInteger ind = new AtomicInteger();
	    for (int j = 0; j < TOTAL_THREADS; j++) {
	        Thread hj = new Thread(new Runnable() {
	        	int threadId = ind.getAndIncrement();
	            public void run() {
	                //for (int i = 0; (i = ind.getAndIncrement()) < TOTAL_OPERATIONS;) {
	                for (int i = 0; i < TOTAL_OPERATIONS_PER_THREAD; i++) {	
	                    try {
	                    	//TODO metric.time between here and ...
	                        Jedis jedis = pool.getResource();
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-1"));
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-2"));
	                        System.out.println(threadId + " --- " + i + " --- " + jedis.get("key-3"));
	                        pool.returnResource(jedis);
	                        // ...here! (to see also how much the pool config affects performances)
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        numberOfFailures.getAndIncrement();
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
	    
	    System.out.println("Failures: " + numberOfFailures.get());
	}
	
	public static void writeFixtureData(JedisPool pool){
		Jedis jedis = pool.getResource();
		
		jedis.set("key-1", "value-1");
		jedis.set("key-2", "value-2");
		jedis.set("key-3", "value-3");
		
		pool.returnResource(jedis);
	}
}
