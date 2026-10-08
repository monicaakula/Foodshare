FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY webapp/ /usr/local/tomcat/webapps/ROOT/

COPY src/ /tmp/src/

COPY lib/mysql-connector-j-26.7.0.jar /usr/local/tomcat/lib/

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    javac -cp "/usr/local/tomcat/lib/*:/usr/local/tomcat/webapps/ROOT/WEB-INF/classes" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    /tmp/src/*.java

RUN rm -rf /tmp/src

EXPOSE 8080

CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]