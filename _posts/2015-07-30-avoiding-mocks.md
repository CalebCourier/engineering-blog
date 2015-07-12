---
layout: post
title: "Testing without mocking in Scala"
header-img: "img/mon-field_rows.jpg"
author: Jessica Kerr
githubProfile : "jessitron"
twitterHandle : "jessitron"
tags: [scala, testing, functional]
---

<style scoped>
  .interface { color: #D907E8 }
  .logic { color: #19BEFF }
  .jsonClient {color: #E80D0C }
  .functionParam {color: #1ab955 }
  .port {color: #FF9C00 }
</style>


For unit testing in Java, mocking frameworks replace classes necessary for the code under test, but not under test themselves. These mock frameworks don't transfer easily to Scala. That's OK: the functional side of Scala can make mocking unnecessary. As someone told me the other day at PolyConf (link): mocks are the sound of your code crying out, "please structure me differently!"

Don't use mocks? Structure code differently? Easier said than done. What follows is a practical example of removing the need for a mock object, and at the same time separating concerns of interface and business logic.

Say there's an IdentityService that returns a Facebook username based on an access token. Internally, it calls out to AccessTokenService, retrieving
information about the access token. Then it interprets the result: success provides an identity; anything else means proceed 
anonymously (return no identity). The code looks like:

// TODO: this doesn't format right. Maybe Chris can help?

<div class="highlight"><pre><code class="language-scala" data-lang="scala">
class FacebookIdentityClient({{ "jsonClient: JsonClient" | sc: "jsonClient" }})  {

  def fetchFacebookIdentity(accessToken: String) : Future[Option[FacebookIdentity]] = {
    {{ "jsonClient" | sc: "jsonClient" }}<span class="interface">.getWithoutSession(
      Path("identities"),
      Params("access_token" -> </span>accessToken<span class="interface">)<br/>
    )</span>.map <span class="logic">{
      case JsonResponse(OkStatus, json, _, _) => Some(FacebookIdentity.from(json))
      case _ => None
    }</span>
  }
}</code></pre></div>

The tests want to say, "If the inner call returns success, provide the returned identity; if it fails, return none." To unit-test that, we need to mock the {{"JsonClient"|sc: "jsonClient"}}, and its getWithoutSession method, and check the arguments... ugh, mocking.

The secret here is to recognize that part of the method under test is about the {{"interface" | sc: "interface" }}, and part of it is {{"business logic" | sc: "logic" }}.

The {{"interface"|sc:"interface"}} is only testable in integration tests. That's where we check our assumptions about the path structure, the input and the output of the other service. 
The {{"business logic"|sc:"logic"}} part of this is unit-testable once we separate the two. Instead of passing in a general {{"JsonClient"|sc:"jsonClient"}},
 let's pass in {{"a function"|sc:"functionParam"}} that contains all the interface code. That function needs an access token, and it returns a future response.

<div class="highlight"><pre><code class="language-scala" data-lang="scala">
class FacebookClient({{"howToCheck: String => Future[JsonResponse]" | sc: "port"}}) {

  def fetchFacebookIdentity(accessToken: String) : Future[Option[FacebookIdentity]] = {
    {{"howToCheck"|sc:"port"}}(accessToken).map <span class="logic">{
      case JsonResponse(OkStatus, json, _, _) => Some(FacebookIdentityMapper(json))
      case _ => None
    }</span>
  }
}
</code></pre></div>

Meanwhile, the real {{"interface code"|sc:"interface"}} is shipped off to a handy object somewhere:

<div class="highlight"><pre><code class="language-scala" data-lang="scala">
object RealJsonClient {
  def reallyCheckAccessToken({{"jsonClient:JsonClient"|sc:"jsonClient"}})(accessToken: String): Future[JsonResponse] = jsonClient<span class="interface">.getWithoutSession(
    Path() / "identity",
    Params("access_token" -> accessToken)
  )</span>
}
</code></pre></div>

The production code can instantiate the Facebook client using that object, but the test is free to provide {{"a fake function implementation"|sc:"functionParam"}}, without duplicating any specifics about this particular interface:

<div class="highlight"><pre><code class="language-scala" data-lang="scala">
it("returns a FacebookIdentity when received from facebook") {
  val jsonBody = Map("identity" -> Map("id" -> "a_facebook_id")).toJson
  val facebookClient = new FacebookIdentityClient({{"_ => Future(JsonResponse(OkStatus, jsonBody))" | sc: "functionParam" }})

  Await.result(facebookClient.fetchFacebookIdentity("an_access_token"), 1.second) ===
    Some(FacebookIdentity("a_facebook_id"))
}
</code></pre></div>

This test constructs the expected response and then provides {{"a function"|sc:"functionParam"}} that returns that, no matter what. 
It isn't checking the arguments, although it could. We can pass a function that does whatever we want, for the purposes of our test.
 There's no {{"JsonClient"|sc:"jsonClient"}} object to mock. (Technically, {{"the function we passed"|sc:"functionParam"}} is a fake, which is different from a mock (link to Justin's talk if I can find it).)

This is a minimal example, and the test isn't perfect. Yet, it shows how passing a "{{"how"|sc:"functionParam"}}" instead of passing {{"an object"|sc:"jsonClient"}} can make testing easier in Scala. Check the sample code before (link) and after (link) to see the difference.

This example illustrates a ports-and-adapters architecture. By removing the interface code, we created a {{"port"|sc:"port"}} -- like a hole, like a Java interface. Then the RealJsonClient contains an {{"adapter"|sc:"functionParam"}}:
 a plug for the hole that hooks up to a real-life system. The function passed in the test is an {{"adapter"|sc:"functionParam"}} that fits the same hole.

Whenever you see mocking in Scala, look for an opportunity to separate {{"decisionmaking code"|sc:"logic"}} from {{"interface code"|sc:"interface"}}. Consider this style instead.

Thanks to Duana (twitter link) for asking me these questions and providing the example.
