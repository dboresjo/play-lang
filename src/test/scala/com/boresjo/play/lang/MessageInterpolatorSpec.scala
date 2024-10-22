package com.boresjo.play.lang

import com.boresjo.play.lang.MessageInterpolator.ENGLISH
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.shouldBe
import play.api.i18n.{DefaultMessagesApi, Lang}

class MessageInterpolatorSpec extends AnyFreeSpec {
  private val WELSH = Lang("cy")

  "Lang's" - {
    "m interpolator" - {
      val messagesApi = DefaultMessagesApi(messages = Map(
        "en" -> Map(
          "canine" -> "Dog",
          "feline" -> "Cat"
        )
      ))
      given MessageInterpolator(messagesApi)

      "fetches message text from the repository" in {
        given Lang = ENGLISH

        val foo = m"canine"
        foo shouldBe "Dog"
      }

      "interpolates a variable" in {
        given Lang = ENGLISH
        val and = "chases"
        val foo = m"canine $and feline"
        foo shouldBe "Dog chases Cat"
      }
    }

    "en interpolator" - {
      val messagesApi = DefaultMessagesApi(messages = Map(
        "en" -> Map("a.message.name" -> "Dog {0}, Cat {1}"),
        "cy" -> Map("a.message.name" -> "Cath {1}, Ci {0}")
      ))
      given MessageInterpolator(messagesApi)

      "interpolates a variable" in {
        given Lang = WELSH
        val (a, b) = (1, 2)
        val foo = en"Dog $a, Cat $b"
        foo shouldBe "Cath 2, Ci 1"
      }
    }

    "custom interpolator" - {
      val messagesApi = DefaultMessagesApi(messages = Map(
        "en" -> Map("a.message.name" -> "Dog {1}, Cat {0}"),
        "cy" -> Map("a.message.name" -> "Cath {0}, Ci {1}")
      ))
      given playLang: MessageInterpolator = MessageInterpolator(messagesApi)

      extension (sc: StringContext) {
        def cy(args: Any*)(using lang: Lang): String = playLang.interpolator(sc)(WELSH, lang, args *)
      }

      "interpolates a variable" in {
        given Lang = ENGLISH
        val (a, b) = (1, 2)
        val foo = cy"Cath $a, Ci $b"
        foo shouldBe "Dog 2, Cat 1"
      }
    }
  }
}