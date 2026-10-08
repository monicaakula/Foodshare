FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY webapp/ /usr/local/tomcat/webapps/ROOT/

COPY lib/mysql-connector-j-26.7.0.jar /usr/local/tomcat/lib/

EXPOSE 8080

CMD ["catalina.sh", "run"]
