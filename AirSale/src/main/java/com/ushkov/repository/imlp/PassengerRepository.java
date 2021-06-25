package com.ushkov.repository.imlp;

import com.ushkov.domain.Passenger;
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
public class PassengerRepository implements CrudOperations<Long, Passenger> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Passenger> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Passenger> passengerCriteriaQuery = cb.createQuery(Passenger.class);
            passengerCriteriaQuery.select(passengerCriteriaQuery.from(Passenger.class));

            return session.createQuery(passengerCriteriaQuery).getResultList();
        }
    }

    @Override
    public Passenger findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Passenger.class, id);
        }
    }

    @Override
    public List<Passenger> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Passenger> passengerCriteriaQuery = cb.createQuery(Passenger.class);
            passengerCriteriaQuery.select(passengerCriteriaQuery.from(Passenger.class));

            return session.createQuery(passengerCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Passenger> saveAll(List<Passenger> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Passenger> resultOfSavingPassengerEntitiesList = entities.stream().map(e->e=(Passenger) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPassengerEntitiesList;
        }
    }

    @Override
    public Passenger saveOne(Passenger entity) {
        try(Session session = sessionFactory.openSession()){
            return (Passenger) session.save(entity);
        }
    }

    @Override
    public Passenger updateOne(Passenger entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
