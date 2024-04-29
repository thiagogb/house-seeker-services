package br.com.houseseeker;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@SpringBootTest(classes = DataApplication.class)
@DirtiesContext
public abstract class AbstractJpaIntegrationTest implements PostgreSQLIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private TransactionStatus status;

    @BeforeAll
    static void setupBeforeClass() {
        POSTGRESQL_CONTAINER.start();
    }

    @BeforeEach
    void setup() {
        status = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
    }

    @AfterAll
    static void finishAfterClass() {
        POSTGRESQL_CONTAINER.stop();
    }

    @AfterEach
    void finish() {
        platformTransactionManager.rollback(status);
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

}
