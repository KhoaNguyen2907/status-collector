# Common Issues and Solutions

## Issue 1: Expired GitHub Credentials

If the GitHub credentials have expired, the config server may not start.

**Solution:** Log into GitHub and check if the personal access token has expired.

## Issue 2: Missing ENCRYPT_KEY

The `ENCRYPT_KEY` is needed to decrypt the password in the config server.

**Solution:** Add the following line to the `application.yml` file of the config server:

```yaml
encrypt:
  key: ${ENCRYPT_KEY}
```

## Issue 3: Encrypting and Decrypting Passwords
**Solution:** nstall the Spring Boot CLI, then install the Spring Cloud extension for the Spring Boot CLI. You can then use the spring encrypt and spring decrypt commands to encrypt and decrypt the password.