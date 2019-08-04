package com.fragnostic.templates.service

import com.fragnostic.templates.service.impl.TemplateServiceMapImpl

object CakeServiceTemplate {

  lazy val templateServicePiece = new TemplateServiceMapImpl {}

  val templateService = templateServicePiece.templateService

}
