package com.rize.test.model.mapper;


import com.rize.test.model.rest.AbstractArtistReqRes;
import com.rize.test.model.rest.ArtistRes;
import com.rize.test.model.rest.IndexArtistRes;
import com.rize.test.model.rest.SaveArtistReq;
import com.rize.test.model.storage.ArtistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", imports = {Collections.class})
public abstract class ArtistMapper {
    protected int fromCategoriesEnum(AbstractArtistReqRes.CategoriesEnum category) {
        return category.getIndex();
    }

    protected AbstractArtistReqRes.CategoriesEnum fromIndex(int index) {
        switch (index) {
            case 0:
                return AbstractArtistReqRes.CategoriesEnum.ACTOR;
            case 1:
                return AbstractArtistReqRes.CategoriesEnum.PAINTER;
            case 2:
                return AbstractArtistReqRes.CategoriesEnum.SCULPTOR;
            default:
                throw new IllegalArgumentException("Unknown category Id");
        }
    }

    @Mapping(target = "categoryId", source = "category")
    public abstract ArtistEntity fromSaveReqToEntity(SaveArtistReq req);

    @Mapping(target = "category", source = "categoryId")
    public abstract ArtistRes fromEntityToRes(ArtistEntity entity);

    public abstract List<ArtistRes> fromEntityToRes(List<ArtistEntity> entities);

    public IndexArtistRes fromEntitiesToIndexRes(List<ArtistEntity> artists, Boolean hasMore) {
        return new IndexArtistRes(fromEntityToRes(artists), hasMore);
    }
}
