package com.ushkov.repository.imlp;

import com.ushkov.domain.FlightPlane;
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
public class FlightPlaneRepository implements CrudOperations<Long, FlightPlane> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<FlightPlane> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<FlightPlane> flightPlaneCriteriaQuery = cb.createQuery(FlightPlane.class);
            flightPlaneCriteriaQuery.select(flightPlaneCriteriaQuery.from(FlightPlane.class));

            return session.createQuery(flightPlaneCriteriaQuery).getResultList();
        }
    }

    @Override
    public FlightPlane findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(FlightPlane.class, id);
        }
    }

    @Override
    public List<FlightPlane> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<FlightPlane> flightPlaneCriteriaQuery = cb.createQuery(FlightPlane.class);
            flightPlaneCriteriaQuery.select(flightPlaneCriteriaQuery.from(FlightPlane.class));

            return session.createQuery(flightPlaneCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<FlightPlane> saveAll(List<FlightPlane> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<FlightPlane> resultOfSavingFlightPlaneEntitiesList = entities.stream().map(e->e=(FlightPlane) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingFlightPlaneEntitiesList;
        }
    }

    @Override
    public FlightPlane saveOne(FlightPlane entity) {
        try(Session session = sessionFactory.openSession()){
            return (FlightPlane) session.save(entity);
        }
    }

    @Override
    public FlightPlane updateOne(FlightPlane entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
