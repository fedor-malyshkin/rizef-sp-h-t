CREATE INDEX artists_category_ndx ON artists (category_id);
CREATE INDEX artists_birthday_month_ndx ON artists (birthday);
CREATE INDEX first_name_trgm_idx ON artists (first_name);
CREATE INDEX last_name_trgm_idx ON artists (last_name);