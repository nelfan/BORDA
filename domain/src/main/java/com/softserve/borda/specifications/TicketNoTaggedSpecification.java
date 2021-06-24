package com.softserve.borda.specifications;

import com.softserve.borda.entities.Ticket;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TicketNoTaggedSpecification implements Specification<Ticket>{
    @Override
    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isEmpty(root.get("tags"));
    }
}
