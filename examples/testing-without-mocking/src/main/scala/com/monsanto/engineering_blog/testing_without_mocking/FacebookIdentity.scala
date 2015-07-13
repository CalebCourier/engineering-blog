package com.monsanto.engineering_blog.testing_without_mocking

import spray.json.JsValue
import spray.json.DefaultJsonProtocol._

case class FacebookIdentity(username: String)

object FacebookIdentity {
  def from(json: JsValue): FacebookIdentity = {
    // this is crappy code to make the example compile. Do not copy.
    val interpreted = json.convertTo[Map[String,Map[String,String]]]
    interpreted.get("identity").flatMap(_.get("id")).map(FacebookIdentity(_)).get
  }
}
