docker-compose down

cd .. && ./gradlew clean build integrationTest docker
cd docker && docker-compose up
