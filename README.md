# hypersuck
A web service that can source a Tableau extract (.twbx) URL and provide access to the data within it, as CSV.

This is useful for using a Tableau dashboard as a data source for any other dashboard, e.g. Google Data Studio.

## Build

On Windows, provide `gradle` and Tableau hyperapi jars/executables in `./lib`, then `gradle build`.

For Linux/container, `docker build .` is what you want. The build is based off a Java/hyperapi container I published
and will codify soon (so I said May 13, 2020.)

## Run

For Linux/container, an example run command:

```
docker run -p8080:7777 -ePORT=7777 -eHYPERPATH=/hyperapi/lib/hyper <image-id-or-tag>
```

- **PORT** port to listen on
- **HYPERPATH** path to the hyper executables, e.g. /hyperapi/lib/hyper in the container I published.

## Use

Get .hyper filenames within a twb/twbx:

```
http://localhost:8080/filenames?url=https://public.tableau.com/workbooks/DPHIdahoCOVID-19Dashboard_V2.twb
```

Then get data out given a filename:

```
http://localhost:8080/data?url=https://public.tableau.com/workbooks/DPHIdahoCOVID-19Dashboard_V2.twb&filename=County%20(COVID%20State%20Dashboard.V1).hyper
```

## Deploy

Although I'll do CI eventually, can easily version and push a `docker build` result to GCR as follows:

```
docker tag <tested-image-id> us.gcr.io/hypersuck/hypersuck:v1
docker push us.gcr.io/hypersuck/hypersuck:v1
```

