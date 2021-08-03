package com.ushkov.repository.imlp;

import com.ushkov.domain.User;
import com.ushkov.domain.User_;
import com.ushkov.repository.CrudOperations;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository implements CrudOperations<Integer, User> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<User> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery = cb.createQuery(User.class);
            userCriteriaQuery.select(userCriteriaQuery.from(User.class));

            return session.createQuery(userCriteriaQuery).getResultList();
        }
    }

    @Override
    public User findOne(Integer id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(User.class, id);
        }
    }

    @Override
    public List<User> findLimitOffset(Integer limit, Integer offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery = cb.createQuery(User.class);
            userCriteriaQuery.select(userCriteriaQuery.from(User.class));

            return session.createQuery(userCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    public User findByLogin(String login){
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery = cb.createQuery(User.class);
            Root<User> userRoot = userCriteriaQuery.from(User.class);
            ParameterExpression<String> type = cb.parameter(String.class);
            Expression<String> param = userRoot.get(User_.login);
            userCriteriaQuery.select(userRoot).where(cb.like(param,type));
            return session.createQuery(userCriteriaQuery).getSingleResult();
        }
    }

    @Override
    public List<User> saveAll(List<User> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<User> resultOfSavingUserEntitiesList = entities.stream().map(e->e=findOne((Integer) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingUserEntitiesList;
        }
    }

    @Override
    public User saveOne(User entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Integer) session.save(entity));
        }
    }

    @Override
    public User updateOne(User entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }


}
