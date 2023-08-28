#!/bin/bash
BASE_URL="http://localhost:8081/exchange-rate"

test_endpoint_put() {
	curl -X PUT $BASE_URL/$1
}

test_endpoint_post() {
	curl -X POST $BASE_URL/$1
}

test_endpoint_get() {
	curl -X GET $BASE_URL/$1
}

test_endpoint_delete() {
	curl -X DELETE $BASE_URL/$1
}

test_endpoint_put "RON/EUR/4.92" # 201
test_endpoint_put "RON/EUR/4.92" # 200
test_endpoint_delete "RON/EUR"   # 204
test_endpoint_delete "RON/EUR"   # 404
test_endpoint_post "RON/EUR/4.9" # 201
test_endpoint_post "RON/EUR/4.9" # 200
test_endpoint_get "RON/EUR"      # 200
