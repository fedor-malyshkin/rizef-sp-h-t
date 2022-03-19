package com.rize.test.service;

import com.rize.test.model.storage.ArtistEntity;
import com.rize.test.respository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    @Value("${app.artist.query.limit}")
    private int artistQueryLimit;

    private final ArtistRepository artistRepository;

    public Optional<ArtistEntity> get(Integer id) {
        return artistRepository.findById(id);
    }

    public ArtistEntity save(ArtistEntity artistEntity) {
        return artistRepository.save(artistEntity);
    }

    public boolean delete(Integer id) {
        if (!artistRepository.existsById(id))
            return false;
        artistRepository.deleteById(id);
        return true;
    }

    public Pair<List<ArtistEntity>, Boolean> index(Integer categoryId, Integer birthdayMonth, String searchTexts) {
        var result = artistRepository.advancedSearch(categoryId,
                birthdayMonth,
                searchTexts, artistQueryLimit);
        return Pair.of(result, result.size() >= artistQueryLimit);
    }
}
