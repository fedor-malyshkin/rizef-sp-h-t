package com.rize.test.respository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.rize.test.model.storage.ArtistEntity;
import com.rize.test.model.storage.QArtistEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomArtistRepositoryImpl implements CustomArtistRepository {

    private final EntityManager entityManager;

    @Override
    public List<ArtistEntity> advancedSearch(Integer categoryId, Integer birthdayMonth, String searchTexts, int artistQueryLimit) {
        QArtistEntity artist = QArtistEntity.artistEntity;
        var query = new JPAQuery<ArtistEntity>(entityManager);
        final Optional<Predicate> expression = composePredicate(artist,
                categoryId,
                birthdayMonth,
                searchTexts);
        JPAQuery<ArtistEntity> from = query.from(artist);
        if (expression.isPresent())
            from = from.where(expression.get());
        return from
                .limit(artistQueryLimit)
                .fetch();
    }

    private Optional<Predicate> composePredicate(QArtistEntity artist,
                                                 Integer categoryId,
                                                 Integer birthdayMonth,
                                                 String searchTexts) {

        List<BooleanExpression> expressions = new LinkedList<>();
        if (Objects.nonNull(categoryId)) {
            expressions.add(artist.categoryId.eq(categoryId));
        }
        if (Objects.nonNull(birthdayMonth)) {
            expressions.add(artist.birthday.month().eq(birthdayMonth));
        }
        if (Objects.nonNull(searchTexts) && StringUtils.isNotEmpty(searchTexts)) {
            var subStrExp = String.format("%%%s%%", searchTexts.trim());
            expressions.add(artist.firstName.likeIgnoreCase(subStrExp)
                    .or(artist.lastName.likeIgnoreCase(subStrExp)));
        }

        return joinByAnd(expressions);
    }

    private Optional<Predicate> joinByAnd(List<BooleanExpression> expressions) {
        if (expressions == null || expressions.size() == 0) return Optional.empty();
        final BooleanExpression head = expressions.iterator().next();

        return Optional.of(expressions.stream()
                .reduce(head, BooleanExpression::and));
    }
}
