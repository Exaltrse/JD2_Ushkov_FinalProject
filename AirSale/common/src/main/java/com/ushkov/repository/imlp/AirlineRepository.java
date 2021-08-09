package com.ushkov.repository.imlp;

import com.ushkov.domain.Airline;
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
public class AirlineRepository implements CrudOperations<Short, Airline> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Airline> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Airline> airlineCriteriaQuery = cb.createQuery(Airline.class);
            airlineCriteriaQuery.select(airlineCriteriaQuery.from(Airline.class));

            return session.createQuery(airlineCriteriaQuery).getResultList();
        }
    }

    @Override
    public Airline findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Airline.class, id);
        }
    }

    @Override
    public List<Airline> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Airline> airlineCriteriaQuery = cb.createQuery(Airline.class);
            airlineCriteriaQuery.select(airlineCriteriaQuery.from(Airline.class));

            return session.createQuery(airlineCriteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
        }
    }

    @Override
    public List<Airline> saveAll(List<Airline> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Airline> resultOfSavingAirlineEntitiesList = entities.stream().map(e->e=findOne((Short) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingAirlineEntitiesList;
        }

    }

    @Override
    public Airline saveOne(Airline entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Short) session.save(entity));
        }
    }

    @Override
    public Airline updateOne(Airline entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
