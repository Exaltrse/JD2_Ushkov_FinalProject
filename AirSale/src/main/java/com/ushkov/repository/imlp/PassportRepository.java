package com.ushkov.repository.imlp;

import com.ushkov.domain.Passport;
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
public class PassportRepository implements CrudOperations<Long, Passport> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Passport> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Passport> passportCriteriaQuery = cb.createQuery(Passport.class);
            passportCriteriaQuery.select(passportCriteriaQuery.from(Passport.class));

            return session.createQuery(passportCriteriaQuery).getResultList();
        }
    }

    @Override
    public Passport findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Passport.class, id);
        }
    }

    @Override
    public List<Passport> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Passport> passportCriteriaQuery = cb.createQuery(Passport.class);
            passportCriteriaQuery.select(passportCriteriaQuery.from(Passport.class));

            return session.createQuery(passportCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Passport> saveAll(List<Passport> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Passport> resultOfSavingPassportEntitiesList = entities.stream().map(e->e=findOne((Long) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingPassportEntitiesList;
        }
    }

    @Override
    public Passport saveOne(Passport entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Long) session.save(entity));
        }
    }

    @Override
    public Passport updateOne(Passport entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
