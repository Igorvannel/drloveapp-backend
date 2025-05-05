package com.drloveapp.drloveapp_backend.repository;


import java.util.List;

import com.drloveapp.drloveapp_backend.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {

    List<Tip> findAllByOrderByCreatedAtDesc();

}