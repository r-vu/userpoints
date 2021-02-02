# User Points

A simulation of user accounts with points, served using a REST API

# Getting Started

The application was written, tested, and executed on Ubuntu 20.04 with Java 8 Update 275

## Dependencies

Ensure you have the Java 8 JDK installed. You can check your current Java JDK version within the terminal using

```bash
javac -version
```

Something like the following should show up. Ensure the second number is 8

```bash
javac 1.8.0_275
```

If not, install the Java 8 JDK using apt (or your respective package manager)

```bash
sudo apt install openjdk-8-jdk
```

## Getting the Code

Clone the repository onto your computer

```bash
git clone https://github.com/r-vu/userpoints.git
```

Navigate to the folder

```bash
cd userpoints
```

Start the application through Maven

```bash
./mvnw spring-boot:run
```

The application should now be up and running! If the application needs to be terminated, use the `CTRL+C` keyboard combination to kill the process.

# Usage

*For quick reference, please take a look at the specific API documentation. Otherwise, follow below for detailed examples and results.*

With the application started, we can now add or remove points as we please to specific user accounts. We can start by looking at the list of open accounts:

```bash
curl -v -X GET "http://localhost:8080/users"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /users HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:04:51 GMT
<
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users"
    },
    "createUser" : {
      "href" : "http://localhost:8080/users"
    }
  }
```

Initially this should be an empty list, as no accounts have been made. However, there are two links for potential actions. One is a self-reference link, and the other is a link to create a new user. Let's make a new account:

```bash
curl -v -X POST "http://localhost:8080/users"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 201
< Location: http://localhost:8080/users/1
< Content-Length: 0
< Date: Tue, 02 Feb 2021 06:05:15 GMT
<
```

The URI in the Location header points to our fresh, new user account. Let's get some information on it:

```bash
curl -v -X GET "http://localhost:8080/users/1"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /users/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:05:37 GMT
<
{
  "userId" : 1,
  "balance" : 0,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/1"
    },
    "addPoints" : {
      "href" : "http://localhost:8080/users/1/addpoints?payer={payer}&points={points}",
      "templated" : true
    },
    "deductPoints" : {
      "href" : "http://localhost:8080/users/1/deductpoints?points={points}",
      "templated" : true
    },
    "pointBreakdown" : {
      "href" : "http://localhost:8080/users/1/pointbreakdown"
    },
    "userList" : {
      "href" : "http://localhost:8080/users"
    }
  }
```

As we can see, there now exists an account with a `userId` of `1`, which contains a `balance` of `0`, and several different potential actions.

Let's try adding some points into the account. In this example, our payer will be `ZAPOW` and the amount will be `100`:

```bash
curl -v -X POST "http://localhost:8080/users/1/addpoints?payer=ZAPOW&points=100"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users/1/addpoints?payer=ZAPOW&points=100 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:12:21 GMT
<
{
  "userId" : 1,
  "balance" : 100
}
```

We can add another transaction with another payer, let's say `Dynamite123` for `150` points:

```bash
curl -v -X POST "http://localhost:8080/users/1/addpoints?payer=Dynamite123&points=150"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users/1/addpoints?payer=Dynamite123&points=150 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:16:10 GMT
<
{
  "userId" : 1,
  "balance" : 250
}
```

Let's try deducting some points now, let's say `200`:

```bash
*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /users/1/deductpoints?points=200 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:18:49 GMT
<
{
  "ZAPOW" : -100,
  "Dynamite123" : -100
}
```

As we can see, `100` points were taken from each payer on the account. We can then see the point breakdown:

```bash
curl -v -X GET "http://localhost:8080/users/1/pointbreakdown"

*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /users/1/pointbreakdown HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 02 Feb 2021 06:19:46 GMT
<
{
  "ZAPOW" : 0,
  "Dynamite123" : 50
}
```
