# job-interview-room-occupancy-manager
job-interview-room-occupancy-manager


# Room Occupancy Manager

## Requirements

Build a room occupancy optimization tool for one of our hotel clients! Our customer has a certain number of free rooms each night, as well as potential guests that would like to book a room for that night.

Our hotel clients have two different categories of rooms: Premium and Economy. Our hotels want their customers to be satisfied: they will not book a customer willing to pay EUR 100 or more for the night into an Economy room. But they will book lower paying customers into Premium rooms if these rooms are empty and all Economy rooms are occupied with low paying customers. The highest paying customers below EUR 100 will get preference for the “upgrade”. Customers always only have one specific price they are willing to pay for the night.

Please build a small API that provides an interface for hotels to enter the numbers of Premium and Economy rooms that are available for the night and then tells them immediately how many rooms of each category will be occupied and how much money they will make in total. Potential guests are represented by an array of numbers that is their willingness to pay for the night.

Use the following raw JSON file/structure as mock data for potential guests in your tests: `[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]`

### Requirements for a Valid Solution

- A working algorithm implemented in at least Java 11.
- Tracking progress through Git commits.
- Minimal README explaining how to run the project and tests.
- Tests/TDD (at least use the ones specified above).
- Clean code structure and formatting.
- Thoughtful naming of variables and functions.
- Go for high code quality rather than completeness, if you feel time pressure.

### Nice to Haves

- A restful JSON API implemented with Spring Boot or a comparable framework.
- Anything else you would like to add.

## Tests

### Test 1

- **Input**
  - Free Premium rooms: 3
  - Free Economy rooms: 3
- **Output**
  - Usage Premium: 3 (EUR 738)
  - Usage Economy: 3 (EUR 167.99)

### Test 2

- **Input**
  - Free Premium rooms: 7
  - Free Economy rooms: 5
- **Output**
  - Usage Premium: 6 (EUR 1054)
  - Usage Economy: 4 (EUR 189.99)

### Test 3

- **Input**
  - Free Premium rooms: 2
  - Free Economy rooms: 7
- **Output**
  - Usage Premium: 2 (EUR 583)
  - Usage Economy: 4 (EUR 189.99)

### Test 4

- **Input**
  - Free Premium rooms: 7
  - Free Economy rooms: 1
- **Output**
  - Usage Premium: 7 (EUR 1153)
  - Usage Economy: 1 (EUR 45.99)




# Room Occupancy Manager - Technical Side


## Prerequisites

Before you begin, ensure you have the following software installed:

- Java 17 or higher
- Maven 3.9.6 or higher
- Git (optional, for cloning the repository)

## Setup

1. **Clone the repository (if applicable):**

   ```sh
   git clone https://github.com/your-repo/room-occupancy-manager.git
   cd room-occupancy-manager

## Build the project:
```
mvn clean install
```

## Running Tests
```
mvn test
```
## Running the Application
```
mvn spring-boot:run
```


## Docker - Running the Application 
```
mvn clean install
docker build --tag=room-occupancy-manager:latest .
docker run -p 8080:8080 room-occupancy-manager:latest
```

## Docker-Compose - Running the Application 
```
mvn clean install
docker-compose up -d 
```


## Endpoints

### Optimize Occupancy

**POST** `/api/occupancy`

#### Request Body:
```json
{
    "premiumRooms": 3,
    "economyRooms": 3,
    "guestPayments": [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0]
}
```

#### Response Body:
```json
{
    "usedPremiumRooms": 3,
    "usedEconomyRooms": 3,
    "totalPremiumEarnings": 738.0,
    "totalEconomyEarnings": 167.99
}
```


## Swagger
You can access the Swagger UI for this API at:

http://localhost:8080/swagger-ui/index.html
