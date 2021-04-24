# stronghand-3e-api

[![Build Status](https://www.travis-ci.com/saingsab/stronghand-3e-api.svg?branch=main)](https://www.travis-ci.com/saingsab/stronghand-3e-api)

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
