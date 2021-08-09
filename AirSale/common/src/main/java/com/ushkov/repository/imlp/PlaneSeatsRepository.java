package com.ushkov.repository.imlp;

import com.ushkov.domain.PlaneSeats;
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
public class PlaneSeatsRepository implements CrudOperations<Integer, PlaneSeats> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<PlaneSeats> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PlaneSeats> planeSeatsCriteriaQuery = cb.createQuery(PlaneSeats.class);
            planeSeatsCriteriaQuery.select(planeSeatsCriteriaQuery.from(PlaneSeats.class));

            return session.createQuery(planeSeatsCriteriaQuery).getResultList();
        }
    }

    @Override
    public PlaneSeats findOne(Integer id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(PlaneSeats.class, id);
        }
    }

    @Override
    public List<PlaneSeats> findLimitOffset(Integer limit, Integer offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PlaneSeats> planeSeatsCriteriaQuery = cb.createQuery(PlaneSeats.class);
            planeSeatsCriteriaQuery.select(planeSeatsCriteriaQuery.from(PlaneSeats.class));

            return session.createQuery(planeSeatsCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<PlaneSeats> saveAll(List<PlaneSeats> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<PlaneSeats> resultOfSavingPlaneSeatsEntitiesList = entities.stream().map(e->e=findOne((Integer) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPlaneSeatsEntitiesList;
        }
    }

    @Override
    public PlaneSeats saveOne(PlaneSeats entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Integer) session.save(entity));
        }
    }

    @Override
    public PlaneSeats updateOne(PlaneSeats entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
