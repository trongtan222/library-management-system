Chatbot configuration

This application can call Google Generative Language (Gemini) to provide live chatbot responses. There are two supported authentication methods; configure one of them in `src/main/resources/application.properties` or via environment variables.

1) API key (quick setup)
- Property: `gemini.api.key`
- Set to your Google API key (starts with `AIza` in many cases).
- The app will make requests with `?key=YOUR_KEY` as a query parameter.

2) Service account (recommended for production)
- Property: `gemini.service.account.path`
- Point this to a local JSON service-account key file (download from Google Cloud Console) or set `GOOGLE_APPLICATION_CREDENTIALS` accordingly and set the property to the same path.
- The app will use the Google Auth library to obtain an OAuth access token and send `Authorization: Bearer <token>` to the Generative API.

Mock fallback behavior
- If neither a valid `gemini.service.account.path` nor a non-empty `gemini.api.key` is configured, the controller will return a harmless mocked response indicating the chatbot is running in local mode.
- This is intended for local development so the UI still shows a reply instead of a hard failure.

Troubleshooting
- If you receive HTTP 401 from the Generative API:
  - Verify the API key is correct and the Generative API is enabled in your Google Cloud project.
  - Check that billing is enabled for the project.
  - Prefer service-account auth for production usage; ensure the service account has appropriate roles and the JSON key path is readable by the application.

Security notes
- Do not commit service-account JSON files or real API keys to source control.
- Use environment variables or an external secrets store in production.

Example properties (do NOT commit real keys):

```
# API key method
gemini.api.key=AIzaSy...YOUR_KEY_HERE...

# Or service-account method
gemini.service.account.path=C:/secrets/gcloud-service-account.json
```

If you want, I can help you switch to Application Default Credentials (ADC) or wire a secrets provider for safer key management.