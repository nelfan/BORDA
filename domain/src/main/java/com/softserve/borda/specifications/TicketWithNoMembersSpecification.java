package com.softserve.borda.specifications;

import com.softserve.borda.entities.Ticket;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class TicketWithNoMembersSpecification implements Specification<Ticket> {

    @Override
    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isEmpty(root.get("members"));
    }
}
