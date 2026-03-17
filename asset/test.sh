#!/bin/bash

BASE_URL="http://localhost:8080/api/v1/friend"
MY_ID=1
TARGET_ID=2
R1=7777101
R2=7777102

echo "--- 1. Friend List (Pagination) ---"
curl -v -s -X GET "$BASE_URL?page=0&size=10&sort=approvedAt,desc" \
     -H "X-User-Id: $MY_ID" \
     -H "Accept: application/json" 2>&1 | grep -E '^(>|<|{)'
echo -e "\n"

echo "--- 2. Received Requests (Window) ---"
curl -v -s -X GET "$BASE_URL/requests?maxSize=20&window=90d" \
     -H "X-User-Id: $MY_ID" \
     -H "Accept: application/json" 2>&1 | grep -E '^(>|<|{)'
echo -e "\n"

echo "--- 3. Send Friend Request ---"
curl -v -s -X POST "$BASE_URL/request" \
     -H "X-User-Id: $MY_ID" \
     -H "Content-Type: application/json" \
     -d "{\"toAccountId\": $TARGET_ID}" 2>&1 | grep -E '^(>|<|{)'
echo -e "\n"

echo "--- 4. Accept Request ($R1) ---"
curl -v -s -X POST "$BASE_URL/accept/$R1" \
     -H "X-User-Id: 707" 2>&1 | grep -E '^(>|<|{)'
echo -e "\n"

echo "--- 5. Reject Request ($R2) ---"
curl -v -s -X POST "$BASE_URL/reject/$R2" \
     -H "X-User-Id: 707" 2>&1 | grep -E '^(>|<|{)'
echo -e "\n"