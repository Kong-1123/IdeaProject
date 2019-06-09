package cn.com.dhcc.creditquery.ent.queryweb.util.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.query.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.PersistenceProvider;

public class CommonJpaRepositoryFactory extends JpaRepositoryFactory
{
    private final EntityManager entityManager;
    
    private final QueryExtractor extractor;
    
    public CommonJpaRepositoryFactory(EntityManager entityManager)
    {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
        
    }
    
}
