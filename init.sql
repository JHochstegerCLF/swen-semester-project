CREATE TABLE "media"(
                        "id" SERIAL NOT NULL,
                        "title" VARCHAR(255) NOT NULL,
                        "description" VARCHAR(255) NOT NULL,
                        "mediaType" BIGINT NOT NULL,
                        "releaseYear" INTEGER NOT NULL,
                        "genres" BIGINT[] NOT NULL,
                        "ageRestriction" BIGINT NOT NULL,
                        "creator" BIGINT NOT NULL
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
ALTER TABLE
    "rating" ADD CONSTRAINT "rating_media_foreign" FOREIGN KEY("media") REFERENCES "media"("id");
ALTER TABLE
    "rating" ADD CONSTRAINT "rating_creator_foreign" FOREIGN KEY("creator") REFERENCES "user"("id");
ALTER TABLE
    "media" ADD CONSTRAINT "media_creator_foreign" FOREIGN KEY("creator") REFERENCES "user"("id");