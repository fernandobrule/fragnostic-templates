package com.fragnostic.templates.service

import org.scalatest._

/**
 * Created by fernandobrule on 8/19/16.
 */
class TemplateServiceTest extends FunSpec with Matchers {

  describe("Template Service Test") {

    it("Can Retrieve Template") {

      CakeServiceTemplate.templateService.getTemplate("i.am.a.template", Option(List(("uno", "UNO"), ("dos", "DOS")))) fold (
        error => throw new IllegalStateException(error),
        template =>
          template should be(
            """
              |#
              |Yep UNO y DOS
              |""".stripMargin))

    }

  }
}
