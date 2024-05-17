package br.com.houseseeker;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

public abstract class AbstractJpaIntegrationTest extends PostgreSQLIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private TransactionStatus status;

    @BeforeEach
    public void setup() {
        status = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
    }

    @AfterEach
    public void finish() {
        platformTransactionManager.rollback(status);
    }

    @BeforeAll
    static void setupBeforeClass() {
        POSTGRESQL_CONTAINER.start();
    }

    protected final Provider findProviderById(int id) {
        return entityManager.createQuery("select p from Provider p where p.id = :id", Provider.class)
                            .setParameter("id", id)
                            .getSingleResult();
    }

    protected final UrbanProperty findUrbanPropertyById(int id) {
        return entityManager.createQuery("select up from UrbanProperty up where up.id = :id", UrbanProperty.class)
                            .setParameter("id", id)
                            .getSingleResult();
    }

    protected final <T> long countBy(EntityPathBase<T> entityPath, Predicate... predicates) {
        return jpaQueryFactory.select(entityPath.count())
                              .from(entityPath)
                              .where(predicates)
                              .fetchFirst();
    }

}
