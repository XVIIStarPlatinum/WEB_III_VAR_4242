package example.repository;

import example.entity.ResultEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

import java.util.Properties;
import java.util.Collection;

@Getter
public class ResultRepository implements Repository {
    private final EntityManager entityManager;
    public ResultRepository(){
         entityManager = JPAUtils.getFactory().createEntityManager();
    }
    @Override
    public void addNewResult(ResultEntity result) {
        entityManager.getTransaction().begin();
        entityManager.persist(result);
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateResult(Long result_id, ResultEntity result) {
        entityManager.getTransaction().begin();
        entityManager.merge(result);
        entityManager.getTransaction().commit();
    }

    @Override
    public ResultEntity getResultById(Long result_id) {
        return entityManager.getReference(ResultEntity.class, result_id);
    }

    @Override
    public Collection<ResultEntity> getAllResults() {
        var cm = entityManager.getCriteriaBuilder().createQuery(ResultEntity.class);
        Root<ResultEntity> root = cm.from(ResultEntity.class);
        return entityManager.createQuery(cm.select(root)).getResultList();
    }

    @Override
    public void deleteResult(ResultEntity result) {
        entityManager.getTransaction().begin();
        entityManager.remove(result);
        entityManager.getTransaction().commit();
    }

    @Override
    public void clearResults() {
        entityManager.getTransaction().begin();
        try {
            Query query = entityManager.createQuery("DELETE FROM ResultEntity r");
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.clear();
        }
    }
}
