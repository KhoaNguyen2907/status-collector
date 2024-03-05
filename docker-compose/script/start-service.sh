#! /bin/bash
# start status-fetcher

java -cp @/app/jib-classpath-file com.devkhoa.statuscollector.statusloader.StatusLoaderApplication
java -cp @/app/jib-classpath-file com.devkhoa.statuscollector.statusfetcher.StatusFetcherApplication
java -cp @/app/jib-classpath-file com.devkhoa.statuscollector.statusretriever.StatusRetrieverApplication


