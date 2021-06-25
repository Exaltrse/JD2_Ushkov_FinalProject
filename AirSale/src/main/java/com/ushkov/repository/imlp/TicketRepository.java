package com.ushkov.repository.imlp;

import com.ushkov.domain.Ticket;
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
public class TicketRepository implements CrudOperations<Long, Ticket> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<Ticket> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> ticketCriteriaQuery = cb.createQuery(Ticket.class);
            ticketCriteriaQuery.select(ticketCriteriaQuery.from(Ticket.class));

            return session.createQuery(ticketCriteriaQuery).getResultList();
        }
    }

    @Override
    public Ticket findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(Ticket.class, id);
        }
    }

    @Override
    public List<Ticket> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> ticketCriteriaQuery = cb.createQuery(Ticket.class);
            ticketCriteriaQuery.select(ticketCriteriaQuery.from(Ticket.class));

            return session.createQuery(ticketCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Ticket> resultOfSavingTicketEntitiesList = entities.stream().map(e->e=(Ticket) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingTicketEntitiesList;
        }
    }

    @Override
    public Ticket saveOne(Ticket entity) {
        try(Session session = sessionFactory.openSession()){
            return (Ticket) session.save(entity);
        }
    }

    @Override
    public Ticket updateOne(Ticket entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
