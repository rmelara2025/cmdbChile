package com.telefonicatech.cmdbChile.repository;

import com.telefonicatech.cmdbChile.model.DashboardRecurrentesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRecurrenteRepository extends JpaRepository<DashboardRecurrentesView, Long> {
}
