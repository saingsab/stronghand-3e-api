# stronghand-3e-api

[![build and deploy](https://github.com/saingsab/stronghand-3e-api/actions/workflows/build_deploy.yml/badge.svg)](https://github.com/saingsab/stronghand-3e-api/actions/workflows/build_deploy.yml)

### Run the application locally

`lein ring server`

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/server.jar
```

### Packaging as war

`lein ring uberwar`

## License

Copyright ©  saing sab
