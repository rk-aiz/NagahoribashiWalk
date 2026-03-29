-- H2用テストスキーマ (SERIALをBIGINT AUTO_INCREMENTに変換、PostgreSQL固有のFUNCTION/TRIGGERは除外)

DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS spot_photos;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS spots;
DROP TABLE IF EXISTS sub_categories;
DROP TABLE IF EXISTS categories;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    display_order INTEGER NOT NULL
);

CREATE TABLE sub_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_sub_categories_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_sub_categories_category_name
        UNIQUE (category_id, name)
);

CREATE TABLE spots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
        ON DELETE SET NULL
);

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    spot_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_spot FOREIGN KEY (spot_id) REFERENCES spots(id) ON DELETE CASCADE,
    CONSTRAINT uq_reviews_user_spot UNIQUE (user_id, spot_id)
);

CREATE TABLE favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    spot_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_spot FOREIGN KEY (spot_id) REFERENCES spots(id) ON DELETE CASCADE,
    CONSTRAINT uq_favorites_user_spot UNIQUE (user_id, spot_id)
);

CREATE TABLE spot_photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spot_id INTEGER NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    display_order INTEGER NOT NULL,
    CONSTRAINT fk_spot_photos_spot FOREIGN KEY (spot_id) REFERENCES spots(id) ON DELETE CASCADE,
    CONSTRAINT uq_spot_photos_spot_order UNIQUE (spot_id, display_order)
);
