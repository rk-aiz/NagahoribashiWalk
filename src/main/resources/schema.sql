DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS spot_photos;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS spots;
DROP TABLE IF EXISTS sub_categories;
DROP TABLE IF EXISTS categories;

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS '
	BEGIN
	NEW.updated_at = CURRENT_TIMESTAMP;
	RETURN NEW;
	END;
' language 'plpgsql';

CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	role VARCHAR(20) NOT NULL DEFAULT 'USER',
	display_name VARCHAR(50) NOT NULL,
	deleted_at TIMESTAMP,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE categories(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) UNIQUE NOT NULL,
	display_order INTEGER NOT NULL,
	is_default BOOLEAN DEFAULT FALSE
);

-- categoriesでデフォルトを1件に制限
CREATE UNIQUE INDEX uq_categories_default
ON categories (is_default)
WHERE is_default = TRUE;

CREATE TABLE sub_categories (

	id SERIAL PRIMARY KEY,
	
	category_id INTEGER NOT NULL,
	
	name VARCHAR(100) NOT NULL,
	
	is_default BOOLEAN DEFAULT FALSE,
	
	CONSTRAINT fk_sub_categories_category
		FOREIGN KEY (category_id) REFERENCES categories(id)
		ON DELETE RESTRICT,
	CONSTRAINT uq_sub_categories_category_name
		UNIQUE (category_id, name)
);

-- sub_categoriesでデフォルトを1件に制限
CREATE UNIQUE INDEX uq_sub_categories_default
ON sub_categories (category_id, is_default)
WHERE is_default = TRUE;

CREATE TABLE spots(
	id SERIAL PRIMARY KEY,
	spot_name VARCHAR(100) NOT NULL,
	sub_category_id INTEGER,
	website_url VARCHAR(255),
	gmap_url VARCHAR(500),
	address VARCHAR(255),
	business_hours VARCHAR(255),
	closed_days VARCHAR(255),
	estimated_budget VARCHAR(255),
	keywords VARCHAR(255),
	details TEXT,
	deleted_at TIMESTAMP,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_spots_sub_category
		FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
		ON DELETE RESTRICT
);

CREATE TABLE reviews (
	id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	spot_id INTEGER NOT NULL,
	rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
	comment TEXT,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_reviews_user
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE,
	CONSTRAINT fk_reviews_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE,
	CONSTRAINT uq_reviews_user_spot
		UNIQUE (user_id, spot_id)
);

CREATE TABLE favorites (
	id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	spot_id INTEGER NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_favorites_user
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE,
	CONSTRAINT fk_favorites_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE,
	CONSTRAINT uq_favorites_user_spot
		UNIQUE (user_id, spot_id)
);


CREATE TABLE spot_photos (
	id SERIAL PRIMARY KEY,
	spot_id INTEGER NOT NULL,
	photo_url VARCHAR(255) NOT NULL,
	display_order INTEGER NOT NULL,
	CONSTRAINT fk_spot_photos_spot
		FOREIGN KEY (spot_id) REFERENCES spots(id)
		ON DELETE CASCADE,
	CONSTRAINT uq_spot_photos_spot_order
		UNIQUE (spot_id, display_order)
);


CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_spots_updated_at
BEFORE UPDATE ON spots
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_reviews_updated_at
BEFORE UPDATE ON reviews
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();