# User Points API Usage

See all current user accounts:

```bash
curl -v -X GET "http://localhost:8080/users"
```

Create a new user account:

```bash
curl -v -X POST "http://localhost:8080/users"
```

Get information about a certain user acccount:

```bash
curl -v -X GET "http://localhost:8080/users/{id}"
```

Add points to an account:

```bash
curl -v -X POST "http://localhost:8080/users/{id}/addpoints?payer={payer}&points={points}"
```

Deduct points from an account:

```bash
curl -v -X POST "http://localhost:8080/users/{id}/deductpoints?points={points}"
```

Get the point breakdown for an account:

```bash
curl -v -X GET "http://localhost:8080/users/{id}/pointbreakdown"
```