package com.monsanto.engineering_blog.testing_without_mocking

import com.monsanto.engineering_blog.testing_without_mocking.JsonStuff._
import org.scalatest.FunSpec

import scala.concurrent.{Await, Future}
import spray.json._
import spray.json.DefaultJsonProtocol._
import scala.concurrent.duration._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.ExecutionContext.Implicits.global



/// Sooooo.... I'm gonna need internet for this UnitSpecification and VerifiedMocks.

class FacebookIdentityClientTest extends FunSpec with MockFactory {

  describe("FacebookIdentityClient") {
    it("returns a FacebookIdentity when received from Facebook") {
      val jsonClient = mock[JsonClient]
      val path = Path("/identity")
      val params = Params("access_token" -> "an_access_token")
      val facebookClient = new FacebookIdentityClient(jsonClient)
      val jsonBody = Map("identity" -> Map("id" -> "an_access_token")).toJson
      (jsonClient.getWithoutSession _).expects(path, params).returning(Future(
        JsonResponse(OkStatus, jsonBody)))

      Await.result(facebookClient.fetchFacebookIdentity("an_access_token"), 1.second) ===
        Some(FacebookIdentity("a_facebook_id"))
    }
  }
}
