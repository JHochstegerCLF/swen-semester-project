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

echo "$RESPONSE"
echo "$TOKEN"
echo -e "\n"

sleep 1

# --- Create Media 1 ---
echo "Creating media 1..."
curl -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "Inception",
  "description": "Sci-fi thriller",
  "mediaType": "movie",
  "releaseYear": 2010,
  "genres": ["sci-fi", "thriller"],
  "ageRestriction": 12
}'
echo -e "\n"

# --- Create Media 2 ---
echo "Creating media 2..."
curl -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "The Matrix",
  "description": "Sci-fi action",
  "mediaType": "movie",
  "releaseYear": 1999,
  "genres": ["sci-fi", "action"],
  "ageRestriction": 16
}'
echo -e "\n"

# --- Create Media 3 ---
echo "Creating media 3..."
curl -X POST "$BASE_URL/media" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "The Lord of the Rings",
  "description": "Fantasy epic",
  "mediaType": "movie",
  "releaseYear": 2001,
  "genres": ["fantasy", "adventure"],
  "ageRestriction": 12
}'
echo -e "\n"

# --- List All Media with filters ---
echo "Listing all media with filters..."
curl -X GET "$BASE_URL/media?title=incep&genre=sci-fi&sortBy=score" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Get one media item ---
echo "Getting media item with ID 1..."
curl -X GET "$BASE_URL/media/1" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Update a media item ---
echo "Updating media item with ID 1..."
curl -X PUT "$BASE_URL/media/1" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "title": "Inception Updated",
  "description": "Updated description",
  "mediaType": "movie",
  "releaseYear": 2010,
  "genres": ["sci-fi", "action"],
  "ageRestriction": 16
}'
echo -e "\n"

# --- Delete a media item ---
echo "Deleting media item with ID 1..."
curl -X DELETE "$BASE_URL/media/1" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"

# --- Check media list after delete ---
echo "Listing media to confirm deletion..."
curl -X GET "$BASE_URL/media?title=incep&genre=sci-fi&sortBy=score" \
-H "Authorization: Bearer $TOKEN"
echo -e "\n"