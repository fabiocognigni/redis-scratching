package redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.s5a.metrics.MetricNamespace;
import com.s5a.metrics.Recorder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BenchmarkJedisPool {
	
	/*
	 * look at http://www.programcreek.com/java-api-examples/index.php?api=redis.clients.jedis.JedisPool
	 * try to implement that same with executors
	 */

	public static void main(String[] args) {
		System.setProperty("env","dev");
		System.setProperty("configDir","config");
		
		final int TOTAL_THREADS = 80;
		final int TOTAL_OPERATIONS_PER_THREAD = 10000;
		final int POOL_SIZE = 250;
		final AtomicInteger numberOfFailures = new AtomicInteger();
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(POOL_SIZE);
		poolConfig.setMaxIdle(POOL_SIZE);
		poolConfig.setMinIdle(POOL_SIZE);
		poolConfig.setBlockWhenExhausted(true);  //true by default 
		final JedisPool pool = new JedisPool(poolConfig, "qaslot2.saksdirect.com"/*"127.0.0.1"*/, 6379, 250);
		
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
	                    	long startPool = System.currentTimeMillis();
	                        Jedis jedis = pool.getResource();
	                        
	                        long start = System.currentTimeMillis();
	                        String res = jedis.get("key-1");
	                        long getTime = System.currentTimeMillis() - start;
	                        Recorder.time(MetricNamespace.TOGGLES, "JEDIS.BENCHMARK.get", getTime);
	                        
	                        pool.returnResource(jedis);
	                        long getTimeWithPool = System.currentTimeMillis() - startPool;
	                        Recorder.time(MetricNamespace.TOGGLES, "JEDIS.BENCHMARK.getPool", getTimeWithPool);
	                        
	                        if(getTimeWithPool > 5000)
	                        	System.out.println("more then 5 secs!!");
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        numberOfFailures.getAndIncrement();
	                        
	                        Recorder.increment(MetricNamespace.TOGGLES, "JEDIS.BENCHMARK.failure");
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
	    
	    removeFixtureData(pool);

	    pool.destroy();
	    
	    System.out.println("Completed.");
	    System.out.println("Failures: " + numberOfFailures.get());
	}
	
	public static void writeFixtureData(JedisPool pool){
		Jedis jedis = pool.getResource();
		
		jedis.set("key-1", "value-1");
		jedis.set("key-2", "value-2");
		jedis.set("key-3", "value-3");
		
		pool.returnResource(jedis);
	}
	
	public static void removeFixtureData(JedisPool pool){
		Jedis jedis = pool.getResource();
		
		jedis.del("key-1");
		jedis.del("key-2");
		jedis.del("key-3");
		
		pool.returnResource(jedis);
	}
}
