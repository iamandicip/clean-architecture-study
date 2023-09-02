#!/bin/bash
BASE_URL='http://localhost:8081/exchange-rate'

# function to test the header of the request against expected value
test_endpoint_get_status() {
	echo 'command        :' $1 $2
	echo 'expected result:' $3
  result="$(curl --request $1 --head --silent --location $BASE_URL/$2 | awk '/^HTTP/{print $2}')"
	echo 'actual result  :' $result
	test_result $3 $result
	echo '-----------------------------------'
}

# function to test the body of the request against expected value
test_endpoint_get_body() {
	echo 'command        :' $1 $2
	echo 'expected result:' $3
  result="$(curl --request $1 --silent --location $BASE_URL/$2)"
	echo 'actual result  :' $result
	test_result $3 $result
	echo '-----------------------------------'
}

# function to test if 2 values are equal
test_result() {
	if [[ $1 == $2 ]]; then
  echo 'test outcome   : OK'
	else
	echo 'test outcome   : NOK'
	fi
}

# test suite
test_endpoint_get_status 'GET' 'RON/EUR' '404'
test_endpoint_get_status 'PUT' 'RON/EUR/4.92' '201'
test_endpoint_get_status 'PUT' 'RON/EUR/4.95' '200'
test_endpoint_get_status 'DELETE' 'RON/EUR' '204'
test_endpoint_get_status 'DELETE' 'RON/EUR' '404'
test_endpoint_get_status 'POST' 'RON/EUR/4.92' '201'
test_endpoint_get_status 'POST' 'RON/EUR/4.96' '200'
test_endpoint_get_body   'GET' 'RON/EUR' '{"from":"RON","to":"EUR","rate":4.96}'
test_endpoint_get_status 'DELETE' 'RON/EUR' '204'
