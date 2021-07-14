package com.ushkov.repository.imlp;

import com.ushkov.domain.Discount;
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
public class DiscountRepository implements CrudOperations<Short, Discount> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Discount> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Discount> discountCriteriaQuery = cb.createQuery(Discount.class);
            discountCriteriaQuery.select(discountCriteriaQuery.from(Discount.class));

            return session.createQuery(discountCriteriaQuery).getResultList();
        }
    }

    @Override
    public Discount findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Discount.class, id);
        }
    }

    @Override
    public List<Discount> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Discount> discountCriteriaQuery = cb.createQuery(Discount.class);
            discountCriteriaQuery.select(discountCriteriaQuery.from(Discount.class));

            return session.createQuery(discountCriteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
        }
    }

    @Override
    public List<Discount> saveAll(List<Discount> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Discount> resultOfSavingDiscountEntitiesList = entities.stream().map(e->e=findOne((Short) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingDiscountEntitiesList;
        }
    }

    @Override
    public Discount saveOne(Discount entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Short) session.save(entity));
        }
    }

    @Override
    public Discount updateOne(Discount entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
