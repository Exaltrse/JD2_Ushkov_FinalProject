package com.ushkov.repository.imlp;
import com.ushkov.domain.PassengerPassport;
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
public class PassengerPassportRepository implements CrudOperations<Long, PassengerPassport> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<PassengerPassport> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PassengerPassport> passengerPassportCriteriaQuery = cb.createQuery(PassengerPassport.class);
            passengerPassportCriteriaQuery.select(passengerPassportCriteriaQuery.from(PassengerPassport.class));

            return session.createQuery(passengerPassportCriteriaQuery).getResultList();
        }
    }

    @Override
    public PassengerPassport findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(PassengerPassport.class, id);
        }
    }

    @Override
    public List<PassengerPassport> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PassengerPassport> passengerPassportCriteriaQuery = cb.createQuery(PassengerPassport.class);
            passengerPassportCriteriaQuery.select(passengerPassportCriteriaQuery.from(PassengerPassport.class));

            return session.createQuery(passengerPassportCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<PassengerPassport> saveAll(List<PassengerPassport> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<PassengerPassport> resultOfSavingPassengerPassportEntitiesList = entities.stream().map(e->e=(PassengerPassport) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPassengerPassportEntitiesList;
        }
    }

    @Override
    public PassengerPassport saveOne(PassengerPassport entity) {
        try(Session session = sessionFactory.openSession()){
            return (PassengerPassport) session.save(entity);
        }
    }

    @Override
    public PassengerPassport updateOne(PassengerPassport entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
