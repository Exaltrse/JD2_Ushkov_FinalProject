package com.ushkov.repository.imlp;

import com.ushkov.domain.Role;
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
public class RoleRepository implements CrudOperations<Short, Role> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Role> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Role> roleCriteriaQuery = cb.createQuery(Role.class);
            roleCriteriaQuery.select(roleCriteriaQuery.from(Role.class));

            return session.createQuery(roleCriteriaQuery).getResultList();
        }
    }

    @Override
    public Role findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Role.class, id);
        }
    }

    @Override
    public List<Role> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Role> roleCriteriaQuery = cb.createQuery(Role.class);
            roleCriteriaQuery.select(roleCriteriaQuery.from(Role.class));

            return session.createQuery(roleCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }


    @Override
    public List<Role> saveAll(List<Role> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Role> resultOfSavingRoleEntitiesList = entities.stream().map(e->e=findOne((Short) session.save(e))).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingRoleEntitiesList;
        }
    }

    @Override
    public Role saveOne(Role entity) {
        try(Session session = sessionFactory.openSession()){
            return findOne((Short) session.save(entity));
        }
    }

    @Override
    public Role updateOne(Role entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
