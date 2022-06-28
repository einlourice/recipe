## Getting Started

### Installation

```
./mvnw install
```

### Running unit test

```
./mvnw test
```

### Running integration test

```
./mvnw verify -Psurefire
```

### Running the application

```
./mvnw spring-boot:run
```

### API documentation

```
http://localhost:8080/swagger-ui/index.html
```

### Accessing H2 Database

```
JDBC URL: jdbc:h2:mem:recipeDB
username: sa
password:
http://localhost:8080/h2-console/
```

## CURL

### Create request

```
curl --location --request POST 'localhost:8080/api/recipe' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test2",
    "isVegetarian": false,
    "numberOfServings": 6,
    "instructionList": [
       {
           "step": "boil"
       },
       {
           "step": "fry"
       }
    ],
    "ingredientList": [
       {
           "name": "chicken"
       },
       {
           "name": "orange"
       }
    ]
}'
```

### Update request

```
curl --location --request PUT 'localhost:8080/api/recipe/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "isVegetarian": "true",
    "numberOfServings": 3,
    "ingredientList": [
        {
            "name": "allo"
        }
    ],
    "instructionList": [
        {
            "step": "Boiled the chicken"
        },
         {
            "step": "Fry the apple, and put ketchup"
        }
    ]
}'
```

### Get recipe

```
curl --location --request GET 'localhost:8080/api/recipe?isVegetarian=false&numberOfServings=6&instruction=boil&ingredientList=orange&ingredientFilterType=include'
```

### Delete recipe

```
curl --location --request DELETE 'localhost:8080/api/recipe/1'
```