***In this project was made the following assumptions:***
- drone's weight is the sum of all loaded medications
- drone can carry copies of the same medication. For example, two packs of painkillers means that the drone is loaded with medicine in the amount of 2 pcs.
- drone registration sets state to IDLE.

The check for the drones available for loading takes the following requirements into account:

- only the drones with IDLE or LOADING status may be available
- the drone must be prevented from being in LOADING state if the battery level (capacity) is below 25%, the message 'Drone battery less than 25%' is issued
- loading each new medication increases the drone weight and fill map of (medication -> quantity).
- loading process results in the status change for LOADING if only the overall weight of loaded medications does not exceed the weight limit of 500 g
- whe drone loaded for max weight, status changes to LOADED
- when the weight limit is reached, the message 'Medication is too heavy for this drone' is issued

***Some notes on implementation***

Not according to the common practice I use natural primary keys for both the drone and medication tables/entities. Also I added Element Collection table for the same medication counting.

I have chosen H2 database, in-memory variant which is volatile, and the data is lost on application restart. The database is up and running on application start.  Postman must be enough for the interaction with Rest API and H2.  Still H2 Console is available at http://localhost:8089/h2-console/.  Employ 'sa' as user name and 'sa' as password to log into.

Validation of the drone and medication parameters is implemented at the controller layer

Registering drones and medications is implemented by one controller

Removing medications and drones was not mentioned as a functional requirements and is left out of the scope of the application.

***A few notes on configuration***

Periodic task to check battery levels is managed by the property 'app.delay' and currently runs once a 5 sec

Log file for the periodic battery check is saved to the 'logs' subfolder within the project directory.


***Requirements***

server.port: 8089

java 17

set JAVA_HOME env to java installation directory


***Build/Run instruction***

open terminal in project root and run ./mvnw spring-boot:run

***Documentation***
Http request can be sent to the server on the port specified by server.port property.
All sever endpoints:

1) Register drone

curl -L -X POST 'http://localhost:8089/drone/1.0/api/registerDrone?serialNumber=drone02&type=LIGHTWEIGHT&battery=50' \
-H 'Content-Type: application/json'

{
"errorCode": 0,
"errorCause": null,
"errorMessage": null,
"serialNumber": "drone02"
}

2) get all available drones

curl -L -X GET 'http://localhost:8089/drone/1.0/api/getAvailableDrones'

[
{
"serialNumber": "drone02",
"version": 1,
"type": "LIGHTWEIGHT",
"weight": 0,
"batteryLevel": 50,
"state": "IDLE",
"loadedMeds": {}
}
]

3) register medication


   curl -L -X POST 'http://localhost:8089/drone/1.0/api/registerMed?medicationCode=MED_003&name=med_003_name&weight=500' \
   -H 'Content-Type: application/json' \
   --data-binary '@/Users/ramilyusipov/Desktop/Снимок экрана 2022-11-15 в 19.33.52.png'

{
"errorCode": -1,
"errorCause": "com.musalasoft.demo.exception.RegistrationException",
"errorMessage": "Medication with this code is already registered",
"medicationCode": null
}

4) load drone with medication


curl -L -X POST 'http://localhost:8089/drone/1.0/api/loadDrone?serialNumber=drone02&medicationCode=MED_002'

{
"errorCode": 0,
"errorCause": null,
"errorMessage": null,
"loadedMeds": {
"MED_001": 2,
"MED_002": 1
}
}