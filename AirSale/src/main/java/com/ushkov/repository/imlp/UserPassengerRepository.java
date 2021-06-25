package com.ushkov.repository.imlp;

import com.ushkov.domain.UserPassenger;
import com.ushkov.repository.CrudOperations;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserPassengerRepository implements CrudOperations<Long, UserPassenger> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<UserPassenger> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserPassenger> userPassengerCriteriaQuery = cb.createQuery(UserPassenger.class);
            userPassengerCriteriaQuery.select(userPassengerCriteriaQuery.from(UserPassenger.class));

            return session.createQuery(userPassengerCriteriaQuery).getResultList();
        }
    }

    @Override
    public UserPassenger findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(UserPassenger.class, id);
        }
    }

    @Override
    public List<UserPassenger> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserPassenger> userPassengerCriteriaQuery = cb.createQuery(UserPassenger.class);
            userPassengerCriteriaQuery.select(userPassengerCriteriaQuery.from(UserPassenger.class));

            return session.createQuery(userPassengerCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<UserPassenger> saveAll(List<UserPassenger> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<UserPassenger> resultOfSavingUserPassengerEntitiesList = entities.stream().map(e->e=(UserPassenger) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingUserPassengerEntitiesList;
        }
    }

    @Override
    public UserPassenger saveOne(UserPassenger entity) {
        try(Session session = sessionFactory.openSession()){
            return (UserPassenger) session.save(entity);
        }
    }

    @Override
    public UserPassenger updateOne(UserPassenger entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
