package be.vandenn3.quiestce.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, be.vandenn3.quiestce.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, be.vandenn3.quiestce.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, be.vandenn3.quiestce.domain.User.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Authority.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.User.class.getName() + ".authorities");
            createCache(cm, be.vandenn3.quiestce.domain.PersistentToken.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, be.vandenn3.quiestce.domain.Room.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Player.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.UserAnonymous.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Picture.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Theme.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Theme.class.getName() + ".mainPictures");
            createCache(cm, be.vandenn3.quiestce.domain.ThemeCard.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.ThemeCard.class.getName() + ".themes");
            createCache(cm, be.vandenn3.quiestce.domain.ThemeCard.class.getName() + ".pictures");
            createCache(cm, be.vandenn3.quiestce.domain.Game.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Game.class.getName() + ".rooms");
            createCache(cm, be.vandenn3.quiestce.domain.Game.class.getName() + ".themes");
            createCache(cm, be.vandenn3.quiestce.domain.Game.class.getName() + ".winners");
            createCache(cm, be.vandenn3.quiestce.domain.Message.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.Message.class.getName() + ".games");
            createCache(cm, be.vandenn3.quiestce.domain.Message.class.getName() + ".authors");
            createCache(cm, be.vandenn3.quiestce.domain.Game.class.getName() + ".nextTurnPlayers");
            createCache(cm, be.vandenn3.quiestce.domain.Room.class.getName() + ".player1s");
            createCache(cm, be.vandenn3.quiestce.domain.Room.class.getName() + ".player2s");
            createCache(cm, be.vandenn3.quiestce.domain.GameCard.class.getName());
            createCache(cm, be.vandenn3.quiestce.domain.GameCard.class.getName() + ".games");
            createCache(cm, be.vandenn3.quiestce.domain.GameCard.class.getName() + ".themeCards");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
