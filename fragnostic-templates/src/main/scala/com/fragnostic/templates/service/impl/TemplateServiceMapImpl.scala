package com.fragnostic.templates.service.impl

import java.nio.charset.{ Charset, StandardCharsets }

import com.fragnostic.conf.service.CakeServiceConf
import com.fragnostic.support.{ FilesSupport, MapSupport }
import com.fragnostic.templates.service.api.TemplateServiceApi
import org.slf4j.{ Logger, LoggerFactory }

import scala.annotation.tailrec

trait TemplateServiceMapImpl extends TemplateServiceApi {

  def templateService = new DefaultTemplateService

  class DefaultTemplateService extends TemplateServiceApi with FilesSupport with MapSupport {

    private[this] val logger: Logger = LoggerFactory.getLogger(getClass.getName)

    private val FRAGNOSTIC_TEMPLATES_BASE_PATH = CakeServiceConf.confService.getConf("FRAGNOSTIC_TEMPLATES_BASE_PATH")
      .map(templatesBasePath => templatesBasePath)
      .getOrElse(throw new IllegalStateException("template.service.error.on.get.templates.base.path"))

    private def getKV: String => (String, String) = (line: String) => {
      val parts: Array[String] = line.split("->")
      if (parts.length == 2) {
        (parts(0).trim, parts(1).trim)
      } else {
        logger.error("getKV() - error al obtener (k,v) para la linea:\u0027{}\u0027", line)
        ("NA", "NA")
      }
    }

    private def valida: String => Boolean = (line: String) => !line.isEmpty && !line.trim.startsWith("#")

    private val extension: String =
      fileToList(s"$FRAGNOSTIC_TEMPLATES_BASE_PATH/templates.conf", StandardCharsets.UTF_8.name()).fold(
        error => {
          logger.error("extension - error al obtener extension, on fileToList")
          "NA"
        },
        list => getMap(list, valida, getKV).getOrElse("file.extension", {
          logger.error("extension - error al obtener extension, on get value from map")
          "NA"
        }))

    private val templates: Map[String, String] =
      fileToList(s"$FRAGNOSTIC_TEMPLATES_BASE_PATH/templates.map", StandardCharsets.UTF_8.name()).fold(
        error => {
          logger.error(s"templates - error on fileToList : $error")
          Map[String, String]()
        },
        list => getMap(list, valida, getKV))

    @tailrec
    private def applyParam(template: String, params: List[(String, String)]): String =
      if (params.isEmpty) {
        template
      } else {
        val param = params.head
        applyParam(template.replace(s"{{${param._1}}}", param._2), params.tail)
      }

    override def getTemplate(name: String, params: Option[List[(String, String)]]): Either[String, String] =
      templates get name match {
        case Some(templatePath) =>
          val pathName = s"""$FRAGNOSTIC_TEMPLATES_BASE_PATH/$templatePath.$extension"""

          if (logger.isInfoEnabled()) logger.info("getTemplate() -\n\t- name : {}\n\t- extension : {}\n\t- pathName : {}", name, extension, pathName)

          fileToString(pathName, Charset.defaultCharset().name()).fold(
            error => Left("template.service.error.on.read.file.to.string"),
            template => {

              if (logger.isInfoEnabled()) logger.info("getTemplate() - template : {}", template)

              params.map(list => Right(applyParam(template, list))) getOrElse Right(template)

            })
        case None => Left(s"""template.service.error.on.templates.name.does.not.exist : \u0022$name\u0022""")
      }

  }

}
