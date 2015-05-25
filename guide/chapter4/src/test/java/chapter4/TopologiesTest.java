package chapter4;

import static org.junit.Assert.*;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.junit.Before;
import org.junit.Test;

public class TopologiesTest {

	private DefaultCacheManager cacheManager;
	private Cache<String, String> localCache;

	@Before
	public void setUpBeforeClass() throws Exception {
		String config = "sample.xml";
		cacheManager = new DefaultCacheManager(config);
		
		// Return the DefaultLocalCache
		localCache = cacheManager.getCache("DefaultLocalCache");
		cacheManager.start();
	}
	

	
   @Test
   public void testLocalCache(){
	   assertEquals(localCache.size(), 0);
	   localCache.put("K01","V01");
	   assertEquals(localCache.get("K01"), "V01");
	   
	   
   }
  /* 
   @Test
   public void testLocalCacheWithAPI(){
		ClusteringConfiguration conf = new ClusteringConfiguration();
		EmbeddedCacheManager manager = conf.getLocalCacheManager();
		Cache<String, String> localCache = manager.getCache();
		localCache.put("K01", "V01");
	   
   }*/

}
