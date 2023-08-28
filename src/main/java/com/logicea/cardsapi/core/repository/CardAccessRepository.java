package com.logicea.cardsapi.core.repository;

import com.logicea.cardsapi.core.entity.CardAccess;
import com.logicea.cardsapi.core.entity.CardAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface CardAccessRepository extends JpaRepository<CardAccess, CardAccessId> {
}
