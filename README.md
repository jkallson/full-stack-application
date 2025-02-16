# How to run?
- Clone repository
- Have Docker running on the background

### Run manually
- Start backend. This should also start Postgres DB and seed some data into it.
    - Run by running the application from your IDE or by running in the backend folder `./gradlew bootRun`   
    - Backend runs at http://localhost:3000
- Start frontend
    - `cd frontend` 
    - Run `npm ci`
    - Run `npm run dev`
    - Frontend runs at http://localhost:8080

### Run via docker
- Navigate to backend folder `cd backend` and run `./gradlew build`. That generates a build file which is needed for docker-compose
- After that navigate back to the root folder (where docker-compose is) and run ` docker-compose up --build`
- Application should start at http://localhost:8080

## To login into the application I have created two users
* username `marimets`, password `marimets`
* username `madismets`, password `madismets`
