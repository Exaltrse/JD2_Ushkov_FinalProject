package com.ushkov.repository.imlp;

import com.ushkov.domain.TicketStatus;
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
public class TicketStatusRepository implements CrudOperations<Short, TicketStatus> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<TicketStatus> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TicketStatus> ticketStatusCriteriaQuery = cb.createQuery(TicketStatus.class);
            ticketStatusCriteriaQuery.select(ticketStatusCriteriaQuery.from(TicketStatus.class));

            return session.createQuery(ticketStatusCriteriaQuery).getResultList();
        }
    }

    @Override
    public TicketStatus findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(TicketStatus.class, id);
        }
    }

    @Override
    public List<TicketStatus> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TicketStatus> ticketStatusCriteriaQuery = cb.createQuery(TicketStatus.class);
            ticketStatusCriteriaQuery.select(ticketStatusCriteriaQuery.from(TicketStatus.class));

            return session.createQuery(ticketStatusCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<TicketStatus> saveAll(List<TicketStatus> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<TicketStatus> resultOfSavingTicketStatusEntitiesList = entities.stream().map(e->e=(TicketStatus) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingTicketStatusEntitiesList;
        }
    }

    @Override
    public TicketStatus saveOne(TicketStatus entity) {
        try(Session session = sessionFactory.openSession()){
            return (TicketStatus) session.save(entity);
        }
    }

    @Override
    public TicketStatus updateOne(TicketStatus entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
