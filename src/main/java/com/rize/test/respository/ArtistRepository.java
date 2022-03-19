package com.rize.test.respository;

import com.rize.test.model.storage.ArtistEntity;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<ArtistEntity, Integer>,
        QuerydslPredicateExecutor<ArtistEntity>,
        CustomArtistRepository {


}
