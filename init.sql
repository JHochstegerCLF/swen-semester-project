CREATE TABLE "media"(
                        "id" SERIAL NOT NULL,
                        "title" VARCHAR(255) NOT NULL,
                        "mediaType" BIGINT NOT NULL,
                        "releaseYear" BIGINT NOT NULL
);
ALTER TABLE
    "media" ADD PRIMARY KEY("id");

CREATE TABLE "user"(
                       "id" SERIAL NOT NULL,
                       "username" VARCHAR(255) NOT NULL,
                       "password" VARCHAR(255) NOT NULL,
                       "email" VARCHAR(255) NOT NULL,
                       "favoriteGenre" BIGINT NOT NULL
);
ALTER TABLE
    "user" ADD PRIMARY KEY("id");

CREATE TABLE "rating"(
                         "id" SERIAL NOT NULL,
                         "creator" BIGINT NOT NULL,
                         "media" BIGINT NOT NULL,
                         "rating" INTEGER NOT NULL,
                         "comment" VARCHAR(255) NOT NULL,
                         "timestamp" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);
ALTER TABLE
    "rating" ADD PRIMARY KEY("id");

CREATE TABLE "genre"(
                        "id" SERIAL NOT NULL,
                        "name" BIGINT NOT NULL
);
ALTER TABLE
    "genre" ADD PRIMARY KEY("id");
CREATE TABLE "media_genre"(
                              "id" SERIAL NOT NULL,
                              "genreId" BIGINT NOT NULL,
                              "mediaId" BIGINT NOT NULL
);
ALTER TABLE
    "media_genre" ADD PRIMARY KEY("id");
ALTER TABLE
    "media_genre" ADD CONSTRAINT "media_genre_mediaid_foreign" FOREIGN KEY("mediaId") REFERENCES "media"("id");
ALTER TABLE
    "user" ADD CONSTRAINT "user_favoritegenre_foreign" FOREIGN KEY("favoriteGenre") REFERENCES "genre"("id");
ALTER TABLE
    "rating" ADD CONSTRAINT "rating_media_foreign" FOREIGN KEY("media") REFERENCES "media"("id");
ALTER TABLE
    "rating" ADD CONSTRAINT "rating_creator_foreign" FOREIGN KEY("creator") REFERENCES "user"("id");
ALTER TABLE
    "media_genre" ADD CONSTRAINT "media_genre_genreid_foreign" FOREIGN KEY("genreId") REFERENCES "genre"("id");