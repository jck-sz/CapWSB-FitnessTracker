package pl.wsb.fitnesstracker.training.internal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.user.api.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Training save(Training training) {
        if (training.getId() == null) {
            entityManager.persist(training);
            return training;
        } else {
            return entityManager.merge(training);
        }
    }

    public Optional<Training> findById(Long id) {
        Training training = entityManager.find(Training.class, id);
        return Optional.ofNullable(training);
    }

    public List<Training> findAll() {
        String jpql = "SELECT t FROM Training t";
        TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
        return query.getResultList();
    }

    public List<Training> findByUser(User user) {
        String jpql = "SELECT t FROM Training t WHERE t.user = :user";
        TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public List<Training> findByActivityType(ActivityType activityType) {
        String jpql = "SELECT t FROM Training t WHERE t.activityType = :activityType ORDER BY t.startTime DESC";
        TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
        query.setParameter("activityType", activityType);
        return query.getResultList();
    }

    public List<Training> findByDateRange(Date startDate, Date endDate) {
        String jpql = "SELECT t FROM Training t WHERE t.startTime >= :startDate AND t.endTime <= :endDate";
        TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public void delete(Training training) {
        if (entityManager.contains(training)) {
            entityManager.remove(training);
        } else {
            entityManager.remove(entityManager.merge(training));
        }
    }

    public void deleteById(Long id) {
        Training training = entityManager.find(Training.class, id);
        if (training != null) {
            entityManager.remove(training);
        }
    }
}
