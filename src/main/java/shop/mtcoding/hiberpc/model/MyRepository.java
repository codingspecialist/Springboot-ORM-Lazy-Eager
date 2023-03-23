package shop.mtcoding.hiberpc.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class MyRepository<T> {
    private final EntityManager em;

    public MyRepository(EntityManager em) {
        this.em = em;
    }

    public T findById(int id){
        return em.find(getEntityClass(), id);
    }
    public List<T> findAll(){
        return em.createQuery("select alias from "+getEntityName()+" alias", getEntityClass()).getResultList();
    }

    @Transactional
    public T save(T entity){
        try {
            // Object id = getEntityClass().getMethod("getId").invoke(entity);

            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            if (id == null) {
                em.persist(entity);
            } else {
                entity = em.merge(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save entity: " + entity, e);
        }
        return entity;
    }

    @Transactional
    public void delete(T entity){
        em.remove(entity);
    }

    protected abstract Class<T> getEntityClass();

    protected abstract String getEntityName();
}
