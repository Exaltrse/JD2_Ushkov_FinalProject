package com.ushkov.repository.imlp;

import com.ushkov.domain.AirlinePlane;
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
public class AirlinePlaneRepository implements CrudOperations<Long, AirlinePlane> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<AirlinePlane> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<AirlinePlane> airlinePlaneCriteriaQuery = cb.createQuery(AirlinePlane.class);
            airlinePlaneCriteriaQuery.select(airlinePlaneCriteriaQuery.from(AirlinePlane.class));

            return session.createQuery(airlinePlaneCriteriaQuery).getResultList();
        }
    }

    @Override
    public AirlinePlane findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(AirlinePlane.class, id);
        }
    }

    @Override
    public List<AirlinePlane> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<AirlinePlane> airlinePlaneCriteriaQuery = cb.createQuery(AirlinePlane.class);
            airlinePlaneCriteriaQuery.select(airlinePlaneCriteriaQuery.from(AirlinePlane.class));

            return session.createQuery(airlinePlaneCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<AirlinePlane> saveAll(List<AirlinePlane> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<AirlinePlane> resultOfSavingAirlinePlaneEntitiesList = entities.stream().map(e->e = findOne((Long) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingAirlinePlaneEntitiesList;
        }
    }

    @Override
    public AirlinePlane saveOne(AirlinePlane entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Long) session.save(entity));
        }
    }

    @Override
    public AirlinePlane updateOne(AirlinePlane entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
