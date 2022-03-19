package com.rize.test.respository;

import com.rize.test.model.storage.ArtistEntity;

import java.util.List;

public interface CustomArtistRepository {
    public List<ArtistEntity> advancedSearch(Integer categoryId, Integer birthdayMonth, String searchTexts, int artistQueryLimit);
}
