package com.ushkov.repository.imlp;

import com.ushkov.domain.Plane;
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
public class PlaneRepository implements CrudOperations<Integer, Plane> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Plane> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Plane> planeCriteriaQuery = cb.createQuery(Plane.class);
            planeCriteriaQuery.select(planeCriteriaQuery.from(Plane.class));

            return session.createQuery(planeCriteriaQuery).getResultList();
        }
    }

    @Override
    public Plane findOne(Integer id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Plane.class, id);
        }
    }

    @Override
    public List<Plane> findLimitOffset(Integer limit, Integer offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Plane> planeCriteriaQuery = cb.createQuery(Plane.class);
            planeCriteriaQuery.select(planeCriteriaQuery.from(Plane.class));

            return session.createQuery(planeCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Plane> saveAll(List<Plane> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Plane> resultOfSavingPlaneEntitiesList = entities.stream().map(e->e=(Plane) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPlaneEntitiesList;
        }
    }

    @Override
    public Plane saveOne(Plane entity) {
        try(Session session = sessionFactory.openSession()){
            return (Plane) session.save(entity);
        }
    }

    @Override
    public Plane updateOne(Plane entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
