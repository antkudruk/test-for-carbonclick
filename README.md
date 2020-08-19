# test-for-carbonclick

## Running instructions

* Set up My Sql Database Service
* Create new database called `secretsanta`.

    `create database secretsanta;`
* Set up your credentials in src\main\resources\application.yml in the following fields
    ```$xslt
    spring:
      datasource:
        username: 
        password: 
    ```
* Run the application via gradle
    * To clean up your build run the command
        `gradlew.bat clean`
    * To build the project run the command
        `gradlew.bat build`
    * To run the project run
        `gradlew.bat bootRun`        
        
## Migrations

To modify database schema you should add your Flyway migration into the directory
`src\main\resources\db\migration`.

The project uses the following conventions:

* Entity names should be in Upper Camel Case and should end with the word 
  `Entity`, like `ParticipantEntity`
* Table name of the corresponding Entity class is taken from the entity name
  ba taking of the suffix `Entity` and converting the rest to the dash-case.
* Column names are taken from the corresponding entity field names converted to 
  dash-case
  
That process is performed by `com.carbonclick.tsttask.secretsanta.util.NamingStrategy`
class.

   