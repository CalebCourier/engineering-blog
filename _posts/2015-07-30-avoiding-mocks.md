---
layout: post
title: "Testing without mocking in Scala"
header-img: "img/mon-field_rows.jpg"
author: Jessica Kerr
githubProfile : "jessitron"
twitterHandle : "jessitron"
tags: [scala, testing, functional]
---

When unit testing in Java, we often use mocking frameworks to replace the classes that are necessary for the code under test, but not currently under test themselves. These mock frameworks don't transfer easily to Scala. That's not a bad thing: the functional side of Scala can make mocking unnecessary. As someone told me the other day at PolyConf (link): mocks are the sound of your code crying out, "please structure me differently!"

Don't use mocks? Structure code differently? Easier said than done. What follows is a practical example that shows how to remove the need for a mock object, and at the same time separate concerns of interface and business logic.

Say there's an IdentityService that returns an identity based on an access token. Internally, it makes another call to an AccessTokenService, retrieving
information about the access token. Then it interprets the result: success provides an identity, and anything else means we should proceed 
anonymously (return no identity). The code looks like:

class FacebookIdentityClient(jsonClient: JsonClient) {

  def fetchFacebookIdentity(accessToken: String) : Future[Option[FacebookIdentity]] = {
    jsonClient.getWithoutSession(
      Path() / "facebook" / "identities",
      Params("access_token" -> accessToken)
    ).map {
      case JsonResponse(OkStatus, json, _, _) => Some(FacebookIdentityMapper(json))
      case _ => None
    }
  }
}

The tests want to say, "If the inner call returns success, provide the returned identity; if it fails, return none." To unit-test that, we need to mock the JsonClient, and its getWithoutSession method, and check the arguments... ugh, mocking.

The secret here is to recognize that part of the method under test is about the interface, and part of it is business logic.

<repeat code with highlighting>

The interface part of this is only testable in integration tests. That's where we check our assumptions about the path structure, the input and the output of the other service. The business logic part of this is unit-testable once we separate the two. Instead of passing in a general JsonClient, let's pass in a function that contains all the interface code. That function needs an access token, and it returns a future response.

class FacebookClient(howToCheck: String => Future[JsonResponse]) {

  def fetchFacebookIdentity(accessToken: String) : Future[Option[FacebookIdentity]] = {
    howToCheck(accessToken).map {
      case JsonResponse(OkStatus, json, _, _) => Some(FacebookIdentityMapper(json))
      case _ => None
    }
  }
}

Meanwhile, the real interface code is shipped off to a handy object somewhere:

object RealJsonClient {
  def reallyCheckAccessToken(jsonClient:JsonClient)(accessToken: String): Future[JsonResponse] = jsonClient.getWithoutSession(
    Path() / "identity",
    Params("access_token" -> accessToken)
  )
}

The production code can instantiate the Facebook client using the RealJsonClient.reallyCheckAccessToken (link), but the test is free to provide a fake function implementation, without duplicating any specifics about this particular interface:

  "returns a FacebookIdentity when received from facebook" in new Context {

    val jsonBody = Json.toJsValue(Map("identity" -> Map("id" -> "a_facebook_id")))
    val facebookClient = new FacebookClient(_ => Future.value(JsonResponse(OkStatus, jsonBody)))
    Await.result(sociocastTokensClient.fetchFacebookIdentity("an_access_token"))  ====
      Some(FacebookIdentity("a_facebook_id"))
  }

In this case, the test constructs the expected response and then provides a function that returns that, no matter what. It isn't checking the arguments, although it could. We can pass a function that does whatever we want, for the purposes of our test. There's no JsonClient object to mock. (The function we passed is a fake, which is different from a mock (link to Justin's talk if I can find it).)

This is a minimal example, and the test still isn't perfect. Yet, it shows how passing a "how" instead of passing an object can make testing easier in Scala. Check the sample code before (link) and after (link) to see the difference.

This is also an example of implementing a ports-and-adapters architecture. By removing the interface code, we created a port -- like a hole, an interface. Then the RealJsonClient contains an adapter: a plug for the hole that hooks up to a real-life system. The function passed in the test is also an adapter that fits the same hole.

Whenever you see mocking in Scala, look for an opportunity to separate decisionmaking code from interface code. Consider this style instead.

Thanks to Duana (twitter link) for asking me these questions and providing the example.
