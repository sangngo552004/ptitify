# fast-deploy.ps1
# Script to quickly package and hot-deploy the application to the running Wildfly container.

Write-Host "Packaging the application using Maven..." -ForegroundColor Cyan
.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven build failed. Aborting deploy." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Copying music-app.war into the running Docker container (ejb-app)..." -ForegroundColor Cyan
docker cp .\target\music-app.war ejb-app:/opt/jboss/wildfly/standalone/deployments/

Write-Host "Done! Wildfly will automatically detect the new file and hot-reload." -ForegroundColor Green
