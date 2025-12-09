package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.Card;
import com.scavengerhunt.hunt.model.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByType(CardType type);
}