package org.carl.json;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

public class TestJson {
  static String json =
      """
      {
        "login": "michelleclar",
        "id": 70901171,
        "node_id": "MDQ6VXNlcjcwOTAxMTcx",
        "avatar_url": "https://avatars.githubusercontent.com/u/70901171?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/michelleclar",
        "html_url": "https://github.com/michelleclar",
        "followers_url": "https://api.github.com/users/michelleclar/followers",
        "following_url": "https://api.github.com/users/michelleclar/following{/other_user}",
        "gists_url": "https://api.github.com/users/michelleclar/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/michelleclar/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/michelleclar/subscriptions",
        "organizations_url": "https://api.github.com/users/michelleclar/orgs",
        "repos_url": "https://api.github.com/users/michelleclar/repos",
        "events_url": "https://api.github.com/users/michelleclar/events{/privacy}",
        "received_events_url": "https://api.github.com/users/michelleclar/received_events",
        "type": "User",
        "site_admin": false,
        "name": "Carl",
        "company": null,
        "blog": "",
        "location": null,
        "email": null,
        "hireable": null,
        "bio": null,
        "twitter_username": null,
        "public_repos": 8,
        "public_gists": 0,
        "followers": 0,
        "following": 0,
        "created_at": "2020-09-07T13:35:45Z",
        "updated_at": "2024-06-03T13:14:16Z"
      }
      """;

  @Test
  public void testLocalDateTimeToJsonValue() {
    JsonObject _json = new JsonObject(json);
    System.out.println(_json);
  }
}