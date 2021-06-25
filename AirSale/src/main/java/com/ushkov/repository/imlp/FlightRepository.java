package com.ushkov.repository.imlp;

import com.ushkov.domain.Flight;
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
public class FlightRepository implements CrudOperations<Integer, Flight> {

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Flight> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Flight> flightCriteriaQuery = cb.createQuery(Flight.class);
            flightCriteriaQuery.select(flightCriteriaQuery.from(Flight.class));

            return session.createQuery(flightCriteriaQuery).getResultList();
        }
    }

    @Override
    public Flight findOne(Integer id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Flight.class, id);
        }
    }

    @Override
    public List<Flight> findLimitOffset(Integer limit, Integer offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Flight> flightCriteriaQuery = cb.createQuery(Flight.class);
            flightCriteriaQuery.select(flightCriteriaQuery.from(Flight.class));

            return session.createQuery(flightCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Flight> saveAll(List<Flight> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Flight> resultOfSavingFlightEntitiesList = entities.stream().map(e->e=(Flight) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingFlightEntitiesList;
        }
    }

    @Override
    public Flight saveOne(Flight entity) {
        try(Session session = sessionFactory.openSession()){
            return (Flight) session.save(entity);
        }
    }

    @Override
    public Flight updateOne(Flight entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
