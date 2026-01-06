CREATE TABLE "user"
(
    "id"            SERIAL       NOT NULL,
    "username"      VARCHAR(255) NOT NULL,
    "password"      VARCHAR(255) NOT NULL,
    "email"         VARCHAR(255),
    "favoriteGenre" BIGINT,
    PRIMARY KEY ("id")
);

CREATE TABLE "media"
(
    "id"             SERIAL       NOT NULL,
    "title"          VARCHAR(255) NOT NULL,
    "description"    VARCHAR(255) NOT NULL,
    "mediaType"      BIGINT       NOT NULL,
    "releaseYear"    INTEGER      NOT NULL,
    "genres"         BIGINT[]     NOT NULL,
    "ageRestriction" BIGINT       NOT NULL,
    "creator"        BIGINT       NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "media_creator_foreign" FOREIGN KEY ("creator") REFERENCES "user" ("id")
);

CREATE TABLE "rating"
(
    "id"        SERIAL                         NOT NULL,
    "creator"   BIGINT                         NOT NULL,
    "media"     BIGINT                         NOT NULL,
    "rating"    INTEGER                        NOT NULL,
    "comment"   VARCHAR(255)                   NOT NULL,
    "timestamp" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "confirmed" BOOLEAN                        NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "rating_media_foreign" FOREIGN KEY ("media") REFERENCES "media" ("id"),
    CONSTRAINT "rating_creator_foreign" FOREIGN KEY ("creator") REFERENCES "user" ("id"),
    CONSTRAINT rating_unique_user_media UNIQUE ("creator", "media")
);

CREATE TABLE "favorite"
(
    "id"    SERIAL NOT NULL,
    "user"  BIGINT NOT NULL,
    "media" BIGINT NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "favorite_user_foreign" FOREIGN KEY ("user") REFERENCES "user" ("id"),
    CONSTRAINT "favorite_media_foreign" FOREIGN KEY ("media") REFERENCES "media" ("id"),
    CONSTRAINT favorite_unique_user_media UNIQUE ("user", "media")
);

CREATE TABLE "like"
(
    "id"     SERIAL NOT NULL,
    "user"   BIGINT NOT NULL,
    "rating" BIGINT NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "like_user_foreign" FOREIGN KEY ("user") REFERENCES "user" ("id"),
    CONSTRAINT "like_rating_foreign" FOREIGN KEY ("rating") REFERENCES "rating" ("id"),
    CONSTRAINT like_unique_user_rating UNIQUE ("user", "rating")
);