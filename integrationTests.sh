#!/bin/bash

# Base URL for the API
BASE_URL="http://localhost:8080/api"

# --- Register a new user ---
echo "Registering user..."
curl -X POST "$BASE_URL/users/register" \
-H "Content-Type: application/json" \
-d '{
  "username": "user1",
  "password": "pass123"
}'
echo -e "\n"

# --- Login and save auth token ---
echo "Logging in..."
RESPONSE=$(curl -s -X POST "$BASE_URL/users/login" \
-H "Content-Type: application/json" \
-d '{
  "username": "user1",
  "password": "pass123"
}')

TOKEN=$(echo $RESPONSE | jq -r '.token')
USER_ID=$(echo $RESPONSE | jq -r '.id')

echo "$RESPONSE"
echo "Token: $TOKEN"
echo "User ID: $USER_ID"
echo -e "\n"

sleep 1

# --- Create Media 1 ---
echo "Creating media 1..."
RESPONSE=$(curl -s -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "Inception",
  "description": "Sci-fi thriller",
  "mediaType": "movie",
  "releaseYear": 2010,
  "genres": ["SCIENCE_FICTION", "DRAMA"],
  "ageRestriction": 12
}')
MEDIA_ID_1=$(echo $RESPONSE | jq -r '.id')
echo "Media 1 ID: $MEDIA_ID_1"
echo -e "\n"

# --- Create Media 2 ---
echo "Creating media 2..."
RESPONSE=$(curl -s -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "The Matrix",
  "description": "Sci-fi action",
  "mediaType": "movie",
  "releaseYear": 1999,
  "genres": ["SCIENCE_FICTION", "ACTION_AND_ADVENTURE"],
  "ageRestriction": 16
}')
MEDIA_ID_2=$(echo $RESPONSE | jq -r '.id')
echo "Media 2 ID: $MEDIA_ID_2"
echo -e "\n"

# --- Create Media 3 ---
echo "Creating media 3..."
RESPONSE=$(curl -s -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "The Lord of the Rings",
  "description": "Fantasy epic",
  "mediaType": "movie",
  "releaseYear": 2001,
  "genres": ["ACTION_AND_ADVENTURE"],
  "ageRestriction": 12
}')
MEDIA_ID_3=$(echo $RESPONSE | jq -r '.id')
echo "Media 3 ID: $MEDIA_ID_3"
echo -e "\n"

# --- List All Media with filters ---
echo "Listing all media with filters..."
curl -X GET "$BASE_URL/media?title=incep&genre=SCIENCE_FICTION&sortBy=score" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Get one media item ---
echo "Getting media item with ID $MEDIA_ID_1..."
curl -X GET "$BASE_URL/media/$MEDIA_ID_1" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Update a media item ---
echo "Updating media item with ID $MEDIA_ID_1..."
curl -X PUT "$BASE_URL/media/$MEDIA_ID_1" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "Inception Updated",
  "description": "Updated description",
  "mediaType": "movie",
  "releaseYear": 2010,
  "genres": ["SCIENCE_FICTION", "ACTION_AND_ADVENTURE"],
  "ageRestriction": 16
}'
echo -e "\n"

# --- Delete a media item ---
echo "Deleting media item with ID $MEDIA_ID_1..."
curl -X DELETE "$BASE_URL/media/$MEDIA_ID_1" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Check media list after delete ---
echo "Listing media to confirm deletion..."
curl -X GET "$BASE_URL/media?title=incep&genre=SCIENCE_FICTION&sortBy=score" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

sleep 1

# --- User Profile Tests ---
echo "Getting user profile..."
curl -X GET "$BASE_URL/users/$USER_ID/profile" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

echo "Updating user profile..."
curl -X PUT "$BASE_URL/users/$USER_ID/profile" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "username": "user1",
  "email": "user1@example.com",
  "favoriteGenre": "SCIENCE_FICTION"
}'
echo -e "\n"

echo "Getting user profile after update..."
curl -X GET "$BASE_URL/users/$USER_ID/profile" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

sleep 1

# --- Favorites Tests ---
echo "Adding media 2 (ID: $MEDIA_ID_2) to favorites..."
curl -X POST "$BASE_URL/media/$MEDIA_ID_2/favorite" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{}'
echo -e "\n"

echo "Listing user favorites..."
curl -X GET "$BASE_URL/users/$USER_ID/favorites" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

echo "Removing media 2 from favorites..."
curl -X DELETE "$BASE_URL/media/$MEDIA_ID_2/favorite" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

sleep 1

# --- Ratings Tests ---
echo "Rating media 2 (ID: $MEDIA_ID_2)..."
curl -X POST "$BASE_URL/media/$MEDIA_ID_2/rate" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "stars": 5,
  "comment": "Awesome movie!"
}'
echo -e "\n"

echo "Getting user ratings to find ID..."
RESPONSE=$(curl -s -X GET "$BASE_URL/users/$USER_ID/ratings" \
-H "Authorization: Bearer $TOKEN")

echo "$RESPONSE"
# Extract the ID of the last rating
RATING_ID=$(echo $RESPONSE | jq -r '.[-1].id')
echo "Rating ID: $RATING_ID"
echo -e "\n"

if [ "$RATING_ID" != "null" ]; then
  echo "Liking rating $RATING_ID..."
  curl -X POST "$BASE_URL/ratings/$RATING_ID/like" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{}'
  echo -e "\n"

  echo "Confirming rating $RATING_ID..."
  curl -X POST "$BASE_URL/ratings/$RATING_ID/confirm" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{}'
  echo -e "\n"

  echo "Updating rating $RATING_ID..."
  curl -X PUT "$BASE_URL/ratings/$RATING_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "stars": 4,
    "comment": "Still good, but reconsidered."
  }'
  echo -e "\n"

  echo "Deleting rating $RATING_ID..."
  curl -X DELETE "$BASE_URL/ratings/$RATING_ID" \
  -H "Authorization: Bearer $TOKEN"
  echo -e "\n"
else
  echo "Failed to get Rating ID. Skipping dependent tests."
fi

# --- Recommendations Test ---
echo "Getting recommendations..."
curl -X GET "$BASE_URL/users/$USER_ID/recommendations" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"