package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
