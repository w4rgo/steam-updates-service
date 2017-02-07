# steam-updates-service
A service that will return a history of the updates of the steam games configured done to learn Kotlin

# Configuration

Rename default-config.json to config.json and then modify it
```
{
  "steamcmd": {
    "path": "steamcmd",
    "refreshMinutes" : "5"
  },
  "database": {
    "url": "your.database.domain:3306/your-database-name",
    "user": "user",
    "pass": "pass"
  },
  "games": [
    {
      "name": "conan",
      "steamGameId": "443030"
    },
    {
      "name": "rust",
      "steamGameId": "258550"
    }
  ]
}
```
