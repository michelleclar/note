### Log

> Across modules, most interactions are handled using events (due to potential future service splits), with the majority
> being asynchronous. TraceId propagation hasn't been implemented yet; currently, logs are traced using icon-based
> indicators for troubleshooting:

⛵ One icon represents normal operation, while three consecutive icons indicate an issue requiring investigation.
⛵ Users authenticate via third-party services, followed by asynchronous user information registration.
♠️ This icon indicates a potential malicious attack, triggering logging.

> Add traceId ,remove icon.

### Development Deficiencies

- [ ] refreshToken should be generated during manual login; a new refreshToken should be generated each time a user logs
  in.
- [ ] Need to introduce caching middleware or store tokens in the database; currently stored only in memory.
- [ ] Security issues during login need further optimization; current focus is on implementing functionality.

### Development notes

> Modify the permission logic: using JWT will only involve create, update, read, and some delete functionalities. For
> critical permissions such as deactivating accounts or deleting everything, email verification is required.
> Add a vector database for article text similarity search, with Milvus temporarily selected as the vector database.
> Introducing the vector database is aimed at refining articles and laying the groundwork for future AI article.
> recommendations.
> Add ollama.