# RePlate API Collection

This directory contains reusable API requests, automated
checks, and example responses for the RePlate backend.

## Structure

- `bruno/` — Bruno collection, environments, and API tests
- `examples/adaptations/` — Sample JSON responses

## Requirements

- Bruno API Client
- RePlate Spring Boot backend running on port 8080
- PostgreSQL running through Docker Compose

## Getting Started

1. Start the RePlate backend.
2. Open Bruno.
3. Open the collection located in `api/bruno/`.
4. Select the `local` environment.
5. Verify that `baseUrl` is `http://localhost:8080`.

## Available Requests

### Create Vegan Adaptation

`POST /api/v1/adaptations`

Generates a vegan adaptation of the submitted recipe.

The response contains a UUID, adaptation goal, and generated
recipe with ingredients, instructions, changes, and warnings.

The Bruno test saves the returned UUID into the runtime
variable `adaptationId`.

When the backend runs with the `ai` profile, this request
uses OpenAI and may incur API charges.

### Get Adaptation by ID

`GET /api/v1/adaptations/{{adaptationId}}`

Retrieves a previously created adaptation from PostgreSQL.

Run the POST request first to populate the runtime variable.

### Connection Check

Sends a GET request using a nonexistent adaptation UUID.

The expected response is HTTP 404, confirming that the
backend is reachable and handles missing adaptations.

## Response Examples

The `examples/adaptations/` directory contains sample JSON
responses, including successful creation and retrieval.

These examples are snapshots, not live API responses.

## Testing

Bruno tests check HTTP status codes, response structure,
and consistency between created and retrieved adaptations.

The Java backend has its own automated test suite, which
can be executed separately using Maven.

## Security

- Never commit OpenAI API keys or database passwords.
- Environment secrets are managed outside the collection.
- Review response examples before committing them.
- Do not use real personal data in committed examples.
