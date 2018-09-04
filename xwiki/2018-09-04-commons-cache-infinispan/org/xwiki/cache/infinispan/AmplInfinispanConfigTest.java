package org.xwiki.cache.infinispan;


import org.xwiki.cache.infinispan.internal.InfinispanCacheFactory;
import org.xwiki.cache.internal.DefaultCacheFactory;
import org.xwiki.cache.internal.DefaultCacheManager;
import org.xwiki.cache.internal.DefaultCacheManagerConfiguration;
import org.xwiki.cache.test.AbstractTestCache;
import org.xwiki.test.annotation.ComponentList;


@ComponentList({ InfinispanCacheFactory.class, DefaultCacheManager.class, DefaultCacheFactory.class, DefaultCacheManagerConfiguration.class })
public class AmplInfinispanConfigTest extends AbstractTestCache {
    public AmplInfinispanConfigTest() {
        super("infinispan");
    }
}

