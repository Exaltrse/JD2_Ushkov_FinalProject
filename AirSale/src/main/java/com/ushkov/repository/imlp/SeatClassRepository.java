package com.ushkov.repository.imlp;

import com.ushkov.domain.SeatClass;
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
public class SeatClassRepository implements CrudOperations<Short, SeatClass> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<SeatClass> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SeatClass> seatClassCriteriaQuery = cb.createQuery(SeatClass.class);
            seatClassCriteriaQuery.select(seatClassCriteriaQuery.from(SeatClass.class));

            return session.createQuery(seatClassCriteriaQuery).getResultList();
        }
    }

    @Override
    public SeatClass findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(SeatClass.class, id);
        }
    }

    @Override
    public List<SeatClass> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SeatClass> seatClassCriteriaQuery = cb.createQuery(SeatClass.class);
            seatClassCriteriaQuery.select(seatClassCriteriaQuery.from(SeatClass.class));

            return session.createQuery(seatClassCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<SeatClass> saveAll(List<SeatClass> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<SeatClass> resultOfSavingSeatClassEntitiesList = entities.stream().map(e->e=(SeatClass) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingSeatClassEntitiesList;
        }
    }

    @Override
    public SeatClass saveOne(SeatClass entity) {
        try(Session session = sessionFactory.openSession()){
            return (SeatClass) session.save(entity);
        }
    }

    @Override
    public SeatClass updateOne(SeatClass entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
