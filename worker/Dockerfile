FROM openjdk:15
COPY ./build/libs/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-Xmx1024m", "-Xms256m","-jar","Worker.jar"]