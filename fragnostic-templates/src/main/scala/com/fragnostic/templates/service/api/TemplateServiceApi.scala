package com.fragnostic.templates.service.api

trait TemplateServiceApi {

  def templateService: TemplateServiceApi

  trait TemplateServiceApi {

    def getTemplate(name: String, params: Option[List[(String, String)]]): Either[String, String]

  }

}
