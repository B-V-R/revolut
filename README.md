# revolut-task
Test task for Revolut.

The REST Api for money transfer.
Technologies used:
1. Maven as project management tool
2. SparkJava framework for REST
3. Guice for dependency injection
4. JUnit and Mockito for testing

## Usage
To run the application:
1. `mvn clean package` //will create jar file
2. `java -jar revolut\target\revolut-1.0-SNAPSHOT.jar` //will run application on port 8080

### Implementations
This application has two impementations of services:
1. When datastore is represented as Map.
2. When datastore is represented as H2 (in-memory) database.

By default application starts in MAP mode (using Map as datastore).
User can specify the mode when the programm is started:
1. SQL mode: `java -jar revolut\target\revolut-1.0-SNAPSHOT.jar SQL`

### Api's

| Description | Request | Path | Response | Example |
| ------ | ------ | ------ | ------ | ------ |
| Create new account with balance | POST | /account | JSON with info about account | Input: <br/>`{ "balance": 100 }` <br/>Output: <br/>`{ "id": 1, "balance": 100}` |
| Get account by id | GET | /account/{id} | JSON with info about account | Output: <br/>`{ "id": 1, "balance": 100}` |
| Transfer money | POST | /transfer | JSON with info about transfer status | Input: <br/>`{ "fromId": 1, "toId": 2, "amount": 100 }` <br/> Output: <br/> `{Transfer Completed}` |

Example CURLs

`curl -i -d "{ \"balance\": 100 }" --header "Content-Type: application/json" -X POST http://localhost:8080/account`

`curl -i -d "{\"fromId\": 1, \"toId\": 2, \"amount\": 100}" --header "Content-Type: application/json" -X POST http://localhost:8080/transfer`

`curl -i --header "Content-Type: application/json" -X GET http://localhost:8080/account/2`

The possible exceptions:
1. In case the account doesn't exists (int get account or transfer REST calls) the _"Account with id = {id} wasn't found"_ will be displayed;
2. In case transfer is happening to the same account the _"Can't transfer the money to the same account."_ will be displayed;
3. In case the balance on the fromAccount is less than the required amout the _"Account with id = {id} doesn't have enough balance to transfer this amount = {amount}"_ will be displayed;

