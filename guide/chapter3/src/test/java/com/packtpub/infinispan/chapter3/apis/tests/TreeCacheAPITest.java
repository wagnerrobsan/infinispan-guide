package com.packtpub.infinispan.chapter3.apis.tests;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.TreeCache;


import org.infinispan.tree.TreeCacheFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wrsantos on 10/05/15.
 */

public class TreeCacheAPITest {

    private EmbeddedCacheManager manager;

    @Before
    public void setUp() throws Exception {
        //Configuration config = new ConfigurationBuilder().transaction().invocationBatching().enable()
          //      .build();
        manager = new DefaultCacheManager("sample.xml");

    }

    @Test
    public void testTreeCacheConfiguration() throws Exception {
        TreeCache<String, String> tc = new TreeCacheFactory().createTreeCache(manager.<String, String>getCache("TreeCache"));
        tc.put("/books/packt/infinispan/guide", "ISBN", "1782169970");
        tc.put("/books/packt/infinispan/guide", "Author", "Wagner R. Santos");
        tc.put("/books/packt/infinispan/guide", "ReleaseYear", "2014");
        assertEquals("1782169970", tc.get("/books/packt/infinispan/guide", "ISBN"));
        assertEquals("Wagner R. Santos", tc.get("/books/packt/infinispan/guide", "Author"));
        assertEquals("2014", tc.get("/books/packt/infinispan/guide", "ReleaseYear"));
        tc.remove("/books/packt/infinispan/guide", "ReleaseYear");
        assertNull(tc.get("/books/packt/infinispan/guide", "ReleaseYear"));
    }

}
