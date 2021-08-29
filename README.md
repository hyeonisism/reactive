### Run to demo
```shell
$ git clone https://github.com/hyeonisism/reactive.git
$ cd reactive
$ ./gradlew bootRun --args='--spring.profiles.active=demo'

## Get all products
$ curl -i X GET http://localhost:8080/products

## Get product by id
$ curl -i X GET http://localhost:8080/product/1
$ curl -i X GET http://localhost:8080/products/1
```
