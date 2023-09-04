# load-balancer
It is simple load balancer which sits in front of a group of servers and routes client requests across all of the servers that can process those requests. 
If a server goes offline the load balancer redirects traffic to the remaining live servers.

This repository contains two applications
1. Load balancer application. (lbapp)
2. Web server application. (web-server-app)

#### Load balancer application

Load balancer application performs following functions
- Distributes client requests efficiently across multiple servers.
- Ensures high availability by sending requests only to servers that are live and capable to serve the requests.

1. The load balancer listens for connections on specified port (i.e 8080 for HTTP).
   
   Request example
   
```
curl http://localhost:8080
```

   Request returns response from back end server

   -- insert screen shot

 The request is forwarded to a back end server. In order to handle multiple client requests concurrently, load balancer application handles requests asynchronously using  ``CompletableFuture, @Async annotation, custom ThreadPoolTaskExecutor``    


 The multiple incoming requests are distributed between live servers using a basic    scheduling algorithm - **round robin**. 

 In a nutshell it works by sending each new request to the next server in the list of servers.

2. Load balancer performs another second most important task of checking health of the servers periodically. If any server fails the health check then it will stop sending requests to that server.
   The Health check runs as background task which pings the servers periodically after specific interval of time (e.g 60 seconds).
   
   To check load balancer can handle multiple concurrent requests, curl can be used with the file containing the urls to check (urls.txt file given in root folder of the lbapp)
   
```
url = "http://localhost:8080"
url = "http://localhost:8080"
url = "http://localhost:8080"
url = "http://localhost:8080"
url = "http://localhost:8080"
url = "http://localhost:8080"
```

Invoke curl to make concurrent requests:
``curl --parallel --parallel-immediate --parallel-max 3 --config urls.txt``   

Tweak the maximum parallelisation to see how well server copes!


#### Web server application

This is a simple backend server application. The main purpose of this application is to spawn multiple HTTP servers.

Servers are created using ``HttpHandler`` interface (i.e ``WebHttpHandler.java`` class). It creates servers for `9090,9092,9093,9094` port numbers.
 
The created servers listens two endpoints ``/test`` and ``/health``
