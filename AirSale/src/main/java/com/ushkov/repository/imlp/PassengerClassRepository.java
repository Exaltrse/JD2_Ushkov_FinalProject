package com.ushkov.repository.imlp;

import com.ushkov.domain.PassengerClass;
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
public class PassengerClassRepository implements CrudOperations<Short, PassengerClass> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<PassengerClass> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PassengerClass> passengerClassCriteriaQuery = cb.createQuery(PassengerClass.class);
            passengerClassCriteriaQuery.select(passengerClassCriteriaQuery.from(PassengerClass.class));

            return session.createQuery(passengerClassCriteriaQuery).getResultList();
        }
    }

    @Override
    public PassengerClass findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(PassengerClass.class, id);
        }
    }

    @Override
    public List<PassengerClass> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PassengerClass> passengerClassCriteriaQuery = cb.createQuery(PassengerClass.class);
            passengerClassCriteriaQuery.select(passengerClassCriteriaQuery.from(PassengerClass.class));

            return session.createQuery(passengerClassCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<PassengerClass> saveAll(List<PassengerClass> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<PassengerClass> resultOfSavingPassengerClassEntitiesList = entities.stream().map(e->e=(PassengerClass) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPassengerClassEntitiesList;
        }
    }

    @Override
    public PassengerClass saveOne(PassengerClass entity) {
        try(Session session = sessionFactory.openSession()){
            return (PassengerClass) session.save(entity);
        }
    }

    @Override
    public PassengerClass updateOne(PassengerClass entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }

}
