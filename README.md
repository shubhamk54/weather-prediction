# Weather Prediction APIs

## Steps to run application on local

`mvn install`

To run the service on localhost </br>

`mvn spring-boot:run`

This will run application on port `8080` by default.

### Project files contains

`Dockerfile` and `Jenkinsfile` for deployment

`deployment-mf` is the deployment manifest for all available Environments.

Please configure `API_KEY_WEATHER_APP` as OpenWeather API key as env varieble/

- on mac/linux: ` export API_KEY_WEATHER_APP=<API_KEY>`

- on windows:
  ` set API_KEY_WEATHER_APP=<API_KEY>`
