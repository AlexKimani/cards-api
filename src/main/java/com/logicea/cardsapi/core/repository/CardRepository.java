package com.logicea.cardsapi.core.repository;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByName(String name);
    Optional<Card> findCardById(long id);
    Optional<Card> findCardByIdAndCreatedBy(long id, User user);

    Page<Card> findCardsByCreatedBy(User user, Pageable pageable);
}
