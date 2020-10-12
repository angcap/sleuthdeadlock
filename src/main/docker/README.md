# Creating of the docker image and push on docker desktop 
    mvn package docker:build
    
# Running of app with docker desktop
The first time you have to run
    docker run --name organisation -p 8081:8081 -t organisation:0.0.1-SNAPSHOT

then you have to execute
    docker start organisation -a
    
The backend is accessible on the localhost:8081.