package com.monsanto.engineering_blog.testing_without_mocking

import com.monsanto.engineering_blog.testing_without_mocking.JsonStuff._
import org.scalatest.FunSpec

import scala.concurrent.{Await, Future}
import spray.json._
import spray.json.DefaultJsonProtocol._
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

class FacebookIdentityClientTest extends FunSpec {

  describe("the FacebookIdentityClient") {

    it("returns a FacebookIdentity when received from facebook") {
      val jsonBody = Map("identity" -> Map("id" -> "a_facebook_id")).toJson
      val facebookClient = new FacebookIdentityClient(_ => Future(JsonResponse(OkStatus, jsonBody)))

      Await.result(facebookClient.fetchFacebookIdentity("an_access_token"), 1.second) ===
        Some(FacebookIdentity("a_facebook_id"))
    }
  }


  it("returns None when facebook gives us a 400 due to bad access token") {
    val facebookClient = new FacebookIdentityClient(_ => Future(JsonResponse(BadStatus, Map[String,String]().toJson)))

    Await.result(facebookClient.fetchFacebookIdentity("an_access_token"), 1.second) ===
      Some(None)
  }

}
