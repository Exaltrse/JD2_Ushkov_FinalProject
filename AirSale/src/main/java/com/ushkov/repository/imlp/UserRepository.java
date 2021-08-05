package com.ushkov.repository.imlp;

import com.ushkov.domain.Users;
import com.ushkov.domain.Users_;
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
public class UserRepository implements CrudOperations<Integer, Users> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Users> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Users> userCriteriaQuery = cb.createQuery(Users.class);
            userCriteriaQuery.select(userCriteriaQuery.from(Users.class));

            return session.createQuery(userCriteriaQuery).getResultList();
        }
    }

    @Override
    public Users findOne(Integer id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Users.class, id);
        }
    }

    @Override
    public List<Users> findLimitOffset(Integer limit, Integer offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Users> userCriteriaQuery = cb.createQuery(Users.class);
            userCriteriaQuery.select(userCriteriaQuery.from(Users.class));

            return session.createQuery(userCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    public Users findByLogin(String login){
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Users> userCriteriaQuery = cb.createQuery(Users.class);
            Root<Users> userRoot = userCriteriaQuery.from(Users.class);
            ParameterExpression<String> type = cb.parameter(String.class);
            Expression<String> param = userRoot.get(Users_.login);
            userCriteriaQuery.select(userRoot).where(cb.like(param,type));
            return session.createQuery(userCriteriaQuery).getSingleResult();
        }
    }

    @Override
    public List<Users> saveAll(List<Users> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Users> resultOfSavingUserEntitiesList = entities.stream().map(e->e=findOne((Integer) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingUserEntitiesList;
        }
    }

    @Override
    public Users saveOne(Users entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Integer) session.save(entity));
        }
    }

    @Override
    public Users updateOne(Users entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }


}
