package com.monsanto.engineering_blog.testing_without_mocking

import com.monsanto.engineering_blog.testing_without_mocking.JsonStuff._

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class FacebookIdentityClient(jsonClient: JsonClient)  {

  def fetchFacebookIdentity(accessToken: String) : Future[Option[FacebookIdentity]] = {
    jsonClient.getWithoutSession(
      Path("/identity"),
      Params("access_token" -> accessToken)
    ).map {
      case JsonResponse(OkStatus, json) => Some(FacebookIdentity.from(json))
      case _ => None
    }
  }
}
