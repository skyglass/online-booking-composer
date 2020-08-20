package com.manning.salonapp.salonservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonServiceDetailRepository extends JpaRepository<SalonServiceDetail,Long> {

    List<SalonServiceDetail> findAll();
}
