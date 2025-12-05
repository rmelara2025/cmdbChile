package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.TotalRecurrentesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalRecurrentesRepository extends JpaRepository<TotalRecurrentesView, Long> {
}
