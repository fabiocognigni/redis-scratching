package redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

public class MainJedisJava {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("localhost");
		//Jedis jedis = new Jedis("qaslot2.saksdirect.com", 6379);
		
		//SET ===============================
		long start = System.currentTimeMillis();
		Object res = jedis.set("jedis-key", "jedis-val");
		System.out.println("SET:" + (System.currentTimeMillis() - start));
		System.out.println(res);
		
		//GET =============================== 
		start = System.currentTimeMillis();
		Object val = jedis.get("jedis-key");
		System.out.println("GET:" + (System.currentTimeMillis() - start));
		System.out.println(val);
		
		jedis.del("jedis-key");
		
		jedis.close();
		//jedis.shutdown();
		
		//======================================================================
		// PIPELINING
		//======================================================================  
		/*
		JedisShardInfo si = new JedisShardInfo("127.0.0.1", 6379);
	    List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
	    list.add(si);
	    ShardedJedis shardedJedis = new ShardedJedis(list);
		
	    start = System.currentTimeMillis();
	    ShardedJedisPipeline pipeline = shardedJedis.pipelined();
	    Response<Long> resPipeline =pipeline.append("jedis-pipeline-key", "jedis-pipeline-val");
	    pipeline.sync();
	    
	    System.out.println("SET-PIPELINE:" + (System.currentTimeMillis() - start));
		System.out.println(resPipeline.get());
		*/
	}

}
