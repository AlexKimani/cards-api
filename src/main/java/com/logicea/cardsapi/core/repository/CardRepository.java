package com.logicea.cardsapi.core.repository;

import com.logicea.cardsapi.core.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByName(String name);
    Optional<Card> findCardById(long id);
}
