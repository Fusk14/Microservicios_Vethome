package com.example.resenas.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resenas.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
}

