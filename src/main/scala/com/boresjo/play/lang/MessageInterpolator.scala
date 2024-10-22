package com.boresjo.play.lang

import MessageInterpolator.ENGLISH
import play.api.i18n.{Lang, MessagesApi}

import java.text.MessageFormat
import java.util.Locale
import javax.inject.*
import scala.collection.immutable

object MessageInterpolator {
  val ENGLISH: Lang = Lang(Locale.ENGLISH)
}

@Singleton
class MessageInterpolator @Inject()(messagesApi: MessagesApi) {

  private lazy val mapbacks: Map[String, Map[String, String]] = messagesApi.messages.view.mapValues { _.map { case (n,v) => (v, n) } }.toMap
  private def messagesForLang(lang: Lang): Map[String, String] = messagesApi.messages(lang.language)

  def messageText(lang: Lang)(messageName: String): String = messagesForLang(lang).getOrElse(messageName, throw RuntimeException(s"No $language mapping for \"$messageName\""))
  def messageName(lang: Lang)(messageText: String): String = mapbacks(lang.language).getOrElse(messageText, throw RuntimeException(s"No $language mapping for \"$messageText\""))

  extension (sc: StringContext) {
    def m(args: Any*)(using lang: Lang): String = {
      sc.parts.zipWithIndex.map { (part, idx) =>
        val name = part.trim
        val message = messageText(lang)(name)
        val replacement = part.replace(name, message)
        if (idx == 0) {
          replacement
        } else {
          args(idx-1).toString + replacement
        }
      }.mkString
    }
  }

  extension (sc: StringContext) {
    def en(args: Any*)(using lang: Lang): String = interpolator(sc)(ENGLISH, lang, args*)
  }

  def interpolator(sc: StringContext)(from: Lang, to: Lang, args: Any*): String = {
    val text: String = {
      val parts = sc.parts.toSeq
      parts.head + parts.tail.zipWithIndex.map { (v, i) => s"{$i}$v" }.mkString
    }
    val name = messageName(from)(text)
    val pattern = messageText(to)(name)

    MessageFormat.format(pattern, args *)
  }
}