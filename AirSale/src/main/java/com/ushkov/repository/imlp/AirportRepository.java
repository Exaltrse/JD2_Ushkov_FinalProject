package com.ushkov.repository.imlp;

import com.ushkov.domain.Airport;
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
public class AirportRepository implements CrudOperations<Short, Airport> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Airport> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Airport> airportCriteriaQuery = cb.createQuery(Airport.class);
            airportCriteriaQuery.select(airportCriteriaQuery.from(Airport.class));

            return session.createQuery(airportCriteriaQuery).getResultList();
        }
    }

    @Override
    public Airport findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Airport.class, id);
        }
    }

    @Override
    public List<Airport> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Airport> airportCriteriaQuery = cb.createQuery(Airport.class);
            airportCriteriaQuery.select(airportCriteriaQuery.from(Airport.class));

            return session.createQuery(airportCriteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
        }
    }

    @Override
    public List<Airport> saveAll(List<Airport> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Airport> resultOfSavingAirportEntitiesList = entities.stream().map(e->e=(Airport) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingAirportEntitiesList;
        }
    }

    @Override
    public Airport saveOne(Airport entity) {
        try(Session session = sessionFactory.openSession()){
            return (Airport) session.save(entity);
        }
    }

    @Override
    public Airport updateOne(Airport entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
