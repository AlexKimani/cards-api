package com.logicea.cardsapi.core.repository;

import com.logicea.cardsapi.core.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
