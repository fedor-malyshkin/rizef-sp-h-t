CREATE  INDEX artists_category_ndx ON artists USING hash (category_id);
CREATE  INDEX artists_birthday_month_ndx ON artists USING hash (EXTRACT(MONTH FROM birthday));
CREATE INDEX first_name_trgm_idx ON artists USING GIN (first_name gin_trgm_ops);
CREATE INDEX last_name_trgm_idx ON artists USING GIN (last_name gin_trgm_ops);