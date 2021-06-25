package com.ushkov.repository.imlp;

import com.ushkov.domain.Country;
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
public class CountryRepository implements CrudOperations<Short, Country> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Country> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Country> countryCriteriaQuery = cb.createQuery(Country.class);
            countryCriteriaQuery.select(countryCriteriaQuery.from(Country.class));

            return session.createQuery(countryCriteriaQuery).getResultList();
        }
    }

    @Override
    public Country findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Country.class, id);
        }
    }

    @Override
    public List<Country> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Country> countryCriteriaQuery = cb.createQuery(Country.class);
            countryCriteriaQuery.select(countryCriteriaQuery.from(Country.class));

            return session.createQuery(countryCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Country> saveAll(List<Country> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Country> resultOfSavingCountryEntitiesList = entities.stream().map(e->e=(Country) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingCountryEntitiesList;
        }
    }

    @Override
    public Country saveOne(Country entity) {
        try(Session session = sessionFactory.openSession()){
            return (Country) session.save(entity);
        }
    }

    @Override
    public Country updateOne(Country entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
