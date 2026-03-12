FROM quay.io/wildfly/wildfly:latest

# Copy thư viện và script vào thư mục tạm
COPY --chown=jboss:root mysql-connector.jar /tmp/
COPY --chown=jboss:root setup-db.cli /tmp/

# Dùng $JBOSS_HOME thay cho đường dẫn tĩnh
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=/tmp/setup-db.cli && \
    rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ && \
    rm /tmp/mysql-connector.jar /tmp/setup-db.cli

# Copy file war của ứng dụng vào thư mục deploy
COPY --chown=jboss:root target/*.war $JBOSS_HOME/standalone/deployments/

EXPOSE 8080 9990